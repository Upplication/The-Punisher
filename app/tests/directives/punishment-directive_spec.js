'use strict';

describe('punishment-directive_spec.js', function () {
    var rootScope, $scope, $scope2, $location, editablePunishment, nonEditablePunishment, $httpBackend, $timeout,
        punishment = {
            id : 1,
            title : 'Title',
            description : 'Description'
        };
    beforeEach(module('ThePunisher'));

    beforeEach(inject(function ($rootScope, $injector) {
        rootScope = $rootScope;
        $location = $injector.get('$location');
        var $compile = $injector.get('$compile');
        $httpBackend = $injector.get('$httpBackend');
        $timeout = $injector.get('$timeout');

        rootScope.p = punishment;

        $scope = $rootScope.$new();
        $scope2 = $rootScope.$new();

        editablePunishment = $compile('<punishment model="p" editable="true"></punishment>')($scope);
        nonEditablePunishment = $compile('<punishment model="p" editable="false"></punishment>')($scope2);
        $scope.$digest();
        $scope2.$digest();
    }));

    describe('given an editable punishment', function () {
        it('should show the punishment info', function () {
            var isolated = editablePunishment.isolateScope();

            expect(isolated.model.id).toBe(punishment.id);
            expect(isolated.model.title).toBe(punishment.title);
            expect(isolated.model.description).toBe(punishment.description);
        });

        it('should be editable', function () {
            expect(editablePunishment.isolateScope().editable).toBe(true);
        });

        it('when the edit button is clicked it should show the editor', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.edit();
            expect(isolated.editing).toBe(true);
            expect(isolated.data.id).toBe(punishment.id);
            expect(isolated.data.title).toBe(punishment.title);
            expect(isolated.data.description).toBe(punishment.description);
        });

        it('when we edit and the title is empty, isValid should return false', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.edit();
            isolated.data.title = '';
            expect(isolated.data.title).not.toBe(isolated.model.title);
            expect(isolated.isValid()).toBe(false);
        });

        it('when we edit and the description is empty, isValid should return false', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.edit();
            isolated.data.description = '';
            expect(isolated.data.description).not.toBe(isolated.model.description);
            expect(isolated.isValid()).toBe(false);
        });

        it('when we edit and the data is valid the punishment should be updated', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.edit();
            isolated.data.description = 'Description updated';
            expect(isolated.isValid()).toBe(true);

            isolated.saveChanges();

            $httpBackend.expectPUT('/punishment/1').respond(200, {
                id : 1,
                title : "Title",
                description : "Description updated"
            });

            $httpBackend.flush();

            expect(isolated.data.id).toBe(punishment.id);
            expect(isolated.data.title).toBe(punishment.title);
            expect(isolated.data.description).toBe("Description updated");
        });

        it('when we edit and the data is valid but there is an error the punishment should not be updated and an error must be shown', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.edit();
            isolated.data.description = 'Description updated';
            expect(isolated.isValid()).toBe(true);

            isolated.saveChanges();

            $httpBackend.expectPUT('/punishment/1').respond(400, {});

            $httpBackend.flush();

            expect(isolated.data.id).toBe(punishment.id);
            expect(isolated.data.title).toBe(punishment.title);
            expect(isolated.data.description).toBe(punishment.description);
            expect(isolated.error).toBe(true);
        });

        it('when we edit and then we cancel the editing the data should remain equal', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.edit();
            expect(isolated.editing).toBe(true);
            isolated.cancel();
            expect(isolated.editing).toBe(false);

            expect(isolated.data.id).toBe(punishment.id);
            expect(isolated.data.title).toBe(punishment.title);
            expect(isolated.data.description).toBe(punishment.description);
        });

        it('when we delete the punishment it should the undo screen', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.remove();
            expect(isolated.showUndo).toBe(true);
            expect(isolated.delete).toBe(true);
            expect(isolated.visible).toBe(true);
        });

        it('when we delete the punishment and click undo the punishment must not be deleted', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.remove();
            expect(isolated.showUndo).toBe(true);
            expect(isolated.delete).toBe(true);
            expect(isolated.visible).toBe(true);

            isolated.undo();
            expect(isolated.showUndo).toBe(false);
            expect(isolated.delete).toBe(false);
            expect(isolated.visible).toBe(true);
        });

        it('when we delete the punishment and we do not click undo after 6 seconds the punishment should be deleted', function () {
            var isolated = editablePunishment.isolateScope();

            isolated.remove();
            $httpBackend.expectDELETE('/punishment/1').respond(200, {});
            $timeout.flush();
            $httpBackend.flush();

            expect(isolated.visible).toBe(false);
        });
    });

    describe('given a non editable punishment', function () {
        it('should show the punishment info', function () {
            var isolated = nonEditablePunishment.isolateScope();

            expect(isolated.model.id).toBe(punishment.id);
            expect(isolated.model.title).toBe(punishment.title);
            expect(isolated.model.description).toBe(punishment.description);
        });

        it('should not be editable', function () {
            expect(nonEditablePunishment.isolateScope().editable).toBe(false);
        });
    });
});