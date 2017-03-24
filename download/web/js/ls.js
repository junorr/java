

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



var curdir = new Vue({
  el: "#current-dir",
  data: {
    dir: false
  },
  computed: {
    size: function() {
      return new FSize(this.dir.size.bytes).toString();
    },
    modTime: function() {
      var t = new Date(1970, 0, 1); // Epoch
      t.setSeconds(this.dir.time.modified.seconds);
      return t.toLocaleString();
    },
    creTime: function() {
      var t = new Date(1970, 0, 1); // Epoch
      t.setSeconds(this.dir.time.modified.seconds);
      return t.toLocaleString();
    },
    acsTime: function() {
      var t = new Date(1970, 0, 1); // Epoch
      t.setSeconds(this.dir.time.modified.seconds);
      return t.toLocaleString();
    }
  }
});


$.get("ls", function(data) {
  curdir.dir = data;
}, "json");