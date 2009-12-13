<%@ page contentType="text/html;charset=UTF-8" %>
<g:each var="commentInstance" in="${commentInstanceList}" status="i">
	<div class="comment ${i % 2 == 0 ? 'even' : 'odd'}">
		<div class="head">
			<span class="nickname">${commentInstance.nickname}</span>
			<span class="timestamp">@ <joda:format value="${commentInstance.timestamp}"/></span>
		</div>
		<div class="text">${commentInstance.text}</div>
	</div>
</g:each>
