
function migtip(elt, opts) {
  return new MigTooltip(elt, opts);
}


function MigTooltip(elt, opts) {
  
  var ref = this;
  
  ref.elt = typeof elt === 'string' ? document.querySelector(elt) : elt;
  
  ref.tooltip = false;
  
  var effects = {
    'fade': ['mig-fx-fadein', 'mig-fx-fadeout']
  };
  
  var triggers = {
    'mouseover': true,
    'click': true,
    'focus': true,
    'none': false
  };
  
  var normalOpts = function(opts) {
    var o = {
      position: 'top',
      trigger: 'mouseover',
      hideOnClick: true,
      effect: 'fade',
      content: ''
    };
    if(opts && typeof opts.postion === 'string' && opts.postion.length > 2) {
      o.position = opts.position;
    }
    if(opts && typeof opts.trigger === 'string' && triggers.hasOwnProperty(opts.trigger)) {
      o.trigger = opts.trigger;
    }
    if(opts && typeof opts.hideOnClick === 'boolean') {
      o.hideOnClick = opts.hideOnClick;
    }
    if(opts && typeof opts.effect === 'string' && effects.hasOwnProperty(opts.effect)) {
      o.effect = opts.effect;
    }
    if(opts && typeof opts.content === 'string' && opts.content.length > 1) {
      o.content = opts.content;
    }
    else {
      var tooltip = ref.elt.getAttribute("data-tooltip");
      if(typeof tooltip === 'string' && tooltip.length > 0) {
        o.content = tooltip;
      }
    }
    console.log("* opts="+ JSON.stringify(o));
    return o;
  };
  
  ref.opts = normalOpts(opts);
  
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
    var _x = el.offsetLeft;
    var _y = el.offsetTop;
    while( el && !isNaN( el.offsetLeft ) && !isNaN( el.offsetTop ) ) {
        _x += el.offsetLeft - el.scrollLeft + el.clientLeft;
        _y += el.offsetTop - el.scrollTop + el.clientTop;
        el = el.offsetParent;
    }
    return { top: _y, left: _x };
  };
  
  var toggle = function() {
    if(!ref.tooltip || ref.tooltip.style['display'] === 'none') {
      ref.show();
    }
    else {
      ref.hide();
    }
  };
  
  ref.create = function(show) {
    var eltype = typeof ref.elt;
    if(eltype !== 'string' && eltype !== 'object') {
      throw "Bad argument type: " + eltype;
    }
    var el = ref.elt;
    if(!ref.opts.content || ref.opts.content.length < 1) return;
    var div = document.createElement("div");
    div.className = "mig-baloon-bottom mig-bg-gray-darker mig-color-white mig-font-sans mig-font-14 mig-font-bold";
    div.innerHTML = ref.opts.content;
    var wrap = document.createElement("div");
    wrap.id = "mig-tooltip-wrap";
    wrap.style['position'] = 'absolute';
    wrap.style['z-index'] = '9999';
    wrap.style['float'] = 'left';
    wrap.style["filter"] = "drop-shadow(3px 3px 3px rgba(0,0,0,0.5))";
    wrap.appendChild(div);
    document.body.appendChild(wrap);
    var ofs = getOffset(el);
    console.log("* elt.offset: "+ JSON.stringify(ofs));
    var tsize = getTextSize(ref.opts.content, "mig-font-14 mig-font-bold");
    var dtop = ofs.top - tsize.height - 43;
    var dleft = ofs.left - el.clientWidth / 2 - tsize.width / 2;
    console.log("* div.position: {top="+ dtop+ ", left="+ dleft + "}");
    div.style['width'] = tsize.width + 20 + "px";
    div.style['height'] = tsize.height + "px";
    wrap.style['top'] = dtop + "px";
    wrap.style['left'] = dleft + "px";
    if(ref.opts.hideOnClick) {
      wrap.addEventListener("click", ref.hide);
    }
    ref.tooltip = wrap;
    if(!show) ref.hide();
  };
  
  ref.show = function() {
    if(!ref.tooltip) {
      ref.create(true);
    }
    ref.tooltip.style["display"] = "block";
    ref.tooltip.className = effects[ref.opts.effect][0];
  };
  
  ref.hide = function() {
    if(ref.tooltip) {
      ref.tooltip.className = effects[ref.opts.effect][1];
      setTimeout(()=>{ref.tooltip.style["display"] = "none";}, 500);
    }
  };
  
  ref.destroy = function() {
    if(ref.tooltip) {
      ref.tooltip.style["animation"] = "fadeout 1s";
      setTimeout(()=>{document.body.removeChild(ref.tooltip);}, 500);
    }
  };
  
  var applyTrigger = function() {
    switch(ref.opts.trigger) {
      case "click":
        ref.elt.addEventListener("click", toggle);
        break;
      case "focus":
        ref.elt.addEventListener("focus", ref.show);
        ref.elt.addEventListener("blur", ref.hide);
        break;
      case "none":
        break;
      case "mouseover":
        ref.elt.onmouseover = ref.show;
        ref.elt.onmouseout = ref.hide;
        break;
    }
  };
  
  applyTrigger();
  
  elt.mig = ref;
  
}


var btntip = migtip("#btn-baloon", {trigger: 'click'});
var btntip = migtip("#input-baloon", {trigger: 'focus'});
var btntip = migtip("#input-baloon2", false);