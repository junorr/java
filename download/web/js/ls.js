function isScrolledIntoView(elem) {
  var docViewTop = $(window).scrollTop();
  var docViewBottom = docViewTop + $(window).height();
  var elemTop = $(elem).offset().top;
  var elemBottom = elemTop + $(elem).height();
  return ((elemBottom >= docViewTop) && (elemTop <= docViewBottom) && (elemBottom <= docViewBottom) && (elemTop >= docViewTop));
}


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
  if(i < 0) i = path.lastIndexOf("\\");
  var rpath = path;
  if(i >= 0) {
    rpath = path.substring(
      i+1, path.length);
  }
  return encodeURI(rpath);
}


function get(path, complete) {
  //console.log("* get: "+ path);
  url = "ls/"+ fileName(path);
  if(!path) {
    url = "ls";
  }
  //console.log("* get.url: "+ url);
  $.get(url, function(data) {
    console.log("* get.data: "+ JSON.stringify(data));
    curdir.dir = data[0];
    data.shift();
    curdir.ls = data;
    if(typeof path === 'function') {
      complete = path;
    }
    if(typeof complete === 'function') {
      complete();
    }
  }, "json");
}


function parent() {
  $.get("up", function(data) {
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


function mkdirDialog() {
  $("#mkdir").load("nav/mkdir.html", function() {
    $("#modal-mkdir").modal("show");
    $("#idir").on("keyup", function(evt) {
      if(evt.which === 13) {
        mkdir($("#idir").val());
      }
    }).focus();
    $("#idir").focus();
  }).focus();
}


function mkdir(dirname) {
  if(!dirname || typeof dirname !== 'string' || dirname.length < 1) {
    alert("Bad File Name: '"+ dirname+ "'");
    $("#modal-mkdir").modal("hide");
    return;
  }
  $.get("mkdir/"+ encodeURI(dirname), function() {
    $("#modal-mkdir").modal("hide");
    get();
  }, "text");
}


var selections = [];


function rmDialog() {
  if(selections.length < 1) {
    return;
  }
  $("#rm").load("nav/rm.html", function() {
    var selects = new Vue({
      el: "#rmlist",
      computed: {
        files: function() {
          return selections;
        }
      }
    });
    $("#modal-rm").modal("show");
  });
}


function rm() {
  if(selections.length < 1) {
    return;
  }
  for(var i = 0; i < selections.length; i++) {
    $.ajax({
      method: "GET",
      async: false,
      url: "rm/"+ btoa(selections[i].path),
      dataType: false
    });
  }
  $(".select-file").prop("checked", false);
  $("#modal-rm").modal("hide");
  selections = [];
  get();
}


function select(file) {
  var idx = selections.indexOf(file);
  if(idx < 0) {
    selections.push(file);
  }
  else {
    selections.splice(idx, 1);
  }
}


function search(table, input) {
  $(table).find("tbody tr").each(function(idx, elt) {
    $(elt).show();
  });
  var val = $(input).val();
  if(!val || val === '') {
    return;
  }
  var regex = new RegExp(val, 'i');
  $(table).find("tbody tr").filter(":visible").each(function(idx, elt) {
    if(!regex.test($(elt).text())) {
      $(elt).closest("tr").hide();
    }
  });
}


function toggleSecondButtons() {
  if(isScrolledIntoView("#main-buttons")) {
    $("#second-buttons").hide();
  }
  else {
    $("#second-buttons").css("top", $(window).height() / 2 - 130);
    $("#second-buttons").show();
  }
}


var curdir = new Vue({
  el: "#current-dir",
  data: {
    dir: false,
    ls: false
  }
});


get();


$(window).scroll(function() {
  toggleSecondButtons();
});

