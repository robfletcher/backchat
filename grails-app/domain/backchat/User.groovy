package backchat

class User {

	String nickname
	String email

	static hasMany = [comments: Comment]

    static constraints = {
		nickname blank: false
		email blank: false, email: true
    }
}
