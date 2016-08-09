//http://www.dwmkerr.com/promises-in-angularjs-the-definitive-guide/
app.factory('facebookService', function ($q, $http, $document) {

    var service = {};

    service.logIn = function () {
        var deferred = $q.defer();

        var requiredScopes = "email";
        FB.login(function (response) {
            try {
                if (response.status === 'connected') {

                    deferred.resolve(response);

                } else if (response.status === 'not_authorized') {
                    // The person is logged into Facebook, but not your app.
                    deferred.reject(response);
                } else {
                    // The person is not logged into Facebook, so we're not sure if
                    // they are logged into this app or not.
                    deferred.reject("user not logged into Facebook");
                }
            } catch (ex) {
                deferred.reject(ex);
            }

        }, {scope: requiredScopes, return_scopes: true});

        return deferred.promise;
    };

    service.getLogInStatus = function () {

        var deferred = $q.defer();

        var forceGetLogInStatus = true;
        FB.getLoginStatus(function (response) {
            try {
                // The response object is returned with a status field that lets the
                // app know the current login status of the person.
                // Full docs on the response object can be found in the documentation
                // for FB.getLoginStatus().
                if (response.status === 'connected') {
                    var user = {
                        facebookAppScopeUserId: response.authResponse.userID,
                        facebookAccessToken: response.authResponse.accessToken
                    };
                    deferred.resolve({data: user});
                } else if (response.status === 'not_authorized') {
                    // The person is logged into Facebook, but not your app.
                    deferred.reject("The person is logged into Facebook, but not your app");
                } else {
                    // The person is not logged into Facebook, so we're not sure if
                    // they are logged into this app or not.
                    deferred.reject("The person is not logged into Facebook, so we're not sure if");
                }
            } catch (ex) {
                deferred.reject(ex);
            }

        }, forceGetLogInStatus);

        return deferred.promise;
    };

    service.getUserInfo = function (user) {
        var deferred = $q.defer();
        FB.api('/me?fields=picture.width(540).height(540),id,first_name,email', function (response) {
            if (user.facebookAppScopeUserId === response.id) {
                user.profileUrl = response.picture.data.url;
                user.name = response.first_name;
                user.email = response.email;
                deferred.resolve({data: user});
            } else {
                deferred.reject("error getUserInfo");
            }
        });
        return deferred.promise;
    };

    service.isExistingUser = function (user) {
        var deferred = $q.defer();
        var url = sprintf("%s/users/existing?facebookId=%s",
            window.apiEndpoint, user.facebookAppScopeUserId);
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

        $http(req)
            .then(function (response) {
                deferred.resolve({
                    data: {
                        existingUser: response.data.existingUser
                    }
                });
            }).catch(function (response) {
                deferred.reject(response);
            });

        return deferred.promise;
    }
    ;

    service.logInWithNewFacebookToken = function (user) {
        var deferred = $q.defer();
        var url = sprintf("%s/users/login/%s",
            window.apiEndpoint,
            user.facebookAppScopeUserId);
        var method = "PUT";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: {
                facebookAccessToken: user.facebookAccessToken
            }
        };

        $http(req).then(function (response) {
            deferred.resolve({data: {apiToken: response.data.apiToken}});
        }).catch(function (response) {
            deferred.reject(response);
        });

        return deferred.promise;
    };

    service.isExistingUserWithEmail = function (user) {

        var deferred = $q.defer();
        var url = sprintf("%s/users/existing?email=%s", window.apiEndpoint, user.emailFromFacebook);
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

        $http(req)
            .then(function (response) {
                deferred.resolve({
                    data: {
                        existingUser: response.data.existingUser
                    }
                });
            })
            .catch(function (response) {
                deferred.reject(response);
            });

        return deferred.promise;
    };


    service.updateUserWithFacebook = function (user) {

        var deferred = $q.defer();
        var url = sprintf("%s/users/facebook", window.apiEndpoint);
        var method = "PUT";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: user
        };

        $http(req)
            .then(function (response) {
                deferred.resolve({});

            }).catch(function (response) {
                deferred.reject(response);
            });

        return deferred.promise;

    };


    service.registerNewUser = function (user) {
        var deferred = $q.defer();
        var url = sprintf("%s/users/facebook", window.apiEndpoint);
        var method = "POST";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: user
        };
        $http(req)
            .then(function (response) {
                deferred.resolve({data: {userId: response}});
            }).catch(function (reponse) {
                deferred.reject({data: response});
            });

        return deferred.promise;
    };

    service.showIntroLogInModal = function () {
        $document.find("#introLogIn").modal('show');
    };


    service.requestUserLogIn = function () {
        var deferred = $q.defer();
        service.showIntroLogInModal();
        service.onSuccessLoggedIn = function () {
            deferred.resolve({});
        };
        service.onErrorLoggedIn = function () {
            deferred.reject({});
        };
        return deferred.promise;
    };


    return service;
})
;
