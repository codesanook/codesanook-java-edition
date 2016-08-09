app.factory('commentService', function ($q, $http,contentService) {

    var service = {};
    angular.extend(service, contentService);

    service.getComment = function (commentId) {
        var deferred = $q.defer();
        var url = sprintf("%s/comments/%d", window.apiEndpoint, commentId);
        var method = "GET";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            }
        };

        var success = function (response) {
            deferred.resolve(response);
        };


        var error = function (response) {
            deferred.reject(response)
        };
        $http(req).then(success, error);

        return deferred.promise;
    };

    service.addComment = function (comment) {
        var deferred = $q.defer();
        var url = sprintf("%s/comments", window.apiEndpoint);
        var method = "POST";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: comment
        };

        var success = function (response) {
            deferred.resolve(response);
        };


        var error = function (response) {
            deferred.reject(response)
        };
        $http(req).then(success, error);

        return deferred.promise;
    };


    service.deleteComment = function (commentId) {
        var deferred = $q.defer();
        var url = sprintf("%s/comments/%d", window.apiEndpoint, commentId);
        var method = "DELETE";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            }
        };

        var success = function (response) {
            deferred.resolve(response);
        };

        var error = function (response) {
            deferred.reject(response)
        };
        $http(req).then(success, error);

        return deferred.promise;
    };

    return service;

});
