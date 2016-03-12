angular.module('hello', [ 'ngRoute' ])
  .config(function($routeProvider, $httpProvider) {

    $routeProvider.when('/', {
      templateUrl : 'home.html',
      controller : 'home',
      controllerAs: 'controller'
    }).when('/login', {
      templateUrl : 'login.html',
      controller : 'navigation',
      controllerAs: 'controller'
    }).otherwise('/');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

  })
  
  .controller('home', function($http) {
  var self = this;
  $http.get('token').success(function(token) {
    $http({
      url : 'http://localhost:9000',
      method : 'GET',
      headers : {
        'X-Auth-Token' : token.token
      }
    }).success(function(data) {
      self.greeting = data;
    });
  })
})
 
  .controller('navigation', 
		  function($rootScope, $http, $location, $route) {

	  var self = this;

	  $http.get('user').success(function(data) {
	    if (data.name) {
	      $rootScope.authenticated = true;
	    } else {
	      $rootScope.authenticated = false;
	    }
	  }).error(function() {
	    $rootScope.authenticated = false;
	  });

  self.credentials = {};

  
  self.logout = function() {
	  $http.post('logout', {}).finally(function() {
	    $rootScope.authenticated = false;
	    $location.path("/");
	  });
	}
  
});