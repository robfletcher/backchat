package backchat

import grails.converters.JSON
import grails.test.ControllerUnitTestCase
import grails.web.JSONBuilder
import groovy.xml.StreamingMarkupBuilder
import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.springframework.mock.web.MockHttpServletResponse
import javax.servlet.http.HttpServletResponse
import static javax.servlet.http.HttpServletResponse.*

class CommentControllerTests extends ControllerUnitTestCase {

	Client client
	Document document

	void setUp() {
		super.setUp()

		mockCommandObject AddCommentCommand

		client = new Client(name: "Grails.org")
		document = new Document(client: client, name: "Home Page", url: "http://grails.org/")

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

	void testShowRequiresDocumentId() {
		controller.show()

		assertEquals SC_NOT_FOUND, controller.response.status
	}

	void testShowRetrievesCommentsForASingleDocument() {
		["blackbeard", "roundhouse", "ponytail"].eachWithIndex { name, i ->
			def comment = new Comment(document: document, nickname: name, email: "$name@energizedwork.com", text: "Comment $i")
			assert comment.save()
			document.addToComments comment
		}

		def document2 = new Document(client: client, name: "Download Page", url: "http://grails.org/Download")
		assert document2.save(), document2.errors.allErrors.collect {"$it.field: $it.code" }.join("\n")
		assert new Comment(document: document2, nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "Comment on other document").save()

		controller.params.id = document.id
		def model = controller.show()

		assertEquals 3, model.commentInstanceList.size()
		assertTrue model.commentInstanceList.every {
			it.document == document
		}
	}
}