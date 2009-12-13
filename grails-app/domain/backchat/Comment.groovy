package backchat

class Comment {

	User commenter
	String text

	static belongsTo = [document: Document]

    static constraints = {
		document()
		commenter()
		text blank: false
    }
}
