function encrypt(hexKey) {
  var email = $("#iemail").val();
  var pass = $("#ipass").val();
          
  var bemail = cryptoHelpers.convertStringToByteArray(email);
  var bpass = cryptoHelpers.convertStringToByteArray(pass);
  var key = hex.fromHexString(hexKey);
  var iv = hex.get16BytesLength(key);
          
  var encemail = slowAES.encrypt(bemail, slowAES.modeOfOperation.CBC, key, iv);
  var encpass = slowAES.encrypt(bpass, slowAES.modeOfOperation.CBC, key, iv);
          
  $("#hemail").val(hex.toHexString(encemail));
  $("#hpass").val(hex.toHexString(encpass));
  
  $("#loading").show();
}

