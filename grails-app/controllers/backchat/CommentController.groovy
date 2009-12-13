package backchat

import backchat.Client
import backchat.Document
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

class CommentController {

	def commentService

	def add = {AddCommentCommand command ->
		if (command.hasErrors()) {
			render(contentType: "application/json") {
				status = "FAIL"
				errors = command.errors.allErrors.collect {
					message error: it
				}
			}
		} else {
			def c = commentService.addComment(command)
			render(contentType: "application/json") {
				status = "OK"
				comment = [id: c.id, nickname: c.nickname, text: c.text]
			}
		}
	}

	def show = {
		def document = params.id ? Document.read(params.id) : null
		if (document) {
			def commentInstanceList = document.comments ?: []
			return [commentInstanceList: commentInstanceList]
		} else {
			response.sendError SC_NOT_FOUND
		}
	}

}

class AddCommentCommand {

	Client client
	String documentUrl
	String nickname
	String email
	String text
	int timeZoneOffsetMinutes

	static constraints = {
		client nullable: false
		documentUrl nullable: false, blank: false
		nickname nullable: false, blank: false
		email nullable: false, blank: false, email: true
		text nullable: false, blank: false
	}

}
