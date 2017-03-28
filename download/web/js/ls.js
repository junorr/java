

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
  
  ref.value = function() {
    var sd = parseFloat(parseFloat(ref.bytes) / parseFloat(ref.unit.size));
    console.log("sd="+ sd);
    var i = parseInt(sd);
    console.log("i="+ i);
    var d = parseInt(Math.round((sd - i) * Math.pow(10, 2)));
    console.log("d="+ d);
    console.log("value="+ parseFloat(i + d / Math.pow(10, 2)));
    return parseFloat(i + d / Math.pow(10, 2));
  };

  ref.toString = function() {
    return ref.value() + " " + ref.unit.name;
  };
  
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


function get(path) {
  url = "ls/"+ path;
  if(!path) {
    url = "ls";
  }
  $.get(url, function(data) {
    curdir.dir = data[0];
    data.shift();
    curdir.ls = data;
  }, "json");
}


var curdir = new Vue({
  el: "#current-dir",
  data: {
    dir: false,
    ls: false
  }
});


get();