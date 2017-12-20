var exec = require('cordova/exec');

/**
 *
 * @param startPosition 开始位置
 * @param endPosition 结束位置
 * @param type 1-实时导航,0-模拟导航
 * @param successCallback 成功回调
 * @param errorCallback 失败回调
 */
exports.navigation = function (startPosition, endPosition, type, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Navigation", 'navigation', [startPosition, endPosition, type]);
};

