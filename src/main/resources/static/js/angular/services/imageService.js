app.factory('imageService', function ($q, $http, $document) {

    var MAX_FILE_UPLOAD_COUNT = 5;
    var service = {};


    service.latestContextReferenceId = function (files) {
        return files.length;
        //var latestContextReferenceId = 0;
        //if (files && files.length > 0) {
        //    var index = 0;
        //    var max = 0;
        //    for (index; index < files.length; index++) {
        //        var file = files[index];
        //        if (file.contextReferenceId > max) {
        //            max = file.contextReferenceId;
        //        }
        //    }
        //    latestContextReferenceId = max;
        //}
        //return latestContextReferenceId;
    };

    service.addOrUpdateUploadedFile = function (files, uploadedFile) {

        var existingUploadedFile = service.getUploadedFileById(files, uploadedFile.id);
        if (existingUploadedFile) {
            existingUploadedFile.width = uploadedFile.width;
            existingUploadedFile.height = uploadedFile.height;

            //adjust cache
            existingUploadedFile.absoluteUrl = uploadedFile.absoluteUrl;

        } else {//not found add it
            var latestContextReferenceId = service.latestContextReferenceId(files);
            latestContextReferenceId++;
            uploadedFile.contextReferenceId = latestContextReferenceId;
            files.push(uploadedFile);
        }

    };


    service.getUploadedFileById = function (files, fileId) {
        var map = files.map(function (item) {
            return item.id;
        });
        var foundAtIndex = map.indexOf(fileId);
        if (foundAtIndex >= 0) {
            return files[foundAtIndex];
        }
        return null;
    };


    service.getTimestamp = function () {
        var date = new Date();
        var timestamp = date.getTime();
        return timestamp;
    };

    service.createUploadForm = function () {

        var deferred = $q.defer();
        <!--hidden uploadFile-->
        <!--iframe must have the same name as name attribute although it will open new tab-->
        var cacheKey = sprintf("uploadedFile-%d-%d", window.loggedInUser.id, service.getTimestamp());
        var url = sprintf("%s/images/upload-image?width=%d&height=%d&cacheId=%s",
            window.apiEndpoint, 700, 700, cacheKey);
        var uploadFormContainer = angular.element(
            '<div style="visibility: hidden">' +
            '<iframe id="uploadIFrame" name ="uploadIFrame" style="visibility: hidden"></iframe>' +

            '<form id="uploadImageForm" target="uploadIFrame" enctype="multipart/form-data" action="' +
            url +
            '" method="post" style="visibility: hidden">' +
            '<input id="fileInput" type="file" name="file" value="upload" style="visibility: hidden" />' +
            '</form>' +

            '</div>');

        $document.find("body").append(uploadFormContainer);
        var uploadFileInput = $document.find("#fileInput");
        var uploadForm = $document.find("#uploadImageForm");

        uploadFileInput.trigger('click');
        uploadFileInput.on("change", function () {
            //uploadForm.submit();
            deferred.resolve({
                container: uploadFormContainer,
                fileInput: uploadFileInput,
                form: uploadForm
            })
        });

        return deferred.promise;
    };

    service.uploadImage = function (formObj) {
        var deferred = $q.defer();

        var iframe = formObj.container.find("iframe");
        iframe.load(function () {
            var iframe = this;
            var rawHtml = iframe.contentWindow.document.body.innerHTML;
            var uploadedImageId = service.parseUploadedImageId(rawHtml);
            formObj.container.remove();

            if (uploadedImageId) {
                deferred.resolve(uploadedImageId);
            } else {
                deferred.reject("Error in uploading image, please reload page and try again.");
            }
        });

        setTimeout(function () {
            formObj.form.trigger("submit");
        }, 250);

        return deferred.promise;
    };


    service.getImageInfo = function (uploadedImageId) {
        var deferred = $q.defer();
        var req = {
            method: "GET",
            url: sprintf("%s/images/%d", window.apiEndpoint, uploadedImageId),
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            }
        };

        $http(req).then(function (response) {
            deferred.resolve(response);
        }).catch(function (response) {
            console.log(response);
            deferred.reject(response);
        });

        return deferred.promise;

    };

    service.parseUploadedImageId = function (rawHtml) {

        var statusCode;
        var uploadedFileId = null;
        try {
            var match = /(\d{3}):(\d+)/.exec(rawHtml);
            statusCode = match[1];
            uploadedFileId = match[2];
            console.log("status code %s", statusCode);
            if (statusCode != '200')  throw  "not get status code 200";

        } catch (e) {
            console.log(e);
            $window.alert("Error upload photo, please try again");
        }
        return uploadedFileId
    };

    service.delay = function (delayExpression) {
        var delay = 100;
        window.setTimeout(function () {
            console.log("delay of %d milliseconds", delay);
            delayExpression();
        }, delay);

    };

    service.saveEditedImage = function (file, result) {

        var deferred = $q.defer();
        console.log("width %d, height %d", file.width, file.height);
        var url = sprintf("%s/images/%d", window.apiEndpoint, file.id);
        var method = "PUT";
        console.log("edit imageResult x=%f, y=%f, width=%f, height=%f, rotate=%f, scaleX=%f, scaleY=%f",
            result.x, result.y, result.width, result.height, result.rotate, result.scaleX, result.scaleY);

        //adjust crop width
        result.x = Math.floor(result.x);
        var cropX = result.x < 0 ? 0 : result.x;
        var maxCropWidth = file.width - cropX;
        result.width = Math.floor(result.width);
        var cropWidth = result.width > maxCropWidth ? maxCropWidth : result.width;

        //adjust crop height
        result.y = Math.floor(result.y);
        var cropY = result.y < 0 ? 0 : result.y;
        var maxCropHeight = file.height - cropY;
        result.height = Math.floor(result.height);
        var cropHeight = result.height > maxCropHeight ? maxCropHeight : result.height;

        var data = {
            "cropX": cropX,
            "cropY": cropY,
            "cropWidth": cropWidth,
            "cropHeight": cropHeight
        };

        var req = {
            method: method,
            url: url,
            headers: {
                'Content-Type': "application/json",
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            },
            data: data
        };

        $http(req)
            .then(function (response) {
                deferred.resolve(response);
            }).catch(function (response) {
                deferred.reject(response);
            });

        return deferred.promise;
    };//end method

    service.setSelectedImage = function (file) {
        var deferred = $q.defer();
        setTimeout(function () {
            deferred.resolve(file);
        }, 250);
        return deferred.promise;
    };


    service.getUploadedFileIndex = function (files, fileId) {
        var map = files.map(function (item) {
            return item.id;
        });
        var foundAtIndex = map.indexOf(fileId);
        return foundAtIndex;
    };


    service.removeFile = function (files, file) {
        var foundIndex = service.getUploadedFileIndex(files, file.id);
        if (foundIndex == -1)return;
        files.splice(foundIndex, 1);

        var startIndex = 0;
        for (startIndex; startIndex < files.length; startIndex++) {
            var currentFile = files[startIndex];
            currentFile.contextReferenceId = startIndex + 1;
        }
    };


    return service;

});
