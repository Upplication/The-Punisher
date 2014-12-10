'use strict';

describe('roulette-ctrl_spec.js: Roulette - ', function () {
    var $scope, $httpBackend;

    var punishments = [
        {
            id : 1,
            title : "Name",
            description : "Description"
        },
        {
            id : 2,
            title : "Name2",
            description : "Description2"
        },
        {
            id : 3,
            title : "Name3",
            description : "Description3"
        },
        {
            id : 4,
            title : "Name4",
            description : "Description4"
        }
    ];

    beforeEach(module('ThePunisher'));

    beforeEach(inject(function ($rootScope, $controller, $injector) {
        $scope = $rootScope.$new();
        $controller('RouletteCtrl', {$scope: $scope});
        $httpBackend = $injector.get('$httpBackend');
    }));

    describe('when the controller is initialised', function () {
        it('a list of punishments should be loaded', function () {
            expect($scope.loading).toBe(true);
            expect($scope.showButton).toBe(false);
            $scope.init();

            $httpBackend.expectGET(/punishment\/list/).respond(200, {punishments: punishments});
            $httpBackend.flush();
            expect($scope.loading).toBe(false);
            expect($scope.showButton).toBe(true);
            expect($scope.punishments.length).toBe(punishments.length);
        });
    });

});