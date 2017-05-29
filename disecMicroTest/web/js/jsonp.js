function jsonp(callbackName) {
  var ref = this;
  ref.url = null;
  ref.complete = null;
  ref.callback = (callbackName 
      ? callbackName 
      : "jsonp_" + Math.round(1000000 * Math.random())
  );

  var setup = function() {
    window[ref.callback] = function(data) {
      ref.complete(data);
    };
    ref.url += (ref.url.indexOf("?") > 0 ? "&" : "?");
    ref.url += "callback="+ ref.callback;
  };

  var doScript = function() {
    var script = document.createElement("script");
    script.src = ref.url;
    script.type = "text/javascript";
    script.async = true;
    return script;
  };

  ref.send = function(url, complete) {
    ref.url = url;
    ref.complete = complete;
    setup();
    document.getElementsByTagName("head")[0]
            .appendChild(doScript());
  };
}
