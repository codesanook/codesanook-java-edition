app.controller('broadcast2Controller', function ($scope, broadcastService) {


    $scope.receiveNotify = function () {
        broadcastService.listen(function (event, args) {
            console.log(args);
        })
    };


});

