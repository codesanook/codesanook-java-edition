app.factory('broadcastService', function ($rootScope) {

    var service = {};

    service.broadcast = function (args) {
        $rootScope.$broadcast("event",args)
    };

    service.listen = function (callback) {
        $rootScope.$on('event', callback);
    };

    return service;

});
