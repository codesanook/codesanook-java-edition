var codesanookServices = angular.module('codesanookServices', []);

codesanookServices
    .factory('UserService', function ($http, $q) {
        var objReturn = {};

        objReturn.getUser = function (userId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'GET',
                url: '/users/'+userId,
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("getUser userId : %i Succeeded Result: %o", userId, result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("getUser userId : %i Failed Result: %o", userId, result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.register = function (data) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'POST',
                url: '/users',
                headers: getHeader(),
                data: JSON.stringify(data)
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("User Register Succeeded Result: %o", result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("User Register Failed Result: %o", result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.logIn = function (data) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url: '/users/login',
                headers: getHeader(),
                data: JSON.stringify(data)
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("User LogIn Succeeded Result: %o", result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("User LogIn Failed Result: %o", result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.logOut = function (userId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url: '/users/logout/'+userId,
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("User LogOut Succeeded Result: %o", result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("User LogOut Failed Result: %o", result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.editProfile = function (userId, data) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url: '/users/' + userId,
                headers: getHeader(),
                data: JSON.stringify(data)
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("editProfile Succeeded Result: %o", result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("editProfile Failed Result: %o", result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.createRole = function (roleName) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'POST',
                url: '/users/role',
                headers: getHeader(),
                data: JSON.stringify({name:roleName})
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("createRole %s Succeeded Result: %o", roleName, result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("createRole %s Failed Result: %o", roleName, result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.addRoleToUser = function (userId, roleId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url: '/users/'+userId+'/role',
                headers: getHeader(),
                data: JSON.stringify({roleId:roleId})
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("addRole roleId : %i ToUser userId : %i Succeeded Result: %o", roleId, userId, result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("addRole roleId : %i ToUser userId : %i Failed Result: %o", roleId, userId, result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.removeUserFromRole = function (userId, roleId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'DELETE',
                url: '/users/'+userId+'/role',
                headers: getHeader(),
                data: JSON.stringify({roleId:roleId})
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("removeUserFromRole roleId : %i userId : %i Succeeded Result: %o", roleId, userId, result);
                    deferred.resolve(result);//synchronous
                })
                .error(function (result, status, headers, config) {
                    console.log("removeUserFromRole roleId : %i userId : %i Failed Result: %o", roleId, userId, result);
                    deferred.reject();
                });

            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        return objReturn;
    })

    .factory('PostService', function ($http, $q) {
        var objReturn = {};

        objReturn.createPost = function (data) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'POST',
                url:  '/posts',
                headers: getHeader(),
                data: JSON.stringify(data)
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("createPost Succeeded Result: %o", result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("createPost Failed Result: %o", result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.getPosts = function (pageIndex,itemsPerPage,sort) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'GET',
                url:  API_ROOT+'posts/'+pageIndex+'/'+itemsPerPage+'/'+sort,
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("getPosts Succeeded Result: %o", result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("getPosts Failed Result: %o", result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.getPost = function(postId){
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'GET',
                url:  '/posts/'+ postId,
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("getPost postId : %i Succeeded Result: %o", postId, result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("getPosts postId : %i Failed Result: %o", postId, result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.editPost = function (postId, data) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url:  '/posts/'+postId,
                headers: getHeader(),
                data: JSON.stringify(data)
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("editPost postId : %i Succeeded Result: %o", postId, result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("editPost postId : %i Failed Result: %o", postId, result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.deletePost = function (postId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'DELETE',
                url:  '/posts/'+postId,
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("deletePost postId : %i Succeeded Result: %o", postId, result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("deletePost postId : %i Failed Result: %o", postId, result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.publishPost = function (postId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url:  '/posts/'+postId+'/publish',
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("publishPost postId : %i Succeeded Result: %o", postId, result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("publishPost postId : %i Failed Result: %o", postId, result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        objReturn.updateTagsToPost = function (postId, tags) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'PUT',
                url:  '/posts/'+postId+'/tags',
                headers: getHeader(),
                data: JSON.stringify(tags)
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("updateTagsToPost postId : %i Succeeded Result: %o", postId, result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("updateTagsToPost postId : %i Failed Result: %o", postId, result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        return objReturn;
    })

    .factory('TagService', function ($http, $q) {
        var objReturn = {};

        objReturn.getAllTags = function(){
            var deferred = $q.defer();
            var promise = deferred.promise;
            var req = {
                method: 'GET',
                url:  API_ROOT+'tags',
                headers: getHeader()
            };
            $http(req)
                .success(function (result, status, headers, config) {
                    console.log("getAllTags Succeeded Result: %o", result);
                    deferred.resolve(result);
                })
                .error(function (result, status, headers, config) {
                    console.log("getAllTags Failed Result: %o", result);
                    deferred.reject();
                });
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            };
            return promise;
        };

        return objReturn;
    })

    .factory('UrlService', function ($http, $q) {
        var objReturn = {};

        objReturn.getParam = function (path) {
            try {
                var full = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
                var template = earl(full + path);
                var params = template.extract(window.location.href);
                return params
            }catch(ex){
                console.log(ex);
                return null;
            }
        };

        return objReturn;
    });

function getHeader(){
    var header = {};
    return header;
};