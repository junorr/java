
function itemFormat(position, item) {
  var it = new ItemFormat();
  it.setItemFormat(position, item);
  return it;
}

function ItemFormat() {
  
  var item;
  
  var position;
  
  
  this.getItem = getItem;
  
  this.setItem = setItem;
  
  this.getPosition = getPosition;
  
  this.setPosition = setPosition;
  
  this.setItemFormat = setItemFormat;
  
  this.string = string;
  
  
  function getItem() {
    return this.item;
  }
  
  
  function setItem(it) {
    this.item = it;
  }
  
  
  function getPosition() {
    return this.position;
  }
  
  
  function setPosition(pos) {
    this.position = pos;
  }
  
  
  function setItemFormat(pos, it) {
    this.item = it;
    this.position = pos;
  }
  
  
  function string() {
    return "[ItemFormat: { "+ this.position+ ", '"+ this.item+ "' }]";
  }
  
}


function parseFormat(format) {
  if(!format) return null;
  
  var items = [];
  var idx = 0;
  
  for(i = 0; i < format.length; i++) {
    var c = format.substr(i, 1);
    if(c != "_")
      items[idx++] = itemFormat(i, c);
  }
  
  return items;
}


function backspace(key) {
  if(typeof(key) != "undefined"
      && key == 8) return true;
  return false;
}


function fv(val, maxlength, items) {
  if(!val || !maxlength || !items) return null;
  
  var nval = val;
  for(i = 0; i < items.length; i++) {
    var item = items[i];
    if(!item) return null;
    if(nval.length >= item.getPosition())
      nval = nval.substr(0, item.getPosition()) 
          + item.getItem() 
          + nval.substr(item.getPosition());
  }
  
  return nval;
}


function formatNumber(comp, format, key) {
  if(backspace(key)) return;
  if(!comp || !format) return;
  
  var maxlength = format.length;
  var val = comp.value;
  if(!val) return;
  
  if(val.length >= maxlength) {
    comp.value = val.substr(0, maxlength);
    return;
  }
  
  var nval = "";
  for(var i = 0; i < val.length; i++) {
    var c = val.substring(i, i+1);
    if(isNumber(c)) nval += c;
  }
  
  comp.value = fv(nval, maxlength, parseFormat(format));
}


function formatLetters(comp, format, key) {
  if(backspace(key)) return;
  if(!comp || !format) return;
  
  var maxlength = format.length;
  var val = comp.value;
  if(!val) return;
  
  if(val.length >= maxlength) {
    comp.value = val.substr(0, maxlength);
    return;
  }
  
  var nval = "";
  for(var i = 0; i < val.length; i++) {
    var c = val.substring(i, i+1);
    if(isLetter(c)) nval += c;
  }
  
  comp.value = fv(nval, maxlength, parseFormat(format));
}


function format(comp, format, key) {
  if(backspace(key)) return;
  if(!comp || !format) return;
  
  var maxlength = format.length;
  var val = comp.value;
  if(!val) return;
  
  if(val.length >= maxlength) {
    comp.value = val.substr(0, maxlength);
    return;
  }
  
  comp.value = fv(nval, maxlength, parseFormat(format));
}


function isNumber(n) {
  if(!n) return false;
  var numbers = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"];
  var i = 0;
  for(i = 0; i < numbers.length; i++) {
    if(n == numbers[i])
      return true;
  }
  return false;
}


function isLetter(n) {
  if(!n) return false;
  var letters = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"];
  var i = 0;
  for(i = 0; i < letters.length; i++) {
    if(n.toLowerCase() == letters[i])
      return true;
  }
  return false;
}

