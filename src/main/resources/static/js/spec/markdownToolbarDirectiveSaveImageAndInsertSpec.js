describe("markdownToolbarDirective saveAndInsertImage", function () {

    var el = null;
    var controller = null;
    var $scope = null;
    var imageService;
    var saveEditedImageDeferred;

    beforeEach(function () {
        angular.mock.module('codesanook', 'ngTemplates');
        angular.mock.inject(function ($compile, $rootScope, $http, _imageService_, $q) {

            imageService = _imageService_;

            window.buildNumber = 1;
            $rootScope.formModel = {
                uploadedFiles: []
            };

            $rootScope.editor = {};
            $rootScope.editorLoaded = function () {
            };

            var htmlElement = angular.element('<markdown-toolbar form-model="formModel"></markdown-toolbar>');
            el = $compile(htmlElement)($rootScope);
            $rootScope.$digest();

            spyOn(imageService, 'uploadImage');

            saveEditedImageDeferred = $q.defer();
            spyOn(imageService, 'saveEditedImage').and.returnValue(saveEditedImageDeferred.promise);

            controller = el.controller("markdownToolbar", {imageService: imageService});
            $scope = el.isolateScope();

            spyOn($scope, 'insertImageMarkdown');
            spyOn($scope, 'hideEditImageModal');
            spyOn($scope, 'makeImageEditable');


        });
    });

    it("scope should be defined", function () {
        expect($scope).toBeDefined();
    });


    it("controller should be defined", function () {
        expect(controller).toBeDefined();
    });


    var setupSaveAndInsertImage = function () {
        var uploadedFile = {
            id: 1,
            contextReferenceId: 0,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 500,
            height: 400
        };


        $scope.selectedImage = uploadedFile;
        $scope.editedImageResult = {
            x: 0,
            y: 0,
            width: 400,
            height: 300
        };


        $scope.saveAndInsertImage();
        var newUploadedFile = {
            id: 1,
            contextReferenceId: 0,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 400,
            height: 300
        };
        saveEditedImageDeferred.resolve({data: newUploadedFile});
        $scope.$apply();
    };

    it("$scope.insertImageOnly uploadedFile length should be 1", function () {

        setupSaveAndInsertImage();
        expect($scope.formModel.uploadedFiles.length).toBe(1);
        expect($scope.formModel.uploadedFiles[0].id).toBe(1);
        expect($scope.formModel.uploadedFiles[0].contextReferenceId).toBe(1);
    });



    it("$scope.insertImageOnly 1 existing file, uploadedFile length should be 1", function () {

        var existingUploadedFile = {
            id: 1,
            contextReferenceId: 1,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 500,
            height: 400
        };

        $scope.formModel.uploadedFiles = [existingUploadedFile];

        var uploadedFile = {
            id: 2,
            contextReferenceId: 0,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 500,
            height: 400
        };


        $scope.selectedImage = uploadedFile;
        $scope.editedImageResult = {
            x: 0,
            y: 0,
            width: 400,
            height: 300
        };


        $scope.saveAndInsertImage();
        var newUploadedFile = {
            id: 2,
            contextReferenceId: 0,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 400,
            height: 300
        };
        saveEditedImageDeferred.resolve({data: newUploadedFile});
        $scope.$apply();

        expect($scope.formModel.uploadedFiles.length).toBe(2);
        expect($scope.formModel.uploadedFiles[1]).toBe(newUploadedFile);
        expect($scope.formModel.uploadedFiles[1].contextReferenceId).toBe(2);
    });


});