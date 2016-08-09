app.controller('navigationController', function ($controller, $scope, $element, $window) {



    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);



    angular.element($window).resize(function () {
        console.log("window size changed");
        var el = $element.find(".display-block");
        el.removeClass('display-block');
    });

    $scope.isShow = function (el) {
        var display = el.css("display");
        console.log("display " + display);
        var isShow = display != 'none';
        return isShow;
    };


    $scope.toggleMenu = function () {
        var toggleMenu = $element.find(".collapse-menu");
        var isShow = $scope.isShow(toggleMenu);
        if (isShow) {
            toggleMenu.removeClass("display-block");
        } else {
            toggleMenu.addClass("display-block");
        }
    };

    $scope.showSubMenu = function (event) {
        var target = event.target;
        console.log(target);
        var el = angular.element(target);
        var ul = el.parent().find("ul").eq(0);
        var isShow = $scope.isShow(ul);
        if (isShow) {
            ul.removeClass("display-block");
        } else {
            ul.addClass("display-block");
        }
    }


});

