package backchat

class Comment {

	String nickname
	String email
	String text

	static belongsTo = [document: Document]

    static constraints = {
		document()
		nickname blank: false
		email blank: false, email: true
		text blank: false
    }
}
