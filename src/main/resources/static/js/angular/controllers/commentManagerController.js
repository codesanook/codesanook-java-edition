app.controller('commentManagerController', function ($scope, $http, $element, $document, commentService) {

    $scope.buttonLabel = "save comment";
    $scope.comments = [];
    $scope.postId = null;
    $scope.showAddCommentForm = true;

    $scope.notifyCommentStateChange = function (state) {
        console.log("new state %s", state);
        switch (state.toUpperCase()) {
            case 'EDITING':
                $scope.showAddCommentForm = false;
                break;
            case 'READING':
                $scope.showAddCommentForm = true;
                break;
            default :
                $scope.showAddCommentForm = true;
                break;

        }
    };


    $scope.redirect = function () {
        console.log(location.href);
        var pathname = "/";
        var reg = /.+?\:\/\/.+?(\/.+?)(?:#|\?|$)/;
        var paths = reg.exec(location.href);
        if (paths && paths.length > 2) {
            pathname = paths[1];
        }

        var redirectUrl = sprintf("/user/login?redirect=%s",pathname);
        location.href = redirectUrl;
    };
});

