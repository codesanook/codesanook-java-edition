var app = angular.module('codesanook',
    [
        'ngSanitize',
        'ui.sortable',
        'LocalStorageModule',
        'toastr'
    ]);

app.config(function (localStorageServiceProvider) {
    localStorageServiceProvider.setPrefix('codesanook');
    localStorageServiceProvider.setStorageCookie(30, '/');
    localStorageServiceProvider .setStorageType('localStorage');
});

app.directive('myDirective', function () {
    return {
        restrict: 'EA',
        scope: {},
        controller: function ($scope) {
            $scope.isInitialized = true;
        },
        template: '<div>{{isInitialized}}</div>'
    }
});

app.config(function(toastrConfig) {
    angular.extend(toastrConfig, {
        autoDismiss: false,
        containerId: 'toast-container',
        maxOpened: 0,
        newestOnTop: true,
        positionClass: 'toast-top-center',
        preventDuplicates: false,
        preventOpenDuplicates: false,
        target: 'body',
        timeOut: 3000
    });
});





