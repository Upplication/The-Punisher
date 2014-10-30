'use strict';

var Static = require('node-static'),
    file = new Static.Server('./public', { headers: {"Cache-Control": "no-cache, must-revalidate"} }),
    http = require('http');

module.exports = function () {
    http.createServer(function (req, res) {
        req.addListener('end', function () {
            file.serve(req, res);
        }).resume();
    }).listen(8888);
};