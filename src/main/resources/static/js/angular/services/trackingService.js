app.factory('trackingService', function ($q) {
    var service = {};


    service.createFunctionWithTimeout = function (callback, opt_timeout) {
        var called = false;
        var needToCalled = function () {
            if (!called) {
                called = true;
                callback();
            }
        };

        setTimeout(function () {
            console.log("called by timeout");
            needToCalled();
        }, opt_timeout || 1000);//always called

        return needToCalled();
    };


    service.trackMultipartPost = function (id, name) {
        var deferred = $q.defer();
        ga('send', {
            hitType: 'event',
            eventCategory: 'multipart-post',
            eventAction: 'click',
            eventLabel: sprintf("postId: %d - %s", id, name),
            eventValue: id,
            hitCallback: service.createFunctionWithTimeout(function () {
                console.log('hit sent');
                deferred.resolve({});
            })
        });
        return deferred.promise;
    };


    service.trackMenu = function (name) {
        var deferred = $q.defer();
        ga('send', {
            hitType: 'event',
            eventCategory: 'menu',
            eventAction: 'click',
            eventLabel: name,
            hitCallback: service.createFunctionWithTimeout(function () {
                console.log(sprintf('hit sent for %s', name));
                deferred.resolve({});
            })
        });
        return deferred.promise;
    };

    service.trackFeaturedTag = function (name) {
        var deferred = $q.defer();
        ga('send', {
            hitType: 'event',
            eventCategory: 'featured-tag',
            eventAction: 'click',
            eventLabel: name,
            hitCallback: service.createFunctionWithTimeout(function () {
                console.log('hit sent');
                deferred.resolve({});
            })
        });
        return deferred.promise;
    };

    return service;
});

