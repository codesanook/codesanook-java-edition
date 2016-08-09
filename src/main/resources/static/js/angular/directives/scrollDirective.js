app.directive("scroll", function ($window) {

    var isMostlyVisible = function (element, win) {
        // if ca 25% of element is visible
        var scroll_pos = angular.element(win).scrollTop();
        //var window_height = angular.element($window).height();
        var el_top = element.offset().top;
        var el_height = element.height();
        var el_bottom = el_top + el_height;
        //var el25 = el_bottom - (el_height * 0.40);
        var el25 = el_top + (el_height * 0.65);
        var result = scroll_pos > el25;
        return result;
    };


    var returnFunction = function ($scope, $element, $attrs) {
        angular.element($window).bind("scroll", function () {
            if (isMostlyVisible($element, $window) && !$scope.isLoadingPosts) {
                //console.log("try loading more post");
                $scope.isLoadingPosts = true;
                $scope.getMorePosts();

                //$scope.$apply(function () {
                //});
            }
        });

    };


    return returnFunction;

});



