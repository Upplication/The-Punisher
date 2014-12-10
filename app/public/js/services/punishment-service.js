(function (app, config) {
    'use strict';

    app.service('punishmentService', ['$q', '$http',
        function ($q, $http) {
            var service = {};

            /**
             * Creates a new punishment on the server
             * @param {String} name        Name of the punishment
             * @param {String} description Description of the punishment
             * @return {Promise} Promise that will be fulfilled after the creation
             */
            service.create = function (name, description) {
                var deferred = $q.defer();

                $http.post(config.backend + '/punishment/create', {
                    title : name,
                    description : description
                }).then(function (response) {
                    deferred.resolve(response.data);
                }, function () {
                    deferred.reject(false);
                });

                return deferred.promise;
            };

            /**
             * Updates a punishment on the server
             * @param {Number} id          Identifier of the punishment
             * @param {String} name        Name of the punishment
             * @param {String} description Description of the punishment
             * @return {Promise} Promise that will be fulfilled after the update
             */
            service.update = function (id, name, description) {
                var deferred = $q.defer();

                $http.put(config.backend + '/punishment/' + Number(id), {
                    title : name,
                    description : description
                }).then(function (response) {
                    deferred.resolve(response.data);
                }, function () {
                    deferred.reject(false);
                });

                return deferred.promise;
            };

            /**
             * Removes a punishment from the server
             * @param {Number} id Identifier of the punishment
             * @return {Promise} Promise that will be fulfilled after the deletion
             */
            service.remove = function (id) {
                var deferred = $q.defer();

                $http.delete(config.backend + '/punishment/' + Number(id))
                    .then(function () {
                        deferred.resolve(true);
                    }, function () {
                        deferred.reject(false);
                    });

                return deferred.promise;
            };

            /**
             * Retrieves a list of punishments from the server
             * @returns {Promise} Promise that will be fulfilled after the punishments are loaded
             */
            service.list = function () {
                var deferred = $q.defer();

                $http.get(config.backend + '/punishment/list')
                    .then(function (response) {
                        if (response.data && response.data.punishments) {
                            deferred.resolve(response.data.punishments);
                        } else {
                            deferred.reject(false);
                        }
                    }, function () {
                        deferred.reject(false);
                    });

                return deferred.promise;
            };

            return service;
        }
    ]);

}(window.punisher, window.config));