
const OpType = {
  CREATE: "create",
  GET: "get",
  SET: "set",
  METHOD: "method",
  LS_JARS: "lsjars",
  LS_CLASS: "lsclass",
  LS_METH: "lsmeth",
  LS_CACHE: "lscache"
};

const ArgType = {
  STRING: "java.lang.String",
  CHAR: "java.lang.Character",
  BOOLEAN: "java.lang.Boolean",
  SHORT: "java.lang.Short",
  INT: "java.long.Integer",
  LONG: "java.lang.Long",
  DOUBLE: "java.lang.Double",
  FLOAT: "java.lang.Float",
  DATE: "java.util.Date",
  CLASS: "java.lang.Class"
};


class JsonOpBuilder {
 
  constructor() {
    this.name = null;
    this.argtypes = [];
    this.arguments = [];
    this.optype = null;
    this.class = null;
    this.ops = new Array();
    this.baseUrl = "http://localhost:9085/jmi/";
  }
 
  withName(name) {
    this.name = name;
    return this;
  }
 
  withArgs(args) {
    this.arguments = args;
    return this;
  }
 
  onClass(cls) {
    this.class = cls;
    return this;
  }
 
  clearVars() {
    this.name = null;
    this.optype = null;
    this.argtypes = [];
    this.arguments = [];
    return this;
  }
 
  get(name) {
    if(typeof name === "string" && name.length > 0) {
      this.withName(name);
    }
    if(!this.name || typeof this.name !== "string") {
      throw "Bad null op name";
    }
    var url = this.baseUrl + OpType.GET + "/" + this.class + "/" + this.name;
    this.ops.push(url);
    return this.clearVars();
  }
 
  set(name, val) {
    if(typeof name === "string" && name.length > 0) {
      this.withName(name);
    }
    if(typeof val !== "undefined") {
      this.withArgs([val]);
    }
    if(!this.name || typeof this.name !== "string") {
      throw "Bad null op name";
    }
    if(this.arguments.length < 1) {
      throw "Bad null arguments";
    }
    var url = this.baseUrl + OpType.SET + "/" + this.class + "/" + this.name;
    for(i = 0; i < this.arguments.length; i++) {
      url += "/" + encodeURIComponent(this.arguments[i]);
    }
    this.ops.push(url);
    return this.clearVars();
  }
 
  method(name, args) {
    if(typeof name === "string" && name.length > 0) {
      this.withName(name);
    }
    if(typeof args !== "undefined") {
      this.withArgs(args);
    }
    if(!this.name || typeof this.name !== "string") {
      throw "Bad null op name";
    }
    var url = this.baseUrl + OpType.METHOD + "/" + this.class + "/" + this.name;
    for(i = 0; i < this.arguments.length; i++) {
      url += "/" + encodeURIComponent(this.arguments[i]);
    }
    this.ops.push(url);
    return this.clearVars();
  }
 
  create(args) {
    if(typeof args !== "undefined") {
      this.withArgs(args);
    }
    var url = this.baseUrl + OpType.CREATE + "/" + this.class;
    for(i = 0; i < this.arguments.length; i++) {
      url += "/" + encodeURIComponent(this.arguments[i]);
    }
    this.ops.push(url);
    return this.clearVars();
  }
 
  build() {
    this.ops;
  }
 
  showBuild() {
    return JSON.stringify(this.build());
  }
 
}
