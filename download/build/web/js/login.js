$("#iuser").on("keydown", function(evt) {
  if(evt.which === 13) {
    $("#ipass").focus();
  }
}).focus();
$("#ipass").on("keydown", function(evt) {
  if(evt.which === 13) {
    doLogin();
  }
});

$("#iuser").focus();


function validateInput(user, pass) {
  if(!user || user.length < 1) {
    $("#iuser").focus();
    $(".alert-danger").show();
    return false;
  }
  if(!pass || pass.length < 1) {
    $("#ipass").focus();
    $(".alert-danger").show();
    return false;
  }
  return true;
}


function doLogin() {
  var user = $("#iuser").val();
  var pass = $("#ipass").val();
  if(!validateInput(user, pass)) {
    return;
  }
  $(".alert-danger").hide();
  $.post("login/", {
    user: user,
    pass: btoa(pass)
  }, function(data) {
    console.log("typeof login: "+ typeof login);
    console.log("typeof login.user: "+ typeof login.user);
    login.user = data;
    console.log("login.user: "+ login.user);
    console.log("login.userName: "+ login.userName);
    console.log("login.isLogged: "+ login.isLogged);
    if(login.isLogged) {
      $("#page-body").load("nav/ls.html");
    }
  }, "json").fail(function() {
    $(".alert-danger").show();
  });
}


console.log("typeof login: "+ typeof login);
//console.log("typeof stringify(login): "+ JSON.stringify(login));
console.log("typeof login.user: "+ typeof login.user);
console.log("typeof stringify(login.user): "+ JSON.stringify(login.user));
console.log("typeof login.userName: "+ typeof login.userName);
console.log("typeof stringify(login.userName): "+ JSON.stringify(login.userName));
console.log("typeof login.isLogged: "+ typeof login.isLogged);
console.log("typeof stringify(login.isLogged): "+ JSON.stringify(login.isLogged));


if(typeof login.isLogged === 'boolean' && login.isLogged) {
  $("#page-body").load("nav/ls.html");
}
