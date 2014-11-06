'use strict';

describe("app_spec.js: App", function () {

    it('should map routes to controllers', function () {
        module('ThePunisher');

        var routes = [{
            route: '/admin',
            ctrl: 'AdminCtrl',
            tpl: 'admin-ctrl'
        }, {
            route: '/',
            ctrl: 'RouletteCtrl',
            tpl: 'roulette-ctrl'
        }];

        inject(function ($route) {
            var testRoute = function (route, ctrl, tpl) {
                expect($route.routes[route].controller).toBe(ctrl);
                expect($route.routes[route].templateUrl).toEqual('templates/' + tpl + '.html');
            };

            routes.forEach(function (r) {
                testRoute(r.route, r.ctrl, r.tpl);
            });
        });
    });

});