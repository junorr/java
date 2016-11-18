<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />
<html>
	<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<head>
		<title>DISEC</title>
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico"/>
		<!--  Início Estilos default - NÃO REMOVER OU ALTERAR -->
    <style type="text/css">
      .footer-line {
        margin-left: 10px;
        margin-right: 10px;
        margin-top: 40px;
        margin-bottom: 5px;
        width: 98.5%; 
        height: 0px; 
        border-top: solid thin rgb(180,180,210); 
        box-shadow: 0px 2px 3px rgba(50,50,50,0.5);
      }
      .footer-info {
        margin-left: 10px; 
        width: 98.5%; 
        font-size: 10px;
      }
      .footer-info td {
        width: 33.3%;
      }
      .nmAplic {
        width: 100%; 
        text-align: center;
      }
      #conteudo {
        margin-left: 5%; 
        width: 90%;
      }
    </style>
		
    <link rel="stylesheet" href="css/jquery-ui.min.css">
    <link rel="stylesheet" href="css/jquery-ui.theme.min.css">

		<script type="text/javascript" src="js/jquery-1.12.3.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/micro-core.js"></script>
		
		<!--  Fim Scripts default - NÃO REMOVER OU ALTERAR -->
	</head>
	<body>
		<input type="hidden" id="chaveUsuarioLogado" value="<c:out value='${user.chave}'/>">
		<input type="hidden" id="nomeUsuarioLogado" value="<c:out value='${user.nome}'/>">
		<input type="hidden" id="apelidoUsuarioLogado" value="<c:out value='${user.nomeGuerra}'/>">
		<input type="hidden" id="prefixoUsuarioLogado" value="<c:out value='${user.prefixoDepe}'/>">
		<input type="hidden" id="comissaoUsuarioLogado" value="<c:out value='${user.codigoComissao}'/>">        
		
	<!--  Manter -->
			
	<div id="paginaAplicacao">	
		
			<!--  Início Testeira -->
			<div id="header"></div>
			<!--  Fim Testeira -->

			
			<!-- Nome da Aplicação -->
      <div class="nmAplic">
        <h2 id="nmAplic">${dcrctu.nmCtu}</h2>
      </div>
			<!-- Fim do Nome da Aplicação -->
			
			<!-- Início do Conteúdo Página -->						
			<div id="conteudo">
				
			</div>		
			<!--  Fim do Conteúdo-->
      
      
      <!-- Rodapé com informações do DcrCtu -->
      <div id="dcrctu">
        <div class="footer-line"></div>
        <table class="footer-info">
          <tr>
            <td>Fonte: ${dcrctu.nmFon}</td>
            <td style="text-align: center;">Responsável: ${dcrctu.nmRsp}</td>
            <td style="text-align: right;">
              Publicação: <fmt:formatDate pattern="dd/MM/yyyy" value="${dcrctu.dtAtlCtu}"/>
            </td>
          </tr>
        </table>
      </div>
			
			
			<!-- GIF Loaders tanto para chamada da modal quanto das funções mostrarCarregando e aposCarregamento ver (index.js) -->			
			<div id="carregando" style="display: none;">
				<div style="text-align: center;">
          <img src="images/loading.gif" border="0" height="80" width="80" /><br><span>Aguarde...</span>
				</div>
			</div>						
			<!-- Fim do GIF Loaders -->	
				
		</div>
<!--  Fim Manter -->
		
	</body>
	
	
	<script type="text/javascript" src="/header/js/index.js" charset="UTF-8"></script>
	
  
	<script>
    $("#carregando").show();
    $("#conteudo").load("login", function() {
      $("#carregando").hide();
    });
    
    function appendTime(time) {
      var h = document.createElement("h3");
      $(h).html(time);
      document.body.appendChild(h);
    }
    
    $.micro.get("http://172.16.142.25:9088/time", appendTime);
	</script>


</html>