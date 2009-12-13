
<%@ page import="backchat.Comment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'comment.label', default: 'Comment')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir: '')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${commentInstance}">
            <div class="errors">
                <g:renderErrors bean="${commentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="document"><g:message code="comment.document.label" default="Document" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commentInstance, field: 'document', 'errors')}">
                                    <g:select name="document.id" from="${backchat.Document.list()}" optionKey="id" value="${commentInstance?.document?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nickname"><g:message code="comment.nickname.label" default="Nickname" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commentInstance, field: 'nickname', 'errors')}">
                                    <g:textField name="nickname" value="${commentInstance?.nickname}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="email"><g:message code="comment.email.label" default="Email" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commentInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${commentInstance?.email}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text"><g:message code="comment.text.label" default="Text" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commentInstance, field: 'text', 'errors')}">
                                    <g:textField name="text" value="${commentInstance?.text}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
