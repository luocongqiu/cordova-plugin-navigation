# cordova-plugin-navigation
高德地图导航定位插件
## 安装
`cordova plugin add https://github.com/luocongqiu/cordova-plugin-navigation.git --variable NAVIGATION_API_KEY=这里填您申请的高德地图API_KEY`
## 定位
```
 import { Injectable } from '@angular/core';
 import { Observable } from 'rxjs/Observable';
 
 declare const GeolocationPlugin;
 
 @Injectable()
 export class Geolocation {
 
   constructor() {
   }
 
   getCurrentPosition(): Promise<any> {
     return new Promise((resolve, reject) => {
       if (typeof GeolocationPlugin === 'undefined') {
         reject('请使用手机获取位置');
         return;
       }
       GeolocationPlugin.getCurrentPosition(result => {
         if (result.longitude !== undefined) {
           resolve(result);
         }
       }, error => reject(error));
     });
   }
 
   watchPosition(): Observable<any> {
     return new Observable(observer => {
         if (typeof GeolocationPlugin === 'undefined') {
           observer.error('请使用手机获取位置');
           return;
         }
         GeolocationPlugin.watchPosition(result => {
           if (result.longitude !== undefined) {
             observer.next(result);
           }
         }, error => observer.error(error));
       }
     );
   }
 }
```

## 导航

```
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
```

```
    let start = {lat:28.227416, lng: 112.881383};
    let end = {lat:28.214546, lng: 112.892288};
    Navigation.navigation(start, end, 1);
```
