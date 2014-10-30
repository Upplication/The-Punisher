'use strict';

describe("app_spec.js: App", function () {

    it('should map routes to controllers', function () {
        module('ThePunisher');

        var routes = [{
            route: '/admin',
            ctrl: 'AdminCtrl',
            tpl: 'admin-ctrl'
        }, {
            route: '/punishment/new',
            ctrl: 'NewPunishmentCtrl',
            tpl: 'punishment-detail-ctrl'
        }, {
            route: '/punishment/:id/edit',
            ctrl: 'EditPunishmentCtrl',
            tpl: 'punishment-detail-ctrl'
        }, {
            route: '/',
            ctrl: 'PunishmentsCtrl',
            tpl: 'punishments-ctrl'
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