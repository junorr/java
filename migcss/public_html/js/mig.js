
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
   * MigTooltip unique ID.
   */
  ref.uid = (new Date().getTime() + ref.elt.outerHTML.length + JSON.stringify(opts).length);
  
  /**
   * The tooltip element.
   */
  ref.tooltip = false;
  
  /**
   * Available tooltip effects names and classes.
   * @type {object} 
   */
  var effects = [
    {//effect
      name: 'fade', 
      animation: [
        { className: 'mig-fx-fadein', duration: 400 }, //show animation classes (existing in css)
        { className: 'mig-fx-fadeout', duration: 400} //hide animation classes (existing in css)
      ]
    }, {//effect
      name: 'slide-left', 
      animation: [ 
        {//show animation class and keyframes (creating)
          className: 'mig-fx-slidein-left', 
          keyframes: {
            '0%': { left: '0px', opacity: 0 },
            '80%': { left: '${this.left + 50}px', opacity: 0.8 },
            '100%': { left: '${this.left}px', opacity: 1 }
          },
          duration: 300
        }, {//hide animation class and keyframes (creating)
          className: 'mig-fx-slideout-left', 
          keyframes: {
            '0%': { left: '${this.left}px', opacity: 1 },
            '20%': { left: '${this.left + 50}px', opacity: 0.8 },
            '100%': { left: '0px', opacity: 0 }
          },
          duration: 300
        }
      ]//animation
    }, {//effect
      name: 'slide-right', 
      animation: [
        {//show animation class and keyframes (creating)
          className: 'mig-fx-slidein-right', 
          keyframes: {
            '0%': { left: '${document.width - this.width}px', opacity: 0 },
            '80%': { left: '${this.left - 50}px', opacity: 0.8 },
            '100%': { left: '${this.left}px', opacity: 1 }
          },
          duration: 300
        }, {//hide animation class and keyframes (creating)
          className: 'mig-fx-slideout-right', 
          keyframes: {
            '0%': { left: '${this.left}px', opacity: 1 },
            '20%': { left: '${this.left - 50}px', opacity: 0.8 },
            '100%': { left: '${document.width - this.width}px', opacity: 0 }
          },
          duration: 300
        }
      ]//animation
    }//effect
  ];//effects
  
  
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
    if(opts && typeof opts.position === 'string' && opts.position.length > 2) {
      o.position = opts.position;
    }
    if(opts && typeof opts.trigger === 'string' && triggers.hasOwnProperty(opts.trigger)) {
      o.trigger = opts.trigger;
    }
    if(opts && typeof opts.hideOnClick === 'boolean') {
      o.hideOnClick = opts.hideOnClick;
    }
    if(opts && typeof opts.effect === 'string') {
      o.effect = opts.effect;
    } 
    else if(opts && typeof opts.effect === 'object' 
        && opts.effect.hasOwnProperty('name') 
        && opts.effect.hasOwnProperty('animation')
        && opts.effect.animation.length === 2) {
      ref.addFx(opts.effect.name, opts.effect.animation[0], opts.effect.animation[1]);
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
    console.log("* opts: "+ JSON.stringify(o));
    return o;
  };
  
  /**
   * Tooltip options.
   */
  ref.opts = normalOpts(opts);
  
  
  var createCssAnimation = function(anim) {
    console.log("* createCssAnimation( '" + JSON.stringify(anim) + "' )");
    if(!anim 
        || !anim.hasOwnProperty('className') 
        || !anim.hasOwnProperty('keyframes') 
        || !anim.hasOwnProperty('duration')) {
      throw "Bad animation argument: Missing object properties (className/keyframes/duration)";
    }
    var style = document.createElement('style');
    style.setAttribute('type', 'text/css');
    var kfname = anim.className + "_keyframes";
    style.innerHTML = "@keyframes " + kfname + " { ";
    for(var kf in anim.keyframes) {
      if(!anim.keyframes.hasOwnProperty(kf)) continue;
      style.innerHTML += kf + " { ";
      for(var prop in anim.keyframes[kf]) {
        if(!anim.keyframes[kf].hasOwnProperty(prop)) continue;
        style.innerHTML += prop + ": " + anim.keyframes[kf][prop] + "; ";
      }
      style.innerHTML += "} ";
    }
    style.innerHTML += "} ";
    style.innerHTML += "." + anim.className + " { animation: " + kfname + " " + anim.duration + "ms; }";
    style.setAttribute('id', anim.className);
    document.querySelector('head').appendChild(style);
  };
  
  
  var evalAndReplace = function(keyframes, bounds) {
    for(var kf in keyframes) {
      if(!keyframes.hasOwnProperty(kf)) continue;
      for(var prop in keyframes[kf]) {
        if(!keyframes[kf].hasOwnProperty(prop) 
            || typeof keyframes[kf][prop] !== 'string') continue;
        var iex = keyframes[kf][prop].indexOf('${');
        if(iex < 0) continue;
        var eex = keyframes[kf][prop].indexOf('}');
        var exp = keyframes[kf][prop].substring(iex + 2, eex);
        exp = exp.replace(/this.top/g, bounds.top);
        exp = exp.replace(/this.left/g, bounds.left);
        exp = exp.replace(/document.width/g, document.body.offsetWidth);
        exp = exp.replace(/document.height/g, document.body.offsetHeight);
        exp = exp.replace(/this.width/g, bounds.width);
        exp = exp.replace(/this.height/g, bounds.height);
        console.log("* evalAndReplace(): " + keyframes[kf][prop] + " => " + exp + " => " + eval(exp));
        keyframes[kf][prop] = keyframes[kf][prop].substring(0, iex) 
            + eval(exp)
            + keyframes[kf][prop].substring(eex + 1);
      }
    }
    return keyframes;
  };
  
  
  var containsCssId = function(id) {
    var css = document.querySelector("#"+id);
    console.log("* containsCssId( '" + id + "' ): "+ css + " = " + (css !== null));
    return css !== null;
  };
  

  var processEffects = function(bounds) {
    for(var ifx = 0; ifx < effects.length; ifx++) {
      var fx = effects[ifx];
      for(var i = 0; i < fx.animation.length; i++) {
        if(!fx.animation[i].hasOwnProperty('keyframes')) continue;
        var cname = fx.animation[i].className + "_" + ref.uid;
        if(containsCssId(fx.animation[i].className) 
            || containsCssId(cname)) continue;
        fx.animation[i].className = cname;
        fx.animation[i].keyframes = evalAndReplace(fx.animation[i].keyframes, bounds);
        createCssAnimation(fx.animation[i]);
      }
    }
  };
  
  
  /**
   * Return the size needed for the informed content text/html.
   * @param {string} text Text/Html
   * @param {object} css (Optional) Styles applied on the content.
   * @param {string} classes (Optional) Classes applied on the content.
   * @return {object} {width, height} needed for the informed content.
   */
  var getContentSize = function(text, css, classes) {
    var span = document.createElement("div");
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
    span.innerHTML = text;
    span.style['visibility'] = 'hidden';
    span.style['float'] = 'left';
    span.style['position'] = 'absolute';
    span.style['top'] = '0px';
    span.style['left'] = '0px';
    span.style['display'] = 'block';
    span.style['z-index'] = '9999';
    document.body.appendChild(span);
    var size = {width: span.offsetWidth, height: span.offsetHeight};
    document.body.removeChild(span);
    return size;
  };
  
  
  /**
   * TEST METHOD. Not in use.
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
  
  
  /**
   * Return where tooltip should be positioned on screen (absolute), 
   * according to opts.position (top, right, bottom or left).
   * @param {string} html The tooltip html.
   * @return {object} position {top, left}
   */
  var getTooltipBounds = function(html) {
    var ofs = offset(ref.elt);
    var size = getContentSize(html);
    var pos = {top: 0, left: 0, width: size.width, height: size.height};
    if(ref.opts.position === 'top') {
      pos.top = ofs.top - size.height - 10;
      pos.left = ofs.left + ref.elt.offsetWidth / 2 - size.width / 2;
    }
    else if(ref.opts.position === 'right') {
      pos.top = ofs.top + ref.elt.offsetHeight / 2 - size.height / 2;
      pos.left = ofs.left + ref.elt.offsetWidth + 10;
    }
    else if(ref.opts.position === 'bottom') {
      pos.top = ofs.top + ref.elt.offsetHeight + 10;
      pos.left = ofs.left + ref.elt.offsetWidth / 2 - size.width / 2;
    }
    else {
      pos.top = ofs.top + ref.elt.offsetHeight / 2 - size.height / 2;
      pos.left = ofs.left - size.width - 10;
    }
    return pos;
  };
  
  
  /**
   * Set tooltip classes according to opts.position (top, right, bottom or left).
   * @param {type} elt Tooltip element.
   * @return {unresolved}
   */
  var setTooltipClasses = function(elt) {
    if(ref.opts.position === 'top') {
      elt.className = "mig-baloon-bottom mig-bg-gray-darker mig-color-white mig-font-sans mig-font-12";
    }
    else if(ref.opts.position === 'right') {
      elt.className = "mig-baloon-left mig-bg-gray-darker mig-color-white mig-font-sans mig-font-12";
    }
    else if(ref.opts.position === 'bottom') {
      elt.className = "mig-baloon-top mig-bg-gray-darker mig-color-white mig-font-sans mig-font-12";
    }
    else {
      elt.className = "mig-baloon-right mig-bg-gray-darker mig-color-white mig-font-sans mig-font-12";
    }
    return elt;
  };
	
  
  ref.addFx = function(name, fxin, fxout) {
    if(typeof name !== 'string') {
      throw "Bad argument 'name': '" + name + "' is not a string";
    }
    if(!fxin 
        || !fxin.hasOwnProperty('className') 
        || !fxin.hasOwnProperty('keyframes') 
        || !fxin.hasOwnProperty('duration')) {
      throw "Bad argument 'fxin': Missing object properties (className/keyframes/duration)";
    }
    if(!fxout 
        || !fxout.hasOwnProperty('className') 
        || !fxout.hasOwnProperty('keyframes') 
        || !fxout.hasOwnProperty('duration')) {
      throw "Bad argument 'fxout': Missing object properties (className/keyframes/duration)";
    }
    effects.push({
      name: name,
      animation: [fxin, fxout]
    });
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
    var div = setTooltipClasses(document.createElement("div"));
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
    wrap.style['opacity'] = '0';
    wrap.style["filter"] = "drop-shadow(3px 3px 3px rgba(0,0,0,0.5))";
    wrap.appendChild(div);
    var bounds = getTooltipBounds(wrap.innerHTML);
    wrap.style['top'] = bounds.top + "px";
    wrap.style['left'] = bounds.left + "px";
    processEffects(bounds);
    if(ref.opts.hideOnClick) {
      wrap.addEventListener("click", ref.hide);
    }
    ref.tooltip = wrap;
    if(!show) wrap.style["display"] = "none";;
    document.body.appendChild(wrap);
  };
  
  
  var getConfiguredFx = function() {
    var fxname = typeof ref.opts.effect === 'string' ? ref.opts.effect : ref.opts.effect.name;
    var fx = false;
    for(var i = 0; i < effects.length; i++) {
      if(effects[i].name === fxname) {
        return effects[i];
      }
    }
    return false;
  };
  
  
  /**
   * Show the tooltip.
   * @return {undefined}
   */
  ref.show = function() {
    if(!ref.tooltip) {
      ref.create(true);
    }
    var fx = getConfiguredFx();
    ref.tooltip.style["display"] = "block";
    if(!fx) {
      ref.tooltip.style["opacity"] = "1";
      return;
    }
    setTimeout(()=>{
      ref.tooltip.style["opacity"] = "1";
    }, fx.animation[0].duration);
    console.log("* show(): className="+ fx.animation[0].className);
    ref.tooltip.className = fx.animation[0].className;
  };
  
  
  /**
   * Hide the tooltip.
   * @return {undefined}
   */
  ref.hide = function() {
    if(ref.tooltip) {
      var fx = getConfiguredFx();
      if(!fx) {
        ref.tooltip.style["opacity"] = "0";
        ref.tooltip.style["display"] = "none";
        return;
      }
      setTimeout(()=>{
        ref.tooltip.style["opacity"] = "0";
        ref.tooltip.style["display"] = "none";
      }, fx.animation[1].duration);
      console.log("* hide(): className="+ fx.animation[1].className);
      ref.tooltip.className = fx.animation[1].className;
    }
  };
  
  
   /**
   * Hide and destroy the tooltip element.
   * @return {undefined}
   */
  ref.destroy = function() {
    if(ref.tooltip && document.body.contains(ref.tooltip)) {
      document.body.removeChild(ref.tooltip);
      ref.tooltip = false;
    }
  };
  
  
  /**
   * Update tooltip content.
   * @param {text/html} content The tooltip content.
   * @return {undefined}
   */
  ref.update = function(content) {
    if(typeof content !== 'string' || content.length < 1) {
      return;
    }
    ref.destroy();
    ref.opts.content = content;
    ref.show();
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


var btntip = migtip("#btn-baloon", {trigger: 'click', position: 'left', effect: 'slide-left'});
var inputtip = migtip("#input-baloon", {trigger: 'focus', css: {'background-color': 'white', 'border': 'solid thin blue', 'color': 'black'}});
var inputtip2 = migtip("#input-baloon2", {position: 'right', effect: 'slide-right'});