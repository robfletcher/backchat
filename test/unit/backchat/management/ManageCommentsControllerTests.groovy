package backchat.management

import grails.test.*
import backchat.Comment

class ManageCommentsControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
        registerMetaClass LinkedHashMap
        LinkedHashMap.metaClass.'int' = { String input ->
            delegate.get(input).toInteger()
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testListReturns10Comments() {
        def comments = []
        11.times {
            comments << new Comment()
        }
        mockDomain Comment, comments

        def model = controller.list()
        assertEquals 11, model.commentInstanceTotal
        assertEquals 10, model.commentInstanceList.size()
    }

    void testListReturnsMaxWhenPassedInAsParam(){
        def comments = []
        5.times {
            comments << new Comment()
        }
        mockDomain Comment, comments

        mockParams.max = '4'
        def model = controller.list()
        assertEquals 5, model.commentInstanceTotal
        assertEquals 4, model.commentInstanceList.size()
    }

}
