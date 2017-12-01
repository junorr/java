var ELT = $("#msg");

function finalMsg() {
  ELT.html("KumuluzEE Hello Successful with static Resources!");
}

var COUNT = 5;

function countDown() {
  COUNT--;
  if(COUNT >= 0) {
    ELT.html("["+COUNT+"] "+ELT.html().substring(4));
    setTimeout(countDown, 1000);
  }
}

setTimeout(countDown, 1000);
setTimeout(finalMsg, 6000);