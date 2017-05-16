
var login = new Vue({
  el: "#loggedUser",
  data: {
    user: ""
  },
  computed: {
    userName: function() {
      return this.isLogged ? "Welcome " 
          + this.user.toUpperCase() + "!" : "";
    },
    isLogged: function() {
      return this.user && this.user.length > 0;
    }
  }
});


$.get("login", function(data) {
  if(typeof data === 'string') {
    login.user = data;
  }
}, "json")/*.fail(function(data) {
  console.log("# fail on login: "+ JSON.stringify(data));
})*/;

$("#page-body").load("nav/login.html");