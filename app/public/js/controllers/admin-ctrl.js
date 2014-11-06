(function (app) {
    'use strict';

    app.controller('AdminCtrl', ['$scope', 'punishmentService', function ($scope, punishmentService) {
        $scope.punishments = [];
        $scope.loading = true;

        /**
         * Add a new punishment to the admin list when it is created
         */
        $scope.$on('punishment-added', function (e, p) {
            $scope.punishments.push(p);
        });

        /**
         * Load punishments when the controller is loaded
         */
        $scope.init = function () {
            punishmentService.list()
                .then(function (punishments) {
                    for (var i = 0, len = punishments.length; i < len; i++) {
                        $scope.punishments.push(punishments[i]);
                    }

                    $scope.loading = false;
                });
        };
    }]);

}(window.punisher));