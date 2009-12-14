package backchat.client

class CommentsTagLib {

	static namespace = "comments"

	def resources = { attrs ->
		if (!attrs.clientId) throwTagError("Attribute 'clientId' is required.")
		if (!attrs.documentUrl) throwTagError("Attribute 'documentUrl' is required.")
		out << g.javascript {
"""
function clearErrors() {
	\$\$('#commentForm input, #commentForm select, #commentForm textarea').each(function(element) {
		element.removeClassName('error');
	});
}

function commentSuccess(transport) {
	var json = transport.responseText.evalJSON();
	var status = json.status;
	if (status == "OK") {
		var form = \$('addComment');
		Form.reset(form);
		loadComments();
	} else {
		for (var i = 0; i < json.errors.length; i++) {
			var field = json.errors[i]['field'];
			\$(field).addClassName('error');
		}
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
