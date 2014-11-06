(function (app) {
    'use strict';

    app.directive('appHeader', function () {
        return {
            restrict: 'E',
            template: '',
            controller: ['$scope', '$location', function ($scope, $location) {
                $scope.showAdminBtn = true;
                $scope.showBackBtn = false;

                /**
                 * Goes to the admin screen
                 */
                $scope.goToAdmin = function () {
                    $location.path('/admin');
                    $scope.showAdminBtn = false;
                    $scope.showBackBtn = true;
                };

                /**
                 * Goes back to the main screen
                 */
                $scope.goBack = function () {
                    $location.path('/');
                    $scope.showAdminBtn = true;
                    $scope.showBackBtn = false;
                }
            }]
        };
    });

}(window.punisher));