describe("markdownToolbarDirective insertImageOnly", function () {

    var el = null;
    var controller = null;
    var $scope = null;
    var imageService;

    beforeEach(function () {
        angular.mock.module('codesanook', 'ngTemplates');
        angular.mock.inject(function ($compile, $rootScope, $http, _imageService_) {

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


    it("$scope.insertImageOnly $scope.insertImageMarkdown called once", function () {
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
        $scope.insertImageOnly();
        expect($scope.insertImageMarkdown).toHaveBeenCalledTimes(1);
    });


    it("$scope.insertImageOnly $scope.hideEditImageModal called once", function () {

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
        $scope.insertImageOnly();
        expect($scope.hideEditImageModal).toHaveBeenCalledTimes(1);
    });


});