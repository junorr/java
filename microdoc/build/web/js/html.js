
class Element {
  
  constructor(tag) {
    var type = typeof tag;
    this.props = [];
    this.styles = [];
    this.events = [];
    this.classes = [];
    this.childs = [];
    this.txt = false;
    if("object" === type) {
      this.fromHtml(tag);
    }
    else if("string" === type) {
      this.tag = tag;
    }
    else {
      throw "Element.constructor: Bad tag type ("+ type+ ")";
    }
  }
  
  id(id) {
    var type = typeof id;
    if("undefined" === type) {
      return this.prop("id");
    }
    else if("function" === type) {
      this.prop("id", id());
    }
    else {
      this.prop("id", id);
    }
    return this;
  }
  
  text(txt) {
    var type = typeof txt;
    if("function" === type) {
      this.txt = txt();
    }
    else if("undefined" !== type) {
      this.txt = txt;
    }
    else {
      return this.txt;
    }
    return this;
  } 
  
  clearProps() {
    this.props = [];
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
  
  clearClasses() {
    this.classes = [];
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
  
  clearStyles() {
    this.styles = [];
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
  
  clearEvents() {
    this.events = [];
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
  
  clearChilds() {
    this.childs = [];
    this.txt = false;
  }
  
  hasChild(child) {
    var type = typeof child;
    if("undefined" !== type) {
      return this.childs.indexOf(child) >= 0;
    }
  }
  
  append(child) {
    var type = typeof child;
    if("function" === type) {
      this.childs.push(child());
    }
    else if("object" === type) {
      this.childs.push(child);
    }
    return this;
  }
  
  rmChild(child) {
    var idx = this.childs.indexOf(child);
    if(idx >= 0) this.childs.splice(idx, 1);
    return this;
  }
  
  setClasses(el) {
    if(this.classes.length < 1) {
      return el;
    }
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
  
  setChilds(el) {
    for(var child in this.childs) {
      if(typeof this.childs[child].tagName !== "undefined") {
        el.appendChild(this.childs[child]);
      }
      else if(typeof this.childs[child].tag !== "undefined") {
        el.appendChild(this.childs[child].create());
      }
    }
    return el;
  }
  
  create() {
    var el = document.createElement(this.tag);
    el = this.setClasses(el);
    for(var s in this.styles) {
      el.style[s] = this.styles[s];
    }
    for(var evt in this.events) {
      el.addEventListener(evt, this.events[evt]);
    }
    for(var p in this.props) {
      el.setAttribute(p, this.props[p]);
    }
    if(this.txt && this.txt.length > 0) {
      el.innerHTML = this.txt;
    }
    return this.setChilds(el);
  }
  
  fromHtml(el) {
    if(el.className.length > 0) {
      var clss = el.className.split(" ");
      for(var c in clss) {
        this.addClass(clss[c]);
      }
    }
    if(el.style.length > 0) {
      this.styles = el.style;
    }
    if(el.hasAttributes()) {
      var attrs = el.attributes;
      for(var i = attrs.length - 1; i >= 0; i--) {
        this.props[attrs[i].name] = attrs[i].value;
      }
    }
    this.txt = el.innerHTML;
    this.tag = el.tagName;
    return this;
  }
  
  insert(el) {
    var created = this.create();
    if("string" === typeof el) {
      el = document.querySelector(el);
    }
    if("function" === typeof el.appendChild) {
      el.appendChild(created);
    }
    else if("function" === typeof el.append) {
      el.append(created);
    }
    return created;
  }
  
}




class Input extends Element {
  constructor(type) {
    super("input");
    this.prop("type", (typeof type === "undefined" ? "text" : type));
  }
  
  value(val) {
    var type = typeof val;
    if("function" === type) {
      this.prop("value", val());
    }
    if("undefined" !== type) {
      this.prop("value", val);
    }
    return this;
  }
}




class Div extends Element {
  constructor() {
    super("div");
  }
}




class Button extends Element {
  constructor(type) {
    super("button");
    this.prop("type", (typeof type === "undefined") ? "button" : type);
  }
}




class A extends Element {
  constructor(href) {
    super("a");
    if(typeof href === "function") {
      href = href();
    }
    if(typeof href === "string") {
      this.prop("href", href);
    }
  }
}




class I extends Element {
  constructor() {
    super("i");
  }
}




class Strong extends Element {
  constructor() {
    super("strong");
  }
}




class P extends Element {
  constructor() {
    super("p");
  }
}




class H extends Element {
  constructor(size) {
    super("h"+ size);
  }
}




class Span extends Element {
  constructor() {
    super("span");
  }
}




class Label extends Element {
  constructor(pfor) {
    super("label");
    if(typeof pfor === "function") {
      pfor = pfor();
    }
    if(typeof pfor === "string") {
      this.prop("for", pfor);
    }
  }
}




class Br extends Element {
  constructor() {
    super("br");
  }
}




class Hr extends Element {
  constructor() {
    super("hr");
  }
}




class Li extends Element {
  constructor() {
    super("li");
  }
}




class Ul extends Element {
  constructor() {
    super("ul");
  }
}




class Ol extends Element {
  constructor() {
    super("ol");
  }
}




class Th extends Element {
  constructor() {
    super("th");
  }
}




class Tr extends Element {
  constructor() {
    super("tr");
  }
}




class Td extends Element {
  constructor() {
    super("td");
  }
}




class Table extends Element {
  constructor() {
    super("table");
    this.head = new Element("thead");
    this.body = new Element("tbody");
    this.foot = new Element("tfoot");
  }
  
  clearHead() {
    this.head = new Element("thead");
  }
  
  clearBody() {
    this.body = new Element("tbody");
  }
  
  clearFoot() {
    this.foot = new Element("tfoot");
  }
  
  addToHead(child) {
    this.head.append(child);
  }
  
  addToBody(child) {
    this.body.append(child);
  }
  
  addToFoot(child) {
    this.foot.append(child);
  }
  
  rmFromHead(child) {
    this.head.rmChild(child);
  }
  
  rmFromBody(child) {
    this.body.rmChild(child);
  }
  
  rmFromFoot(child) {
    this.foot.rmChild(child);
  }
  
  create() {
    var el = super.create();
    el.appendChild(this.thead.create());
    el.appendChild(this.tbody.create());
    el.appendChild(this.tfoot.create());
    return el;
  }
}
