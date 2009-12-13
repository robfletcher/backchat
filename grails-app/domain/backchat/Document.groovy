package backchat

class Document {

	String id
	String name
	String url

	static belongsTo = [client: Client]
	static hasMany = [comments: Comment]

    static constraints = {
		name nullable: true
		url blank: false, unique: "client"
    }

	static mapping = {
		comments sort: "timestamp"
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (!o || getClass() != o.getClass()) return false
		if (client != o.client) return false
		if (url != o.url) return false
		return true
	}

	int hashCode() {
		int result = 17
		result = 31 * result + (client?.hashCode() ?: 0)
		result = 31 * result + (url?.hashCode() ?: 0)
		return result
	}

	String toString() {
		"Document['$name', '$url']"
	}

}
