<%@page language="java" trimDirectiveWhitespaces="true" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />

<style>
	.fmtComment {
		font-size: 14px;
		color: gray;
	}
	#uploadDiv {
		font-size: 14px;
		border: solid 4px rgba(60,60,120,0.7);
		border-radius: 8px;
		box-shadow: 6px 6px 5px rgba(60,60,60,0.8);
		background-color: rgba(245, 245, 250, 0.6);
	}
	#uploadResp {
		display: none;
		margin-top: 10px;
		margin-left: 10px;
	}
	#reloadpage {
		display: none;
		margin-top: 10px;
	}
	#innerUpload {
		display: inline-block;
		margin-left: 10px;
		margin-top: 13px;
	}
	#timeval {
		color: red;
	}
</style>

<h3 style="left: 0px;">Arquivos Principais</h3>
<table  class="tbArquivos">
	<thead>
		<tr>
			<th>Nome</th>
			<th>Cod. UOR</th>
			<th>Data Atualização</th>	
			<th>Descrição</th>
			<th>Baixar</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${arquivos}" var="arq">
		<tr>
			<td><a href="/arqRedeDinop/exiba?cmdo=arqRedeDinop.filedownload&arqId=${arq.id}&label=${arq.label}">${arq.nome}</a></td>
			<td>${arq.uor}</td>
			<td><fmt:formatDate value="${arq.atualizacao}" pattern="dd/MM/yyyy HH:mm:ss" var="dtAtl" ></fmt:formatDate>${dtAtl}</td>
			<td>${arq.descricao}</td>
			<td>
				<center>
				<a href="/arqRedeDinop/exiba?cmdo=arqRedeDinop.filedownload&arqId=${arq.id}&label=${arq.label}">
					<img src="/arqRedeDinop/images/download.png" 
						 alt="Baixar Arquivo" style="width: 20px; height: 20px;"></img>
				</a>
				</center>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
<br/>
<div style="${auxiliares.isEmpty() ? 'display: none; height: 1px;' : 'display: block;'}">
<h3 style="left: 0px;">Arquivos Auxiliares</h3>
<table  class="tbArqAuxiliares">
	<thead>
		<tr>
			<th>Nome</th>
			<th>Data Atualização</th>	
			<th>Descrição</th>
			<th>Baixar</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${auxiliares}" var="aux">
		<tr>
			<td><a href="/arqRedeDinop/exiba?cmdo=arqRedeDinop.filedownload&arqId=${aux.id}&label=${aux.label}">${aux.nome}</a></td>
			<td><fmt:formatDate value="${aux.atualizacao}" pattern="dd/MM/yyyy HH:mm:ss" var="dtAtl" ></fmt:formatDate>${dtAtl}</td>
			<td>${aux.descricao}</td>
			<td>
				<center>
				<a href="/arqRedeDinop/exiba?cmdo=arqRedeDinop.filedownload&arqId=${aux.id}&label=${aux.label}">
					<img src="/arqRedeDinop/images/download.png" 
						 alt="Baixar Arquivo" style="width: 20px; height: 20px;"></img>
				</a>
				</center>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</br>

<!-- c:if test="${displayUploadForm}"-->
	<h3>Atualizar Arquivos</h3>
	<div id='uploadDiv'>
		<div id="innerUpload">
		
			<input id='inome' name='inome' type='hidden' value='${usuario.getNomeGuerra()}'/>
			<input id='ichave' name='ichave' type='hidden' value='${usuario.getChave()}'/>
			<input id='iuor' name='iuor' type='hidden' value='${usuario.getCodigoUorDependencia()}'/>
			<input id='icmss' name='icmss' type='hidden' value='${usuario.getCodigoComissaoUsuario()}'/>
			<input id='iuoreqp' name='iuoreqp' type='hidden' value='${usuario.getCdUorEqp()}'/>
			<input id='ilabel' name='ilabel' type='hidden' value='${label}'/>
		
			Atualizar arquivo: <input type="file" id="ifile" name="ifile" size="10485760"/><button id="btnSubmit">Carregar</button><progress style='display: none;'></progress>
		</div>
		
		<div id='uploadResp'></div>
		
		<div id='reloadpage'><h3>A Página será recarregada em <span id='timeval'>10</span> segundos...</h3></div>
	</div>
<!-- /c:if-->

<script>
	function atualizarProgresso(e) {
		if(e.lengthComputable) {
			$('progress').show();
	        $('progress').attr({value:e.loaded,max:e.total});
	    }	
	}
	
	var timecount = 10;
	var reloadvar = null;
	
	$(document).ready(function() {	
		
		if($("#btnSubmit")) {
			$("#btnSubmit").click(function(e) {
				e.preventDefault();
				$("#uploadResp").hide();
				
				var fileup = $("#ifile")[0].files[0];
								
				if(!fileup) {
					alert("Nenhum arquivo selecionado!");
					return;
				}

				var ilabel = $("#ilabel").val();
					
				var fdata = new FormData();
				fdata.append("inome", $("#inome").val());
				fdata.append("ichave", $("#ichave").val());
				fdata.append("iuor", $("#iuor").val());
				fdata.append("icmss", $("#icmss").val());
				fdata.append("iuoreqp", $("#iuoreqp").val());
				fdata.append("ilabel", ilabel);
				fdata.append("ifile", fileup);
					
				$.ajax({
					url: '/arqRedeDinop/uploadfile',
					type: 'POST',
					xhr: function() {  // Custom XMLHttpRequest
			            var myXhr = $.ajaxSettings.xhr();
			            if(myXhr.upload){ // Check if upload property exists
			                myXhr.upload.addEventListener('progress',
			                		atualizarProgresso, false); // For handling the progress of the upload
			            }
			            return myXhr;
			        },
			        processData: false,
			        dataType: 'text',
					data: fdata,
					contentType: false,
					success: function(res) {
						$("#uploadResp").html(res);
						$("#uploadResp").show();
						window.scrollTo(0,document.body.scrollHeight);
						
						if($("#success") === "true") {
							$("#reloadpage").show();
							reloadvar = setInterval(function() {
								timecount--;
								$("#timeval").html(timecount);
								if(timecount <= 0) {
									clearInterval(reloadvar);
									
									//recarrega página
									$("#carregando").modal({});
									$("#tabelaAtb").empty();
									$("#tabelaAtb").load('/arqRedeDinop/exiba?cmdo=arqRedeDinop.filetables&label='+ ilabel, 
										function() {
											$.modal.close();
										}
									);
								}
							}, 1000);
						}//if success
					}//success function
				});//ajax
			});//button click
		}
		
		
		$(".tbArqAuxiliares tbody").on('click', function(event) {
			$(oTableExample.fnSettings().aoData).each(function (){
				$(this.nTr).removeClass('row_selected');
			});
			$(event.target.parentNode).addClass('row_selected');
		});
		
		$(document).ready(function() {
			$('.tbArqAuxiliares tbody tr').on('click', function() {
				$('.tbArqAuxiliares tbody tr').removeClass('selected');
				$(this).addClass('selected');
			});
		});
		$('.tbArqAuxiliares tbody tr').mouseenter(function(){
				if($(this).hasClass('odd')){
					$('td', $(this)).addClass("inside");	
				}else if($(this).hasClass('even')){
					$('td', $(this)).addClass("inside");
				}else{
					$(this).css('cursor', 'default');
				}
		});
		$('.tbArqAuxiliares tbody tr').mouseleave(function(){
			if($(this).hasClass('odd')){
				$('td', $(this)).removeClass("inside");	
			}else if($(this).hasClass('even')){
				$('td', $(this)).removeClass("inside");	
			}else{
				$(this).css('cursor', 'default');
			}
		});
	    var oTableExample = $('.tbArqAuxiliares').dataTable( {
	    	"sDom": '<"H"Tfrl>t<"F"ip>',
            "oTableTools": {
                "aButtons": [
					{
					    "sExtends": "copy",
					    "sButtonText": "Copiar"
					},
                    {
                        "sExtends": "print",
                        "sButtonText": "Imprimir"
                    }
                ]
            },
            
	    	"bJQueryUI": true,
			"bStateSave": true,
			"bLengthChange": true,
			"iDisplayLength": 30,
			"aLengthMenu":[[30,50,100,-1],[30,50,100,"Todos"]],
			"oLanguage": {
		      "oPaginate": {
		        "sNext": "Próximo",
		   		"sPrevious": "Prévio",
		   		"sEmptyTable": "Não há dados disponíveis.",
		   		"sInfo": "Total de _TOTAL_ registros filtrados (_START_ to _END_)",
		   		"sInfoEmpty": "Não há registros disponíveis",
		   		"sInfoFiltered": " - filtrando de _MAX_ records",
		   		"sInfoPostFix": "Todos os registros selecionados advém de informações reais.",
		   		"sLoadingRecords": "Favor, aguarde - carregando...",
		   		"sSearch": "Registros filtrados:"
		      }
		    },
//		    "sScrollX": "100%",
		    "bDestroy":  true,
		    "bRetrieve": true,
//		    "aoColumns": 
		    //"bAutoWidth": true
//		    "aoColumns": [
//						  { "sWidth": "150px"},
//						  { "sWidth": "250px"},
//						  { "sWidth": "450px"},
//			              { "sWidth": "50px"},
//			              { "sWidth": "50px"}
//			]
	    });
		
		$(".tbArquivos tbody").on('click', function(event) {
			$(oTableExample.fnSettings().aoData).each(function (){
				$(this.nTr).removeClass('row_selected');
			});
			$(event.target.parentNode).addClass('row_selected');
		});
		
		$(document).ready(function() {
			$('.tbArquivos tbody tr').on('click', function() {
				$('.tbArquivos tbody tr').removeClass('selected');
				$(this).addClass('selected');
			});
		});
		$('.tbArquivos tbody tr').mouseenter(function(){
			if($(this).hasClass('odd')){
				$('td', $(this)).addClass("inside");	
			}else if($(this).hasClass('even')){
				$('td', $(this)).addClass("inside");
			}else{
				$(this).css('cursor', 'default');
			}
		});
		$('.tbArquivos tbody tr').mouseleave(function(){
			if($(this).hasClass('odd')){
				$('td', $(this)).removeClass("inside");	
			}else if($(this).hasClass('even')){
				$('td', $(this)).removeClass("inside");	
			}else{
				$(this).css('cursor', 'default');
			}
		});
	    var oTableExample = $('.tbArquivos').dataTable( {
	    	"sDom": '<"H"Tfrl>t<"F"ip>',
	    	"oTableTools": {
                "aButtons": [
					{
					    "sExtends": "copy",
					    "sButtonText": "Copiar"
					},
                    {
						"sExtends": "print",
						"sButtonText": "Imprimir"
					}
              ]
           },
            
	    	"bJQueryUI": true,
			"bStateSave": true,
			"bLengthChange": true,
			"iDisplayLength": 30,
			"aLengthMenu":[[30,50,100,-1],[30,50,100,"Todos"]],
			"oLanguage": {
		      "oPaginate": {
		        "sNext": "Próximo",
		   		"sPrevious": "Prévio",
		   		"sEmptyTable": "Não há dados disponíveis.",
		   		"sInfo": "Total de _TOTAL_ registros filtrados (_START_ to _END_)",
		   		"sInfoEmpty": "Não há registros disponíveis",
		   		"sInfoFiltered": " - filtrando de _MAX_ records",
		   		"sInfoPostFix": "Todos os registros selecionados advém de informações reais.",
		   		"sLoadingRecords": "Favor, aguarde - carregando...",
		   		"sSearch": "Registros filtrados:"
		      }
		    },
//		    "sScrollX": "100%",
		    "bDestroy":  true,
		    "bRetrieve": true,
//		    "aoColumns": 
		    //"bAutoWidth": true
//		    "aoColumns": [
//						  { "sWidth": "150px"},
//						  { "sWidth": "250px"},
//						  { "sWidth": "450px"},
//			              { "sWidth": "50px"},
//			              { "sWidth": "50px"}
//			]
	    });	
	});
</script>