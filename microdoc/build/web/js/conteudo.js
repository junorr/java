function showBody(cdedp) {
  var body = $("#panel-body_"+ cdedp);
  var isShown = body.attr("data-shown") === "true";
  if(isShown) {
    body.hide('blind');
  }
  else {
    body.show('blind');
    methods(cdedp);
  }
  body.attr("data-shown", !isShown);
}


var endpoints = new Vue({
  el: "#endpoints",
  data: {
    edps: false,
    meths: false
  }
});

//var url = "http://disec5.intranet.bb.com.br:9088/";
var url = "http://localhost.bb.com.br:9088";

$.micro().post(url+ "/sql/microdoc/endpoints", {cachettl: 3600}, function(data) {
  endpoints.edps = data;
});


function methods(cdedp) {
  $.micro().post(url+"/sql/microdoc/methods", {cachettl: 3600, args: [cdedp]}, function(resp) {
    console.log("* methods: "+ JSON.stringify(resp.data));
    $("#method_"+ cdedp).html("");
    for(var i = 0; i < resp.data.length; i++) {
      console.log("* method: "+ resp.data[i].method);
      var li = document.createElement("li");
      $(li).addClass("list-group-item");
      var spm = document.createElement("span");
      $(spm).addClass("btn-style fbold fsize-16 bg-primary rounded")
          .html(resp.data[i].method);
      var spd = document.createElement("span");
      $(spd).addClass("desc fbold fsize-14")
          .html(resp.data[i].tx_dsc);
      $(li).append(spm).append(spd);
      params(li, resp.data[i].cd_method);
      response(li, resp.data[i].cd_method);
      $("#method_"+ cdedp).append(li);
    }
  });
}


function params(li, cdmt) {
  $.micro().post(url+"/sql/microdoc/params", {cachettl: 3600, args: [cdmt]}, function(pars) {
    for(var i = 0; i < pars.data.length; i++) {
      var hr = document.createElement("hr");
      var p = document.createElement("span");
      $(p).addClass("fsize-14");
      var spt = document.createElement("span");
      $(spt).addClass("fbold").html(pars.data[i].tx_tip_param);
      var br = document.createElement("br");
      $(p).append(spt).append(br);
      var sp = document.createElement("span");
      $(p).append(
          $(sp).clone().addClass("fmono").html("&bullet; Nome.....: "+ pars.data[i].nome)
      ).append($(br).clone());
      $(p).append(
          $(sp).clone().addClass("fmono").html("&bullet; Descri&ccedil;&atilde;o: "+ pars.data[i].tx_dsc)
      ).append($(br).clone());
      $(p).append(
          $(sp).clone().addClass("fmono").html("&bullet; Exemplo..: "+ pars.data[i].tx_exemplo)
      );
      $(li).append(hr).append(p);
    }
  });
}


function response(li, cdmt) {
  $.micro().post(url+"/sql/microdoc/response", {cachettl: 3600, args: [cdmt]}, function(pars) {
    for(var i = 0; i < pars.data.length; i++) {
      var hr = document.createElement("hr");
      var p = document.createElement("span");
      $(p).addClass("fsize-14");
      var spt = document.createElement("span");
      $(spt).addClass("fbold").html("HTTP Response");
      var br = document.createElement("br");
      $(p).append(spt).append(br);
      var sp = document.createElement("span");
      $(p).append(
          $(sp).clone().addClass("fmono").html("&bullet; Status...: "+ pars.data[i].resp_code + " - " + pars.data[i].tx_resp)
      ).append($(br).clone());
      $(p).append(
          $(sp).clone().addClass("fmono").html("&bullet; Descri&ccedil;&atilde;o: "+ pars.data[i].tx_dsc)
      ).append($(br).clone());
      $(li).append(hr).append(p);
    }
  });
}