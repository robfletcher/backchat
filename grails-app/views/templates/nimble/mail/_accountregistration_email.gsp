<html>

<head>

</head>

<body>

<p>
  <a href="${createLink(absolute:true, controller: 'account', action: 'validateuser', id: user.id, params: [activation: user.actionHash])}">Activate my account</a>
</p>

<p>
  If you're having trouble accessing the above link copy and paste the following into your browser
</p>
<p>
${createLink(absolute:true, controller: 'account', action: 'validateuser', id: user.id, params: [activation: user.actionHash])}
</p>

</body>

</html>