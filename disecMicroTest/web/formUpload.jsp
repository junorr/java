<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<fmt:setLocale value="pt_BR" />

<p>Informe o arquivo de apuração manual a ser enviado, em seguida clique em "Transmitir Dados".</p>

<style>
	.fmtComment {
		font-size: 14px;
		color: gray;
	}
	#uploadDiv {
		font-size: 14px;
		border: solid 1px rgba(114,114,114,1.0);
		border-radius: 8px;
		/*box-shadow: 6px 6px 5px rgba(60,60,60,0.8);*/
		background-color: rgba(240, 240, 240, 0.6);
	}
	#uploadResp {
		display: none;
		margin-top: 10px;
		margin-left: 10px;
	}
	#reloadpage {
		display: none;
		margin-left: 10px;
		margin-top: -10px;
	}
	#innerUpload {
		display: inline-block;
		margin-left: 10px;
		margin-top: 13px;
		margin-bottom: 13px;
	}
	#timeval {
		color: red;
	}
	#loadinggif {
		width: 18px;
		height: 18px;
		display: none;
		position: relative;
		top: 5px;
	}
</style>

<script type="text/javascript" src="/disecAplic/js/fsize-formatter.js"></script>

<script>
	$(document).ready(function() {
	  	if(isAPIAvailable()) {
	    	$('#ifile').on('change', function(evento){
	    		var files = evento.target.files; // FileList object
				var file = files[0];
	
	    	  	var extensao = file.name.toLowerCase().substring(file.name.length-3, file.name.length);
	    	  	//if (extensao === 'csv' || extensao === 'ods'){
	    			handleFileSelect(evento, extensao);  
	    	  	//}
	    	  	/*else {
	    			alert('Atenção!\n\nO arquivo selecionado "'
	    				+ file.name + '('+extensao+')' + '" não é suportado.\n'
	    				+ 'Indique apenas arquivos com as extensões .csv ou .ods');
	    		  	$(this).val("");
	    		  	return;
	    	  	}*/
	      	});
	    }
	});
	
	
	function normalize(text) {
		if(!text || text === '') 
			return text;
		var cs = text.split('');
		var subs = false;
		var sb = '';
		for(var i = 0; i < cs.length; i++) {
			if(cs[i] === '\"') {
				subs = !subs;
				continue;
			}
			if(subs && cs[i] === ';') {
				sb += '.';
			} else {
				sb += cs[i];
			}
		}
		return sb;
	}
  
  
  	function isAPIAvailable() {
    	// Check for the various File API support.
    	if (window.File && window.FileReader && window.FileList && window.Blob) {
      		// Great success! All the File APIs are supported.
      		return true;
    	} else {
      		// source: File API availability - http://caniuse.com/#feat=fileapi
      		// source: <output> availability - http://html5doctor.com/the-output-element/
      		document.writeln('The HTML5 APIs used in this form are only available in the following browsers:<br />');
      		// 6.0 File API & 13.0 <output>
      		document.writeln(' - Google Chrome: 13.0 or later<br />');
      		// 3.6 File API & 6.0 <output>
      		document.writeln(' - Mozilla Firefox: 6.0 or later<br />');
      		// 10.0 File API & 10.0 <output>
      		document.writeln(' - Internet Explorer: Not supported (partial support expected in 10.0)<br />');
      		// ? File API & 5.1 <output>
      		document.writeln(' - Safari: Not supported<br />');
      		// ? File API & 9.2 <output>
      		document.writeln(' - Opera: Not supported');
      		return false;
    	}
  	}

  
  	function handleFileSelect(evt, ext) {
    	var files = evt.target.files; // FileList object
    	var file = files[0];

    	// read the file metadata
    	var output = '';
        output += '<span style="font-weight:bold;"><b>Nome do Arquivo: </b>' + escape(file.name) + '</span><br />\n';
        output += ' - Tipo: ' + (file.type || 'n/a') + '<br />\n';
        output += ' - Tamanho: ' + new FileSize(file.size).format() + '<br />\n';
        output += ' - Modificado em: ' + (file.lastModifiedDate ? file.lastModifiedDate.toLocaleDateString() : 'n/a') + '<br />\n';

    	// read the file contents
    	$('#ifile').css('display','none');
    	$('#btnSubmit').css('display','inline');
    	
    	if(ext === 'csv') {
    		printTable(file);
    	}

    	// post the results
    	$('#list').empty().append(output);
  	}

  
    function printTable(file) {
        $("#contents").empty().html('Aguarde a visualização dos dados...');
        var reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function(event) {
          	var csv = event.target.result;
          	csv = normalize(csv);
          	var data = $.csv.toArrays(csv);
          	var html = '';
          	var cont_row = 0;
          	var line_len = 0;
          	for(var row in data) {
          		if(!data[row]) continue;
          		line_len = data[row].length;
          		if(data[row].length != line_len) continue;
        		if (cont_row==0){
        			html+='<thead>';
        		}
        		else if (cont_row==1){
        			html+='<tbody>';
        		}
            	html += '<tr>\r\n';
            	for(var item in data[row]) {
    	        	if (cont_row==0){
    	        		html += '<th>' + data[row][item] + '</th>\r\n';
    	        	}
    	        	else {
    	        		html += '<td style="font-size: 10px !important" nowrap="nowrap">' + data[row][item] + '</td>\r\n';
            		}
            	}
          
            	html += '</tr>\r\n';
            	if (cont_row==0){
            		html += '</thead>';
            	}
            	cont_row++;
          	}
          	html += '</tbody>';
          	html = html.replace('<tr>\r\n</tr>',' ');
          	$('#contents').html(html);
          	var tbApr = $('#contents').dataTable({
        		"bJQueryUI": true,
    			"iDisplayLength": 10,
    			"bDestroy":  true,
    			"bRetrieve": true
          	});
          	//$("#inputs").empty().append("<input type='button' value='Transmitir Dados' id='btnSubmit'>");
        };
        reader.onerror = function(){ alert('Unable to read ' + file.fileName); };
        $("#ccc").remove();
 	}
  
  
	var timecount = 10;
	
	function reloadOnSuccess() {
		if(timecount < 1) {
			//recarrega página
			$("#carregando").modal({});
			$("#conteudo").empty();
			$("#conteudo").load('/disecAplic/exiba?cmdo=planEstrategico.conteudo', function() {
				$.modal.close();
			});

			return;
		}
		else if(timecount > 0) {
			timecount--;
			setTimeout(reloadOnSuccess, 1000);
		}
		$("#timeval").html(timecount);
	}//func realodOnSuccess
	

	function atualizarProgresso(e) {
		if(e.lengthComputable) {
			$('progress').show();
        	$('progress').attr({value:e.loaded,max:e.total});
    	}	
	}
	
	
	function processSubmit() {
		$("#uploadResp").hide();
		var fileup = $("#ifile")[0].files;
		if(!fileup) {
			alert("Nenhum arquivo selecionado!");
			return;
		}
		$("#loadinggif").show();
			
		var fdata = new FormData();
		fdata.append("iopt", $("#iopt").val());
		fdata.append("inome", $("#inome").val());
		fdata.append("ichave", $("#ichave").val());
		fdata.append("icmss", $("#icmss").val());
		fdata.append("icmssusu", $("#icmssusu").val());
		fdata.append("iuor", $("#iuor").val());
		fdata.append("iuoreqp", $("#iuoreqp").val());
		fdata.append("iprefixo", $("#iprefixo").val());
		for(var i = 0; i < fileup.length; i++) {
			fdata.append(("ifile"+ i), fileup[i]);
		}
		
		$.ajax({
			url: '/disecAplic/fileupload',
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
				$("#loadinggif").hide();
				$("#uploadResp").show();
				//window.scrollTo(0,document.body.scrollHeight);
				
				if($("#success").html() === "true") {
					timecount = 10;
					$("#reloadpage").show();
					setTimeout(reloadOnSuccess, 1000);
				}//if success
			}//success function
		});//ajax
	}//func processSubmit
  
</script>

</head>

<body>
<div id='uploadDiv' class="clearfix">
	<div id="innerUpload" style="border-bottom: 1px solid #CCCCCC">
		<input type="file" id="ifile" name="ifile"/>
		<button id="btnSubmit" style="display:none;" onclick="processSubmit();">Transmitir Dados</button>
		&nbsp;&nbsp;
		<img id="loadinggif" src="/disecAplic/images/loading.gif" style="display:none;"></img>
		&nbsp;&nbsp;
		<progress style='display: none;'></progress>
	</div>
	<div id='uploadResp'></div>
	<h4 id='reloadpage'>A Página será recarregada em <span id='timeval'>10</span> segundos...</h4>
</div>

<hr />
<output id='list'></output>
<hr />
<table class='display' id='contents' style="width:100%; height:300px; overflow:scroll">
</table>

</body>
