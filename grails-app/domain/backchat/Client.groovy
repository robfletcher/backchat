package backchat

class Client {

	String name

	static hasMany = [documents: Document]

    static constraints = {
		name blank: false
    }
}
