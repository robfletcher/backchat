package backchat

class Client {

	String id
	String name

	static hasMany = [documents: Document]

    static constraints = {
		name blank: false, unique: true
    }

	static mapping = {
		id generator: "uuid"
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (!o || getClass() != o.getClass()) return false
		if (name != o.name) return false
		return true
	}

	int hashCode() {
		return name?.hashCode() ?: 0
	}

	String toString() {
		"Client['$name']"
	}

}
