(function (app) {
    'use strict';

    app.controller('NewPunishmentCtrl', ['$rootScope', '$scope', '$http', 'punishmentService',
        function ($rootScope, $scope, $http, punishmentService) {
            $scope.name = '';
            $scope.description = '';
            $scope.error = false;
            $scope.success = false;
            $scope.lastPunishment = {};

            /**
             * Determines if the form is valid or not and the punishment can be created
             * @returns {boolean} If is valid
             */
            $scope.isValid = function () {
                return $scope.name.length > 0 && $scope.description.length > 0;
            };

            /**
             * Performs the action and creates the punishment on the server
             */
            $scope.doAction = function () {
                punishmentService.create($scope.name, $scope.description)
                    .then(function (data) {
                        $scope.error = false;
                        $scope.success = true;
                        $scope.name = '';
                        $scope.description = '';
                        $scope.lastPunishment = data;

                        $rootScope.$broadcast('punishment-added', data);
                    }, function () {
                        $scope.error = true;
                        $scope.success = false;
                    });
            };
        }
    ]);

}(window.punisher));