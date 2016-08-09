app.factory('utilService', function ($q, $http) {

    var delay = function (delayExpression, delay) {
        window.setTimeout(function () {
            console.log("delay of %d milliseconds", delay);
            delayExpression();
        }, delay);

    };

    return {
        delay: delay
    };

});
