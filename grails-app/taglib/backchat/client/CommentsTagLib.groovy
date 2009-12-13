package backchat.client

class CommentsTagLib {

	static namespace = "comments"

	def resources = { attrs ->
		if (!attrs.clientId) throwTagError("Attribute 'clientId' is required.")
		if (!attrs.documentUrl) throwTagError("Attribute 'documentUrl' is required.")
		out << g.javascript {
"""
function commentSuccess(transport) {
	var json = transport.responseText.evalJSON();
	var status = json.status;
	if (status == "OK") {
		loadComments();
	} else {
		var messages = "<div class='errors'><ul>";
		for (var i = 0; i < json.messages.length; i++) {
			messages += '<li>' + json.messages[i] + '</li>';
		}
		messages += '</ul></div>';
		\$\$("#commentForm legend")[0].insert({after: messages});
	}
}

function loadComments() {
	new Ajax.Updater("comments", "${createLink(controller: 'comment', action: 'show')}", {
		parameters: { "client.id": "${attrs.clientId}", documentUrl: "${attrs.documentUrl}" }
	});
}

Event.observe(window, "load", loadComments);
"""
		}
	}

}
