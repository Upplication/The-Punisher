(function (angular, window) {
    'use strict';

    var app = angular.module('ThePunisher', ['ngRoute']);

    app.config(['$routeProvider', function (p) {
        p.when('/', {
            controller: 'RouletteCtrl',
            templateUrl: 'templates/roulette-ctrl.html'
        }).when('/admin', {
            controller: 'AdminCtrl',
            templateUrl: 'templates/admin-ctrl.html'
        }).otherwise({
            redirectTo: '/'
        });
    }]);

    window.punisher = app;
}(angular, window));