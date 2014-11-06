'use strict';

describe('header-directive.js: Header directive - ', function () {
    var $scope, $location, header;
    beforeEach(module('ThePunisher'));

    beforeEach(inject(function ($rootScope, $injector) {
        $scope = $rootScope.$new();
        $location = $injector.get('$location');
        var $compile = $injector.get('$compile');

        header = $compile('<app-header></app-header>')($scope);
    }));

    describe('when is first rendered', function () {
        it('should show the admin icon but not the back icon', function () {
            expect($scope.showAdminBtn).toBe(true);
            expect($scope.showBackBtn).toBe(false);
        });
    });

    describe('when the admin icon is clicked', function () {
        it('should go to the admin page and show the back button', function () {
            $scope.goToAdmin();
            expect($scope.showBackBtn).toBe(true);
            expect($scope.showAdminBtn).toBe(false);
            expect($location.path()).toBe('/admin');
        });

        describe('then we click the back button', function () {
            it('should go again to the home page', function () {
                $scope.goBack();
                expect($scope.showBackBtn).toBe(false);
                expect($scope.showAdminBtn).toBe(true);
                expect($location.path()).toBe('/');
            });
        });
    });
});