
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

