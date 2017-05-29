<a href="" id="xls-link">Download .xls</a></br>
<a href="" id="csv-link">Download .csv</a></br></br>

<div id="divtable" style="text-align: center;">
  <img src="images/loading.gif" border="0" height="30" width="30" /><br><span>Aguarde...</span>
</div>

<script type="text/javascript" src="js/sstable.js"></script>
<script type="text/javascript">
  /*
  var opts = {
    url: "http://127.0.0.1:9088/sql",
    group: "orc",
    query: "dspTipoMes",
    cachettl: 600,
    args: ["PACOTE", 2],
    columns: [
      {id: "NOME", sort: true, filter: true},
      {id: "ORCADO", title: "Orçado", css: {"text-align": "right"}, sort: true, filter: true, format: function(val){return formatNumber(val.roundDouble(2));}},
      {id: "PJTD_EXIBIR", title: "Projetado", css: {"text-align": "right"}, sort: true, filter: true, format: function(val){return formatNumber(val.roundDouble(2));}},
      {id: "REALIZADO", title: "Realizado", css: {"text-align": "right"}, sort: true, filter: true, format: function(val){return formatNumber(val.roundDouble(2));}},
      {id: "DSV_VLR", title: "Desvio $", css: {"text-align": "right"}, sort: true, filter: true, format: function(val){return formatNumber(val.roundDouble(2));}},
      {id: "DSV_PER", title: "Desvio %", css: {"text-align": "right"}, sort: true, filter: true, format: function(val){return formatNumber(val.roundDouble(2));}}
    ],
    pagination: 5
  };*/
  
  
  new AjaxCors("http://172.16.142.32:9088/export")
      .post({
        //group: "orc", 
        //query: "dspTipoMes", 
        //args: ["PACOTE", 2], 
        group: "ordServ",
        query: "extDepe",
        cachettl: 3600,
        //limit: [1000,3000],
        format: "xls"
      }, function(status, resp) {
        document.getElementById("xls-link").href = "http://172.16.142.32:9088/extract/"+ resp;
      }
    );
  
  
  new AjaxCors("http://172.16.142.32:9088/export")
      .post({
        //group: "orc", 
        //query: "dspTipoMes", 
        //args: ["PACOTE", 2], 
        group: "ordServ",
        query: "extDepe",
        cachettl: 3600,
        //limit: [1000,1000],
        format: "csv"
      }, function(status, resp) {
        document.getElementById("csv-link").href = "http://172.16.142.32:9088/extract/"+ resp;
      }
    );
  
  
  opts = {
    url: "http://172.16.142.32:9088/sql",
    group: "ordServ",
    query: "extDepe",
    cachettl: 3600,
    columns: [
      {id: "OSRV", title: "Ordem Serviço", sort: true},
      {id: "ANO_OSRV", title: "Ano", css: {"text-align": "center"}, sort: true, filter: true},
      {id: "NRO_OSRV", title: "Número", css: {"text-align": "right"}, sort: true},
      {id: "NM_SIT_OSRV", title: "Situação", sort: true, filter: true},
      {id: "PRF_DEPE_REQS", title: "Prefixo Depe", css: {"text-align": "center"}, sort: true, filter: true},
      {id: "NM_UOR_RED_REQS", title: "Nome Depe", sort: true, filter: true}
    ],
    pagination: 20
  };
  
  var sstable = new SSTable("divtable", opts)
          .setDefaultCss(true)
          .setMultiSelection(false)
          .createTable();
  sstable.onselect = function(idx, json) {
    console.log("* selected: "+ JSON.stringify(sstable.selected)+ ", "+ JSON.stringify(json));
  };
  
</script>