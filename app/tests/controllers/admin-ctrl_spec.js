'use strict';

describe('admin-ctrl_spec.js: Admin controller - ', function () {
    var $scope, $httpBackend, rootScope;

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
        rootScope = $rootScope;
        $scope = $rootScope.$new();
        $controller('AdminCtrl', {$scope: $scope});
        $httpBackend = $injector.get('$httpBackend');
    }));

    describe('when it is first loaded', function () {
        it('should show the loading message', function () {
            expect($scope.loading).toBe(true);
        });
    });

    describe('when it finishes loading the punishments', function () {
        it('should hide the loading message and display all punishments', function () {
            $scope.init();
            $httpBackend.expectGET('/punishment/list').respond(200, {punishments: punishments});
            $httpBackend.flush();

            expect($scope.loading).toBe(false);
            expect($scope.punishments.length).toBe(punishments.length);
        });
    });

    describe('when a new punishment is added', function () {
        it('should add that punishment', function () {
            $scope.init();
            $httpBackend.expectGET('/punishment/list').respond(200, {punishments: punishments});
            $httpBackend.flush();

            rootScope.$broadcast('punishment-added', {
                id: 5,
                title: "name5",
                description: "description5"
            });

            expect($scope.punishments.length).toBe(punishments.length + 1);
        });
    })

});