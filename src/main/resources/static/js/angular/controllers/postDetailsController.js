app.controller('postDetailsController', function ($scope, $http, $element, $document, $window, postService) {

    $scope.user = window.loggedInUser;
    $scope.post = {
        id: null,
        isContentShown: false,
        isLoading: false
    };

    $scope.screenTop = 0;

    $scope.showPostContent = function () {
        $scope.screenTop = $document.scrollTop();
        console.log("screen top = %d", $scope.screenTop);

        if ($scope.post.htmlContent) {
            $scope.post.isContentShown = true;
            return;
        }

        $scope.post.isLoading = true;
        postService.getPost($scope.post.id)
            .then(function (response) {
                $scope.post.htmlContent = response.data.htmlContent;
                $scope.post.isLoading = false;
                $scope.post.isContentShown = true;
                $scope.updateSyntaxHighLight();
            })
            .catch(function (response) {
                console.log(response);
            }).finally(function () {
                $scope.post.isLoading = false;
            });

        console.log("end method");
    };


    $scope.updateSyntaxHighLight = function () {
        $scope.delay(function () {
            prettyPrint();
        }, 250);
    };


    $scope.hidePostContent = function () {
        $scope.post.isContentShown = false;
        // $window.location.hash = "postId" + $scope.post.id;
        $document.scrollTop($scope.screenTop);
    };


    $scope.delay = function (delayExpression, delay) {
        window.setTimeout(function () {
            console.log("delay of %d milliseconds", delay);
            delayExpression();
        }, delay);
    };


    $scope.shareOnFacebook = function (url) {
        console.log("sharing on facebook with url %s", url);
        FB.ui({
            method: 'share',
            href: url
        }, function (response) {
            console.log(response);
        });

    };


});//end controller