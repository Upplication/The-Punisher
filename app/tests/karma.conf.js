'use strict';

module.exports = function (config) {
    config.set({
        basePath: '',
        frameworks: ['jasmine'],
        files: [
            '../public/js/lib/angular/angular.min.js',
            '../public/js/lib/angular-route/angular-route.min.js',
            '../public/js/lib/angular-mocks/angular-mocks.js',
            '../public/js/app.js',
            '../public/js/services/*.js',
            '../public/js/controllers/*.js',
            '../public/js/directives/*.js',
            '**/*_spec.js'
        ],
        port: 9876,
        colors: true,
        reporters: ['dots'],
        browsers: ['PhantomJS'],
        captureTimeout: 60000,
        singleRun: true
    });
};