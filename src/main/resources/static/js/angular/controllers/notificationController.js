app.controller('notificationController', function ($controller, $scope, trackingService,
                                                   $element, $window, $http, localStorageService) {

    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);
    $scope.user = window.loggedInUser;
    $scope.activeTabIndex = 0;
    $scope.newNotification = {
        totalCount: 0,
        lastRead: null
    };
    $scope.notificationList = {
        totalCount: 0,
        knowledge: [],
        questions: []
    };

    $element.click(function (event) {
        event.stopPropagation();
        console.log("click");
    });


    $scope.switchTab = function (event, tabIndex) {
        console.log("tab index " + tabIndex);
        if ($scope.activeTabIndex == tabIndex)return;

        $scope.setActiveTab($scope.activeTabIndex, 'remove');
        $scope.setActiveTab(tabIndex, 'set');

        $scope.activeTabIndex = tabIndex;
    };


    $scope.setActiveTab = function (tabIndex, action) {
        var tab = $element.find('.notification-tab__notification-tab-link').eq(tabIndex);
        var notification = $element.find(".notification").eq(tabIndex);

        switch (action) {
            case 'set':
                tab.addClass('notification-tab__notification-tab-link--active');
                notification.addClass('notification--active');
                break;
            case 'remove':
                tab.removeClass('notification-tab__notification-tab-link--active');
                notification.removeClass('notification--active');
                break;
        }
    };

    $scope.getNewNotification = function () {
        var lastRead = localStorageService.get('notificationLastRead');
        if (!lastRead) {
            //http://momentjs.com/docs/#/manipulating/add/
            var date = moment.utc();
            date = date.subtract(2, 'months');

            console.log(date.format()); // "2015-10-16T15:19:51-07:00"
            console.log(date.toISOString()); // "2015-10-16T22:19:51.993Z"
            lastRead = date.toISOString();
        }

        var url = sprintf("%s/notifications/new-notification?lastRead=%s", window.apiEndpoint, lastRead);
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

        $http(req).then(function (response) {
            var data = response.data;
            $scope.newNotification.totalCount = data.totalCount;
            $scope.newNotification.lastRead = data.lastRead;
            console.log(response.data);

        }).catch(function (response) {

        });
    };

    $scope.getNewNotification();


    $scope.toggleNotification = function (event) {
        event.stopPropagation();

        trackingService.trackMenu("notification")
            .then(function () {
                var link = angular.element(event.currentTarget);
                var menu = $element.find(".notification-box");

                var menuItem = {link: link, menu: menu};
                $scope.doToggle(menuItem);
                $scope.getLatestNotifications();
            });

    };


    $scope.getLatestNotifications = function () {

        var url = sprintf("%s/notifications", window.apiEndpoint);
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


        $http(req).then(function (response) {
            var data = response.data;
            $scope.notificationList.totalCount = data.totalCount;
            $scope.notificationList.knowledge = data.knowledge;
            $scope.notificationList.questions = data.questions;

            localStorageService.set('notificationLastRead', $scope.newNotification.lastRead);
            $scope.newNotification.totalCount = 0;
        }).catch(function (response) {

        });


    };

    $scope.encodeUrl = function (url) {
    console.log("url " + url);
        return $window.encodeURIComponent(url);
    };

});
