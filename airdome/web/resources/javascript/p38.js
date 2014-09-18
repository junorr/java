$(document).ready(function() {
  tab("content-text").centerHorizontal("p38-1")
    .after("p38-1").setVertical("intro", 5)
    .after("intro").setVertical("p38-2", 10)
    .centerHorizontal("p38-2")
    .centerHorizontal("p38-2_label")
    .after("p38-2").setVertical("p38-2_label", 0)
    .after("p38-2_label").setVertical("europe", 5);
});