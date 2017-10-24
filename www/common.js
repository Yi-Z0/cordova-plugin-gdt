var exec = require('cordova/exec');
exports.show = function() {
    exec(function(){}, function(){}, "GDTPlugin", "show", []);
};
exports.close = function() {
    exec(function(){}, function(){}, "GDTPlugin", "close", []);
};
