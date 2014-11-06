'use strict';

describe('punishment-service_spec.js', function () {
    var httpBackend, service;

    beforeEach(module('ThePunisher'));

    beforeEach(inject(function ($injector) {
        httpBackend = $injector.get('$httpBackend');
        service = $injector.get('punishmentService');
    }));

    describe("When we try to create a punishment", function () {
        describe('with valid data', function () {
            it('should return a successful response and the punishment created', function () {
                service.create("Name", "Description")
                    .then(function (data) {
                        expect(data.id).toBe(1);
                    });

                httpBackend.expectPOST('/punishment/create').respond(200, {
                    id : 1,
                    title : "Name",
                    description : "Description"
                });

                httpBackend.flush();
            });
        });

        describe('with invalid data', function () {
            it('should return an error', function () {
                service.create("", "")
                    .then(function () {
                        expect(true).toBe(false);
                    }, function () {
                        expect(true).toBe(true);
                    });

                httpBackend.expectPOST('/punishment/create').respond(400, {});
                httpBackend.flush();
            });
        });
    });

    describe("When we try to update a punishment", function () {
        describe('with valid data', function () {
            it('should return a successful response and the punishment created', function () {
                service.update(1, "Name2", "Description2")
                    .then(function (data) {
                        expect(data.id).toBe(1);
                        expect(data.title).toBe("Name2");
                        expect(data.description).toBe("Description2");
                    });

                httpBackend.expectPUT('/punishment/1').respond(200, {
                    id : 1,
                    title : "Name2",
                    description : "Description2"
                });

                httpBackend.flush();
            });
        });

        describe('with invalid data', function () {
            it('should return an error', function () {
                service.update(1, "", "")
                    .then(function () {
                        expect(true).toBe(false);
                    }, function () {
                        expect(true).toBe(true);
                    });

                httpBackend.expectPUT('/punishment/1').respond(400, {});
                httpBackend.flush();
            });
        });
    });

    describe("When we try to delete a punishment", function () {
        describe('with valid id', function () {
            it('should return a successful response and the punishment created', function () {
                service.remove(1)
                    .then(function () {
                        expect(true).toBe(true);
                    }, function () {
                        expect(false).toBe(true);
                    });

                httpBackend.expectDELETE('/punishment/1').respond(200, {});
                httpBackend.flush();
            });
        });

        describe('with an inexistent id', function () {
            it('should return an error', function () {
                service.remove(0)
                    .then(function () {
                        expect(true).toBe(false);
                    }, function () {
                        expect(true).toBe(true);
                    });

                httpBackend.expectDELETE('/punishment/0').respond(404, {});
                httpBackend.flush();
            });
        });
    });

    describe("When we try to get the list of punishments", function () {
        var punishmentList = [
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

        it("should retrieve the list of all punishments", function () {
            service.list()
                .then(function (punishments) {
                    expect(punishments).toBe(punishmentList);
                });

            httpBackend.expectGET('/punishment/list').respond(200, punishmentList);
            httpBackend.flush();
        });
    });
});