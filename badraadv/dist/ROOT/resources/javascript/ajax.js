function Ajax() {
  
  var ajax = createAjax();
  
  var error;
  
  var success;
  
  var complete;
  
  var processing;
  
  
  this.createAjax = createAjax;
  
  this.doGet = doGet;
  
  this.doSyncGet = doSyncGet;
  
  this.doPost = doPost;
  
  this.doSyncPost = doSyncPost;
  
  this.onError = onError;
  
  this.onSuccess = onSuccess;
  
  this.onComplete = onComplete;
  
  this.onProcessing = onProcessing;
  
  
  function createAjax() {
    var ajax = null;
    if (window.XMLHttpRequest) {
      // code for IE7+, Firefox, Chrome, Opera, Safari
      ajax = new XMLHttpRequest();
    } else {
      // code for IE6, IE5
      ajax = new ActiveXObject("Microsoft.XMLHTTP");
    }
    if(ajax) {
      ajax.onreadystatechange = onReady;
    }
    return ajax;
  }
  
  
  function doGet(url, data) {
    ajax.open("GET", url, true);
    ajax.send(data);
    return this;
  }
  
  
  function doSyncGet(url, data) {
    ajax.open("GET", url, false);
    ajax.send(data);
    return ajax.responseText;
  }
  
  
  function doPost(url, data) {
    ajax.open("POST", url, true);
    ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ajax.send(data);
    return this;
  }
  
  
  function doSyncPost(url, data) {
    ajax.open("POST", url, true);
    ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ajax.send(data);
    return ajax.responseText;
  }
  
  
  function onError(call) {
    error = call;
    return this;
  }
  
  
  function onSuccess(call) {
    success = call;
    return this;
  }
  
  
  function onComplete(call) {
    complete = call;
    return this;
  }
  
  
  function onProcessing(call) {
    processing = call;
    return this;
  }
  
  
  function onReady() {
    //request finished and response is ready
    if(ajax.readyState == 4) {
      if(complete) complete(ajax);
      if(ajax.status == 200 && success) 
        success(ajax.responseText);
      else error(ajax);
    //processing request 
    } else if(ajax.readyState == 3) {
      if(processing) processing();
    }
  }
  
  
  return this;
}