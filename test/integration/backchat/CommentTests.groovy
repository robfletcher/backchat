package backchat

import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.DateTimeZone

class CommentTests extends GroovyTestCase {

	Client client
	Document document

	void setUp() {
		super.setUp()

		client = Client.build()
		document = Document.build(client: client)
	}

	void tearDown() {
		super.tearDown()
	}

	void testCommentsAreOrderedByTimestamp() {
		def now = new DateTime()
		Comment.build(document: document, timestamp: now.minusWeeks(1))
		Comment.build(document: document, timestamp: now.minusMonths(1))
		Comment.build(document: document, timestamp: now.minusDays(1))

		def comments = Comment.list()

		assertEquals comments.timestamp.sort(), comments.timestamp
	}

	void testCommentOrderingRespectsTimeZone() {
		def now = new LocalDateTime()
		Comment.build(document: document, timestamp: now.toDateTime(DateTimeZone.forID("Europe/London")))
		Comment.build(document: document, timestamp: now.toDateTime(DateTimeZone.forID("America/Vancouver")))
		Comment.build(document: document, timestamp: now.toDateTime(DateTimeZone.forID("Asia/Tokyo")))

		def comments = Comment.list()

		assertEquals comments.timestamp.sort(), comments.timestamp
	}
}
