package backchat

import org.gmock.WithGMock
import org.joda.time.DateTime
import static java.util.Collections.*
import static common.TestUtils.randomId
import static javax.servlet.http.HttpServletResponse.*

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
		assertTrue json.errors.contains([field: "client", message: "nullable"])
		assertTrue json.errors.contains([field: "documentUrl", message: "nullable"])
		assertTrue json.errors.contains([field: "email", message: "nullable"])
		assertTrue json.errors.contains([field: "text", message: "nullable"])
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

	void testShowRequiresClientId() {
		controller.show()

		assertEquals SC_UNAUTHORIZED, controller.response.status
	}

	void testShowRequiresDocumentUrl() {
		controller.params."client.id" = client.id

		controller.show()

		assertEquals SC_BAD_REQUEST, controller.response.status
	}

	void testShowReturnsEmptyListIfUnknownDocumentSpecified() {
		mockDomain Document
		controller.params."client.id" = client.id
		controller.params.documentUrl = "http://aol.bv/"

		def model = controller.show()

		assertEquals EMPTY_LIST, model.commentInstanceList
	}

	void testShowRetrievesCommentsForASingleDocument() {
		def document1 = new Document(id: randomId(), client: client, name: "Home Page", url: "http://grails.org/")
		def document2 = new Document(id: randomId(), client: client, name: "Download Page", url: "http://grails.org/Download")
		mockDomain Document, [document1, document2]
		["blackbeard", "roundhouse", "ponytail"].eachWithIndex {name, i ->
			document1.addToComments new Comment(id: randomId(), document: document1, nickname: name, email: "$name@energizedwork.com", text: "Comment $i", timestamp: new DateTime())
			document2.addToComments new Comment(id: randomId(), document: document2, nickname: name, email: "$name@energizedwork.com", text: "Comment $i", timestamp: new DateTime())
		}

		controller.params."client.id" = client.id
		controller.params.documentUrl = document1.url
		def model = controller.show()

		assertEquals 3, model.commentInstanceList.size()
		assertTrue model.commentInstanceList.every {
			it.document == document1
		}
	}
}