class Element {
  
  constructor(tag) {
    this.tag = tag;
    this.props = [];
    this.styles = [];
    this.events = [];
    this.classes = [];
  }
  
  hasProp(prop) {
    var type = typeof prop;
    if("object" === type) {
      var count = 0;
      for(var i = 0; i < prop.length; i++) {
        var idx = this.props.indexOf(prop[i]);
        if(idx >= 0) {
          count++;
        }
      }
      return count === prop.length;
    }
    else if("undefined" !== type) {
      return this.props.indexOf(prop) >= 0;
    }
  }
  
  prop(name, value) {
    if(typeof value === "undefined") {
      if(typeof name === "object") {
        for(var prop in name) {
	        this.props[prop] = name[prop];
        }
      }
      else {
        return this.props[name];
      }
    }
    else {
      this.props[name] = value;
    }
    return this;
  }
  
  rmProp(name) {
    var prop = this.props[name];
    if(typeof prop !== "undefined") {
      delete this.props[name];
    }
    return prop;
  }
  
  hasClass(cls) {
    var type = typeof cls;
    if("object" === type) {
      var count = 0;
      for(var i = 0; i < cls.length; i++) {
        var idx = this.classes.indexOf(cls[i]);
        if(idx >= 0) {
          count++;
        }
      }
      return count === cls.length;
    }
    else if("undefined" !== type) {
      return this.classes.indexOf(cls) >= 0;
    }
  }
  
  addClass(cls) {
    var type = typeof cls;
    if("object" === type) {
      for(var i in cls) {
        this.classes.push(cls[i]);
      }
    }
    else if("undefined" !== type) {
      this.classes.push(cls);
    }
    return this;
  }
  
  rmClass(cls) {
    var type = typeof cls;
    if("object" === type) {
      for(var i = 0; i < cls.length; i++) {
        var idx = this.classes.indexOf(cls[i]);
        if(idx >= 0) {
          this.classes.splice(idx, 1);
        }
      }
    }
    else if("undefined" !== type) {
      var idx = this.classes.indexOf(cls);
      if(idx >= 0) {
        this.classes.splice(idx, 1);
      }
    }
    return this;
  }
  
  hasStyle(style) {
    var type = typeof style;
    if("object" === type) {
      var count = 0;
      for(var i = 0; i < style.length; i++) {
        var idx = this.styles.indexOf(style[i]);
        if(idx >= 0) {
          count++;
        }
      }
      return count === style.length;
    }
    else if("undefined" !== type) {
      return this.styles.indexOf(style) >= 0;
    }
  }
  
  style(name, value) {
    if(typeof value === "undefined") {
      if(typeof name === "object") {
        for(var prop in name) {
	        this.styles[prop] = name[prop];
        }
      }
      else {
        return this.styles[name];
      }
    }
    else {
      this.styles[name] = value;
    }
    return this;
  }
  
  rmStyle(name) {
    var style = this.styles[name];
    if(typeof style !== "undefined") {
      delete this.styles[name];
    }
    return style;
  }
  
  hasEvent(evt) {
    var type = typeof evt;
    if("object" === type) {
      var count = 0;
      for(var i = 0; i < evt.length; i++) {
        var idx = this.events.indexOf(evt[i]);
        if(idx >= 0) {
          count++;
        }
      }
      return count === evt.length;
    }
    else if("undefined" !== type) {
      return this.events.indexOf(evt) >= 0;
    }
  }
  
  event(name, action) {
    if(typeof action === "undefined") {
      if(typeof name === "object") {
        for(var action in name) {
	        this.events[action] = name[action];
        }
      }
      else {
        return this.events[name];
      }
    }
    else {
      this.events[name] = action;
    }
    return this;
  }
  
  rmEvent(name) {
    var action = this.events[name];
    if(typeof action !== "undefined") {
      delete this.events[name];
    }
    return action;
  }
  
  setClasses(el) {
    el.className = "";
    for(var i in this.classes) {
      var cls = this.classes[i];
      cls = (typeof cls === "function" ? cls() : cls);
      el.className += cls + " ";
    }
    el.className = el.className.substring(
        0, el.className.length -1
    );
    return el;
  }
  
  create() {
    var el = document.createElement(this.tag);
    el = setClasses(el);
    el.style = this.styles;
    for(var evt in this.events) {
      el.addEventListener(evt, this.events[evt]);
    }
    for(var prop in this.props) {
      el.setAttribute(prop, this.props[prop]);
    }
    return el;
  }
  
}





class Input extends Element {
  
  constructor(type) {
    this.tag = "input";
    this.prop("type", (typeof type === "undefined" ? "text" : type));
  }
  
}