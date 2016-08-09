app.controller('postAddUpdateController', function ($scope, $controller, $http,
                                                    $element, $document, postService, $window) {

    var baseController = $controller('baseController', {$scope: $scope});
    angular.extend(this, baseController);

    $scope.code = 'oHg5SJYRHA0';


    $scope.getUser = function () {
        return window.loggedInUser;
    };

    $scope.getEditor = function () {
        return $element.find(".textarea-editor").first();
    };


    $scope.getPost = function (postId) {
        if (!postId)return;

        postService.getPost(postId)
            .then(function (response) {
                for (var property in $scope.post) {
                    if (response.data[property]) {
                        $scope.post[property] = response.data[property];
                    }
                }
                $scope.post.tags = $scope.post.tags.join(" ");
                var splitAlias = $scope.post.alias.split("-");
                var alias = splitAlias.join(' ');
                $scope.post.alias = alias;
            })
            .catch(function (response) {
                console.log(response);
                alert("error loading post, please reload page and try again");
            }).finally(function () {
            });
    };

    $scope.post = {
        id: null,
        title: null,
        alias: null,
        tags: null,
        content: null,

        htmlContent: "",
        isMultipartPost: false,
        withAlias: true,
        featuredImage: null,
        uploadedFiles: [],
        postType: {
            id: parseInt(window.postTypeId)
        },
        postSubtype: {
            id: parseInt(window.postSubtypeId)
        }


    };
    $scope.htmlContent = null;
    $scope.post.submitLabel = "save";
    $scope.post.showSaveButton = true;
    if ($scope.post.postType.id == 2) {
        $scope.post.isKnowledge = true;
        $scope.post.showAddYoutubeButton = true;
    }

    $scope.getPost(window.postId);

    $scope.$watch('post.content', function (newValue, oldValue) {
        $scope.htmlContent = postService.toHtml(newValue);
        $scope.delay(function () {
            prettyPrint();
        }, 250);
    });

    $scope.savePost = function (form) {
        if (!form.$valid) return;
        form.$setPristine();

        if ($scope.getUser() == null) {
            $scope.redirect();
            return;
        }

        if ($scope.post.id) {
            $scope.updatePost();
        } else {
            $scope.addPost();
        }
    };

    $scope.addPost = function () {

        $scope.post.isSaving = true;
        $scope.post.isDisabled = true;

        var tags = $scope.tagsStringToArray($scope.post.tags);
        var htmlContent = postService.toHtml($scope.post.content);

        $scope.post.userId = $scope.getUser().id;
        $scope.post.tags = tags;
        $scope.post.htmlContent = htmlContent;

        postService.addPost($scope.post)
            .then(function (response) {
                var postId = response.data;
                $scope.post.id = postId;
                $scope.showEditMultipartFormModal();
            })
            .catch(function (response) {
                console.log(response);
                alert("error adding post, please try again");
            })
            .finally(function () {
                $scope.post.isSaving = false;
                $scope.post.isDisabled = false;
            });
    };


    $scope.showEditMultipartFormModal = function () {
        if (!$scope.post.isMultipartPost) {
            $scope.redirectToHome();
            return;
        }

        var modal = $document.find("#multipartPostModal");
        modal.modal('show');
    };


    $scope.encodeUrl = function (url) {
        console.log("url " + url);
        return $window.encodeURIComponent(url);
    };

    $scope.redirectToHome = function () {
        //get post to get alias
        postService.getPost($scope.post.id)
            .then(function (response) {
                var post = response.data;

                var urlEncode = $scope.encodeUrl(post.alias);
                var url = sprintf("/post/details/%s/%d", urlEncode, post.id);
                location.href = url;

            }).catch(function (response) {
                console.log(response);
            })
            .finally(function () {

            });
    };

    $scope.updatePost = function () {
        $scope.post.isSaving = true;
        $scope.post.isDisabled = true;

        var tags = $scope.tagsStringToArray($scope.post.tags);
        var htmlContent = postService.toHtml($scope.post.content);
        $scope.post.tags = tags;
        $scope.post.htmlContent = htmlContent;

        postService.updatePost($scope.post)
            .then(function (response) {
                $scope.showEditMultipartFormModal();
            })
            .catch(function (response) {
                console.log(response);
            }).finally(function () {
                $scope.post.isSaving = true;
                $scope.post.isDisabled = true;
            });
    };


    $scope.scrollToFixToolbar = 180;
    $scope.editorLoaded = function () {

        var toolbar = $element.find(".markdown-toolbar").first();
        toolbar.scrollToFixed({
            marginTop: 57,
            fixed: function () {
                console.log("fixed");
            },
            unfixed: function () {
                console.log("unfixed");
            }
        });

        $scope.delay(function () {
            $scope.updateEditorHeightToFitContent();
        }, 250);
    };


    $scope.updatePreviewHeight = function () {
        var editorContainer = $element.find(".editor-container").first();
        var previewContent = $element.find(".preview-content").first();
        var border = $scope.getCssValue(previewContent, "border-top-width")
            + $scope.getCssValue(previewContent, "border-top-width");
        console.log("border %d", border);

        var margin = $scope.getCssValue(previewContent, "margin-top") +
            $scope.getCssValue(previewContent, "margin-bottom");
        console.log("margin %d", margin);

        var padding = $scope.getCssValue(previewContent, "padding-top") +
            $scope.getCssValue(previewContent, "padding-bottom");
        console.log("padding %d", padding);
        previewContent.height(editorContainer.height() - border - margin - padding);
    };

    $scope.getCssValue = function (jqObject, cssProperty) {
        return parseInt(jqObject.css(cssProperty).replace("px", ""));
    };


    $scope.updateEditorHeightToFitContent = function () {
        var editor = $scope.getEditor();
        var scrollHeight = editor[0].scrollHeight;
        console.log("scrollHeight %d", scrollHeight);

        editor.css({"height": scrollHeight});
        autosize(editor);

        editor.on('autosize:resized', function () {
            console.log('textarea height updated');
            $scope.updatePreviewHeight();
        });
        $scope.updatePreviewHeight();
    };


    $scope.saveFormModelToLocalStorage = function () {

        if (localStorageService.isSupported) {
            console.log("support local storage");
            localStorageService.set(STORAGE_KEY, $scope.post);
        }
    };


    $scope.getFormModelFromLocalStorage = function () {
        if (localStorageService.isSupported) {
            var savedFormModel = localStorageService.get(STORAGE_KEY);
            localStorageService.remove(STORAGE_KEY);
            return savedFormModel;
        } else {
            return null;
        }
    };

    $scope.tagsStringToArray = function (tagsString) {
        if (!tagsString) return [];

        //http://www.w3schools.com/jsref/jsref_obj_regexp.asp
        var tagsArray = tagsString.split(/[\s,]+/); //space or comma
        var tags = [];
        for (var i = 0; i < tagsArray.length; i++) {
            var currentTag = tagsArray[i];
            //remove empty tag
            if (currentTag) {
                currentTag = currentTag.toLowerCase();
                tags.push(currentTag);
            }
        }
        return tags;
    };


});

