package backchat

import backchat.Client
import backchat.Document
import static javax.servlet.http.HttpServletResponse.*

class CommentController {

	def commentService

	def add = {AddCommentCommand command ->
		if (command.hasErrors()) {
			render(contentType: "application/json") {
				status = "FAIL"
				delegate.errors = command.errors.allErrors.collect {
					[field: it.field, message: message(error: it)]
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
		def client = params."client.id" ? Client.read(params."client.id") : null
		if (!client) {
			response.sendError SC_UNAUTHORIZED
		} else if (!params.documentUrl) {
			response.sendError SC_BAD_REQUEST
		} else {
			def document = Document.findByClientAndUrl(client, params.documentUrl)
			def commentInstanceList = document?.comments ?: []
			return [commentInstanceList: commentInstanceList]
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
