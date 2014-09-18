$(document).ready(function() {
  
  //altera o tamanho do site dinamicamente conforme a largura do navegador
  //mantendo o tamanho mínimo em 1000px.
  var browserWidth = $(document).width();
  var pageWidth = 1000;
  if(browserWidth > 1015) {
    pageWidth = browserWidth - 15;
  }
  $("#header").width(pageWidth);
  $("#header-bg").width(pageWidth);
  $("#content").width(pageWidth - 255);
  $("#content-bg").width(pageWidth - 255);
  $("#content-text").width(pageWidth - 273);
  
  
  
	tab("header").centerHorizontal("header-text");
  
	$("#header-text").css("top", 40);
  
	tab("header").centerHorizontal("header-phrase")
		.after("header-text").setVertical("header-phrase", 15);
    
	tab("document").west("header", 5).north("header", 0);
  
  tab("document").west("sidebar", 5)
    .after("header").setVertical("sidebar", 5);
    
  tab("document").after("header").setVertical("content", 5)
    .after("sidebar").setHorizontal("content", 5);
    
  tab("content").centerHorizontal("madeby").south("madeby", 2);
  
  tab("content").north("content-text", 15).west("content-text", 15);
  
  tab("content").south("firefoxbtn", 0).east("firefoxbtn", -10);
  
  
  //verify url
  var url = $(location).attr("href");
  if(url && url.indexOf("#") > 0) {
    var redir = url.substring(url.indexOf("#") +1, url.length);
    url = url.substring(0, url.indexOf("faces/") +6) + redir;
    $(location).attr("href", url);
  }
});
