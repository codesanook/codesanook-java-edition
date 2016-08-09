app.controller('navbarController', function ($controller, $scope, facebookService, trackingService, $element, $window) {
    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);
    $scope.user = window.loggedInUser;

    $scope.popupMenuItems = [];

    $scope.newQuestion = function (event, name) {
        event.preventDefault();

        trackingService.trackMenu(name.toLowerCase())
            .then(function () {
                if ($scope.user != null) {
                    $scope.redirect("/post/create?type=1");
                    return;
                } else {
                    return facebookService.requestUserLogIn();
                }
            }).then(function () {
                $scope.redirect("/post/create?type=1");
            }).catch(function () {

            }).finally(function () {
            });
    };

    $scope.newArticle = function (event, name) {
        event.preventDefault();
        trackingService.trackMenu(name.toLowerCase()).then(function () {
            $scope.redirect("/post/create?type=2&sub-type=1");
        });
    };


    $scope.newTip = function (event, name) {
        event.preventDefault();
        trackingService.trackMenu(name.toLowerCase()).then(function () {
            $scope.redirect("/post/create?type=2&sub-type=2");
        });
    };


    $scope.newVideo = function (event, name) {
        event.preventDefault();
        trackingService.trackMenu(name.toLowerCase()).then(function () {
            $scope.redirect("/post/create?type=2&sub-type=3");
        });
    };


    $scope.changeLanguage = function (event, lang) {
        event.preventDefault();
        var name = sprintf("lang-%s", lang.toLowerCase());
        trackingService.trackMenu(name).then(function () {
            $scope.redirect(sprintf("/?lang=%s", lang));
        });
        return;
    };

    $scope.listArticle = function (event, menu) {
        event.preventDefault();
        var name = sprintf("list-%s", menu.toLowerCase());
        trackingService.trackMenu(name).then(function () {
            $scope.redirect("/post/list/sub-type/article");
        });
        return;
    };


    $scope.listQuestion = function (event, menu) {
        event.preventDefault();
        var name = sprintf("list-%s", menu.toLowerCase());
        trackingService.trackMenu(name).then(function () {
            $scope.redirect("/post/list/type/question");
        });
        return;
    };


    $scope.listTip = function (event, menu) {
        event.preventDefault();
        var name = sprintf("list-%s", menu.toLowerCase());
        trackingService.trackMenu(name)
            .then(function () {
                $scope.redirect("/post/list/sub-type/tip");
            });
    };


    $scope.listVideo = function (event, menu) {
        event.preventDefault();
        var name = sprintf("list-%s", menu.toLowerCase());
        trackingService.trackMenu(name)
            .then(function () {
                $scope.redirect("/post/list/sub-type/video");
            });
    };


    $scope.notifyComingSoon = function () {
        alert("Coming soon, thanks for your interest.");
    };


    $scope.removeAllPopupMenus = function () {
        var i = 0;
        for (i; i < $scope.popupMenuItems.length; i++) {
            var popupMenu = $scope.popupMenuItems[i];
            popupMenu.menu.removeClass('display-block');
            popupMenu.link.removeClass('nav-menu__menu-link--active');
        }
        $scope.popupMenuItems = [];
    };

    angular.element($window).resize(function () {
        $scope.removeAllPopupMenus();
    });

    angular.element($window).click(function (event) {
        $scope.removeAllPopupMenus();
    });


    $scope.toggleCollapseMenu = function (event) {
        event.stopPropagation();
        var link = angular.element(event.currentTarget);
        var menu = $element.find(".nav-menu--collapse");

        var menuItem = {link: link, menu: menu};
        $scope.doToggle(menuItem);
    };

    $scope.toggleSubMenu = function (event) {
        event.stopPropagation();

        var link = angular.element(event.currentTarget);
        var menu = link.parent().find("ul").first();
        var menuItem = {link: link, menu: menu};
        $scope.doToggle(menuItem);
    };


    $scope.doToggle = function (currentMenuItem) {
        var keepMenuItems = [];
        var isFound = false;
        var i = 0;
        for (i; i < $scope.popupMenuItems.length; i++) {
            var existingMenuItem = $scope.popupMenuItems[i];
            //is parent
            if (existingMenuItem.menu.find(currentMenuItem.menu).length > 0) {
                keepMenuItems.push(existingMenuItem);
                continue;
            }

            //is element
            if (existingMenuItem.menu.is(currentMenuItem.menu)) {
                isFound = true;
            }

            //always remove other
            existingMenuItem.menu.removeClass('display-block');
            existingMenuItem.link.removeClass('nav-menu__menu-link--active');
        }

        $scope.popupMenuItems = keepMenuItems;

        if (!isFound) {
            currentMenuItem.link.addClass('nav-menu__menu-link--active');
            currentMenuItem.menu.addClass('display-block');
            $scope.popupMenuItems.push(currentMenuItem);
        }
        console.log("length " + $scope.popupMenuItems.length);
    };

    $scope.toggleNotificationList = function (event, id) {
        var allList = $element.find("." + "notification-list");
        allList.css("display", "none");

        var ul = $element.find("#" + id);
        ul.css("display", "block");
        console.log("display");
    }

});
