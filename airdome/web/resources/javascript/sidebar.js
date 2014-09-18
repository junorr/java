$(document).ready(function() {
  Link("p38-link").size(160, 108).sizeImg2(220, 168).moveImg2(-30, -20)
    .centerHorizontal().north(15)
    .ajaxChangeTitle("P-38 Lightning")
    .bookmarkableURL("airplanes/p38.xhtml")
    .createAjaxLink("airplanes/p38-ajax.xhtml", "content-text");
    
  Link("bf109-link").size(160, 80).sizeImg2(220, 140).moveImg2(-30, -20)
    .centerHorizontal()
    .after("p38-link").setVertical(40)
    .ajaxChangeTitle("Messerschmitt Bf 109")
    .bookmarkableURL("airplanes/bf109.xhtml")
    .createAjaxLink("airplanes/bf109-ajax.xhtml", "content-text");
});