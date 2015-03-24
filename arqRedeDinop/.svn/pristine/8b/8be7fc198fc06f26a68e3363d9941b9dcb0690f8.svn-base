<%@page trimDirectiveWhitespaces="true" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />
<html>
	<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<head>
		<title>DINOP Diretoria de Apoio aos Negócios e Operações</title>
		<!--  Início Estilos default - NÃO REMOVER OU ALTERAR -->
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/BB16.ico">
		<link type="text/css" rel="stylesheet" href="/header2012/css/cssPortal2012/index.css">
		<link type="text/css" rel="stylesheet" href="/header2012/css/cssPortal2012/colorbox.css">
		<link type="text/css" href="/header2012/js/jsPortal2012/jquery-ui-1.8.21.custom/css/redmond/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
		<link type="text/css" rel="stylesheet" href="/header2012/css/gridNew.css">
		<link type="text/css" href="/header2012/js/jsPortal2012/Wijmo/development-bundle/themes/wijmo/jquery.wijmo.wijutil.css" rel="stylesheet" />
		<link type="text/css" href="/header2012/js/jsPortal2012/Wijmo/development-bundle/themes/wijmo/jquery.wijmo.wijdialog.css" rel="stylesheet" />
		<link type="text/css" href="/header2012/js/jsPortal2012/Wijmo/development-bundle/themes/wijmo/jquery.wijmo.wijaccordion.css" rel="stylesheet" />
		<link type="text/css" href="/header2012/js/jsPortal2012/DataTable/css/demo_table_jui.css" rel="stylesheet" />
		<link type="text/css" href="/header2012/css/cssPortal2012/wt-rotator.css" rel="stylesheet" />
		<link type="text/css" href="/header2012/js/jsPortal2012/slider/styles/styleSlider.css" rel="stylesheet" />
		<link type="text/css" href="/header2012/css/cssPortal2012/dataTable.css" rel="stylesheet" />
		
		<!--  Fim estilos default - NÃO REMOVER OU ALTERAR -->	
		
		<script>window.defaultStatus="DINOP Diretoria de Apoio aos Negócios e Operações - Sistema de Informações Gerenciais";</script>
		<!--  Início Scripts default - NÃO REMOVER OU ALTERAR -->
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery-ui-1.8.21.custom/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery-ui-1.8.21.custom/js/jquery-ui-1.8.21.custom.min.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.colorbox-min.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/Highcharts/js/highcharts.src.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/Highcharts/js/modules/exporting.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/Highcharts/js/themes/padraoDev.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.tooltip.js"></script>
		<script src="/header2012/js/jsPortal2012/Wijmo/development-bundle/wijmo/jquery.wijmo.wijutil.js" type="text/javascript"></script>
		<script src="/header2012/js/jsPortal2012/Wijmo/development-bundle/wijmo/jquery.wijmo.wijaccordion.js" type="text/javascript"></script>
		<script src="/header2012/js/jsPortal2012/Wijmo/development-bundle/wijmo/jquery.wijmo.wijdialog.js" type="text/javascript"></script>
		<script src="/header2012/js/jsPortal2012/jquery.printElement.js" type="text/javascript"></script>
		<script src="/header2012/js/jsPortal2012/jquery-validation-1.8.1/jquery.validate.min.js" type="text/javascript"></script>
		<script src="/header2012/js/jsPortal2012/DataTable/js/jquery.dataTables.js" type="text/javascript"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.base64.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/script.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.easing.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/chili-1.7.pack.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.cycle.all.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.metadata.v2.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.media.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.simplemodal.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.cookie.js"></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/jquery.ocupload-1.1.2.js"></script>
		<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  		<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/UsersOnline.js'></script>
  		<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
		<script type="text/javascript" src="/header2012/js/jsPortal2012/indexAplic.js" charset="utf-8"></script>  		
  		<script type="text/javascript" src="<%=request.getContextPath()%>/js/index.js" charset="utf-8"></script>
		
		<!--  Fim Scripts default - NÃO REMOVER OU ALTERAR -->
	</head>
	<body onload="init(); dwr.engine.setActiveReverseAjax(true);">
		<input type="hidden" id="chaveUsuarioLogado" value="<c:out value='${usuario.chave}'/>">
        <input type="hidden" id="nomeUsuarioLogado" value="<c:out value='${usuario.nome}'/>">
        <input type="hidden" id="apelidoUsuarioLogado" value="<c:out value='${usuario.nomeGuerra}'/>">
        <input type="hidden" id="prefixoUsuarioLogado" value="<c:out value='${usuario.codigoPrefixoDependencia}'/>">
        <input type="hidden" id="comissaoUsuarioLogado" value="<c:out value='${usuario.codigoComissaoUsuario}'/>">
        <input type="hidden" id="foto" value="<c:out value='${foto}'/>">
		<input type="hidden" id="aplic" value="<%=request.getContextPath()%>">
		
		<input type="button" value="Inserir Foto" id="buttonUpFoto" style="display: none;">
		
		<!--  Início Testeira -->
		<div id="header">
		</div>
		<!--  Fim Testeira -->
		<!-- Início do Conteúdo Página -->
		
		<div class="container_24" id="conteudo">
			
		</div>
		
		<!--  Manter -->
		<div id="carregando" style="display: none;">
			<div style="text-align: center;" class="container_24">
				<img src="/header2012/images/ajax-loader.gif" border="0" /><br>Aguarde...
			</div>
		</div>
		<div id="aguarde" style="display: none; vertical-align: middle;" class="container_24"><img src="/header2012/images/ajax-loader.gif" border="0" /><br>Aguarde...</div>
		<div id="showAlert" style="display: none;">
			<img src="/header2012/images/icons/warning.png" class="">
		</div>
		
		<!--  Fim Manter -->
		<!-- Fim do Conteúdo -->
	</body>
	<script>
	var uploadFoto = null;

	uploadFoto = $('#buttonUpFoto').upload({
		name: 'Foto',
		action: 'exiba',
		enctype: 'multipart/form-data',
		params: {
			cmdo:'arqRedeDinop.upload',
			tipo:'foto'
		},
		autoSubmit: false,
		onSubmit: function(){},
		onComplete: function(){
			alert(document.getElementById('iframeFoto').contentWindow.document.body.getElementsByTagName('span')[2].innerHTML);
			if (document.getElementById('iframeFoto').contentWindow.document.body.getElementsByTagName('span')[0].innerHTML === "1") {
				$('.containerAvatar img:eq(0)').attr('src', '/header2012/images/funcis' + document.getElementById('iframeFoto').contentWindow.document.body.getElementsByTagName('span')[1].innerHTML);
				foto = true;
				avatar = false;
			}
			$.modal.close();
		},
		onSelect: function(){}
	});

	$('#buttonUpFoto').on('click', function(){
		uploadFoto.submit;
	});
	</script>
</html>