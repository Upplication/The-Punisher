'use strict';

describe('new-punishment-ctrl_spec.js: New punishment - ', function () {
    var $scope, $httpBackend;

    var isValid = function (valid) {
        expect($scope.isValid()).toBe(valid);
    };

    var doAction = function () {
        $scope.name = 'Name';
        $scope.description = 'Description';
        $scope.doAction();
        $httpBackend.expectPOST('/punishment/create').respond(200, {
            id: 1,
            title: "Name",
            description: "Description"
        });
    };

    var doActionInvalid = function () {
        $scope.name = 'stuff';
        $scope.description = '';
        $scope.doAction();

        $httpBackend.expectPOST('/punishment/create').respond(400, '');
    };

    beforeEach(module('ThePunisher'));

    beforeEach(inject(function ($rootScope, $controller, $injector) {
        $scope = $rootScope.$new();
        $controller('NewPunishmentCtrl', {$scope: $scope});
        $httpBackend = $injector.get('$httpBackend');
    }));

    it('should have name and description empty', function () {
        expect($scope.name).toBe('');
        expect($scope.description).toBe('');
    });

    describe('when triggered "isValid"', function () {
        describe('and description and name are empty', function () {
            it('should return false', function () {
                isValid(false);
            });
        });

        describe('when name is "Name" and description is empty', function () {
            it('should return false', function () {
                $scope.name = "Name";

                isValid(false);
            });
        });

        describe('when name is empty and description is "Description"', function () {
            it('should return false', function () {
                $scope.description = "Description";

                isValid(false);
            });
        });

        describe('when name is "Name" and description is "Description"', function () {
            it('should return true', function () {
                $scope.description = "Description";
                $scope.name = "Name";

                isValid(true);
            });
        });
    });

    describe('when triggered "doAction"', function () {
        describe('when the data is valid', function () {
            it('the data should have been erased', function () {
                doAction();
                $httpBackend.flush();
                expect($scope.name).toBe('');
                expect($scope.description).toBe('');
            });

            it('the error message should not have been shown', function () {
                doAction();
                $httpBackend.flush();
                expect($scope.error).toBe(false);
            });

            it('the success message should have been shown', function () {
                doAction();
                $httpBackend.flush();
                expect($scope.success).toBe(true);
            });

            describe('the response data should match', function () {
                it('id should be 1', function () {
                    doAction();
                    $httpBackend.flush();
                    expect($scope.lastPunishment.id).toBe(1);
                });

                it('name should be "Name"', function () {
                    doAction();
                    $httpBackend.flush();
                    expect($scope.lastPunishment.title).toBe('Name');
                });

                it('description should be "Description"', function () {
                    doAction();
                    $httpBackend.flush();
                    expect($scope.lastPunishment.description).toBe('Description');
                });
            })
        });

        describe('when the data is not valid', function () {
            it('the data should not be erased', function () {
                doActionInvalid();
                $httpBackend.flush();
                expect($scope.name).toBe('stuff');
                expect($scope.description).toBe('');
            });

            it('the error message should have been shown', function () {
                doActionInvalid();
                $httpBackend.flush();
                expect($scope.error).toBe(true);
            });

            it('the success message should not have been shown', function () {
                doActionInvalid();
                $httpBackend.flush();
                expect($scope.success).toBe(false);
            });
        });
    });
});