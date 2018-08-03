if(typeof $ === 'undefined') {
  throw "! Error [micro-table.js]-> JQuery Required";
}

if(typeof $.micro === 'undefined') {
  throw "! Error [micro-table.js]-> micro-core.js Required";
}

Micro.prototype.table = function(selector, opts) {
  return new MicroTable(selector, opts);
};

/*
var opts = {
  url: "http://127.0.0.1:9088/sql/orc/dspTipoMes",
  args: ["PACOTE", 2],
  columns: [
    {id: 0, title: "Nome", sort: true, filter: true},
    {id: 5, title: "Orçado", align: "center", sort: true, filter: true, format: "formatNumber($value.roundDouble(2))"},
    {id: 7, title: "Projetado", align: "center", sort: true, filter: true, format: "formatNumber($value.roundDouble(2))"},
    {id: 8, title: "Realizado", align: "center", sort: true, filter: true, format: "formatNumber($value.roundDouble(2))"},
    {id: 9, title: "Desvio $", align: "center", sort: true, filter: true, format: "formatNumber($value.roundDouble(2))"},
    {id: 10, title: "Desvio %", align: "center", sort: true, filter: true, format: "formatNumber($value.roundDouble(2))"}
  ],
  pagination: 5
};
*/

function MicroTable(id, opts) {
  
  var ref = this;
  
  ref.id = id;
  ref.opts = opts;
  ref.json = null;
  ref.data = null;
  ref.table = null;
  ref.header = null;
  ref.body = null;
  ref.footer = null;
  ref.sortBy = null;
  ref.sortAsc = true;
  ref.filterBy = null;
  ref.filter = null;
  ref.count = (opts.pagination ? opts.pagination : 0);
  ref.index = 0;
  ref.css = true;
  ref.singleSelection = true;
  ref.selected = [];
  ref.onselect = null;
  ref.enableRowClick = false;
  
  
  var loadJson = function(complete) {
    var post = {};
    var metahash = ref.opts.url;
    if(ref.opts.cachettl) {
      post.cachettl = ref.opts.cachettl;
    }
    if(ref.opts.args && ref.opts.args.length) {
      post.args = ref.opts.args;
      metahash += JSON.stringify(ref.opts.args);
    }
    if(ref.filter && ref.filter.length
            && ref.filterBy 
            && ref.filterBy.length) {
      post.filter = ref.filter;
      post.filterBy = ref.filterBy;
    }
    if(ref.sortBy) {
      post.sortBy = ref.sortBy;
      post.sortAsc = (ref.sortAsc ? ref.sortAsc : false);
    }
    if(ref.opts.pagination) {
      post.limit = [ref.index, ref.opts.pagination];
    }
    metahash = btoa(metahash);
    if(sessionStorage[metahash] && sessionStorage[metahash] !== "false") {
      post.metadata = sessionStorage[metahash];
    }
    ref.showLoading();
    var total = (ref.json && ref.json.total ? ref.json.total : 0);
    $.micro().post(ref.opts.url, post, function(data) {
      ref.json = data;
      if(typeof ref.json.metadata === 'string') {
        sessionStorage[metahash] = ref.json.metadata;
      }
      ref.data = ref.json.data;
      if(total !== ref.json.total) {
        updatePages();
      }
      if(typeof complete === "function") {
        complete(ref.data);
      }
      ref.hideLoading();
    });
  };
  
  
  var createCSS = function() {
    var style = document.createElement("style");
    style.type = "text/css";
    style.innerText = ""
        + ".micro-table { "
        + "  border: solid thin gray; "
        + "  padding: 1px; "
        + "  margin: 1px; "
        //+ "  border-collapse: collapse; "
        + "  border-spacing: 0; "
        + "} "
        + ".micro-thead {"
        + "  background: linear-gradient(180deg, rgb(242,242,242), rgb(198,198,198)); "
        + "} "
        + ".micro-th { "
        + "  color: rgb(60,60,60); "
        + "  font-weight: bold; "
        + "  padding: 5px; "
        + "  padding-left: 10px; "
        + "  padding-right: 10px; "
        + "  border-left: thin solid gray; "
        + "} "
        + ".micro-tbody {} "
        + ".micro-tfoot { "
        + "  background-color: rgb(1, 107, 168); "
        + "} "
        + ".micro-tr { "
        + "  background-color: white; "
        + "} "
        + ".micro-tr-selected { "
        + "  background: linear-gradient(180deg, rgb(195,195,205), white, rgb(195,195,205)); "
        + "} "
        + ".micro-tr-colored { "
        + "  background-color: rgb(240, 240, 240); "
        + "} "
        + ".micro-td { "
        + "  padding: 5px; "
        + "} "
        + ".micro-sort { "
        + "  color: rgb(60, 60, 60); "
        + "} "
        + ".micro-filter { "
        + "  border-radius: 5px; "
        + "  margin-top: 6px; "
        + "  margin-bottom: 5px; "
        + "  width: 110px; "
        + "} "
        + ".micro-tfoot { "
        + "  background: linear-gradient(180deg, rgb(242,242,242), rgb(198,198,198)); "
        + "  padding: 5px; "
        + "  height: 40px; "
        + "  border-top: solid 1px lightgray;"
        + "} "
        + ".micro-tfoot-row { "
        + "  text-align: center; "
        + "} "
        + ".micro-page-btn { "
        + "  text-align: center; "
        + "  padding: 5px; "
        + "  padding-left: 12px; "
        + "  padding-right: 12px; "
        + "  border: solid thin gray; "
        + "  background-color: white; "
        + "  border-radius: 8px; "
        + "  font-weight: bold; "
        + "  text-decoration: none; "
        + "} "
        + ".micro-page-btn:hover { "
        + "  background-color: rgb(228,228,233); "
        + "  text-decoration: none;"
        + "} "
        + ".micro-pages { "
        + "  height: 29px; "
        + "  margin-left: 6px; "
        + "} "
        + ".micro-page-next { "
        + "  margin-left: 6px; "
        + "} "
    ;
    if(ref.enableRowClick) {
      style.innerText += ".micro-tr:hover { "
        + "  background-color: rgb(228,228,233); "
        + "} ";
    }
    var head = document.getElementsByTagName("head")[0];
    head.insertBefore(style, head.childNodes[0]);
  };


  var createHeader = function() {
    ref.header = document.createElement("thead");
    ref.header.className = "micro-thead";
    if(ref.opts.columns) {
      for(var i = 0; i < ref.opts.columns.length; i++) {
        var th = document.createElement("th");
        th.className = "micro-th";
        th.setAttribute("data-column", ref.opts.columns[i].id);
        var title = (ref.opts.columns[i].title 
            ? ref.opts.columns[i].title 
            : ref.opts.columns[i].id
        );
        var hd = document.createElement("span");
        hd.innerHTML = title;
        if(ref.opts.columns[i].sort) {
          hd = document.createElement("a");
          hd.className = "micro-sort ";
          hd.href = "#";
          var name = document.createElement("span");
          name.innerHTML = title;
          var sortid = document.createElement("sup");
          sortid.innerHTML = " (aZ)";
          hd.appendChild(name);
          hd.appendChild(sortid);
        }
        th.appendChild(hd);
        if(ref.opts.columns[i].filter) {
          var input = document.createElement("input");
          input.className = "micro-filter ";
          input.placeholder = title;
          th.appendChild(document.createElement("br"));
          th.appendChild(input);
        }
        if(ref.opts.columns[i].css) {
          for(var prop in ref.opts.columns[i].css) {
            th.style[prop] = ref.opts.columns[i].css[prop];
          }
        }
        ref.header.appendChild(th);
      }
    }
    else {
      for(var i = 0; i < ref.json.columns.length; i++) {
        var th = document.createElement("th");
        th.innerHTML = ref.json.columns[i];
        ref.header.appendChild(th);
      }
    }
    ref.table.appendChild(ref.header);
    setupSort();
    setupFilters();
  };
  
  
  var setupSort = function() {
    var ths = $(ref.table).find(".micro-sort");
    for(var i = 0; i < ths.length; i++) {
      ths[i].onclick = function() {
        var th = $(this).closest("th");
        var sscol = $(th).attr("data-column");
        var ssasc = $(this).attr("data-asc");
        ref.index = 0;
        var pgs = $(ref.table).find(".micro-pages");
        if(typeof pgs !== "undefined") {
          $(pgs).val(ref.index / ref.count+1);
        }
        if(sscol !== null) {
          ssasc = (ssasc === null || ssasc === "true");
          ref.sortBy = sscol;
          ref.sortAsc = ssasc;
          var sup = $(this).find("sup");
          if(sup) {
            $(sup).text(ssasc ? " (aZ)" : " (Za)");
          }
          $(this).attr("data-asc", !ssasc);
        }
        loadJson(createBody);
      };
    }
  };
  
  
  var setupPages = function() {
    var ths = $(ref.table).find(".micro-page-prev");
    for(var i = 0; i < ths.length; i++) {
      ths[i].onclick = function() {
        if(ref.index <= 0) return;
        ref.index -= ref.count;
        var pgs = $(ref.table).find(".micro-pages");
        if(pgs) {
          $(pgs).val(ref.index / ref.count+1);
        }
        loadJson(createBody);
      };
    }
    ths = $(ref.table).find(".micro-pages");
    for(i = 0; i < ths.length; i++) {
      ths[i].onchange = function() {
        var step = (this.value - 1) * ref.count;
        ref.index = step;
        loadJson(createBody);
      };
    }
    ths = $(ref.table).find(".micro-page-next");
    for(i = 0; i < ths.length; i++) {
      ths[i].onclick = function() {
        if((ref.index + ref.count) >= ref.json.total) return;
        ref.index += ref.count;
        var pgs = $(ref.table).find(".micro-pages");
        if(pgs) {
          $(pgs).val(ref.index / ref.count+1);
        }
        loadJson(createBody);
      };
    }
  };
  
  
  var filterEvent = function(evt) {
    ref.filterBy = [];
    ref.filter = [];
    var ths = $(ref.table).find(".micro-filter");
    for(var i = 0; i < ths.length; i++) {
      var th = $(ths[i]).parents("th");
      if(!th || th.length === 0) {
        th = $(ths[i]).parents("td");
      }
      if(!th || th.length === 0) {
        return;
      }
      var sscol = $(th).attr("data-column");
      var content = $(ths[i]).val() ? ths[i].value : null;
      if(sscol !== null && content !== null && content.length > 0) {
        ref.filterBy.push(sscol);
        ref.filter.push(content);
      }
    }
    if(evt.keyCode === 13) {
      ref.index = 0;
      var pgs = $(ref.table).find(".micro-pages");
      if(typeof pgs !== "undefined") {
        $(pgs).val(ref.index / ref.count+1);
      }
      loadJson(createBody);
    }
  };
  
  
  var setupFilters = function() {
    var ths = $(ref.table).find(".micro-filter");
    for(var i = 0; i < ths.length; i++) {
      var sscol = $(ths[i]).closest("th").attr("data-column");
      if(sscol === null) return;
      ths[i].onkeyup = filterEvent;
    }
  };
  
  
  var setRowEvents = function(tr) {
    if(!ref.enableRowClick) {
      return tr;
    }
    $(tr).click(function() {
      var selected = !($(this).attr("data-selected") === "true");
      var row = parseInt($(this).attr("data-row"));
      if(ref.singleSelection) {
        ref.selected = [];
        var rows = $(ref.body).find("tr");
        for(var i = 0; i < rows.length; i++) {
          $(rows[i]).attr("data-selected", false);
          $(rows[i]).removeClass("micro-tr-selected");
        }
      }
      if(selected) {
        ref.selected.push(row);
        $(this).addClass("micro-tr-selected");
        if(ref.onselect !== null && typeof ref.onselect === 'function') {
          ref.onselect(row, ref.data[row]);
        }
      } else {
        ref.selected.splice(ref.selected.indexOf(row), 1);
        $(this).removeClass("micro-tr-selected");
      }
      $(this).attr("data-selected", selected);
    });
    return tr;
  };
  
  
  var replaceValues = function(elt, rowIndex) {
    var div = document.createElement("div");
    $(div).append(elt);
    var html = $(div).html();
    var idx = 0;
    while((idx = html.indexOf("{{", idx)) >= 0) {
      var end = html.indexOf("}}", idx);
      if(end < 0) break;
      idx += 2;
      var id = html.substring(idx, end);
      if(id === 'row') {
        html = replaceAll(html, "{{"+id+"}}", rowIndex);
      }
      else if(typeof ref.data[rowIndex] !== 'undefined' 
              && typeof ref.data[rowIndex][id] !== 'undefined') {
        html = replaceAll(html, "{{"+id+"}}", ref.data[rowIndex][id]);
      }
    }//while
    $(div).html(html);
    return $(div).children()[0];
  };
  
  
  var getTrModel = function() {
    var model = $(ref.body).find("tr.micro-model")[0];
    if(typeof model === 'undefined') {
      model = document.createElement("tr");
      $(model).addClass("micro-tr");
      var max = (ref.opts.columns 
          ? ref.opts.columns.length 
          : ref.json.columns.length);
      for(var i = 0; i < max; i++) {
        var td = document.createElement("td");
        var id = (ref.opts.columns 
          ? ref.opts.columns[i].id 
          : ref.json.columns[i]);
        $(td).addClass("micro-td").html("{{"+ id+ "}}");
        $(model).append(td);
      }
    }
    return model;
  };
  
  
  var populateBody = function(trmodel) {
    for(var i = 0; i < ref.json.count; i++) {
      var tr = $(trmodel)[0].cloneNode(true);
      $(tr).removeClass("micro-model").addClass("micro-tr");
      if(i % 2 > 0) $(tr).addClass("micro-tr-colored");
      $(tr).attr("data-selected", false);
      $(tr).attr("data-row", i).show();
      /*
      var tds = $(tr).children("td");
      for(var j = 0; j < tds.length; j++) {
        var td = tds[j];
        if(ref.opts.columns[j].css) {
          for(var prop in ref.opts.columns[j].css) {
            td.style[prop] = ref.opts.columns[j].css[prop];
          }
        }
        var value = $(td).html();
        if(ref.opts.columns && ref.opts.columns[j].format) {
          value = ref.opts.columns[j].format(value);
        }
        $(td).html(value);
      }
      */
      $(ref.body).append(setRowEvents(replaceValues(tr, i)));
    }
  };


  var createBody = function() {
    if(ref.body === null) {
      ref.body = $(ref.table).find("tbody")[0];
    }
    if(ref.body === null) {
      ref.body = document.createElement("tbody");
      ref.table.appendChild(ref.body);
    }
    var trmodel = getTrModel();
    while(ref.body.hasChildNodes()) {
      ref.body.removeChild(ref.body.lastChild);
    }
    $(ref.body).addClass("micro-tbody");
    $(trmodel).hide();
    $(ref.body).append(trmodel);
    populateBody(trmodel);
  };
  
  
  var createFooter = function() {
    ref.footer = document.createElement("tfoot");
    $(ref.footer).addClass("micro-tfoot");
    
    var tr = document.createElement("tr");
    $(tr).addClass("micro-tfoot-row");
    
    var td = document.createElement("td");
    $(td).attr("colspan", $(ref.header).find("th").length);
    
    var prev = document.createElement("a");
    $(prev).addClass("micro-page-prev")
        .addClass("micro-page-btn")
        .attr("href", "#")
        .text("<")
        .attr("title", "Previous Page");
    
    var pages = document.createElement("select");
    $(pages).addClass("micro-pages")
        .addClass("micro-page-btn")
        .attr("title", "Select Page");
    
    var next = document.createElement("a");
    $(next).addClass("micro-page-next")
        .addClass("micro-page-btn")
        .attr("href", "#")
        .text(">")
        .attr("title", "Next Page");
    
    td.appendChild(prev);
    td.appendChild(pages);
    td.appendChild(next);
    tr.appendChild(td);
    ref.footer.appendChild(tr);
    ref.table.appendChild(ref.footer);
    setupPages();
    updatePages();
  };
  
  
  var updatePages = function() {
    var ths = $(ref.table).find(".micro-pages");
    var nums = ref.json.total / ref.json.count;
    nums += (ref.json.total % ref.json.count !== 0 ? 1 : 0);
    for(i = 0; i < ths.length; i++) {
      while(ths[i].hasChildNodes()) {
        ths[i].removeChild(ths[i].lastChild);
      }
      for(var j = 1; j <= nums; j++) {
        var opt = document.createElement("option");
        opt.label = j;
        opt.value = j;
        opt.innerText = j;
        ths[i].appendChild(opt);
      }
    }
  };
  
  
  var replaceAll = function(value, search, replace) {
    var str = value;
    while(str.indexOf(search) >= 0) {
      str = str.replace(search, replace);
    }
    return str;
  };


  var setTable = function() {
    $(ref.id).html("").append(ref.table);
  };
  
  
  ref.showLoading = function() {
    if(!ref.body) return;
    var bounds = ref.body.getBoundingClientRect();
    if(typeof bounds === 'undefined') return;
    var fbounds = {width: 0, height: 0};
    if(ref.footer) {
      var fbounds = ref.footer.getBoundingClientRect();
    }
    var dbounds = document.body.getBoundingClientRect();
    var div = document.createElement("div");
    div.style["background-color"] = "rgba(255,255,255,0.6)";
    div.style["position"] = "absolute";
    div.style["left"] = (bounds.x - dbounds.x) + "px";
    div.style["top"] = (bounds.y - dbounds.y) + "px";
    div.style["width"] = (bounds.width-1) + "px";
    div.style["height"] = (bounds.height + fbounds.height -1) + "px";
    div.style["z-index"] = 999999;
    div.style["text-align"] = "center";
    div.style["vertical-align"] = "middle";
    div.className = "micro-loading";
    var span = document.createElement("span");
    span.innerText = "Loading...";
    span.style["font-size"] = "18px";
    span.style["font-weight"] = "bold";
    span.style["position"] = "relative";
    span.style["top"] = bounds.height /2 -25;
    div.appendChild(span);
    document.body.appendChild(div);
  };
  
  
  ref.hideLoading = function() {
    var loads = document.querySelectorAll(".micro-loading");
    for(var i = 0; i < loads.length; i++) {
      document.body.removeChild(loads[i]);
    }
  };
  
  
  ref.bind = function(complete) {
    if(ref.css) createCSS();
    ref.table = $(ref.id)[0];
    setupSort();
    setupPages();
    setupFilters();
    loadJson(function() {
      createBody();
      if(typeof complete === "function") {
        complete();
      }
    });
  };
  

  ref.createTable = function(complete) {
    if(ref.css) createCSS();
    ref.table = document.createElement("table");
    $(ref.table).addClass("micro-table");
    loadJson(function() {
      createHeader();
      createBody();
      createFooter();
      setTable();
      if(typeof complete === "function") {
        complete();
      }
    });
    return ref;
  };
  
  
  ref.setDefaultCss = function(enabled) {
    ref.css = enabled;
    return ref;
  };
  
  ref.setMultiSelection = function(enabled) {
    ref.singleSelection = !enabled;
    return ref;
  };
  
  
  ref.setRowClicks = function(enabled, callback) {
    ref.enableRowClick = enabled;
    if(ref.enableRowClick && typeof callback === 'function') {
      ref.onselect = callback;
    }
    return ref;
  };

}
