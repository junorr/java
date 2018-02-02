function MethodApp(res) {
  this.methods = res.data;
}

var url = "http://localhost.bb.com.br:9088";

$.micro().post(url+"/sql/microdoc/methods", {cachettl: 3600, args: [cdedp]}, function(mts) {
  var app = new MethodApp(mts);
  ko.applyBindings(app);
});