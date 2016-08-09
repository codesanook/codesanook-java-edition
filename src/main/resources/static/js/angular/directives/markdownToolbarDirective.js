app.directive("markdownToolbar", function () {

    var getTemplateUrl = function () {
        var url = '/js/angular/templates/markdown-toolbar.html?bno=' + window.buildNumber;
        return url;
    };

    var directive = {
        restrict: 'AE',
        scope: {
            formModel: "=",
            loaded: "&",
            editorId: "@"
        },
        templateUrl: getTemplateUrl(),
        link: function ($scope, $element, $attrs) {

            if (!$scope.formModel.uploadedFiles) {
                $scope.formModel.uploadedFiles = [];
            }
            try {
                //scope.loaded();
                $scope.loaded()();
            } catch (ex) {
            }
        },

        controller: function ($scope, $http, $element, $document, imageService, $window, toastr) {
            $scope.selectedImage = null;
            $scope.editedImageResult = null;


            //$scope.updateNoCacheUrl = function () {
            //    var i = 0;
            //    for (i; i < $scope.formModel.uploadedFiles.length; i++) {
            //        var file = $scope.formModel.uploadedFiles[i];
            //        imageService.updateNoCacheUrl(file);
            //    }
            //};

            $scope.getEditor = function () {
                return $document.find("#" + $scope.editorId);
            };

            $scope.waitingModal = $document.find(".waiting-modal");
            $scope.editImageModal = $element.find(".editImageModal");
            $scope.isUploadingImage = false;

            $scope.insertTextAtCursor = function (text) {
                var nativeDomTextArea = $scope.getEditor().get(0);

                var selectionStart = nativeDomTextArea.selectionStart;
                var selectionEnd = nativeDomTextArea.selectionEnd;
                console.log("start %s, end %s", selectionStart, selectionEnd);
                var currentContent = $scope.getEditor().val();

                console.log(currentContent.substring(0, selectionStart));
                var newContent = currentContent.substring(0, selectionStart) + text +
                    currentContent.substring(selectionEnd);
                $scope.formModel.content = newContent;
                $scope.getEditor().val(newContent);
                return selectionStart;
            };


            $scope.highlightTextAtPosition = function (selectionStart, selectionEnd) {
                console.log("selectionStart %d, selectionEnd %d", selectionStart, selectionEnd);

                var input = $scope.getEditor().get(0);

                if (input.setSelectionRange) {
                    console.log("setSelectionRange");
                    input.setSelectionRange(selectionStart, selectionEnd);

                    //input.focus();
                }
                else if (input.createTextRange) {
                    console.log("createTextRange");
                    var range = input.createTextRange();
                    range.collapse(true);
                    range.moveStart('character', selectionStart);
                    range.moveEnd('character', selectionEnd);
                    range.select();
                }
            };


            $scope.bold = function () {

                var selectedText = $scope.getSelectedText();
                var content = "bold text";
                if (selectedText) content = selectedText;

                var template = sprintf(" **%s** ", content);
                var insertAt = $scope.insertTextAtCursor(template);

                var start = insertAt + 3;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
            };


            $scope.getSelectedText = function () {

                var nativeDomTextArea = $scope.getEditor().get(0);

                var selectionStart = nativeDomTextArea.selectionStart;
                var selectionEnd = nativeDomTextArea.selectionEnd;
                if (selectionStart == selectionEnd) return null;

                var currentContent = $scope.getEditor().val();
                var selectedText = currentContent.substring(selectionStart, selectionEnd);
                return selectedText;
            };


            $scope.fontSize = function (fontSize) {
                var selectedText = $scope.getSelectedText();
                var content;
                var template;
                var indent;
                switch (fontSize) {
                    case "h1":
                        content = sprintf("%s text", "h1");
                        if (selectedText) content = selectedText;
                        template = sprintf("\n# %s\n", content);
                        indent = 3;
                        break;

                    case "h2":
                        content = sprintf("%s text", "h2");
                        if (selectedText) content = selectedText;
                        template = sprintf("\n## %s\n", content);
                        indent = 4;
                        break;

                    case "h3":
                        content = sprintf("%s text", "h3");
                        if (selectedText) content = selectedText;
                        template = sprintf("\n### %s\n", content);
                        indent = 5;
                        break;
                }

                var insertAt = $scope.insertTextAtCursor(template);
                var start = insertAt + indent;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
            };


            $scope.italic = function () {
                var selectedText = $scope.getSelectedText();
                var content = "italic text";
                if (selectedText) content = selectedText;
                var template = sprintf(" *%s* ", content);
                var insertAt = $scope.insertTextAtCursor(template);

                var start = insertAt + 2;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
            };


            $scope.quote = function () {
                var selectedText = $scope.getSelectedText();
                var content = "quote text";
                if (selectedText) content = selectedText;

                var template = sprintf("\n>%s\n", content);
                var insertAt = $scope.insertTextAtCursor(template);
                var start = insertAt + 2;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
            };


            $scope.insertCode = function () {

                var selectedText = $scope.getSelectedText();
                var content = "put your code here";
                if (selectedText) content = selectedText;

                var template = sprintf("\n```\n%s\n```\n", content);
                var insertAt = $scope.insertTextAtCursor(template);

                var start = insertAt + 5;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
            };

            $scope.getUrl = function () {
                var url = prompt("Please enter URL");
                if (!url) {
                    alert("Please enter URL");
                }
                return url;
            };

            $scope.insertLink = function () {
                var url = $scope.getUrl();
                if (!url)return;

                var selectedText = $scope.getSelectedText();
                var content = "link title";
                if (selectedText)content = selectedText;

                var template = sprintf(" [%s](%s) ", content, url);
                var insertAt = $scope.insertTextAtCursor(template);

                var start = insertAt + 2;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
            };

            $scope.insertYoutube = function () {
                var url = $scope.getUrl();
                if (!url)return;
                var template = sprintf(" ^[%s] ",url);
                var insertAt = $scope.insertTextAtCursor(template);

                var start = insertAt + 3;
                var end = start + url.length;
                $scope.highlightTextAtPosition(start, end);
            };


            $scope.showEditImageModal = function () {

                $scope.editImageModal.modal('show');
            };

            $scope.hideEditImageModal = function () {
                $scope.editImageModal.modal('hide');
                $scope.selectedImage = null;
                $scope.editedImageResult = null;
            };


            $scope.insertImage = function () {
                $scope.showEditImageModal();
            };

            $scope.uploadImage = function () {
                $scope.selectedImage = null;
                $scope.editedImageResult = null;

                imageService.createUploadForm()
                    .then(function (response) {
                        $scope.isUploadingImage = true;
                        var formObj = response;
                        return imageService.uploadImage(formObj);
                    })
                    .then(function (response) {
                        var uploadedImageId = response;
                        return imageService.getImageInfo(uploadedImageId);
                    })
                    .then(function (response) {
                        $scope.isUploadingImage = false;

                        var uploadedImage = response.data;
                        imageService.addOrUpdateUploadedFile($scope.formModel.uploadedFiles, uploadedImage);
                        $scope.selectedImage = uploadedImage;
                        $scope.updateImage();
                        $scope.makeImageEditable();
                        $scope.makeToastInfo("Please click save button to permanently save.");
                    })
                    .catch(function (response) {
                        console.log(response);
                    });
            };


            $scope.makeImageEditable = function () {

                var imageToEdit = null;
                setTimeout(function () {


                    imageToEdit = $element.find(".imageToEdit");
                    imageToEdit.cropper({
                        //aspectRatio: 1 / 1,
                        background: false,
                        movable: false,
                        highlight: false,
                        checkCrossOrigin: true,
                        modal: true,
                        crop: function (e) {
                            console.log("cropping");
                            $scope.editedImageResult = e;
                        }
                    });

                    imageToEdit.on('built.cropper', function () {
                        console.log("loaded");
                    });

                }, 250);
            };


            $scope.insertImageOnly = function () {
                if ($scope.selectedImage == null) {
                    $window.alert("Please select image first");
                    return;
                }

                var url = $scope.selectedImage.absoluteUrl;
                $scope.insertImageMarkdown(url);
                $scope.hideEditImageModal();
            };


            $scope.saveAndInsertImage = function () {

                if ($scope.selectedImage == null) {
                    $window.alert("Please select image first");
                    return;
                }

                $scope.isUploadingImage = true;
                var selectedImage = angular.copy($scope.selectedImage);
                $scope.selectedImage = null;

                imageService.saveEditedImage(selectedImage, $scope.editedImageResult)
                    .then(function (response) {
                        $scope.isUploadingImage = false;
                        var uploadedImage = response.data;
                        imageService.addOrUpdateUploadedFile($scope.formModel.uploadedFiles, uploadedImage);

                        $scope.selectedImage = uploadedImage;
                        $scope.updateImage();
                        $scope.makeImageEditable();

                    })
                    .catch(function (reponse) {

                    });
            };

            $scope.insertImageMarkdown = function (url) {
                var selectedText = $scope.getSelectedText();
                var content = "image title";
                if (selectedText)content = selectedText;

                var template = sprintf(" ![%s](%s) ", content, url + "?t=" + $scope.getTimestamp());
                var insertAt = $scope.insertTextAtCursor(template);

                var start = insertAt + 3;
                var end = start + content.length;
                $scope.highlightTextAtPosition(start, end);
                autosize.update($scope.getEditor());
            };

            $scope.selectUploadedImage = function (file) {
                $scope.selectedImage = null;
                imageService.setSelectedImage(file)
                    .then(function (response) {
                        $scope.selectedImage = response;
                        $scope.updateImage();
                        $scope.makeImageEditable();
                    });
            };


            $scope.getTimestamp = function () {
                var d = new Date();
                var n = d.getTime();
                return n;
            };

            $scope.updateImage = function () {
                var img = angular.element(
                    '<img crossOrigin class="imageToEdit img-responsive" src="' +
                    $scope.selectedImage.absoluteUrl + "?t=" + $scope.getTimestamp() +
                    '" style="display: inline-block"/>');
                $element.find(".image-container").empty();
                $element.find(".image-container").append(img);
            };


            $scope.removeImage = function () {

                if ($scope.selectedImage == null) {
                    $window.alert("Please select image first");
                    return;
                }


                var confirm = $window.confirm(
                    "Are your sure you want to delete image id "
                    + $scope.selectedImage.contextReferenceId);

                if (confirm) {
                    if ($scope.formModel.featuredImage) {
                        if ($scope.formModel.featuredImage.id == $scope.selectedImage.id) {
                            $scope.formModel.featuredImage = null;
                        }
                    }

                    imageService.removeFile($scope.formModel.uploadedFiles, $scope.selectedImage);
                    $scope.selectedImage = null;
                    $scope.makeToastInfo("Please click save button to permanently save.");
                }
            };

            $scope.setToFeaturedImage = function () {

                if ($scope.selectedImage == null) {
                    $window.alert("Please select image first");
                    return;
                }
                $scope.formModel.featuredImage = $scope.selectedImage;
                $scope.makeToastInfo("Please click save button to permanently save.");
            };


            $scope.makeToastInfo = function (message) {
                toastr.info(message, 'info');

            };


        }//end controller
    };
    return directive;

});
