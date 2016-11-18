	
function Tableone(table) {
	
	var ref = this;
	
	ref.tname = table;
	
	var telement = noq(table).element;
	
	var theader = noq(telement).findChild("thead");
	
	var tbody = noq(telement).findChild("tbody");
	
	var tfooter = noq(telement).findChild("tfoot");
	
	var numCols = theader.children[0].children.length;
	
	var pageRows = -1;
	
	var totalRows = -1;
	
	var filterEnabled = false;
	
	var paginationEnabled = false;
	
	var sortEnabled = false;
  
  var filterIndex = -1;
	
	var rowColorsEnabled = false;
	
	var rowColors = ["white", "rgb(245,245,245)"];
	
	
	ref.enableFilter = function(sizes) {
		filterEnabled = true;
		var tr = document.createElement("tr");
		noq(tr).style("text-align", "center");
		noq(tr).style("padding", "5px");
		noq(tr).addClass("tboneFilterRow");
		for(var i = 0; i < numCols; i++) {
			var hd = theader.children[0].children[i];
			var hdname = (hd.children.length > 0 
					? hd.children[0].innerText : hd.innerText);
			var td = document.createElement("td");
			var ti = document.createElement("input");
			ti.setAttribute("index", i);
			ti.style["margin-top"] = "5px";
			ti.style["border-radius"] = "3px";
      if(sizes && typeof sizes.length !== 'undefined' && sizes.length > i) {
        ti.style["width"] = sizes[i];
      }
			noq(ti).addClass("tboneFilterInput");
			ti.onkeyup = function() {
				ref._filter(this);
			};
			ti.setAttribute("placeholder", hdname);
			td.appendChild(ti);
			tr.appendChild(td);
		}
		tfooter.appendChild(tr);
		return ref;
	};
	
	
	ref.enableRowColors = function(colors) {
		rowColorsEnabled = true;
		rowColors = (colors ? colors : rowColors);
		return ref.applyRowColors();
	};
	
	
	ref.enableColumnSort = function(defIndex, color) {
		sortEnabled = true;
		var idx = (typeof defIndex === 'undefined' ? 0 : defIndex);
		var clr = (color ? color : "rgb(240,240,240)");
		var hds = noq(theader).findChilds("th");
		for(var i = 0; i < hds.length; i++) {
			var icon = document.createElement("i");
			icon.className = "fa fa-sort";
			var a = document.createElement("a");
			a.className = "tboneSortLink";
			a.setAttribute("href", "#");
			var span = document.createElement("span");
			span.innerText = "  ";
			a.appendChild(span);
			a.appendChild(icon);
			a.style["color"] = clr;
			a.style["text-decoration"] = "none";
			a.setAttribute("index", i);
			a.setAttribute("sortby", "asc");
			a.onclick = function() {
				ref._sort(this);
			};
			a.onmouseover = function() {
				this.style["border-bottom"] = "solid thin";
				this.style["border-top"] = "solid thin";
			};
			a.onmouseout = function() {
				this.style["border-bottom"] = "none";
				this.style["border-top"] = "none";
			};
			hds[i].appendChild(a);
		}
		var th = noq(theader).findChilds("th")[idx];
		return ref._sort(noq(th).findChild("tboneSortLink"));
	};
	
	
	ref._sort = function(link) {
		var idx = parseInt(link.getAttribute("index"));
		var meth = link.getAttribute("sortby");
		noq(theader).selectChilds("th").forEach(function(hd) {
			var a = noq(hd).findChild("tboneSortLink");
			a.setAttribute("sortby", "asc");
			var i = noq(a).findChild("i");
			i.className = "fa fa-sort";
		});
		var icon = noq(link).findChild("i");
		if(meth === 'asc') {
			icon.className = "fa fa-sort-amount-asc";
			link.setAttribute("sortby", "desc");
			ref.sortAsc(idx);
		} else {
			icon.className = "fa fa-sort-amount-desc";
			link.setAttribute("sortby", "asc");
			ref.sortDesc(idx);
		}
	};
	
	
	ref._compareCol = function(index, a, b) {
		var acol = noq(a).findChilds("td")[index];
		var bcol = noq(b).findChilds("td")[index];
		if(typeof acol.innerText === 'undefined'
			|| typeof bcol.innerText === 'undefined') {
			return 0;
		}
		var astr = acol.innerText.toLowerCase();
    astr = astr.replace(/^\s+/g, "").replace(/\s+$/g, "");
		var bstr = bcol.innerText.toLowerCase();
    bstr = bstr.replace(/^\s+/g, "").replace(/\s+$/g, "");
		var anum = parseFloat(astr);
		var bnum = parseFloat(bstr);
    if(astr.indexOf(".") < astr.indexOf(",")) {
      anum = parseFloat(astr.replace(/\.+/g, "")
              .replace(/,+/g, "."));
    }
    if(bstr.indexOf(".") < bstr.indexOf(",")) {
      bnum = parseFloat(bstr.replace(/\.+/g, "")
              .replace(/,+/g, "."));
    }
		var res = 0;
		if(astr.length === 10 && astr.indexOf("/") === 2) {
			astr = astr.substring(6, 10)
					+ '-' + astr.substring(3, 5)
					+ '-' + astr.substring(0, 2);
			bstr = bstr.substring(6, 10)
					+ '-' + bstr.substring(3, 5)
					+ '-' + bstr.substring(0, 2);
      res = astr.localeCompare(bstr);
		}
		else if(astr.length === 18 && astr.indexOf("/") === 2 
				&& astr.indexOf(":") === 13) {
			astr = astr.substring(6, 10)
					+ '-' + astr.substring(3, 5)
					+ '-' + astr.substring(0, 2)
					+ 'T' + astr.substring(11, 13)
					+ ':' + astr.substring(14, 16)
					+ ':' + astr.substring(17, 19);
			bstr = bstr.substring(6, 10)
					+ '-' + bstr.substring(3, 5)
					+ '-' + bstr.substring(0, 2)
					+ 'T' + bstr.substring(11, 13)
					+ ':' + bstr.substring(14, 16)
					+ ':' + bstr.substring(17, 19);
      res = astr.localeCompare(bstr);
		}
		else if(anum.toString() !== 'NaN'
			&& bnum.toString() !== 'NaN') {
			res = anum - bnum;
		} 
    else {
			res = astr.localeCompare(bstr);
		}
		return res;
	};
	
	
	ref.sortAsc = function(index) {
		ref.sortFunc(index, function(a, b) {
			return ref._compareCol(index, a, b);
		});
	};
	
	
	ref.sortDesc = function(index) {
		ref.sortFunc(index, function(a, b) {
			var res = ref._compareCol(index, a, b);
			if(res < 0) res = 1;
			else if(res > 0) res = -1;
			return res;
		});
	};
	
	
	ref.sortFunc = function(index, func) {
		if(typeof index !== 'number' 
			|| typeof func !== 'function'
			|| !sortEnabled) return;
		var unsorted = noq(tbody).findChilds("tr");
		while(tbody.firstChild) {
			tbody.removeChild(tbody.firstChild);
		}
		var rows = unsorted.sort(func);
		for(var i = 0; i < rows.length; i++) {
			tbody.appendChild(rows[i]);
		}
		if(paginationEnabled) {
			var select = noq(tfooter).findChilds("select")[0];
			ref._paginate(select, 0);
		} else {
			ref.applyRowColors();
		}
		return ref;
	};
	
	
	ref.applyRowColors = function() {
		if(!rowColorsEnabled) return ref;
		var rows = noq(tbody).findChilds("tr");
		var idx = 0;
		for(var i = 0; i < rows.length; i++) {
			if(typeof rows[i].style.display === 'undefined' 
				|| rows[i].style.display !== 'none') {
				var clr = rowColors[idx++%2];
				noq(rows[i]).style("background-color", clr);
			}
		}
		return ref;
	};
	
	
	ref.isFilterEnabled = function() {
		return filterEnabled;
	};
	
	
	ref.isRowColorsEnabled = function() {
		return rowColorsEnabled;
	};
	
	
	ref.isPaginationEnabled = function() {
		return paginationEnabled;
	};
	
	
	ref.isSortEnabled = function() {
		return sortEnabled;
	};
	
	
	ref.getPaginationRows = function() {
		return pageRows;
	};
	
	
	ref.enablePagination = function(rows) {
		paginationEnabled = true;
		pageRows = (rows ? rows : 10);
		totalRows = noq(tbody).findChilds("tr").length;
		var pages = parseInt(totalRows / rows);
		var style = document.createElement("style");
		style.type = "text/css";
		style.innerText = 
			".tbonePageNavDef {" +
			"   background-color: white;" +
			"   border: solid thin rgb(230,230,230);" +
			"   border-radius: 5px;" +
			"   width: 45px;" +
			"   height: 28px; " +
			"} " +
			".tbonePageNavDef:hover {" +
			"   background-color: rgb(230,230,230); " +
			"}";
		document.getElementsByTagName("head")[0].appendChild(style);
		var td = document.createElement("td");
		td.setAttribute("colspan", numCols);
		td.style["padding"] = "5px";
		td.style["text-align"] = "center";
		td.className = "tbonePageColumn";
		var prevIcon = document.createElement("i");
		prevIcon.className = "fa fa-chevron-left";
		var prevButton = document.createElement("button");
		prevButton.appendChild(prevIcon);
		prevButton.className = "tbonePageNavDef tbonePageNav tbonePagePrev";
		prevButton.setAttribute("title", "Previous Page");
		prevButton.onclick = function() {
			var select = noq(tfooter).findChilds("select")[0];
			var page = parseInt(select.value);
      ref._sortFilter();
			ref._paginate(select, --page);
		};
		td.appendChild(prevButton);
		var sp = document.createElement("i");
		sp.innerText = "  ";
		td.appendChild(sp);
		var select = document.createElement("select");
		select.className = "tbonePageNavDef tbonePageNav tbonePageSelect";
		select.setAttribute("title", "Select Page");
		for(var i = 0; i <= pages; i++) {
			var opt = document.createElement("option");
			opt.value = i;
			opt.innerText = i;
			select.appendChild(opt);
		}
		select.onchange = function() {
			var select = noq(tfooter).findChilds("select")[0];
			var page = parseInt(select.value);
      ref._sortFilter();
			ref._paginate(select, page);
		};
		td.appendChild(select);
		sp = document.createElement("i");
		sp.innerText = "  ";
		td.appendChild(sp);
		var nextIcon = document.createElement("i");
		nextIcon.className = "fa fa-chevron-right";
		var nextButton = document.createElement("button");
		nextButton.appendChild(nextIcon);
		nextButton.className = "tbonePageNavDef tbonePageNav tbonePageNext";
		nextButton.setAttribute("title", "Next Page");
		nextButton.onclick = function() {
			var select = noq(tfooter).findChilds("select")[0];
			var page = parseInt(select.value);
      ref._sortFilter();
			ref._paginate(select, ++page);
		};
		td.appendChild(nextButton);
		var tr = document.createElement("tr");
		tr.className += " tbonePageRow";
		tr.appendChild(td);
		tfooter.appendChild(tr);
		return ref._paginate(select, 0);
	};
  
  
  ref._sortFilter = function() {
    if(filterEnabled && filterIndex >= 0) {
      var text = noq(tfooter).findChilds("input")[filterIndex].value;
      ref.sortFunc(filterIndex, function(a, b) {
        var acol = noq(a).findChilds("td")[filterIndex];
        var bcol = noq(b).findChilds("td")[filterIndex];
        if(typeof acol.innerText === 'undefined'
          || typeof bcol.innerText === 'undefined') {
          return 0;
        }
        var astr = acol.innerText.toLowerCase();
        astr = astr.replace(/\n+/g, " ");
        astr = astr.replace(/^\s+/g, "");
        var bstr = bcol.innerText.toLowerCase();
        bstr = bstr.replace(/\n+/g, " ");
        bstr = bstr.replace(/^\s+/g, "");
        if(astr.startsWith(text.toLowerCase())
                && !bstr.startsWith(text.toLowerCase())) {
          return -1;
        }
        else if(bstr.startsWith(text.toLowerCase())
                && !astr.startsWith(text.toLowerCase())) {
          return 1;
        }
        else return 0;
      });
    }
  };
	
	
	ref._paginate = function(select, page) {
		if(!paginationEnabled) return ref;
		var pages = parseInt(totalRows / pageRows)+1;
		if(page < 0) page = 0;
		if(page >= pages) page = pages-1;
		var skip = (page > 0 && pages > 1 ? page*pageRows : 0);
		select.value = page;
		var rows = noq(tbody).findChilds("tr");
		for(var i = 0; i < rows.length; i++) {
			if((skip > 0 && i < skip) || i >= (page+1)*pageRows) {
				noq(rows[i]).hide();
			} else {
				noq(rows[i]).show();
			}
		}
		return ref.applyRowColors();
	};
	
	
	ref._filter = function(input) {
		var text = input.value;
    filterIndex = parseInt(input.getAttribute("index"));
    if(text === '') {
      filterIndex = -1;
    }
		var trs = noq(tbody).findChilds("tr");
    var showCount = 0;
		for(var i = 0; i < trs.length; i++) {
			var tds = noq(trs[i]).findChilds("td");
			var index = parseInt(input.getAttribute("index"));
			var td = tds[index];
      var tdstr = td.innerText.toLowerCase();
      tdstr = tdstr.replace(/\n+/g, " ");
      tdstr = tdstr.replace(/^\s+/g, "");
			if(!tdstr.startsWith(text.toLowerCase())) {
				noq(trs[i]).hide();
			} else {
        showCount++;
				noq(trs[i]).show();
			}
		}
		var select = noq(tfooter).findChilds("select")[0];
		if(paginationEnabled && text === '') {
			ref._paginate(select, select.value);
		} else {
      if(showCount > pageRows) {
        ref._sortFilter();
        ref._paginate(select, select.value);
      }
			ref.applyRowColors();
		}
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
	}
	
	
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
	}

	
	
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