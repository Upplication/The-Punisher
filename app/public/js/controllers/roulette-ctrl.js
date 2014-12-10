(function (app) {
    'use strict';

    /**
     * CSS class names of colors
     * @type {string[]}
     */
    var colors = [
        'deep-purple', 'purple', 'deep-indigo', 'indigo', 'blue', 'light-blue', 'cyan', 'teal', 'deep-teal',
        'deep-green', 'green', 'deep-light-green', 'light-green', 'deep-lime', 'lime', 'yellow', 'amber', 'orange', 'deep-orange'
    ];

    /**
     * Returns a list of rainbow sorted colors
     * @param {Number} n Number of punishments
     * @return {string[]}
     */
    var getRainbowColors = function (n) {
        var result = [],
            pos = 0,
            reverse = false;

        while (result.length < n) {
            result.push(colors[pos]);
            reverse ? pos-- : pos++;
            if (pos >= colors.length) {
                pos -= 2;
                reverse = true;
            } else if (pos < 0) {
                pos += 2;
                reverse = false;
            }
        }

        return result;
    };

    app.controller('RouletteCtrl', ['$scope', 'punishmentService', '$timeout', function ($scope, punishmentService, $timeout) {
        $scope.punishments = [];
        $scope.loading = true;
        $scope.showButton = false;
        $scope.selected = undefined;
        $scope.colors = [];

        $scope.init = function () {
            punishmentService.list()
                .then(function (punishments) {
                    for (var i = 0, len = punishments.length; i < len; i++) {
                        $scope.punishments.push(punishments[i]);
                    }

                    $scope.colors = getRainbowColors($scope.punishments.length);

                    $scope.loading = false;
                    $scope.showButton = true;
                });
        };

        $scope.spin = function () {
            var timesSpinned = 0,
                lastSelected = 0;
            $scope.showButton = false;

            var nextRandom = function () {
                var selected;
                do {
                    selected = Math.floor((Math.random() * $scope.punishments.length));
                }  while (selected === lastSelected);
                lastSelected = selected;

                return selected;
            };

            var spin = function spin() {
                var selected = nextRandom(),
                    elems = document.getElementsByClassName('roulette-punishment');

                for (var i = 0, len = elems.length; i < len; i++) {
                    elems[i].classList.add('translucid');
                }

                elems[selected].classList.remove('translucid');
                window.scrollTo(0, elems[selected].offsetTop - 50);

                if (timesSpinned < 10) {
                    $timeout(spin, 300);
                    timesSpinned++;
                } else {
                    $scope.selected = $scope.punishments[selected];
                    $scope.done = true;
                }
            };
            spin();
        };
    }]);

}(window.punisher));