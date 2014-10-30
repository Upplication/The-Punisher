'use strict';

exports.config = {
    seleniumAddress: 'http://127.0.0.1:4444/wd/hub',
    specs: ['tests/e2e/*-spec.js'],
    capabilities: {
        'browserName': 'phantomjs'
    },
    baseUrl: 'http://localhost:8888/',
    jasmineNodeOpts: {
        showColors: true
    }
};