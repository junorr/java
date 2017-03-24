
var login = new Vue({
  el: "#loggedUser",
  data: {
    rawUser: ""
  },
  computed: {
    user: function() {
      return this.rawUser.toUpperCase();
    },
    isLogged: function() {
      return this.rawUser && this.rawUser.length > 0;
    }
  }
});


$.get("login", function(data) {
  login.rawUser = data;
}, "json");

