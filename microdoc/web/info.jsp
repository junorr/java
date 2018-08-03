<h1 style="${auth ? 'color: blue;' : 'color: red;'}">
  ${auth ? "Login Successful!" : "Login Failed!"}
</h1>
<ul style="font-family: 'monospace'; ${auth ? '' : 'display: none;'}">
  <li>Matrícula.................: ${user.chave}</li>
  <li>Nome......................: ${user.nome}</li>
  <li>Nome Guerra...............: ${user.nomeGuerra}</li>
  <li>ChaveNome.................: ${user.chaveNome}</li>
  <li>Prefixo Dependência.......: ${user.prefixoDepe}</li>
  <li>UOR Dependência...........: ${user.uorDepe}</li>
  <li>UOR Equipe................: ${user.uorEquipe}</li>
</ul>

<button onclick="goToUpload();">Upload</button>&nbsp;&nbsp; <img src="images/loading.gif" id="btn-loading" style="display: none;"/>

<script>
  
  function goToUpload() {
    $("#btn-loading").show();
    $("#conteudo").load("home?page=upload.jsp", function() {
      $("#btn-loading").hide();
    });
  }
  
</script>