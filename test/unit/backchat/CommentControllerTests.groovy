package backchat

import org.gmock.WithGMock
import org.joda.time.DateTime
import static common.TestUtils.randomId
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

@WithGMock
class CommentControllerTests extends JsonControllerUnitTestCase {

	Client client

	void setUp() {
		super.setUp()

		mockCommandObject AddCommentCommand

		client = new Client(id: randomId(), name: "Grails.org")
		mockDomain Client, [client]
	}

	void testAddFailsWhenCommandInvalid() {
		def command = new AddCommentCommand()
		assertFalse command.validate()

		controller.add(command)

		def json = controller.response.contentAsJson
		assertEquals "FAIL", json.status
		assertTrue json.errors.contains("client: nullable")
		assertTrue json.errors.contains("documentUrl: nullable")
		assertTrue json.errors.contains("nickname: nullable")
		assertTrue json.errors.contains("email: nullable")
		assertTrue json.errors.contains("text: nullable")
	}

	void testAddAddsCommentToDocument() {
		def command = new AddCommentCommand(client: client, documentUrl: "http://grails.org/", nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "This thread sucks!")
		assertTrue command.validate()
		controller.commentService = mock(CommentService)
		controller.commentService.addComment(command).returns(new Comment(id: randomId(), nickname: command.nickname, text: command.text))

		play {
			controller.add(command)
		}

		def json = controller.response.contentAsJson
		assertEquals "OK", json.status
		assertEquals command.nickname, json.comment.nickname
		assertEquals command.text, json.comment.text
		assertNotNull json.comment.id
	}

	void testShowRequiresDocumentId() {
		controller.show()

		assertEquals SC_NOT_FOUND, controller.response.status
	}

	void testShowSetsNotFoundIfInvalidDocumentSpecified() {
		mockDomain Document
		controller.params.id = randomId()

		controller.show()

		assertEquals SC_NOT_FOUND, controller.response.status
	}

	void testShowRetrievesCommentsForASingleDocument() {
		def document1 = new Document(id: randomId(), client: client, name: "Home Page", url: "http://grails.org/")
		def document2 = new Document(id: randomId(), client: client, name: "Download Page", url: "http://grails.org/Download")
		mockDomain Document, [document1, document2]
		["blackbeard", "roundhouse", "ponytail"].eachWithIndex {name, i ->
			document1.addToComments new Comment(id: randomId(), document: document1, nickname: name, email: "$name@energizedwork.com", text: "Comment $i", timestamp: new DateTime())
			document2.addToComments new Comment(id: randomId(), document: document2, nickname: name, email: "$name@energizedwork.com", text: "Comment $i", timestamp: new DateTime())
		}

		controller.params.id = document1.id
		def model = controller.show()

		assertEquals 3, model.commentInstanceList.size()
		assertTrue model.commentInstanceList.every {
			it.document == document1
		}
	}
}