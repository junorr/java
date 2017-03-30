

function FSize(size) {

  var ref = this;

  ref.bytes = size;
  
  ref.BYTE = {
    name: "Bytes",
    size: 0
  };
  
  ref.KB = {
    name: "KB",
    size: 1024
  };
  
  ref.MB = {
    name: "MB",
    size: ref.KB.size * 1024
  };
  
  ref.GB = {
    name: "GB",
    size: ref.MB.size * 1024
  };
  
  ref.TB = {
    name: "TB",
    size: ref.GB.size * 1024
  };
  
  ref.PB = {
    name: "PB",
    size: ref.TB.size * 1024
  };
  
  ref.EB = {
    name: "EB",
    size: ref.PB.size * 1024
  };
  
  
  var calcUnit = function() {
    var unit = ref.BYTE;
    if(ref.bytes >= ref.EB.size) {
      unit = ref.EB;
    }
    else if(ref.bytes >= ref.PB.size) {
      unit = ref.PB;
    }
    else if(ref.bytes >= ref.TB.size) {
      unit = ref.TB;
    }
    else if(ref.bytes >= ref.GB.size) {
      unit = ref.GB;
    }
    else if(ref.bytes >= ref.GB.size) {
      unit = ref.GB;
    }
    else if(ref.bytes >= ref.MB.size) {
      unit = ref.MB;
    }
    else if(ref.bytes >= ref.KB.size) {
      unit = ref.KB;
    }
    else if(ref.bytes >= ref.BYTE.size) {
      unit = ref.BYTE;
    }
    return unit;
  };
  
  ref.unit = calcUnit();
  
  var float = function(n) {
    return parseFloat(n);
  };
  
  ref.value = function() {
    if(ref.bytes === 0) return 0;
    return float(float(ref.bytes) / float(ref.unit.size)).toFixed(2);
    /*
    var sd = float(float(ref.bytes) / float(ref.unit.size));
    //console.log("sd="+ sd);
    var i = parseInt(sd);
    //console.log("i="+ i);
    var d = parseInt(Math.round(float(sd - float(i)) * Math.pow(10, 2)));
    //console.log("d="+ d);
    //console.log("value="+ parseFloat(i + d / Math.pow(10, 2)));
    var sz = float(i + d / Math.pow(10, 2));
    i = sz.to*/
  };

  ref.toString = function() {
    return ref.value() + " " + ref.unit.name;
  };
  
}


var exts = [
  //packages
  {ext: ".zip", icon: "fa-file-archive-o"},
  {ext: ".7z", icon: "fa-file-archive-o"},
  {ext: ".tar", icon: "fa-file-archive-o"},
  {ext: ".gz", icon: "fa-file-archive-o"},
  {ext: ".bz", icon: "fa-file-archive-o"},
  {ext: ".jz", icon: "fa-file-archive-o"},
  {ext: ".rar", icon: "fa-file-archive-o"},
  {ext: ".jar", icon: "fa-file-archive-o"},
  {ext: ".war", icon: "fa-file-archive-o"},
  {ext: ".tgz", icon: "fa-file-archive-o"},
  {ext: ".z", icon: "fa-file-archive-o"},
  //audio
  {ext: ".mp3", icon: "fa-file-audio-o"},
  {ext: ".ogg", icon: "fa-file-audio-o"},
  {ext: ".aif", icon: "fa-file-audio-o"},
  {ext: ".m2a", icon: "fa-file-audio-o"},
  {ext: ".m3u", icon: "fa-file-audio-o"},
  {ext: ".mid", icon: "fa-file-audio-o"},
  {ext: ".mid", icon: "fa-file-audio-o"},
  {ext: ".midi", icon: "fa-file-audio-o"},
  {ext: ".mp2", icon: "fa-file-audio-o"},
  {ext: ".mpg", icon: "fa-file-audio-o"},
  //code
  {ext: ".js", icon: "fa-file-code-o"},
  {ext: ".css", icon: "fa-file-code-o"},
  {ext: ".java", icon: "fa-file-code-o"},
  {ext: ".c", icon: "fa-file-code-o"},
  {ext: ".html", icon: "fa-file-code-o"},
  {ext: ".xhtml", icon: "fa-file-code-o"},
  {ext: ".ini", icon: "fa-file-code-o"},
  {ext: ".xml", icon: "fa-file-code-o"},
  {ext: ".sh", icon: "fa-file-code-o"},
  //excel
  {ext: ".xls", icon: "fa-file-excel-o"},
  {ext: ".xlsx", icon: "fa-file-excel-o"},
  {ext: ".csv", icon: "fa-file-excel-o"},
  {ext: ".ods", icon: "fa-file-excel-o"},
  //images
  {ext: ".jpg", icon: "fa-file-image-o"},
  {ext: ".jpeg", icon: "fa-file-image-o"},
  {ext: ".png", icon: "fa-file-image-o"},
  {ext: ".gif", icon: "fa-file-image-o"},
  {ext: ".svf", icon: "fa-file-image-o"},
  {ext: ".svg", icon: "fa-file-image-o"},
  {ext: ".ico", icon: "fa-file-image-o"},
  {ext: ".bmp", icon: "fa-file-image-o"},
  {ext: ".pic", icon: "fa-file-image-o"},
  {ext: ".rgb", icon: "fa-file-image-o"},
  {ext: ".tif", icon: "fa-file-image-o"},
  //video
  {ext: ".mov", icon: "fa-file-video-o"},
  {ext: ".mpeg", icon: "fa-file-video-o"},
  {ext: ".mpa", icon: "fa-file-video-o"},
  {ext: ".mv", icon: "fa-file-video-o"},
  {ext: ".rv", icon: "fa-file-video-o"},
  {ext: ".vdo", icon: "fa-file-video-o"},
  {ext: ".afl", icon: "fa-file-video-o"},
  {ext: ".asf", icon: "fa-file-video-o"},
  {ext: ".asx", icon: "fa-file-video-o"},
  {ext: ".mp4", icon: "fa-file-video-o"},
  {ext: ".avi", icon: "fa-file-video-o"},
  //pdf
  {ext: ".pdf", icon: "fa-file-pdf-o"},
  //word
  {ext: ".doc", icon: "fa-file-word-o"},
  {ext: ".docx", icon: "fa-file-word-o"},
  {ext: ".odt", icon: "fa-file-word-o"},
  //text
  {ext: ".txt", icon: "fa-file-text-o"},
  //exe
  {ext: ".exe", icon: "fa-gears"},
  {ext: ".msi", icon: "fa-gears"},
  {ext: ".dll", icon: "fa-gears"},
  {ext: ".so", icon: "fa-gears"}
];


function ext(path) {
  return path.substring(
      path.lastIndexOf("."), 
      path.length
  ).toLowerCase();
}


function findIcon(path) {
  var icon = "fa-file-o";
  for(var i = 0; i < exts.length; i++) {
    if(exts[i].ext === ext(path)) {
      icon = exts[i].icon;
    }
  }
  return icon;
}


function getSize(file) {
  console.log("file.size.bytes: "+ file.size.bytes);
  if(!file || typeof file.size.bytes === 'undefined') {
    return new FSize(0).toString();
  }
  return new FSize(file.size.bytes).toString();
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
  console.log("* get: "+ path);
  url = "ls/"+ fileName(path);
  if(!path) {
    url = "ls";
  }
  console.log("* get.url: "+ url);
  $.get(url, function(data) {
    console.log("* get.data: "+ data);
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
  return "get/"+encodeURI(btoa(path));
}


var curdir = new Vue({
  el: "#current-dir",
  data: {
    dir: false,
    ls: false
  }
});


get();