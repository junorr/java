
requirejs(["extensions.js"]);


function ext(path) {
  return path.substring(
      path.lastIndexOf("."), 
      path.length
  ).toLowerCase();
}


function findIcon(path) {
  var icon = "fa-file-o";
  for(var i = 0; i < exts.length; i++) {
    //exts -> extensions.js
    if(exts[i].ext === ext(path)) {
      icon = exts[i].icon;
    }
  }
  return icon;
}


function getSize(file) {
  return file.size.formatted;
}


function getTime(seconds) {
  if(!seconds || seconds < 1) {
    return new FSize(0).toString();
  }
  var t = new Date(1970, 0, 1); // Epoch
  t.setSeconds(seconds);
  return t.toLocaleString();
}


function fileName(path) {
  if(!path 
      || typeof path !== 'string') {
    return path;
  }
  var i = path.lastIndexOf("/");
  var rpath = path;
  if(i >= 0) {
    rpath = path.substring(
      path.lastIndexOf("/")+1, path.length);
  }
  return encodeURI(rpath);
}


function get(path) {
  //console.log("* get: "+ path);
  url = "ls/"+ fileName(path);
  if(!path) {
    url = "ls";
  }
  //console.log("* get.url: "+ url);
  $.get(url, function(data) {
    //console.log("* get.data: "+ data);
    curdir.dir = data[0];
    data.shift();
    curdir.ls = data;
  }, "json");
}


function parent() {
  $.get("ls-up", function(data) {
    curdir.dir = data[0];
    data.shift();
    curdir.ls = data;
  }, "json");
}


function download(path) {
  if(!path || typeof path !== 'string') {
    return;
  }
  return "get/"+btoa(path);
}


var curdir = new Vue({
  el: "#current-dir",
  data: {
    dir: false,
    ls: false
  }
});


get();