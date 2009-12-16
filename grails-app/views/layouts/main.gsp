<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="backchat.Client" %>
<html>
	<head>
		<title><g:layoutTitle default="Grails"/></title>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'forms.css')}"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'comments.css')}"/>
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
		<g:layoutHead/>
		<g:javascript library="prototype"/>
		<g:javascript library="application"/>
		<comments:resources clientId="${Client.findByName('localhost').id}" documentUrl="http://localhost:8080${request.requestURI}"/>
	</head>
	<body>
		<div id="spinner" class="spinner" style="display:none;">
			<img src="${resource(dir: 'images', file: 'spinner.gif')}" alt="Spinner"/>
		</div>
		<div id="grailsLogo" class="logo"><a href="http://grails.org"><img src="${resource(dir: 'images', file: 'grails_logo.png')}" alt="Grails" border="0"/></a></div>
		<g:layoutBody/>
		<div id="commentForm">
			<g:set var="clientId" value="${Client.findByName('localhost').id}"/>
			<g:set var="documentUrl" value="http://localhost:8080${request.requestURI}"/>
			<fieldset>
				<legend>Add a comment</legend>
				<g:formRemote name="addComment" url="[controller:'comment', action:'add']" onSuccess="commentSuccess(e)" onCreate="clearErrors()">
					<g:hiddenField name="client.id" value="${clientId}"/>
					<g:hiddenField name="documentUrl" value="${documentUrl}"/>
					<div class="prop">
						<label for="nickname"><span class="mandatory">*</span>Nickname:</label>
						<input type="text" id="nickname" name="nickname"/>
					</div>
					<div class="prop">
						<label for="email"><span class="mandatory">*</span>Email:</label>
						<input type="text" id="email" name="email"/>
					</div>
					<textarea id="text" name="text" cols="80" rows="5"></textarea>
					<div class="buttons">
						<span class="button">
							<input type="submit" value="Add Comment" class="save"/>
						</span>
					</div>
				</g:formRemote>
			</fieldset>
		</div>
		<div id="comments"></div>
	</body>
</html>