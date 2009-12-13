package backchat

class CommentController {

	def addComment = {AddCommentCommand command ->
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
				comment = [id: c.id
					,nickname: c.nickname
					,text: c.text
				]
			}
		}
	}

}

class AddCommentCommand {

	Client client
	Document document
	String nickname
	String email
	String text

	static constraints = {
		client nullable: false
		document nullable: false
		nickname nullable: false, blank: false
		email nullable: false, blank: false, email: true
		text nullable: false, blank: false
	}

	Comment toComment() {
		new Comment(document: document, nickname: nickname, email: email, text: text)
	}

}
