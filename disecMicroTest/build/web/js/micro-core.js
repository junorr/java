if(typeof $ === 'undefined') {
  throw "! Error [micro-core.js]-> JQuery Required";
}

$.micro = new Micro();

$.jwt = function() {
  return new Micro().jwt();
};


function Micro() {
  
  var ref = this;
  
  
  ref.getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length,c.length);
        }
    }
    return "";
  };
  
  
  var checkAuth = function() {
    if(!ref.isAuth()) {
      throw "! Error [micro-core.js::checkAuth]-> Not Authenticated on Micro Service";
    }
  };
  
  
  ref.user = function() {
    checkAuth();
    return ref.jwt().user;
  };
  
  
  ref.isAuth = function() {
    return (typeof sessionStorage.jwt !== 'undefined') 
        && (typeof JSON.parse(sessionStorage.jwt).user !== 'undefined');
  };
  
  
  ref.isAuthUrl = function(url) {
    if(!ref.isAuth()) return false;
    aurl = ref.jwt().payload.url;
    if(typeof aurl === "string") {
      return url === aurl;
    }
    else {
      for(var i = 0; i < aurl.length; i++) {
        if(url === aurl[i]) return true;
      }
      return false;
    }
  };
  
  
  var toAuthUrl = function(url) {
    if(!url) {
      throw "! Error [micro-core.js::toAuthUrl]-> Bad URL: "+ url;
    }
    var ini = url.indexOf("://") + 3;
    var bar = url.indexOf("/", ini) + 1;
    return url.substring(0, bar) + "jwt";
  };
  
  
  ref.jwt = function() {
    checkAuth();
    return JSON.parse(sessionStorage.jwt);
  };
  
  
  ref.get = function(url, data, onsuccess) {
    ref.ajax(url, "GET", data, onsuccess);
  };
  
  
  ref.post = function(url, data, onsuccess) {
    ref.ajax(url, "POST", data, onsuccess);
  };
  
  
  ref.put = function(url, data, onsuccess) {
    ref.ajax(url, "PUT", data, onsuccess);
  };
  
  
  ref.delete = function(url, data, onsuccess) {
    ref.ajax(url, "DELETE", data, onsuccess);
  };
  
  
  var reauth = function(url) {
    var aurl = new Array();
    aurl.push(url);
    if(ref.isAuth()) {
      if(typeof ref.jwt().payload.url === "string") {
        aurl.push(ref.jwt().payload.url);
      }
      else {
        urls = ref.jwt().payload.url;
        for(var i = 0; i < urls.length; i++) {
          aurl.push(urls[i]);
        }
      }
    }
    ref.auth(toAuthUrl(url), aurl);
  };
  
  
  ref.ajax = function(url, meth, data, onsuccess) {
    if(!url) {
      throw "! Error [micro-core.js::ajax]-> Bad URL: "+ url;
    }
    if(!meth) {
      throw "! Error [micro-core.js::ajax]-> Bad Method: "+ meth;
    }
    if(!ref.isAuth() || !ref.isAuthUrl(url)) {
      reauth(url);
      checkAuth();
    }
    if(!onsuccess) {
      if(!data) {
        onsuccess = false;
        data = false;
      }
      else if(typeof data === "function") {
        onsuccess = data;
        data = false;
      }
    }
    $.ajax({
      url: url,
      method: meth,
      headers: {
        "Authorization": "Bearer "+ ref.jwt().token
      },
      contentType: "application/json",
      data: (meth !== "GET" ? JSON.stringify(data) : data),
      global: false,
      error: function(xhr, status) {
        sessionStorage.jwt = false;
        throw "! Error [micro-core.js::ajax]-> Request Failed. "+ xhr.status+ " "+ xhr.statusText;
      },
      success: onsuccess
    });
  };
  
  
  ref.simulate = function(url, key, onsuccess) {
    if(!url) {
      throw "! Error [micro-core.js::simulate]-> Bad URL: "+ url;
    }
    if(!key) {
      throw "! Error [micro-core.js::simulate]-> Bad User Key: "+ key;
    }
    url = (url.charAt(url.length -1) === '/' 
        ? url + key
        : url + "/" + key);
    ref.post(url, ref.jwt().url, function(data, status, xhr) {
      setJWT(data);
      if(typeof onsuccess === "function") {
        onsuccess(data, status, xhr);
      }
    });
  };
  
  
  var setJWT = function(jwt, bbssotk) {
    if(!jwt) {
      throw "! Error [micro-core.js::setJWT]-> Bad JWT: "+ jwt;
    }
    sessionStorage.jwt = false;
    var pt1 = jwt.indexOf(".") + 1;
    var pt2 = jwt.indexOf(".", pt1);
    var pld = JSON.parse(
        atob(jwt.substring(pt1, pt2))
    );
    sessionStorage.jwt = JSON.stringify({
      bbsso: bbssotk,
      token: jwt,
      user: pld.user,
      url: pld.url,
      payload: pld
    });
  };
  
  
  ref.auth = function(url, urlArray, oncomplete) {
    if(!url) {
      throw "! Error [micro-core.js::auth]-> Bad URL: "+ url;
    }
    if(!urlArray) {
      throw "! Error [micro-core.js::auth]-> Bad URL Array: "+ urlArray;
    }
    var bbssotk = ref.getCookie("BBSSOToken");
    var ssoacr = ref.getCookie("ssoacr");
    if(bbssotk === "") {
      throw "! Error [micro-core.js::auth]-> Bad Null BBSSOToken. User Must be Authenticated";
    }
    $.ajax({
      async: false,
      url: url,
      method: "POST",
      headers: {
        "X-BBSSOToken": bbssotk,
        "X-ssoacr": ssoacr
      },
      contentType: "application/json",
      data: JSON.stringify(urlArray),
      global: false,
      error: function(xhr, status) {
        sessionStorage.jwt = false;
        throw "! Error [micro-core.js::auth]-> Authentication Failed. "+ xhr.status+ " "+ xhr.statusText;
      },
      success: function(data, status, xhr) {
        setJWT(data, bbssotk);
      },
      complete: oncomplete
    });
  };
  
}