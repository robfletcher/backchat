package backchat

import grails.converters.JSON
import grails.test.ControllerUnitTestCase
import grails.web.JSONBuilder
import groovy.xml.StreamingMarkupBuilder
import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.springframework.mock.web.MockHttpServletResponse

abstract class JsonControllerUnitTestCase extends ControllerUnitTestCase {

	void setUp() {
		super.setUp()

		controller.metaClass {
			// wire message taglib onto controller
			message = {attrs ->
				"$attrs.error.field: $attrs.error.code"
			}

			// handle render(contentType: "application/json")
			render = {Map map, Closure c ->
				renderArgs.putAll(map)

				switch (map["contentType"]) {
					case null:
						break

					case "application/xml":
					case "text/xml":
						def b = new StreamingMarkupBuilder()
						if (map["encoding"]) b.encoding = map["encoding"]

						def writable = b.bind(c)
						delegate.response.outputStream << writable
						break

					case "application/json":
					case "text/json":
						delegate.response.outputStream << new JSONBuilder().build(c)
						break

					default:
						println "Nothing"
						break
				}
			}
		}

		// get JSON object directly from response
		registerMetaClass MockHttpServletResponse
		MockHttpServletResponse.metaClass.getContentAsJson = {->
			JSON.parse(delegate.contentAsString)
		}

		new ConvertersConfigurationInitializer().initialize()
	}

}