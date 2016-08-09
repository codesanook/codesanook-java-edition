app.factory('multipartPostService', function ($q, $http) {


    var searchMultipartPost = function (title) {

        var deferred = $q.defer();
        var url = sprintf("%s/multipart-posts?title=%s", window.apiEndpoint, title);
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


    var getMultipartPost = function (multipartPostId) {

        var deferred = $q.defer();
        var url = sprintf("%s/multipart-posts/%d", window.apiEndpoint, multipartPostId);
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


    var addMultipartPost = function (multipartPost) {

        var deferred = $q.defer();
        var url = sprintf("%s/multipart-posts", window.apiEndpoint);
        var method = "POST";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: multipartPost
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


    var updateMultipartPost = function (multipartPost) {

        var deferred = $q.defer();
        var url = sprintf("%s/multipart-posts/%d", window.apiEndpoint, multipartPost.id);
        var method = "PUT";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: multipartPost
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


    return {
        addMultipartPost: addMultipartPost,
        getMultipartPost: getMultipartPost,
        searchedMultipartPost: searchMultipartPost,
        updateMultipartPost:updateMultipartPost
    };

});
