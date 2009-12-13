<%@ page contentType="text/html;charset=UTF-8" %>
<g:each var="commentInstance" in="${commentInstanceList}">
	<div class="comment">
		<h4><span class="nickname">${commentInstance.nickname}</span>@<span class="timestamp"><joda:format value="${commentInstance.timestamp}"/></span></h4>
		<div class="text">${commentInstance.text}</div>
	</div>
</g:each>
