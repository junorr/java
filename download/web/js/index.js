/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var login = new Vue({
  el: "#loggedUser",
  data: {
    rawUser: "",
    isLogged: false
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
