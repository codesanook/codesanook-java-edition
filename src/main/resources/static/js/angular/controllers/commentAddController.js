app.controller('commentAddController', function ($controller, $scope, $http,
                                                 $element, $document, commentService, facebookService) {

    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);

    var NEW_COMMENT_KEY = "new-comment";

    $scope.user = window.loggedInUser;

    $scope.formModel = commentService.getFromStorage(NEW_COMMENT_KEY);
    if ($scope.formModel) {
        commentService.removeFromStorage(NEW_COMMENT_KEY);
    }

    if (!$scope.formModel) {
        $scope.formModel = {
            content: "",
            submitLabel: "save",
            isDisabled: false,
            isSaving: false,
            textareaStyle: {'height': '60px'},
            featuredImage: null,
            uploadedFiles: []
        };
    }

    $scope.getEditor = function () {
        return $element.find(".textarea-editor").first();
    };

    $scope.addComment = function (form) {

        if ($scope.user == null) {
            commentService.saveToStorage(NEW_COMMENT_KEY, $scope.formModel);

            facebookService.requestUserLogIn()
                .then(function () {
                    var url = $scope.getCurrentUrl();
                    $scope.redirect(url);
                })
                .catch(function () {
                })
                .finally(function () {
                });

            return;
        }

        if (!form.$valid) return;
        form.$setPristine();

        $scope.formModel.isSaving = true;
        $scope.formModel.isDisabled = true;

        var htmlContent = commentService.toHtml($scope.formModel.content);
        var newComment = {
            userId: $scope.user.id,
            userName: $scope.user.name,
            userProfileUrl: $scope.user.profileUrl,
            content: $scope.formModel.content,
            htmlContent: htmlContent,
            postId: $scope.postId
        };

        commentService.addComment(newComment)
            .then(function (response) {
                newComment.id = response.data;
                $scope.comments.push(newComment);

                $scope.delay(function () {
                    prettyPrint();
                }, 250);

            })
            .catch(function (response) {
                console.log(response);
                alert("!!! Error adding new comment, please try again");
            }).finally(function () {
                $scope.formModel.content = "";
                $scope.formModel.isSaving = false;
                $scope.formModel.isDisabled = false;
            });
    };

    $scope.editorLoaded = function () {
        console.log("editor loaded");
        autosize($scope.getEditor());
    };

});

