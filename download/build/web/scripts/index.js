
var login = new Vue({
  el: "#loggedUser",
  data: {
    user: ""
  },
  computed: {
    userName: function() {
      return this.isLogged ? "Welcome " 
          + this.rawUser.toUpperCase() + "!" : "";
    },
    isLogged: function() {
      return this.rawUser && this.rawUser.length > 0;
    }
  }
});


$.get("login", function(data) {
  login.user = data;
}, "json");


$("#page-body").load("nav/login.html");