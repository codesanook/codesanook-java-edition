app.controller('timeZoneController', function ($scope, $http, $element, $document, $window) {

    $scope.buildNumber = window.buildNumber;
    $scope.user = window.loggedInUser;

    $scope.setSessionTimezone = function () {
        console.log("current timeZoneMillisecond %d", window.timeZoneMillisecond);

        if (window.timeZoneMillisecond != -1) return;
        var url = sprintf("/user/set-session-timezone");
        var timeZoneMillisecond = moment().utcOffset() * 60 * 1000;
        var data = {timeZoneMillisecond: timeZoneMillisecond};
        var method = "PUT";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: data
        };

        var successCallback = function (response) {
            console.log(response);
            window.timeZoneMillisecond = response.data.timeZoneMillisecond;
            console.log("updated timezone %d", window.timeZoneMillisecond);
        };

        var errorCallback = function (response) {
            console.log(response);
        };

        $http(req).then(successCallback, errorCallback);
    };

    $scope.setSessionTimezone();

});//end controller