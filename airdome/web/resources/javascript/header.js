function toggleMask() {
  var visible = $("#mask").css("visibility");
  if(visible == "hidden") {
    visible = "visible";
  } else {
    visible = "hidden";
  }
  $("#mask").width($(document).width());
  $("#mask").height($(document).height());
  $("#mask").css("visibility", visible);
}


$(document).ready(function() {
  Button("loginButton").createButton(function() {
    toggleMask();
  }).size(90, 30).north(5).east(5);
});