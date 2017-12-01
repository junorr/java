var elt = document.getElementById("out");

setTimeout(function() {
  elt.innerHTML = "WebResource works with Javascript!";
}, 6000);

var count = 5;

function countDown() {
  count--;
  if(count >= 0) {
    elt.innerHTML = "[" + count + "] " + elt.innerHTML.substring(4);
    setTimeout(countDown, 1000);
  }
}

setTimeout(countDown, 1000);
