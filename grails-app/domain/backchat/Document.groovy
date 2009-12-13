package backchat

class Document {

	String name
	String url

	static belongsTo = [client: Client]
	static hasMany = [comments: Comment]

    static constraints = {
		name nullable: true
		url blank: false, unique: "client"
    }
}
