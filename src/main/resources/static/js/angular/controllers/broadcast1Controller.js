app.controller('broadcast1Controller', function ($scope, broadcastService) {

    $scope.notify = function (args) {
        broadcastService.broadcast(args);
    };

});

