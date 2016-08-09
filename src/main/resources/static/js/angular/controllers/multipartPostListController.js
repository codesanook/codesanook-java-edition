app.controller('multipartPostListController', function ($scope, $controller,
                                                        $window, trackingService) {

    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);

    $scope.viewOtherPost = function (event, id, title, url) {
        event.preventDefault();

        trackingService.trackMultipartPost(id,title)
            .then(function (response) {
                $window.location.href = url;
            });
    };

});

