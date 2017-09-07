
const OpType = {
  CREATE: "create",
  CONSTRUCTOR: "constructor",
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
 
  constructor(url) {
    this.name = null;
    this.argtypes = [];
    this.arguments = [];
    this.optype = null;
    this.class = null;
    this.ops = new Array();
    this.baseUrl = url;
    this.getApi = true;
  }
  
  setGetApi(bool) {
    if(typeof bool === "boolean") {
      this.getApi = bool;
    }
    else {
      this.getApi = true;
    }
    return this;
  }
 
  setPostApi() {
    this.getApi = false;
    return this;
  }
 
  withName(name) {
    this.name = name;
    return this;
  }
 
  withArgs(args) {
    if(typeof args !== "object") {
      this.arguments.push(args);
    } 
    else {
      this.arguments = args;
    }
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
  
  buildOp() {
    var op = null;
    if(this.getApi) {
      op = this.baseUrl + this.optype + "/" + this.class;
      if(typeof this.name === "string") {
         op += "/" + this.name;
      }
      for(var i = 0; i < this.arguments.length; i++) {
        op += "/" + encodeURIComponent(this.arguments[i]);
      }
    }
    else {
      op = {
        optype: this.optype,
        name: this.name,
        arguments: this.arguments
      };
    }
    this.ops.push(op);
  }
  
  setOpType(type) {
    if(typeof type === "string") {
      this.optype = this.getApi ? type : type.toUpperCase();
    }
    return this;
  }
 
  get(name) {
    if(typeof name === "string" && name.length > 0) {
      this.withName(name);
    }
    if(!this.name || typeof this.name !== "string") {
      throw "Bad null op name";
    }
    this.setOpType(OpType.GET);
    this.buildOp();
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
    this.setOpType(OpType.SET);
    this.buildOp();
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
    this.setOpType(OpType.METHOD);
    this.buildOp();
    return this.clearVars();
  }
 
  create(args) {
    if(typeof args !== "undefined") {
      this.withArgs(args);
    }
    this.name = this.getApi ? null : "constructor";
    this.setOpType(this.getApi ? OpType.CREATE : OpType.CONSTRUCTOR);
    this.buildOp();
    return this.clearVars();
  }
 
  build() {
    if(this.getApi || typeof this.class !== "string") {
      return this.ops;
    }
    else {
      return {
        class: this.class,
        ops: this.ops
      };
    }
  }
 
  showBuild() {
    return JSON.stringify(this.build());
  }
  
  executeGet() {
    var result = null;
    for(var i = 0; i < this.ops.length; i++) {
      $.ajax({
        url: this.ops[i],
        method: "GET",
        async: false,
        success: function(data) {
          result = data;
        }
      }).fail(function(data) {
        result = {
          successful: false,
          retval: "ajax failed"
        };
      });
    }
    return result;
  }
  
  executePost(complete) {
    var result = null;
    $.ajax({
      url: this.baseUrl,
      method: "POST",
      async: false,
      data: JSON.stringify(this.build()),
      success: function(data) {
        result = data;
      }
    }).fail(function(data) {
      result = {
        successful: false,
        retval: "ajax failed"
      };
    });
    return result;
  }
  
  execute() {
    return this.getApi ? this.executeGet() : this.executePost();
  }
 
}
