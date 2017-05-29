var formatNumber = function(n, t, d) {
  var s = Math.abs(n).toString();
  var t = (t ? t : '.');
  var d = (d ? d : ',');
  var r = "";
  var count = 0;
  var decimal = s.indexOf(".") > 0;
  for(var i = s.length-1; i >= 0; i--) {
    var ch = s.charAt(i);
    if(ch !== '.' && !decimal && count === 3) {
      count = 0;
      r = t + r;
    }
    if(ch === '.') {
      r = d + r;
      decimal = false;
    }
    else {
      r = ch + r;
      if(!decimal) count++;
    }
  }
  return (n < 0 ? "-" : "") + r;
};



Number.prototype.roundDouble = function(dec) {
  var pow = Math.pow(10, dec);
  var l = parseInt(Math.round(this * pow));
  return parseFloat(l / pow);
};




function AjaxCors(url) {
  
  var GET = "GET";
  var POST = "POST";
  var PUT = "PUT";
  var DELETE = "DELETE";
  
  var ref = this;
  ref.url = url;
  ref.http = new XMLHttpRequest();
  
  var encodeFormData = function(data) {
    if(data) {
      var fd = new FormData();
      for(var p in data) {
        fd.append(p, data[p]);
      }
      return fd;
    }
  };
  
  var encodeUrl = function(data) {
    if(data) {
      var url = ref.url;
      for(var p in data) {
        url += (url.indexOf("?") > 0 ? "&" : "?");
        var query = p + "=" + data[p];
        url += encodeURI(query);
      }
      return url;
    }
  };
  
  var setHandler = function(handler) {
    if(handler && typeof handler === 'function') {
      ref.http.onreadystatechange = function() {
        if(ref.http.readyState === 4) {
          handler(ref.http.status, ref.http.responseText, ref.http);
        }
      };
    }
  };
  
  var setHeaders = function() {
    var token = ref.getCookie("BBSSOToken");
    var ssoac = ref.getCookie("ssoacr");
    //console.log("* BBSSOToken: "+ token);
    //console.log("* ssoacr: "+ ssoac);
    ref.http.setRequestHeader("Content-type", "application/json");
    ref.http.setRequestHeader("X-BBSSOToken", token);
    ref.http.setRequestHeader("X-ssoacr", ssoac);
  };
  
  ref.getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length,c.length);
        }
    }
    return "";
  };
  
  ref.get = function(data, handler) {
    setHandler(handler);
    ref.http.open(GET, encodeUrl(data), true);
    ref.http.withCredentials = true;
    setHeaders();
    ref.http.send();
  };
  
  ref.post = function(data, handler) {
    setHandler(handler);
    ref.http.open(POST, ref.url, true);
    ref.http.withCredentials = true;
    setHeaders();
    ref.http.send(JSON.stringify(data));
  };
  
  ref.put = function(data, handler) {
    setHandler(handler);
    ref.http.open(PUT, ref.url, true);
    ref.http.withCredentials = true;
    setHeaders();
    ref.http.send(JSON.stringify(data));
  };
  
  ref.delete = function(data, handler) {
    setHandler(handler);
    ref.http.open(DELETE, ref.url, true);
    ref.http.withCredentials = true;
    setHeaders();
    ref.http.send(JSON.stringify(data));
  };
  
}




function jsonp(callbackName) {
  var ref = this;
  ref.url = null;
  ref.complete = null;
  ref.callback = (callbackName 
      ? callbackName 
      : "jsonp_" + Math.round(1000000 * Math.random())
  );

  var setup = function() {
    window[ref.callback] = function(data) {
      ref.complete(data);
    };
    ref.url += (ref.url.indexOf("?") > 0 ? "&" : "?");
    ref.url += "callback="+ ref.callback;
  };

  var doScript = function() {
    var script = document.createElement("script");
    script.src = ref.url;
    script.type = "text/javascript";
    script.async = true;
    return script;
  };

  ref.send = function(url, complete) {
    ref.url = url;
    ref.complete = complete;
    setup();
    document.getElementsByTagName("head")[0]
            .appendChild(doScript());
  };
}


  var opts = {
    url: "http://127.0.0.1:9088/sql",
    group: "orc",
    query: "dspTipoMes",
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




function SSTable(id, opts) {

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
  
  
  var loadJson = function(complete) {
    var post = {
      group: ref.opts.group,
      query: ref.opts.query
    };
    if(ref.opts.cachettl) {
      post.cachettl = ref.opts.cachettl;
    }
    if(ref.opts.args && ref.opts.args.length) {
      post.args = ref.opts.args;
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
    console.log("* url : "+ ref.opts.url);
    console.log("* post: "+ JSON.stringify(post));
    if(typeof complete === 'undefined') {
      complete = function() {
        createHeader();
        createBody();
        createFooter();
        setTable();
      };
    }
    ref.showLoading();
    var total = (ref.json && ref.json.total ? ref.json.total : 0);
    new AjaxCors(ref.opts.url).post(post, function(status, data) {
      ref.json = JSON.parse(data);
      ref.data = ref.json.data;
      if(total !== ref.json.total) {
        updatePages();
      }
      complete();
      ref.hideLoading();
    });
  };
  
  
  var createCSS = function() {
    var style = document.createElement("style");
    style.type = "text/css";
    style.innerText = ""
        + ".ss-table { "
        + "  border: solid thin gray; "
        + "  border-collapse: collapse; "
        + "} "
        + ".ss-thead {"
        + "  background: linear-gradient(180deg, rgb(242,242,242), rgb(198,198,198)); "
        + "} "
        + ".ss-th { "
        + "  color: white; "
        + "  font-weight: bold; "
        + "  padding: 5px; "
        + "  padding-left: 10px; "
        + "  padding-right: 10px; "
        + "  border-left: thin solid gray; "
        + "} "
        + ".ss-tbody {} "
        + ".ss-tfoot { "
        + "  background-color: rgb(1, 107, 168); "
        + "} "
        + ".ss-tr { "
        + "  background-color: white; "
        + "} "
        + ".ss-tr:hover { "
        + "  background-color: rgb(228,228,233); "
        + "} "
        + ".ss-tr-selected { "
        + "  background: linear-gradient(180deg, rgb(195,195,205), white, rgb(195,195,205)); "
        + "} "
        + ".ss-tr-colored { "
        + "  background-color: rgb(240, 240, 240); "
        + "} "
        + ".ss-td { "
        + "  padding: 5px; "
        + "} "
        + ".ss-sort { "
        + "  color: rgb(60, 60, 60); "
        + "} "
        + ".ss-filter { "
        + "  border-radius: 5px; "
        + "  margin-top: 6px; "
        + "  margin-bottom: 5px; "
        + "  width: 110px; "
        + "} "
        + ".ss-tfoot { "
        + "  background: linear-gradient(180deg, rgb(242,242,242), rgb(198,198,198)); "
        + "  padding: 5px; "
        + "  height: 40px; "
        + "  border-top: solid 1px lightgray;"
        + "} "
        + ".ss-tfoot-row { "
        + "  text-align: center; "
        + "} "
        + ".ss-page-btn { "
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
        + ".ss-page-btn:hover { "
        + "  background-color: rgb(228,228,233); "
        + "  text-decoration: none;"
        + "} "
        + ".ss-pages { "
        + "  height: 29px; "
        + "  margin-left: 6px; "
        + "} "
        + ".ss-page-next { "
        + "  margin-left: 6px; "
        + "} "
    ;
    var head = document.getElementsByTagName("head")[0];
    head.insertBefore(style, head.childNodes[0]);
  };


  var createHeader = function() {
    ref.header = document.createElement("thead");
    ref.header.className = "ss-thead";
    if(ref.opts.columns) {
      for(var i = 0; i < ref.opts.columns.length; i++) {
        var th = document.createElement("th");
        th.className = "ss-th";
        var title = (ref.opts.columns[i].title 
            ? ref.opts.columns[i].title 
            : ref.opts.columns[i].id
        );
        var hd = document.createElement("span");
        hd.innerHTML = title;
        if(ref.opts.columns[i].sort) {
          hd = document.createElement("a");
          hd.className = "ss-sort ";
          hd.href = "#";
          hd.setAttribute("data-ss-col", ref.opts.columns[i].id);
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
          input.className = "ss-filter ";
          input.placeholder = title;
          input.setAttribute("data-ss-col", ref.opts.columns[i].id);
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
    var ths = noq(ref.table).findChilds("ss-sort");
    for(var i = 0; i < ths.length; i++) {
      ths[i].onclick = function() {
        var sscol = this.getAttribute("data-ss-col");
        var ssasc = this.getAttribute("data-ss-asc");
        ref.index = 0;
        noq(ref.table).findChild("ss-pages").value = ref.index / ref.count+1;
        if(sscol !== null) {
          ssasc = (ssasc === null || ssasc === "true");
          ref.sortBy = sscol;
          ref.sortAsc = ssasc;
          var sup = noq(this).findChild("sup");
          if(sup) {
            sup.innerText = (ssasc ? " (aZ)" : " (Za)");
          }
          this.setAttribute("data-ss-asc", !ssasc);
        }
        loadJson(createBody);
      };
    }
  };
  
  
  var setupPages = function() {
    var ths = noq(ref.table).findChilds("ss-page-prev");
    for(var i = 0; i < ths.length; i++) {
      ths[i].onclick = function() {
        if(ref.index <= 0) return;
        ref.index -= ref.count;
        noq(ref.table).findChild("ss-pages").value = ref.index / ref.count+1;
        loadJson(createBody);
      };
    }
    ths = noq(ref.table).findChilds("ss-pages");
    for(i = 0; i < ths.length; i++) {
      ths[i].onchange = function() {
        var step = (this.value - 1) * ref.count;
        ref.index = step;
        loadJson(createBody);
      };
    }
    ths = noq(ref.table).findChilds("ss-page-next");
    for(i = 0; i < ths.length; i++) {
      ths[i].onclick = function() {
        if((ref.index + ref.count) >= ref.json.total) return;
        ref.index += ref.count;
        noq(ref.table).findChild("ss-pages").value = ref.index / ref.count+1;
        loadJson(createBody);
      };
    }
  };
  
  
  var filterEvent = function(evt) {
    ref.filterBy = [];
    ref.filter = [];
    var ths = ref.table.querySelectorAll(".ss-filter");
    for(var i = 0; i < ths.length; i++) {
      var sscol = ths[i].getAttribute("data-ss-col");
      var content = (typeof ths[i].value !== "undefined" ? ths[i].value : null);
      if(sscol !== null && content !== null && content.length > 0) {
        ref.filterBy.push(sscol);
        ref.filter.push(content);
      }
    }
    if(evt.keyCode === 13) {
      ref.index = 0;
      noq(ref.table).findChild("ss-pages").value = ref.index / ref.count+1;
      loadJson(createBody);
    }
  };
  
  
  var setupFilters = function() {
    var ths = noq(ref.table).findChilds("ss-filter");
    for(var i = 0; i < ths.length; i++) {
      var sscol = ths[i].getAttribute("data-ss-col");
      if(sscol === null) return;
      ths[i].onkeyup = filterEvent;
    }
  };
  
  
  var setRowEvents = function(tr) {
    tr.onclick = function() {
      var selected = !(this.getAttribute("data-ss-selected") === "true");
      var row = parseInt(this.getAttribute("data-ss-row"));
      if(ref.singleSelection) {
        ref.selected = [];
        var rows = ref.body.getElementsByTagName("tr");
        for(var i = 0; i < rows.length; i++) {
          rows[i].setAttribute("data-ss-selected", false);
          rows[i].className = rows[i].className.replace(new RegExp(" ss-tr-selected", "g"), "");
        }
      }
      if(selected) {
        ref.selected.push(row);
        this.className += " ss-tr-selected";
        if(ref.onselect !== null && typeof ref.onselect === 'function') {
          ref.onselect(row, ref.data[row]);
        }
      } else {
        ref.selected.splice(ref.selected.indexOf(row), 1);
        this.className = this.className.replace(new RegExp(" ss-tr-selected", "g"), "");
      }
      this.setAttribute("data-ss-selected", selected);
    };
  };


  var createBody = function() {
    if(ref.body !== null) {
      while(ref.body.hasChildNodes()) {
        ref.body.removeChild(ref.body.lastChild);
      }
    } else {
      ref.body = document.createElement("tbody");
      ref.table.appendChild(ref.body);
    }
    ref.body.className = "ss-tbody";
    var jmax = (ref.opts.columns 
        ? ref.opts.columns.length 
        : ref.json.columns.length
    );
    for(var i = 0; i < ref.json.count; i++) {
      var tr = document.createElement("tr");
      tr.className = "ss-tr " + (i % 2 > 0 ? "ss-tr-colored" : "");
      tr.setAttribute("data-ss-selected", false);
      tr.setAttribute("data-ss-row", i);
      setRowEvents(tr);
      for(var j = 0; j < jmax; j++) {
        var title = (ref.opts.columns 
            ? ref.opts.columns[j].id 
            : ref.json.columns[j]
        );
        var td = document.createElement("td");
        td.className = "ss-td";
        if(ref.opts.columns[j].css) {
          for(var prop in ref.opts.columns[j].css) {
            td.style[prop] = ref.opts.columns[j].css[prop];
          }
        }
        var value = ref.data[i][title];
        if(ref.opts.columns && ref.opts.columns[j].format) {
          value = ref.opts.columns[j].format(value);
        }
        td.innerHTML = value;
        tr.appendChild(td);
      }
      ref.body.appendChild(tr);
    }
  };
  
  
  var createFooter = function() {
    ref.footer = document.createElement("tfoot");
    ref.footer.className = "ss-tfoot";
    
    var tr = document.createElement("tr");
    tr.className = "ss-tfoot-row";
    
    var td = document.createElement("td");
    td.setAttribute("colspan", noq(ref.header).findChilds("th").length);
    
    var prev = document.createElement("a");
    prev.className = "ss-page-prev ss-page-btn";
    prev.href = "#";
    prev.innerText = "<";
    prev.title = "Previous Page";
    
    var pages = document.createElement("select");
    pages.className = "ss-pages ss-page-btn";
    pages.title = "Select Page";
    
    var next = document.createElement("a");
    next.className = "ss-page-next ss-page-btn";
    next.href = "#";
    next.innerText = ">";
    next.title = "Next Page";
    
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
    ths = noq(ref.table).findChilds("ss-pages");
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
    var div = document.getElementById(ref.id);
    div.innerHTML = "";
    div.appendChild(ref.table);
  };
  
  ref.showLoading = function() {
    if(!ref.body) return;
    var bounds = ref.body.getBoundingClientRect();
    if(typeof bounds === 'undefined') return;
    var fbounds = ref.footer.getBoundingClientRect();
    var dbounds = document.body.getBoundingClientRect();
    var div = document.createElement("div");
    div.style["background-color"] = "rgba(255,255,255,0.6)";
    div.style["position"] = "absolute";
    div.style["left"] = (bounds.x - dbounds.x) + "px";
    div.style["top"] = (bounds.y - dbounds.y) + "px";
    div.style["width"] = (bounds.width-1) + "px";
    div.style["height"] = (bounds.height + fbounds.height -1) + "px";
    div.style["z-index"] = 10;
    div.style["text-align"] = "center";
    div.style["vertical-align"] = "middle";
    div.className = "ss-loading";
    var img = document.createElement("img");
    img.src = "images/loading.gif";
    img.style["width"] = "40px";
    img.style["height"] = "40px";
    img.style["position"] = "relative";
    img.style["top"] = bounds.height /2 -30;
    //div.appendChild(img);
    //div.appendChild(document.createElement("br"));
    var span = document.createElement("span");
    span.innerText = "Loading...";
    span.style["font-size"] = "18px";
    span.style["font-weight"] = "bold";
    span.style["position"] = "relative";
    span.style["top"] = bounds.height /2 -25;
    div.appendChild(span);
    //div.style["border"] = "solid thin gray";
    document.body.appendChild(div);
  };
  
  
  ref.hideLoading = function() {
    var loads = document.querySelectorAll(".ss-loading");
    for(var i = 0; i < loads.length; i++) {
      document.body.removeChild(loads[i]);
    }
  };
  

  ref.createTable = function() {
    if(ref.css) createCSS();
    ref.table = document.createElement("table");
    ref.table.className = "ss-table";
    loadJson();
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

}




var noq = function(select) { return new NoQuery(select); };

function NoQuery(select) {
	
	var ref = this;
	
	ref.selector = select;
	
	
	ref.replaceClass = function(cls, rpl) {
		ref.apply(function(el) {
			el.className = el.className.replace(new RegExp(cls),rpl);
		});
	};
	
	
	ref.attr = function(name, value) {
		if(value) {
			ref.element.setAttribute(name, value);
		}
		return ref.element.getAttribute(name);
	};
	
	
	ref.addClass = function(cls) {
		ref.apply(function(el) {
			el.className += ' '+ cls;
		});
	};
	
	
	ref.setClass = function(cls) {
		ref.apply(function(el) {
			el.className = cls;
		});
	};
	
	
	ref.apply = function(func) {
		var elts = [ref.element];
		if((typeof ref.selector) === 'string') {
			elts = document.querySelectorAll(ref.selector);
		}
		if(!func || typeof func !== 'function' 
				|| !elts || elts.length < 1) {
			return;
		}
		for(var i = 0; i < elts.length; i++) {
			elts[i].rect = elts[i].getBoundingClientRect();
			func(elts[i]);
		}
	};
	
	
	ref.getOne = function(sel) {
		var elts = document.querySelectorAll(sel);
		if(!elts || elts.length < 1) {
			return 'undefined';
		}
		elts[0].rect = elts[0].getBoundingClientRect();
		return elts[0];
	};
	
	
	ref.toggleClass = function(cls) {
		ref.apply(function(el) {
			if(el.className.indexOf(cls) >= 0) {
				ref.replaceClass(cls, '');
			} else {
				ref.addClass(cls);
			}
		});
	};
	
	
	ref.rmClass = function(cls) {
		ref.replaceClass(cls, '');
	};
	
	
	ref.show = function() {
		ref.element.style.display = null;
	};
	
	
	ref.hide = function() {
		ref.element.style.display = "none";
	};
	
	
	ref.style = function(style, value) {
		if(value) {
			ref.element.style[style] = value;
		}
		return ref.element.style[style];
	};
	
	
	ref.html = function(value) {
		if(value) {
			ref.element.innerHTML = value;
		}
		return ref.element.innerHTML;
	};
	
	
	ref.value = function(val) {
		if(val) {
			ref.element.value = val;
		}
		return ref.element.value;
	};
	
	
	ref.hasValue = function() {
		return (typeof ref.element.value) !== 'undfined'; 
	};
	
	
	ref.childs = function(func, element) {
		var elt = (element ? element : ref.element);
		if((typeof func) === 'function') {
			for(var i = 0; i < elt.children.length; i++) {
				func(elt.children[i]);
				ref.childs(func, elt.children[i]);
			}
		}
		else {
			return ref.element.children;
		}
	};
	
	
	ref.findChilds = function(select, element, childs) {
		var cs = ((typeof childs) !== 'undefined' ? childs : []);
		var elt = (element ? element : ref.element);
		var idx = cs.length;
    //console.log("* findChilds: "+ elt.tagName.toLowerCase()+ " - "+ elt.className);
		if(elt.id === select
				|| noq(elt).containsClass(select)
				|| elt.tagName.toLowerCase() === select.toLowerCase()
				|| elt.getAttribute('name') === select) {
			cs[idx++] = elt;
			return cs;
		}
		for(var i = 0; i < elt.children.length; i++) {
			cs = ref.findChilds(select, elt.children[i], cs);
		}
		return cs;
	};
	
	
	ref.findChild = function(select) {
		return ref.findChilds(select)[0];
	};
	
	
	ref.selectChilds = function(select) {
		return noq(ref.findChilds(select));
	};
	
	
	ref.selectChild = function(select) {
		return noq(ref.findChild(select));
	};
	
	
	ref.forEach = function(func) {
		if(typeof ref.element.length !== 'undefined' 
			&& typeof func === 'function') {
			for(var i = 0; i < ref.element.length; i++) {
				func(ref.element[i], i);
			}
		}
	};
	
	
	ref.containsClass = function(cls) {
		return ref.element.className === cls
			|| ref.element.className.indexOf(" "+ cls) >= 0
			|| ref.element.className.indexOf(cls+ " ") >= 0;
	};

	
	
	ref.findParents = function(select, element, parents) {
		var cs = ((typeof parents) !== 'undefined' ? parents : []);
		var elt = (element ? element : ref.element);
		var idx = cs.length;
		if(elt.id === select
				|| elt.className.indexOf(select) >= 0
				|| elt.tagName.toLowerCase() === select.toLowerCase()
				|| elt.getAttribute('name') === select) {
			cs[idx++] = elt;
			return cs;
		}
		if(elt.parentNode) {
			return ref.findParents(select, elt.parentNode, cs);
		}
	};

	
	
	ref.append = function(value) {
		if(value) {
			ref.element.innerHTML += value;
		}
		return ref.element.innerHTML;
	};
	
	
	ref.width = function(value) {
		if(value) {
			ref.style('width', value+ 'px');
		}
		return ref.style('width');
	};
	
	
	ref.height = function(value) {
		if(value) {
			ref.style('height', value+ 'px');
		}
		return ref.style('height');
	};
	
	
	ref.element = ((typeof ref.selector) === 'string' 
		? ref.getOne(ref.selector) : ref.selector);
	
	ref.bounds = (ref.element ? ref.element.rect : undefined);
}