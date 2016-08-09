app.controller('downloadController', function ($scope, $window) {

    $scope.downloadSourceCode = function (file) {

        ga('send', {
            hitType: 'event',
            eventCategory: 'source-code ',
            eventAction: 'download',
            eventLabel: file.name,
            hitCallback: function () {
                console.log('hit sent');
                $window.location.href = file.url;
            }
        });

    }

});


