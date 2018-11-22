var exopts = {
  position: ['top', 'right', 'bottom', 'left'],
  hideOnClicktrigger: ['mouseover', 'click', 'focus', 'none'],
  hideOnClick: [true, false]
  
};

function migtip(elt, opts) {
  return new MigTooltip(elt, opts);
}


function MigTooltip(elt, opts) {
  
  var ref = this;
  
  ref.elt = typeof elt === 'string' ? document.querySelector(elt) : elt;
  
  ref.opts = normalOpts(opts);
  
  ref.tooltip = false;
  
  var normalOpts = function(opts) {
    var o = {
      position: 'top',
      trigger: 'mouseover',
      hideOnClick: true,
      content: ''
    };
    if(opts && typeof opts.postion === 'string' && opts.postion.length > 2) {
      o.position = opts.position;
    }
    if(opts && typeof opts.trigger === 'string' && opts.trigger.length > 2) {
      o.trigger = opts.trigger;
    }
    if(opts && typeof opts.hideOnClick === 'boolean') {
      o.hideOnClick = opts.hideOnClick;
    }
    if(opts && typeof opts.content === 'string' && opts.content.length > 1) {
      o.content = opts.content;
    }
    else {
      var tooltip = ref.elt.getAttribute("mig-tooltip");
      if(typeof tooltip === 'string' && tooltip.length > 0) {
        o.content = tooltip;
      }
    }
    return o;
  };
  
  var getTextSize = function(text, css, classes) {
    var span = document.createElement("span");
    if(css && typeof css === 'object') {
      for(var prop in css) {
        if(!css.hasOwnProperty(prop)) continue;
        span.style[prop] = css[prop];
      }
    }
    else if(typeof css === 'string' && !classes) {
      classes = css;
    }
    if(classes) span.className = classes;
    span.innerText = text;
    span.style['visibility'] = 'hidden';
    span.style['float'] = 'left';
    span.style['position'] = 'absolute';
    span.style['top'] = '0px';
    span.style['left'] = '0px';
    span.style['display'] = 'block';
    document.body.appendChild(span);
    var size = {'width': span.offsetWidth, 'height': span.offsetHeight};
    document.body.removeChild(span);
    return size;
  };
  
  var getOffset = function(el) {
    var _x = 0;
    var _y = 0;
    while( el && !isNaN( el.offsetLeft ) && !isNaN( el.offsetTop ) ) {
        _x += el.offsetLeft - el.scrollLeft;
        _y += el.offsetTop - el.scrollTop;
        el = el.offsetParent;
    }
    return { top: _y, left: _x };
  };
  
  var get
  
  ref.create = function(show) {
    var eltype = typeof ref.elt;
    if(eltype !== 'string' && eltype !== 'object') {
      throw "Bad argument type: " + eltype;
    }
    var el = ref.elt;
    var tooltip = el.getAttribute("mig-tooltip");
    if(!tooltip || tooltip.length < 1) return;
    var div = document.createElement("div");
    div.className = "mig-baloon-bottom mig-bg-gray-darker mig-color-white mig-font-14 mig-font-bold";
    div.innerHTML = tooltip;
    var wrap = document.createElement("div");
    wrap.style['position'] = 'absolute';
    wrap.style['z-index'] = '9999';
    wrap.style['float'] = 'left';
    wrap.style["filter"] = "drop-shadow(3px 3px 3px rgba(0,0,0,0.5))";
    wrap.appendChild(div);
    document.body.appendChild(wrap);
    var ofs = getOffset(el);
    var tsize = getTextSize(tooltip, "mig-font-14 mig-font-bold");
    var dtop = ofs.top - tsize.height - 43;
    var dleft = ofs.left - el.clientWidth / 2 - tsize.width / 2;
    div.style['width'] = tsize.width + "px";
    div.style['height'] = tsize.height + "px";
    wrap.style['top'] = dtop + "px";
    wrap.style['left'] = dleft + "px";
    ref.tooltip = wrap;
    if(!show) ref.hide();
  };
  
  ref.show = function() {
    if(!ref.tooltip) {
      ref.create(true);
    }
    else {
      ref.tooltip.style["display"] = "block";
    }
  };
  
  ref.hide = function() {
    if(ref.tooltip) {
      ref.tooltip.style["display"] = "none";
    }
  };
  
  ref.destroy = function() {
    if(ref.tooltip) {
      document.body.removeChild(ref.tooltip);
    }
  };
  
  elt.mig = ref;
  
}


migtip("#btn-baloon", false).show();