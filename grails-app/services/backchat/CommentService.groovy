package backchat

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class CommentService {

	Comment addComment(AddCommentCommand command) {
		def document = Document.findByClientAndUrl(command.client, command.documentUrl)
		if (!document) {
			// TODO: need to handle concurrency here
			document = new Document(client: command.client, url: command.documentUrl)
			document.save(failOnError: true)
		}
		def comment = new Comment(document: document)
		comment.nickname = command.nickname
		comment.email = command.email
		comment.text = command.text
		comment.timestamp = calculateCommentTimestamp(command)
		comment.save(failOnError: true)
		return comment
	}

	private static DateTime calculateCommentTimestamp(AddCommentCommand command) {
		int offsetHours = command.timeZoneOffsetMinutes.intdiv(60)
		int offsetMinutes = command.timeZoneOffsetMinutes % 60
		def tz = DateTimeZone.forOffsetHoursMinutes(offsetHours, offsetMinutes)
		return new DateTime().withZone(tz)
	}

}