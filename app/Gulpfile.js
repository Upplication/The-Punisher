'use strict';

var gulp = require('gulp'),
    karma = require('karma').server,
    server = require('./server'),
    less = require('gulp-less'),
    minifyCSS = require('gulp-minify-css'),
    uglify = require('gulp-uglify'),
    concat = require('gulp-concat'),
    lessPath = __dirname + '/public/less/*.less',
    lessDstPath = __dirname + '/dist/css/',
    jsPath = __dirname + '/public/js/**/*.js',
    tplPath = __dirname + '/public/templates/*.html',
    jsDstPath = __dirname + '/dist/js/',
    tplDstPath = __dirname + '/dist/templates/';

gulp.task('test', function (done) {
    karma.start({
        configFile: __dirname + '/tests/karma.conf.js',
        singleRun: true
    }, done);
});

gulp.task('templates', function () {
    gulp.src(tplPath)
        .pipe(gulp.dest(tplDstPath));
});

gulp.task('libs', function () {
    gulp.src(__dirname + '/public/lib/**/*')
        .pipe(gulp.dest(__dirname + '/dist/lib/'));
});

gulp.task('css', function () {
    gulp.src(__dirname + '/public/css/**/*')
        .pipe(gulp.dest(__dirname + '/dist/css/'));
});

gulp.task('index', function () {
    gulp.src(__dirname + '/public/index.html')
        .pipe(gulp.dest(__dirname + '/dist/'));
});

gulp.task('index_watch', function () {
    gulp.watch(__dirname + '/public/lib/**/*', ['index']);
});

gulp.task('less', function () {
    gulp.src(lessPath)
        .pipe(less())
        .pipe(minifyCSS({keepBreaks: false}))
        .pipe(gulp.dest(lessDstPath));
});

gulp.task('js', function () {
    gulp.src(jsPath)
        .pipe(concat('app.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(jsDstPath));
});

gulp.task('server', function () {
    server();
});

// Watches for less files changes
gulp.task('less_watch', function () {
    gulp.watch(lessPath, ['less']);
});

// Watches for changes to the angular source files
gulp.task('js_watch', function () {
    gulp.watch(jsPath, ['js']);
});

// Watches for changes to the templates
gulp.task('templates_watch', function () {
    gulp.watch(tplPath, ['templates']);
});

// Prepares and compiles all files for production
gulp.task('default', ['less', 'js', 'templates']);

// Watches for changes on the development files
gulp.task('watch', ['less', 'js', 'css', 'libs', 'index', 'templates', 'less_watch', 'js_watch', 'index_watch', 'templates_watch']);

gulp.task('default', ['less', 'js', 'css', 'libs', 'index', 'templates']);