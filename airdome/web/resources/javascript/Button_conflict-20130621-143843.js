var parent;


function Link(id) {
  return Button(id, true);
}


function Button(id, link) {
  
  var ID = id;
  
  var button = "#" + ID;
  
  var img1 = button + " img:first";
  
  var img2 = button + " img:last";
  
  var text = button + " span:first";
  
  var img1ID = $(img1).attr("id", ID + "_" + Math.round(Math.random()*1000000) + "_img1").attr("id");
  
  var img2ID;
  if($(img2).attr("src") && $(img1).attr("src") != $(img2).attr("src")) 
    img2ID = $(img2).attr("id", ID + "_" + Math.round(Math.random()*1000000) + "_img2").attr("id");
  
  var textID = $(text).attr("id", ID + "_" + Math.round(Math.random()*1000000) + "_text").attr("id");
  
  parent = $(button).parents("div:first").attr("id");
  
  var tb = tab(parent);
  
  var eastdif = 16;
  
  var linktype = link;
  
  var pagetitle;
  
  var altURL;
  
  
  if(linktype)
    linkFormat();
  else buttonFormat();
  
  
  this.north = north;
  this.south = south;
  this.west = west;
  this.east = east;
  this.after = after;
  this.before = before;
  this.setVertical = setVertical;
  this.setHorizontal = setHorizontal;
  this.centerHorizontal = centerHorizontal;
  this.centerVertical = centerVertical;
  this.centerBoth = centerBoth;
  this.size = size;
  this.sizeImg2 = sizeImg2;
  this.moveImg2 = moveImg2;
  this.createButton = createButton;
  this.createLink = createLink;
  this.createAjaxLink = createAjaxLink;
  this.ajaxChangeTitle = ajaxChangeTitle;
  this.bookmarkableURL = bookmarkableURL;
  
  
  function north(n) {
    tb.north(ID, n);
    return this;
  }
  
  
  function east(n) {
    tb.east(ID, n - eastdif);
    return this;
  }
  
  
  function south(n) {
    tb.south(ID, n);
    return this;
  }
  
  
  function west(n) {
    tb.west(ID, n);
    return this;
  }
  
  
  function after(comp) {
    tb.after(comp);
    return this;
  }
  
  
  function before(comp) {
    tb.before(comp);
    return this;
  }
  
  
  function setVertical(n) {
    tb.setVertical(ID, n);
    return this;
  }
  
  
  function setHorizontal(n) {
    tb.setHorizontal(ID, n);
    return this;
  }
  
  
  function centerHorizontal() {
    tb.centerHorizontal(ID);
    return this;
  }
  
  
  function centerVertical() {
    tb.centerVertical(ID);
    return this;
  }
  
  
  function centerBoth() {
    tb.centerBoth(ID);
    return this;
  }
  
  
  function buttonFormat() {
    linktype = false;
    
    var zindex = $(button).css("z-index");
    if(!zindex) zindex = 2;
    
    $(button).css("position", "absolute")
      .css("z-index", zindex)
      .css("font-family", "Arial")
      .css("font-weight", "bold")
      .css("text-decoration", "none")
      .css("color", "black");
    $(text).css("position", "absolute")
      .css("z-index", zindex + 1);
    $(img1).css("position", "absolute")
      .css("z-index", zindex)
      .css("top", 0).css("left", 0)
      .css("border", 0);
      
    $(button).width($(img1).width())
      .height($(img1).height());
    
    tab(ID).centerBoth(textID);
    
    if(img2ID) {
      $(img2).css("position", "absolute")
        .css("z-index", zindex)
        .css("top", 0).css("left", 0)
        .css("border", 0);
      $(img2).hide();
      
      $(button).mouseover(function () {
        $(img1).hide();
        $(img2).show();
      }).mouseout(function () {
        $(img1).show();
        $(img2).hide();
      });
    }
  }
  
  
  function linkFormat() {
    linktype = true;
    
    var zindex = $(button).css("z-index");
    if(!zindex) zindex = 2;
    
    $(button).css("position", "absolute")
      .css("z-index", zindex)
      .css("font-family", "Arial")
    $(text).css("position", "absolute")
      .css("z-index", zindex + 1);
    $(img1).css("position", "absolute")
      .css("z-index", zindex)
      .css("top", 0).css("left", 0)
      .css("border", 0);
      
    tab(ID).centerHorizontal(textID)
      .after(img1ID).setVertical(textID, 2);

    if(img2ID) {
      $(img2).css("position", "absolute")
        .css("z-index", zindex)
        .css("top", 0).css("left", 0)
        .css("border", 0);
      $(img2).hide();
      
      $(button).mouseover(function () {
        //$(img1).hide();
        $(img2).show();
      }).mouseout(function () {
        //$(img1).show();
        $(img2).hide();
      });
    }
  }
  
  
  function createButton(callback) {
    $(button).click(function(event) {
      event.preventDefault();
      if(img2ID) {
        $(img1).show();
        $(img2).hide();
      }
      
      var sub = function() {$(button).parents("form:first").submit();};
      if(!callback) sub();
      
      var ret;
      ret = callback();
      if(ret || ret == null) sub();
    });
    
    return this;
  }
  
  
  function createAjaxButton(link, target, callback, data) {
    $(button).click(function(event) {
      event.preventDefault();
      if(img2ID) {
        $(img1).show();
        $(img2).hide();
      }
      
      var purl = $(location).attr("href");
      
      //previde partes duplicadas no endereço
      if(link && link.indexOf("/") > 0) {
        var ibar = link.indexOf("/");
        if(purl.indexOf(link.substring(0, ibar)) > 0
            && (purl.indexOf("#") <= 0 
            || purl.indexOf(link.substring(0, ibar)) < 
              purl.indexOf("#")))
          link = link.substring(ibar + 1, link.length);
      }
          
      if(callback) {
        var ret;
        ret = callback();
        if(!ret && ret != null) return;
      }
      
      var ajax = new Ajax();
      
      ajax.onSuccess(function(resp) {
        if(!target) return;
        $("#"+target).html(resp);
        if(pagetitle) $(document).attr("title", pagetitle);
        
        if(altURL) {
          if(purl.indexOf("#") > 0) {
            purl = purl.substring(0, purl.indexOf("#"));
          }
          $(location).attr("href", purl + "#" + altURL);
        }
      });
      
      ajax.onError(function(ajax) {
        if(!target) return;
        $("#"+target).html("AjaxError: " + ajax.status + " - " + ajax.statusText);
      })
      
      ajax.doGet(link, data);
    })
    return this;
  }
  
  
  function createLink(callback, link) {
    if(link) $(button).attr("href", link);
    $(button).click(function(event) {
      if(img2ID) {
        $(img1).show();
        $(img2).hide();
      }
      if(callback) {
        var ret = callback();
        if(!ret && ret != null) event.preventDefault();
      }
    })
    return this;
  }
  
  
  function createAjaxLink(link, target, callback, data) {
    $(button).click(function(event) {
      event.preventDefault();
      if(img2ID) {
        $(img1).show();
        $(img2).hide();
      }
      
      var purl = $(location).attr("href");
      
      //previde partes duplicadas no endereço
      if(link && link.indexOf("/") > 0) {
        var ibar = link.indexOf("/");
        if(purl.indexOf(link.substring(0, ibar)) > 0
            && (purl.indexOf("#") <= 0 
            || purl.indexOf(link.substring(0, ibar)) < 
              purl.indexOf("#")))
          link = link.substring(ibar + 1, link.length);
      }
          
      if(callback) {
        var ret = callback();
        if(!ret && ret != null) return;
      }
      
      var ajax = new Ajax();
      
      ajax.onSuccess(function(resp) {
        if(!target) return;
        $("#"+target).html(resp);
        if(pagetitle) $(document).attr("title", pagetitle);
        
        if(altURL) {
          if(purl.indexOf("#") > 0) {
            purl = purl.substring(0, purl.indexOf("#"));
          }
          $(location).attr("href", purl + "#" + altURL);
        }
      });
      
      ajax.onError(function(x) {
        if(!target) return;
        $("#"+target).html("AjaxError: " + x.status + " - " + x.statusText);
      })
      
      ajax.doGet(link, data);
    })
    return this;
  }
  
  
  function ajaxChangeTitle(newtitle) {
    pagetitle = newtitle;
    return this;
  }
  
  
  function bookmarkableURL(url) {
    altURL = url;
    return this;
  }
  
  
  function size(w, h) {
    $(button).width(w)
      .height(h);
    $(img1).width(w)
      .height(h);
    $(img2).width(w)
      .height(h);
    
    if(w <= 90) $(button).css("font-size", "small");
    
    if(linktype)
      tab(ID).centerHorizontal(textID)
        .after(img1ID).setVertical(textID, 0);
    else 
      tab(ID).centerBoth(textID);
    
    return this;
  }


  function sizeImg2(w, h) {
    $(img2).width(w)
      .height(h);
    return this;
  }
  
  
  function moveImg2(w, h) {
    var x = $(img2).offset().left;
    var y = $(img2).offset().top;
    x += w;
    y += h;
    $(img2).css("left", x).css("top", y);
    return this;
  }


  return this;
}