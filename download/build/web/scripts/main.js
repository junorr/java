
requirejs([
  "jquery-3.2.0.min.js", 
  "vue.min.js", 
  "index.js"
], function(data) {
  console.log("typeof data: "+ typeof data);
  console.log("typeof stringify(data): "+ JSON.stringify(data));
});