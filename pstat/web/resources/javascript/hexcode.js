/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var hex = new HexCode();

function HexCode() {
  
  this.toHexString = toHexString;
  
  this.fromHexString = fromHexString;
  
  this.get16BytesLength = get16BytesLength;
  
  
  function toHexString(array) {
    if(array == null || array.length == 0)
      return false;
          
    var sb = '';
    for(var i = 0; array.length > i; i++) {
      var high = ((array[i] >> 4) & 0xf) << 4;
      var low = array[i] & 0xf;
      if(high == 0) sb += '0';
      var num = (high | low);
      sb += num.toString(16);
    }
          
    return sb.toString();
  }
  
  
  function fromHexString(hex) {
    if(hex == null || hex.length == 0) 
      return false;
    
    var len = hex.length;
    var bytes = new Array(len / 2);
    
    for(var i = 0; i < len; i += 2) {
      bytes[i / 2] = parseInt(
          (parseInt(hex.charAt(i), 16) << 4)
          + parseInt(hex.charAt(i + 1), 16));
    }
    return bytes;
  }
  
  
  function get16BytesLength(bs) {
    if(bs == null || bs.length <= 16)
      return bs;
    
    var nb = new Array(16);
    for(var i = 0; i < nb.length; i++) {
      nb[i] = bs[i];
    }
    return nb;
  }
  
};