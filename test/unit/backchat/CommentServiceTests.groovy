package backchat

import grails.test.GrailsUnitTestCase
import static common.TestUtils.*
import org.joda.time.DateTimeUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class CommentServiceTests extends GrailsUnitTestCase {

	Client client
	CommentService service
	
	void setUp() {
		super.setUp()

		client = new Client(id: randomId(), name: "Life Showbiz")
		mockDomain Document
		mockDomain Comment

		service = new CommentService()
	}

	void tearDown() {
		DateTimeUtils.setCurrentMillisSystem()
		super.tearDown()
	}

	void testAddCommentAttachesCommentToExistingDocument() {
		def document = new Document(id: randomId(), client: client, url: "http://lifeshowbiz.ru/")
		assert document.save()

		def command = new AddCommentCommand(client: client, documentUrl: document.url, nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "What a rip off!")
		def comment = service.addComment(command)

		assertEquals document, comment.document
		assertEquals command.text, comment.text
		assertEquals command.nickname, comment.nickname
		assertEquals command.email, comment.email
	}

	void testAddCommentCreatesNewDocumentIfItDoesNotExist() {
		def command = new AddCommentCommand(client: client, documentUrl: "http://lifeshowbiz.ru/", nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "What a rip off!")
		def comment = service.addComment(command)

		assertNotNull comment.document
		assertEquals client, comment.document.client
		assertEquals command.documentUrl, comment.document.url
	}

	void testAddTimestampsCommentAccordingToUserTimezone() {
		DateTimeUtils.currentMillisFixed = System.currentTimeMillis()
		def timeZoneOffsetMinutes = -8 * 60
		def timeZoneOffsetMillis = timeZoneOffsetMinutes * 60000
		def command = new AddCommentCommand(client: client, documentUrl: "http://lifeshowbiz.ru/", nickname: "blackbeard", email: "blackbeard@energizedwork.com", text: "This thread sucks!", timeZoneOffsetMinutes: timeZoneOffsetMinutes)

		def comment = service.addComment(command)

		def now = new DateTime()
		assertEquals timeZoneOffsetMillis, comment.timestamp.zone.getOffset(now)
		assertEquals now, comment.timestamp
	}

}