(function (app) {
    'use strict';

    app.directive('punishment', function () {
        return {
            restrict : 'E',
            templateUrl : '/templates/punishment-directive.html',
            scope : {
                model : '='
            },
            replace: true,
            controller : ['$scope', 'punishmentService', '$timeout', function ($scope, punishmentService, $timeout) {
                $scope.editing = false;
                $scope.showUndo = false;
                $scope.delete = false;
                $scope.visible = true;

                /**
                 * Starts the editing mode of the punishment
                 */
                $scope.edit = function () {
                    if (!$scope.editable) return;
                    $scope.editing = true;
                    $scope.data = angular.copy($scope.model);
                };

                /**
                 * Cancels the editing mode of the punishment
                 */
                $scope.cancel = function () {
                    $scope.editing = false;
                };

                /**
                 * Checks if the data to be updated is valid or not
                 * @returns {boolean}
                 */
                $scope.isValid = function () {
                    return $scope.data.title.trim().length > 0
                        && $scope.data.description.trim().length > 0;
                };

                /**
                 * Save the punishment to the server
                 */
                $scope.saveChanges = function () {
                    $scope.error = false;
                    if (!$scope.editable) return;

                    var data = angular.copy($scope.data);
                    punishmentService.update(
                        $scope.model.id,
                        data.title,
                        data.description
                    )
                        .then(function () {
                            $scope.model.title = data.title;
                            $scope.model.description = data.description;
                            $scope.editing = false;
                        }, function () {
                            $scope.error = true;
                        });
                };

                /**
                 * Removes the current punishment 6 seconds after showing the UNDO screen if the action is not undone
                 */
                $scope.remove = function () {
                    if (!$scope.editable) return;

                    $scope.showUndo = true;
                    $scope.delete = true;

                    $timeout(function () {
                        if ($scope.delete) {
                            punishmentService.remove($scope.model.id)
                                .then(function () {
                                    $scope.visible = false;
                                    // TODO: Send event and remove
                                }, function () {
                                    $scope.showUndo = false;
                                    $scope.delete = false;
                                });
                        } else {
                            $scope.showUndo = false;
                        }
                    }, 6000);
                };

                /**
                 * Undo the delete action
                 */
                $scope.undo = function () {
                    $scope.delete = false;
                    $scope.showUndo = false;
                    $timeout.flush();
                };
            }],
            link : function (scope, elem, attrs) {
                scope.editable = attrs.editable === 'true';
            }
        };
    });

}(window.punisher));