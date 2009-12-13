import javax.servlet.ServletContext
import backchat.Client

class BootStrap {

	def init = {ServletContext ctx ->
		environments {
			development {
				def client = Client.findByName("localhost")
				if (!client) {
					println "Creating localhost client..."
					client = new Client(name: "localhost")
					client.save(failOnError: true)
				}
			}
		}
	}

	def destroy = {
	}
} 