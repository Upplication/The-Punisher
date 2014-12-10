(function (app) {
    'use strict';

    /**
     * Scrolls body element to top with animation
     * Taken from: http://stackoverflow.com/a/24559613
     * @param {Number} scrollDuration Duration of the scroll
     */
    var scrollToTop = function (scrollDuration) {
        var scrollHeight = window.scrollY,
            scrollStep = Math.PI / ( scrollDuration / 15 ),
            cosParameter = scrollHeight / 2;
        var scrollCount = 0,
            scrollMargin;
        requestAnimationFrame(step);
        function step() {
            setTimeout(function () {
                if (window.scrollY != 0) {
                    requestAnimationFrame(step);
                    scrollCount = scrollCount + 1;
                    scrollMargin = cosParameter - cosParameter * Math.cos(scrollCount * scrollStep);
                    window.scrollTo(0, ( scrollHeight - scrollMargin ));
                }
            }, 15);
        }
    };

    app.directive('appHeader', function () {
        return {
            restrict : 'E',
            templateUrl : '/templates/header-directive.html',
            controller : ['$scope', '$location', function ($scope, $location) {
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
                };

                if ($location.path() === '/admin') {
                    $scope.goToAdmin();
                }
            }],
            link : function (scope, elem) {
                elem.find('h1')[0].addEventListener('click', function () {
                    scrollToTop(200);
                });
            }
        };
    });

}(window.punisher));