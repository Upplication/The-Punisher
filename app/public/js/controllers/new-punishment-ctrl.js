(function (app) {
    'use strict';

    app.controller('NewPunishmentCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.name = '';
        $scope.description = '';
        $scope.error = false;
        $scope.success = false;
        $scope.lastPunishment = {};

        $scope.isValid = function () {
            return $scope.name.length > 0 && $scope.description.length > 0;
        };

        $scope.doAction = function () {
            $http.post('/punishment/create', {
                title: $scope.name,
                description: $scope.description
            }).then(function (response) {
                $scope.error = false;
                $scope.success = true;
                $scope.name = '';
                $scope.description = '';
                $scope.lastPunishment = response.data;
            }, function () {
                $scope.error = true;
                $scope.success = false;
            });
        };
    }]);

}(window.punisher));