package backchat

import grails.converters.JSON
import grails.test.ControllerUnitTestCase
import grails.web.JSONBuilder
import groovy.xml.StreamingMarkupBuilder
import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import org.springframework.mock.web.MockHttpServletResponse
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

class CommentControllerTests extends ControllerUnitTestCase {

	Client client
	Document document

	void setUp() {
		super.setUp()

		mockCommandObject AddCommentCommand

		client = new Client(id: randomId(), name: "Grails.org")
		document = new Document(id: randomId(), client: client, name: "Home Page", url: "http://grails.org/")

		mockDomain Client, [client]
		mockDomain Document, [document]
		mockDomain Comment

		controller.metaClass {
			// wire message taglib onto controller
			message = {attrs ->
				"$attrs.error.field: $attrs.error.code"
			}

			// handle render(contentType: "application/json")
			render = {Map map, Closure c ->
				renderArgs.putAll(map)

				switch (map["contentType"]) {
					case null:
						break

					case "application/xml":
					case "text/xml":
						def b = new StreamingMarkupBuilder()
						if (map["encoding"]) b.encoding = map["encoding"]

						def writable = b.bind(c)
						delegate.response.outputStream << writable
						break

					case "application/json":
					case "text/json":
						delegate.response.outputStream << new JSONBuilder().build(c)
						break

					default:
						println "Nothing"
						break
				}
			}
		}

		// get JSON object directly from response
		registerMetaClass MockHttpServletResponse
		MockHttpServletResponse.metaClass.getContentAsJson = {->
			JSON.parse(delegate.contentAsString)
		}

		new ConvertersConfigurationInitializer().initialize()
	}

	private static String randomId() {
		return UUID.randomUUID() as String
	}

	void tearDown() {
		DateTimeUtils.setCurrentMillisSystem()
		super.tearDown()
	}

	void testAddFailsWhenCommandInvalid() {
		def command = new AddCommentCommand()
		assertFalse command.validate()

		controller.add(command)

		def json = controller.response.contentAsJson
		assertEquals "FAIL", json.status
		assertTrue json.errors.contains("document: nullable")
		assertTrue json.errors.contains("nickname: nullable")
		assertTrue json.errors.contains("email: nullable")
		assertTrue json.errors.contains("text: nullable")
	}

	void testAddAddsCommentToDocument() {
		def command = new AddCommentCommand(document: document, nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "This thread sucks!")
		assertTrue command.validate()

		controller.add(command)

		def json = controller.response.contentAsJson
		assertEquals "OK", json.status
		assertEquals command.nickname, json.comment.nickname
		assertEquals command.text, json.comment.text
		def commentId = json.comment.id

		def comment = Comment.get(commentId)
		assertEquals command.document, comment.document
		assertEquals command.nickname, comment.nickname
		assertEquals command.email, comment.email
		assertEquals command.text, comment.text
	}

	void testAddTimestampsCommentAccordingToUserTimezone() {
		DateTimeUtils.currentMillisFixed = System.currentTimeMillis()
		def command = new AddCommentCommand(document: document, nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "This thread sucks!", timezoneOffsetMinutes: -8 * 60)
		assertTrue command.validate()

		controller.add(command)

		def comment = Comment.get(controller.response.contentAsJson.comment.id)
		assertEquals new DateTime().withZone(DateTimeZone.forID("America/Vancouver")), comment.timestamp
	}

	void testShowRequiresDocumentId() {
		controller.show()

		assertEquals SC_NOT_FOUND, controller.response.status
	}

	void testShowRetrievesCommentsForASingleDocument() {
		["blackbeard", "roundhouse", "ponytail"].eachWithIndex {name, i ->
			def comment = new Comment(id: randomId(), document: document, nickname: name, email: "$name@energizedwork.com", text: "Comment $i", timestamp: new DateTime())
			assert comment.save()
			document.addToComments comment
		}

		def document2 = new Document(id: randomId(), client: client, name: "Download Page", url: "http://grails.org/Download")
		assert document2.save()
		assert new Comment(id: randomId(), document: document2, nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "Comment on other document", timestamp: new DateTime()).save()

		controller.params.id = document.id
		def model = controller.show()

		assertEquals 3, model.commentInstanceList.size()
		assertTrue model.commentInstanceList.every {
			it.document == document
		}
	}
}