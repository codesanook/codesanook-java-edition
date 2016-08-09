app.controller('commentUpdateController', function ($scope, $http, $element, $document, commentService) {

    $scope.user = window.loggedInUser;

    $scope.comment = {
        id: null,
        content: "",
        isEditing: false,
        isDisabled: false,
        isSaving: false,
        isLoading: false,
        isLoaded: false,
        submitLabel: "save",
        withTitle: false,
        featuredImage: null,
        uploadedFiles: []
    };


    $scope.editor = $element.find(".textarea-editor").first();

    $scope.editComment = function () {

        $scope.notifyCommentStateChange('editing');
        $scope.comment.isEditing = true;
        $scope.comment.isLoading = true;

        commentService.getComment($scope.comment.id)
            .then(function (response) {
                var comment = response.data;
                $scope.comment.htmlContent = comment.htmlContent;
                $scope.comment.userId = comment.userId;
                $scope.comment.content = comment.content;
                $scope.comment.postId = comment.postId;

                $scope.delay(function () {
                    $scope.updateEditorHeightToFitContent();
                }, 250);
            })
            .catch(function (response) {
                console.log(response);
            }).finally(function () {
                $scope.comment.isLoading = false;
                $scope.comment.isLoaded = true;
            });
    };


    $scope.delay = function (delayExpression, delay) {
        window.setTimeout(function () {
            console.log("delay of %d milliseconds", delay);
            delayExpression();
        }, delay);

    };


    $scope.deleteComment = function () {
        var result = confirm("Are you sure to delete this comment?");
        if (!result) return;

        $scope.comment.isLoading = true;
        commentService
            .deleteComment($scope.comment.id)
            .then(function (response) {
                console.log(response.data);
                //remove comment
                $scope.comment.isLoading = false;

                $element.remove();
            })
            .catch(function (response) {

                $scope.comment.isLoading = false;
                console.log(response);
            });


    };


    $scope.updateComment = function () {

        console.log("save comment clicked");

        $scope.comment.isDisabled = true;
        $scope.comment.isSaving = true;
        console.log("$scope.comment.content %s", $scope.comment.content);
        var htmlContent = commentService.toHtml($scope.comment.content);
        $scope.comment.htmlContent = htmlContent;

        var url = sprintf("%s/comments/%d", window.apiEndpoint, $scope.comment.id);
        var method = "PUT";
        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: $scope.comment
        };

        var success = function (response) {
            $scope.comment.isEditing = false;
            $scope.comment.isSaving = false;
            $scope.comment.isDisabled = false;
            $scope.notifyCommentStateChange('reading');

            $scope.delay(function () {
                prettyPrint();
            }, 250);

        };

        var error = function (response) {
            console.log(response);
        };
        $http(req).then(success, error);
    };

    $scope.updateEditorHeightToFitContent = function () {
        var editor = $scope.editor;
        var scrollHeight = editor[0].scrollHeight;
        console.log("scrollHeight %d", scrollHeight);
        editor.css({"height": scrollHeight});
        autosize(editor);
    };


});

