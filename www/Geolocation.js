var exec = require('cordova/exec');

module.exports = {
    getCurrentPosition: function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, "Geolocation", 'getCurrentPosition');
    },
    watchPosition: function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, "Geolocation", 'watchPosition');
    }
};
