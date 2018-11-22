
function migtip(elt, opts) {
  return new MigTooltip(elt, opts);
}


function MigTooltip(elt, opts) {
  
  var ref = this;
  
  /**
   * Element binded with tooltip.
   */
  ref.elt = typeof elt === 'string' ? document.querySelector(elt) : elt;
  
  /**
   * The tooltip element.
   */
  ref.tooltip = false;
  
  /**
   * Available tooltip effects names and classes.
   * @type {object} 
   */
  var effects = {
    'fade': ['mig-fx-fadein', 'mig-fx-fadeout']
  };
  
  /**
   * Available triggers to show the tooltip.
   * @type {object}
   */
  var triggers = {
    'mouseover': true,
    'click': true,
    'focus': true,
    'none': false
  };
  
  
  /**
   * Get default options combined with the provided  (if any).
   * @param {object} opts Tooltip options.
   * @return {object} Normalized tooltip options.
   */
  var normalOpts = function(opts) {
    var o = {
      position: 'top',
      trigger: 'mouseover',
      hideOnClick: true,
      effect: 'fade',
      css: false,
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
    if(opts && typeof opts.css === 'object') {
      o.css = opts.css;
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
    return o;
  };
  
  /**
   * Tooltip options.
   */
  ref.opts = normalOpts(opts);
  
  
  /**
   * Return the size needed for the informed content text/html.
   * @param {string} text Text/Html
   * @param {object} css (Optional) Styles applied on the content.
   * @param {string} classes (Optional) Classes applied on the content.
   * @return {object} {width, height} needed for the informed content.
   */
  var getContentSize = function(text, css, classes) {
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
  
  
  /**
   * Return the offset position of the element.
   * @param {HTMLElement} The element.
   * @return {object} {top, left} Offset position.
   */
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
  
  
  /**
   * Return the offset position of the element.
   * @param {HTMLElement} The element.
   * @return {object} {top, left} Offset position.
   */
  var offset = function(el) {
    var rect = el.getBoundingClientRect(),
    scrollLeft = window.pageXOffset || document.documentElement.scrollLeft,
    scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    return { top: rect.top + scrollTop, left: rect.left + scrollLeft };
	};
  
  
  var getTooltipPosition = function() {
    var ofs = offset(ref.elt);
    var tsize = typeof ref.opts.css === 'object' 
        ? getContentSize(ref.opts.content, ref.opts.css)
        : getContentSize(ref.opts.content, "mig-font-14 mig-font-bold");
    var pos = {top: 0, left: 0};
    if(ref.opts.position === 'top') {
      pos.top = ofs.top - tsize.height - 43;
      pos.left = ofs.left + ref.elt.offsetWidth / 2 - (tsize.width + 40) / 2;
    }
    else if(ref.opts.position === 'bottom') {
      
    }
    return pos;
  };
	
  
  /**
   * Create the tooltip element.
   * @param {boolean} show 'true' if the tooltip should be visible 
   * already, 'false' to just create and not show.
   * @return {undefined}
   */
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
    if(ref.opts.css) {
      for(var prop in ref.opts.css) {
        if(!ref.opts.css.hasOwnProperty(prop)) continue;
        div.style[prop] = ref.opts.css[prop];
      }
    }
    var wrap = document.createElement("div");
    wrap.style['position'] = 'absolute';
    wrap.style['z-index'] = '9999';
    wrap.style['float'] = 'left';
    wrap.style["filter"] = "drop-shadow(3px 3px 3px rgba(0,0,0,0.5))";
    wrap.appendChild(div);
    var ofs = offset(el);
    var tsize = getContentSize(ref.opts.content, "mig-font-14 mig-font-bold");
    var dtop = ofs.top - tsize.height - 43;
    var dleft = ofs.left + el.offsetWidth / 2 - (tsize.width + 40) / 2;
    wrap.style['top'] = dtop + "px";
    wrap.style['left'] = dleft + "px";
    if(ref.opts.hideOnClick) {
      wrap.addEventListener("click", ref.hide);
    }
    ref.tooltip = wrap;
    if(show) document.body.appendChild(wrap);
  };
  
  
  /**
   * Show the tooltip.
   * @return {undefined}
   */
  ref.show = function() {
    if(!ref.tooltip) {
      ref.create(true);
    }
    ref.tooltip.style["display"] = "block";
    ref.tooltip.className = effects[ref.opts.effect][0];
  };
  
  
  /**
   * Hide the tooltip.
   * @return {undefined}
   */
  ref.hide = function() {
    if(ref.tooltip) {
      setTimeout(()=>{ref.tooltip.style["display"] = "none";}, 400);
      ref.tooltip.className = effects[ref.opts.effect][1];
    }
  };
  
  
  /**
   * Hide and destroy the tooltip element.
   * @return {undefined}
   */
  ref.destroy = function() {
    if(ref.tooltip) {
      ref.hide();
      setTimeout(()=>{document.body.removeChild(ref.tooltip);}, 400);
    }
  };
  
  
  /**
   * Toggle display of tooltip element.
   * @return {undefined}
   */
  var toggle = function() {
    if(!ref.tooltip || ref.tooltip.style['display'] === 'none') {
      ref.show();
    }
    else {
      ref.hide();
    }
  };
  
  
  /**
   * Apply trigger event to show/hide the tooltip.
   * @return {undefined}
   */
  var applyTrigger = function() {
    switch(ref.opts.trigger) {
      case "mouseover":
        ref.elt.onmouseover = ref.show;
        ref.elt.onmouseout = ref.hide;
        break;
      case "click":
        ref.elt.addEventListener("click", toggle);
        break;
      case "focus":
        ref.elt.addEventListener("focus", ref.show);
        ref.elt.addEventListener("blur", ref.hide);
        break;
      case "none":
        break;
    }
  };
  
  applyTrigger();
  
  /**
   * Create a property 'mig' on the element to 
   * enable access to this MigTooltip object.
   */
  elt.mig = ref;
  
}


var btntip = migtip("#btn-baloon", {trigger: 'click'});
var btntip = migtip("#input-baloon", {trigger: 'focus'});
var btntip = migtip("#input-baloon2", false);