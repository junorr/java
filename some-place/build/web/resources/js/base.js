
$(document).ready(function () {
  linkCurrent();
  $(".link").mouseenter(linkEnter);
  $(".link").mouseleave(linkExit);
  
  //position footer
  var doch = $(document).height();
  var docw = $(document).width();
  var ftw = $(".footer-div").width();
  var fth = $(".footer-div").height();
  
  $(".footer-div").css("left", (docw / 2 - ftw / 2));
  $(".footer-div").css("top", (doch - fth - 5));
  
  //position content
  var ctw = $(".content-div").width();
  $(".content-div").css("left", (docw / 2 - ctw / 2));
});
      
function linkCurrent() {
  curr = $("#formhd\\:actlink").val();
  console.log("actlink="+ curr);
  $(".link").children(".link-over").hide();
  $("."+ curr).children(".link-over").show();
}

function linkEnter() {
  if(!$(this).hasClass($("#formhd\\:actlink").val()))
    $(this).children(".link-over").slideDown(300);
}

function linkExit() {
  if(!$(this).hasClass($("#formhd\\:actlink").val()))
    $(this).children(".link-over").slideUp(300);
}
