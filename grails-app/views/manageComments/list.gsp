
<%@ page import="backchat.Comment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'comment.label', default: 'Comment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir: '')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'comment.id.label', default: 'Id')}" />
                        
                            <th><g:message code="comment.document.label" default="Document" /></th>
                   	    
                            <g:sortableColumn property="nickname" title="${message(code: 'comment.nickname.label', default: 'Nickname')}" />
                        
                            <g:sortableColumn property="email" title="${message(code: 'comment.email.label', default: 'Email')}" />
                        
                            <g:sortableColumn property="text" title="${message(code: 'comment.text.label', default: 'Text')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${commentInstanceList}" status="i" var="commentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${commentInstance.id}">${fieldValue(bean: commentInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: commentInstance, field: "document")}</td>
                        
                            <td>${fieldValue(bean: commentInstance, field: "nickname")}</td>
                        
                            <td>${fieldValue(bean: commentInstance, field: "email")}</td>
                        
                            <td>${fieldValue(bean: commentInstance, field: "text")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${commentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
