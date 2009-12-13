package backchat

import static javax.servlet.http.HttpServletResponse.*
import org.joda.time.DateTime

class CommentController {

	def add = {AddCommentCommand command ->
		if (command.hasErrors()) {
			render(contentType: "application/json") {
				status = "FAIL"
				errors = command.errors.allErrors.collect {
					message error: it
				}
			}
		} else {
			def c = command.toComment()
			c.save(failOnError: true)
			render(contentType: "application/json") {
				status = "OK"
				comment = [id: c.id, nickname: c.nickname, text: c.text]
			}
		}
	}

	def show = {
		if (!params.id) {
			response.sendError SC_NOT_FOUND
		} else {
			def document = Document.read(params.id)
			def commentInstanceList = document.comments ?: []
			return [commentInstanceList: commentInstanceList]
		}
	}

}

class AddCommentCommand {

	Document document
	String nickname
	String email
	String text

	static constraints = {
		document nullable: false
		nickname nullable: false, blank: false
		email nullable: false, blank: false, email: true
		text nullable: false, blank: false
	}

	Comment toComment() {
		new Comment(document: document, nickname: nickname, email: email, text: text, timestamp: new DateTime())
	}

}
