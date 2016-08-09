app.controller('facebookLogInButtonController', function ($controller, $scope, facebookService) {
    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);

    $scope.facebookSdkLoaded = false;
    $scope.showIntroLogInModal = function (redirectUrl) {
        var url = redirectUrl ? redirectUrl : $scope.getCurrentUrl();

        facebookService.requestUserLogIn()
            .then(function () {
                $scope.redirect(url);
            })
            .catch(function () {
            })
            .finally(function () {
            });
    }
});

app.controller('facebookLogInController', function ($scope, facebookService, $document, $window) {

    $scope.isServicedLoaded = facebookService.loaded;

    $scope.result = null;
    $scope.user = {};

    $scope.logIn = function () {

        $scope.hideIntroLogInModal();
        facebookService
            .logIn()
            .then(function (response) {
                $scope.showWaitingModal();
                return facebookService.getLogInStatus();
            })
            .then(function (response) {
                $scope.updateUser(response.data);
                return facebookService.getUserInfo($scope.user);
            })
            .then(function (response) {
                $scope.updateUser(response.data);
                return facebookService.isExistingUser($scope.user);
            })
            .then(function (response) {
                if (response.data.existingUser) {
                    $scope.logInExistingUser();
                } else {
                    $scope.registerNewUser();
                }
            })
            .catch(function (response) {
                console.log(response);
                $window.alert("Error, please reload page and try again");
            })
            .finally(function () {
            });
    };

    $scope.logInExistingUser = function () {
        facebookService.logInWithNewFacebookToken($scope.user)
            .then(function (response) {
                $scope.logInWithCookieToken(response.data.apiToken);
            })
            .catch(function (response) {
                $window.alert("Error, please reload page and try again");
            })
            .finally(function () {
            });
    };

    $scope.registerNewUser = function () {
        facebookService.isExistingUserWithEmail($scope.user)
            .then(function (response) {
                if (response.data.existingUser) {
                    return facebookService.updateUserWithFacebook($scope.user);
                } else {
                    return facebookService.registerNewUser($scope.user);
                }
            })
            .then(function () {
                $scope.logInExistingUser();
            })
            .catch(function (response) {
                $window.alert("Error, please reload page and try again");
            })
            .finally(function () {

            });
    };

    $scope.updateUser = function (obj) {
        for (var property in obj) {
            $scope.user[property] = obj[property];
        }
    };

    $scope.logInWithCookieToken = function (apiToken) {
        $.cookie.raw = true;
        $.cookie('cs-token', apiToken, {expires: 30, path: '/'});

        facebookService.onSuccessLoggedIn();
    };

    $scope.hideIntroLogInModal = function () {
        $document.find("#introLogIn").modal('hide');
    };


    $scope.showWaitingModal = function () {
        var waitingModal = $document.find(".waiting-modal");
        waitingModal.modal('show');
    };

    $scope.hideWaitingModal = function () {
        var waitingModal = $document.find(".waiting-modal");
        waitingModal.modal('hide');
    };

}); //end controller