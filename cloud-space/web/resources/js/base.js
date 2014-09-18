var wmod = 500;
var defw = 3120.0 + wmod;
var defh = 2170;

$(document).ready(function () {
  var sw = $(this).width() + wmod;
  var sh = $(this).height();
  var perc = sw / defw;
  $("body").css("background-size", (sw+ "px "+ (defh * perc)+ "px"));
  linkCurrent(currlink);
});
      
function linkCurrent(linkID) {
  $("#"+ linkID).children(".lk-img").hide();
  $("#"+ linkID).children(".lk-img-over").hide();
  $("#"+ linkID).children(".lk-img-curr-over").hide();
  $("#"+ linkID).children(".lk-img-current").show();
}

function linkOver(link) {
  $(link).children(".lk-img").hide();
  $(link).children(".lk-img-current").hide();
  if($(link).attr("id") === currlink)
    $(link).children(".lk-img-curr-over").show();
  else
    $(link).children(".lk-img-over").show();
}

function linkExit(link) {
  $(link).children(".lk-img-over").hide();
  $(link).children(".lk-img-curr-over").hide();
  if($(link).attr("id") === currlink)
    $(link).children(".lk-img-current").show();
  else
    $(link).children(".lk-img").show();
}
