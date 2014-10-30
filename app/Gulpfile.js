'use strict';

var gulp = require('gulp'),
    karma = require('karma').server;

gulp.task('test', function (done) {
    karma.start({
        configFile: __dirname + '/tests/karma.conf.js',
        singleRun: true
    }, done);
});

gulp.task('default', ['test']);