
const OpType = {
  CONSTRUCTOR: "CONSTRUCTOR",
  GET: "GET",
  SET: "SET",
  METHOD: "METHOD"
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
  }
 
  withName(name) {
    this.name = name;
    return this;
  }
 
  withTypes(types) {
    this.argtypes = types;
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
    var op = {
      optype: OpType.GET,
      name: this.name,
      argtypes: this.argtypes,
      arguments: this.arguments
    };
    this.ops.push(op);
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
    var op = {
      optype: OpType.SET,
      name: this.name,
      argtypes: this.argtypes,
      arguments: this.arguments
    };
    this.ops.push(op);
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
    var op = {
      optype: OpType.METHOD,
      name: this.name,
      argtypes: this.argtypes,
      arguments: this.arguments
    };
    this.ops.push(op);
    return this.clearVars();
  }
 
  create(args) {
    if(typeof args !== "undefined") {
      this.withArgs(args);
    }
    var op = {
      optype: OpType.CONSTRUCTOR,
      name: "constructor",
      argtypes: this.argtypes,
      arguments: this.arguments
    };
    this.ops.push(op);
    return this.clearVars();
  }
 
  build() {
    return {
      "class": this.class,
      ops: this.ops
    };
  }
 
  showBuild() {
    return JSON.stringify(this.build());
  }
 
}
