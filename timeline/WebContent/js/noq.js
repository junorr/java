function noq(select) {
	return new NoQuery(select);
}


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
		var elts = document.querySelectorAll(ref.selector);
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
	
	
	ref.element = ref.getOne(select);
	
	ref.bounds = ref.element.rect;
	
}