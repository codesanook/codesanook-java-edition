app.controller('baseController', function ($scope) {

    $scope.delay = function (delayExpression, delay) {
        window.setTimeout(function () {
            delayExpression();
        }, delay);
    };

    $scope.redirect = function (url) {
        window.location.href = url;
    };

    $scope.getCurrentUrl = function () {
        return window.location.href;
    };


});

