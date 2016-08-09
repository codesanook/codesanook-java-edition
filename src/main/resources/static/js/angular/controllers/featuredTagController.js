app.controller('featuredTagController', function ($controller, $scope, trackingService) {
    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);

    $scope.showPostsByTag = function (event, tag) {
        event.preventDefault();

        trackingService.trackFeaturedTag(tag)
            .then(function () {
                if (tag === 'all') {
                    $scope.redirect("/post/all-tags");
                } else {
                    $scope.redirect(sprintf("/post/list/tag/%s", tag));
                }
            }).catch(function () {

            }).finally(function () {
            });
    };


});
