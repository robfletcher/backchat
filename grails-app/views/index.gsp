<%@ page import="backchat.Client" %>
<html>
	<head>
		<title>Welcome to Grails</title>
		<meta name="layout" content="main"/>
		<style type="text/css" media="screen">

		#nav {
			margin-top: 20px;
			margin-left: 30px;
			width: 228px;
			float: left;

		}

		.homePagePanel * {
			margin: 0px;
		}

		.homePagePanel .panelBody ul {
			list-style-type: none;
			margin-bottom: 10px;
		}

		.homePagePanel .panelBody h1 {
			text-transform: uppercase;
			font-size: 1.1em;
			margin-bottom: 10px;
		}

		.homePagePanel .panelBody {
			background: url(images/leftnav_midstretch.png) repeat-y top;
			margin: 0px;
			padding: 15px;
		}

		.homePagePanel .panelBtm {
			background: url(images/leftnav_btm.png) no-repeat top;
			height: 20px;
			margin: 0px;
		}

		.homePagePanel .panelTop {
			background: url(images/leftnav_top.png) no-repeat top;
			height: 11px;
			margin: 0px;
		}

		h2 {
			margin-top: 15px;
			margin-bottom: 15px;
			font-size: 1.2em;
		}

		#pageBody {
			margin-left: 280px;
			margin-right: 20px;
		}
		</style>
	</head>
	<body>
		<div id="nav">
			<div class="homePagePanel">
				<div class="panelTop">

				</div>
				<div class="panelBody">
					<h1>Application Status</h1>
					<ul>
						<li>App version: <g:meta name="app.version"></g:meta></li>
						<li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
						<li>JVM version: ${System.getProperty('java.version')}</li>
						<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
						<li>Domains: ${grailsApplication.domainClasses.size()}</li>
						<li>Services: ${grailsApplication.serviceClasses.size()}</li>
						<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
					</ul>
					<h1>Installed Plugins</h1>
					<ul>
						<g:set var="pluginManager"
								value="${applicationContext.getBean('pluginManager')}"></g:set>

						<g:each var="plugin" in="${pluginManager.allPlugins}">
							<li>${plugin.name} - ${plugin.version}</li>
						</g:each>

					</ul>
				</div>
				<div class="panelBtm">
				</div>
			</div>

		</div>
		<div id="pageBody">
			<h1>Welcome to Grails</h1>
			<p>Congratulations, you have successfully started your first Grails application! At the moment
			this is the default page, feel free to modify it to either redirect to a controller or display whatever
			content you may choose. Below is a list of controllers that are currently deployed in this application,
			click on each to execute its default action:</p>

			<div id="controllerList" class="dialog">
				<h2>Available Controllers:</h2>
				<ul>
					<g:each var="c" in="${grailsApplication.controllerClasses}">
						<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
					</g:each>
				</ul>
			</div>
		</div>

		<g:set var="clientId" value="${Client.findByName('localhost').id}"/>
		<g:set var="documentUrl" value="http://localhost:8080${request.requestURI}"/>
		<g:javascript>
			var documentUrl = "";

			function commentSuccess(transport) {
				var json = transport.responseText.evalJSON();
				var status = json.status;
				if (status != "OK") {
					var messages = "";
					for (var i = 0; i < json.messages.length; i++) {
						if (i > 0) messages += "\n";
						messages += json.messages[i];
					}
					alert(messages);
				}
			}

			function loadComments() {
				new Ajax.Updater("comments", "${createLink(controller: 'comment', action: 'show')}", {
					parameters: { "client.id": "${clientId}", documentUrl: "${documentUrl}" }
				});
			}

			Event.observe(window, "load", loadComments);
		</g:javascript>
		<fieldset>
			<legend>Add a comment...</legend>
			<g:formRemote name="commentForm" url="[controller:'comment', action:'add']" onSuccess="commentSuccess(e)">
				<g:hiddenField name="client.id" value="${clientId}"/>
				<g:hiddenField name="documentUrl" value="${documentUrl}"/>
				<div class="formField">
					<label for="nickname">Nickname:</label>
					<input type="text" id="nickname" name="nickname"/>
				</div>
				<div class="formField">
					<label for="email">Email:</label>
					<input type="text" id="email" name="email"/>
				</div>
				<div class="formField">
					<textarea id="text" name="text" cols="80" rows="10"></textarea>
				</div>
				<input type="submit" value="Add Comment"/>
			</g:formRemote>
		</fieldset>
		<div id="comments"></div>
	</body>
</html>