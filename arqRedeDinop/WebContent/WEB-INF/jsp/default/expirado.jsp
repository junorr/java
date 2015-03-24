<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />
<html>
<head>
<title>arqRedeDinop DINOP</title>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/intranet.css" />
</head>
<body>
	<div align="center" class='width:600px'>
		<p>
			<b>DINOP Administração de Contratos - Sessão Expirou</b>
		</p>
		<p class='mini'>Desculpe o incômodo</p>
		<p class='mini'>
			O prazo de sua sessão expirou e você deve executar nova autenticação.<br />
			Pressione a tecla F5 ou clique no menu Atualizar para continuar
			navegando no aplicativo.
		</p>
	</div>
</body>
</html>