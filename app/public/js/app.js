(function (angular, window) {
    'use strict';

    var app = angular.module('ThePunisher', ['ngRoute']);

    app.config(['$routeProvider', function (p) {
        p.when('/', {
            controller: 'PunishmentsCtrl',
            templateUrl: 'templates/punishments-ctrl.html'
        }).when('/admin', {
            controller: 'AdminCtrl',
            templateUrl: 'templates/admin-ctrl.html'
        }).when('/punishment/:id/edit', {
            controller: 'EditPunishmentCtrl',
            templateUrl: 'templates/punishment-detail-ctrl.html'
        }).when('/punishment/new', {
            controller: 'NewPunishmentCtrl',
            templateUrl: 'templates/punishment-detail-ctrl.html'
        }).otherwise({
            redirectTo: '/'
        });
    }]);

    window.punisher = app;
}(angular, window));