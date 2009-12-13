package backchat

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTime

class Comment {

	String id
	String nickname
	String email
	String text
	DateTime timestamp

	static belongsTo = [document: Document]

    static constraints = {
		document()
		nickname blank: false
		email blank: false, email: true
		text blank: false
    }

	static mapping = {
		timestamp type: PersistentDateTime
		sort "timestamp"
	}
}
