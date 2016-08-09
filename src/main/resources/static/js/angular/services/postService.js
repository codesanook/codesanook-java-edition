app.factory('postService', function ($q, $http, contentService) {

    var service = {};
    angular.extend(service, contentService);

    service.getPost = function (postId) {

        var deferred = $q.defer();
        var url = sprintf("%s/posts/%d", window.apiEndpoint, postId);
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


    service.addPost = function (post) {

        var deferred = $q.defer();
        var url = sprintf("%s/posts", window.apiEndpoint);
        var method = "POST";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: post
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

    service.updatePost = function (post) {

        var deferred = $q.defer();
        var url = sprintf("%s/posts/%d", window.apiEndpoint, post.id);
        var method = "PUT";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: post
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

    service.searchPost = function (postTitle) {
        var deferred = $q.defer();
        var url = sprintf("%s/posts?title=%s", window.apiEndpoint, postTitle);
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

    return service;
});
