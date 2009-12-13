package backchat

class Document {

	String url

	static belongsTo = [client: Client]
	static hasMany = [comments: Comment]

    static constraints = {
		url blank: false, unique: "client"
    }
}
