<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />
<html>
<head>
<title>DINOP Diretoria de Apoio aos Negócios e Operações</title>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/images/BB16.ico">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/intranet.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css">

<script>window.defaultStatus="DINOP Diretoria de Apoio aos Negócios e Operações - Sistema de Informações Gerenciais"</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.3.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.6.custom/js/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.corner.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jqueryUI1.8.6/db/ui/jquery.ui.core.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js//jqueryUI1.8.6/db/ui/jquery.ui.widget.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jqueryUI1.8.6/db/ui/jquery.ui.mouse.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jqueryUI1.8.6/db/ui/jquery.ui.draggable.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jqueryUI1.8.6/db/ui/jquery.ui.position.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jqueryUI1.8.6/db/ui/jquery.ui.resizable.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jqueryUI1.8.6/db/ui/jquery.ui.dialog.js"></script>

<script>
		$(document).ready(function(){
			var cdAsnt = 0;
			var cdUsu = "${sessionScope['acesso.chave']}";
			var nmUsu = "${sessionScope['acesso.nome']}";
			$('fieldset').corner();
			$('#header').load('/arqRedeDinop/testeira/dinop/testeira.jsp');
			$('#header:empty').html("<p>Não foi possível carregar cabeçalho</p>");			//MENSAGEM SE NÃO FOR POSSÍVEL
		});
		</script>
</head>
<body>
	<div id='header'></div>
	<fieldset id="Result" class="fonte"
		style="color: #22407B; display: inline;">
		<legend> Navegador incompatível: </legend>
		<p class="fonte" style="text-align: center;">Aplicativo disponível
			somente para navegadores Mozilla Firefox.</p>
	</fieldset>
</body>
</html>