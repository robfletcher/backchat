<% import grails.persistence.Event %>
<%=packageName%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="\${resource(dir: '')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="\${flash.message}">
            <div class="message">\${flash.message}</div>
            </g:if>
            <g:hasErrors bean="\${${propertyName}}">
            <div class="errors">
                <g:renderErrors bean="\${${propertyName}}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
                <g:hiddenField name="id" value="\${${propertyName}?.id}" />
                <g:hiddenField name="version" value="\${${propertyName}?.version}" />
                <div class="dialog">
                    <fieldset>
					<%  excludedProps = Event.allEvents.toList() << 'version' << 'id'
						props = domainClass.properties.findAll { !excludedProps.contains(it.name) }
						Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
						props.each { p ->
							cp = domainClass.constrainedProperties[p.name]
							display = (cp ? cp.display : true)
							optional = (cp ? cp.nullable || (p.type == String && cp.blank) : true)
							if (display) { %>
						<div class="prop<%if(!optional){%> mandatory<%}%> \${hasErrors(bean: ${propertyName}, field: '${p.name}', 'error')}">
							<label for="${p.name}">
								<g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" />
								<%if(!optional){%><span class="indicator">*</span><%}%>
							</label>
							${renderEditor(p)}
						</div>
					<%  }   } %>
                    </fieldset>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="\${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="\${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
