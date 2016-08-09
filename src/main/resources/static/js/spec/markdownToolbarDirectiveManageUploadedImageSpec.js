describe("markdownToolbarDirective manage uploaded image", function () {

    var el = null;
    var controller = null;
    var scope = null;
    var imageService;
    var editorId = "editorId1";
    var $rootScope;
    var $compile;

    beforeEach(function () {
        angular.mock.module('codesanook', 'ngTemplates');
        angular.mock.inject(function (_$compile_, _$rootScope_, _imageService_) {
            imageService = _imageService_;
            $rootScope = _$rootScope_;
            $compile = _$compile_;
        });

        window.buildNumber = 1;


        var uploadedFile1 = {
            id: 1,
            contextReferenceId: 1,
            name: "aa1.jpg",
            relativePath: "aa1.jpg",
            absoluteUrl: "http://s3/aa1.jpg",
            width: 500,
            height: 400
        };

        var uploadedFile2 = {
            id: 2,
            contextReferenceId: 2,
            name: "aa2.jpg",
            relativePath: "aa2.jpg",
            absoluteUrl: "http://s3/aa2.jpg",
            width: 300,
            height: 500
        };


        $rootScope.formModel = {
            uploadedFiles: [uploadedFile1, uploadedFile2]
        };

        $rootScope.loaded = function () {
        };

        spyOn($rootScope, 'loaded');
        var htmlElement = angular.element(
            '<markdown-toolbar form-model="formModel" editor-id="' +
            editorId +
            '" loaded="loaded">' +
            '</markdown-toolbar>');
        el = $compile(htmlElement)($rootScope);
        $rootScope.$digest();

        spyOn(imageService, 'uploadImage');
        controller = el.controller("markdownToolbar", {imageService: imageService});
        scope = el.isolateScope();


        spyOn(scope, 'makeImageEditable');

    });

    it("uploaded image list should be 2", function () {
        var list = el.find(".uploaded-file-list li a");
        expect(list.size()).toBe(scope.formModel.uploadedFiles.length);
    });

    it("$scope.isUploadingImage should be false", function () {
        spyOn(scope, 'selectUploadedImage');
        var imageLink = el.find(".uploaded-file-list li a").first();
        imageLink.trigger("click");
        expect(scope.selectUploadedImage).toHaveBeenCalledTimes(1);
        var file = $rootScope.formModel.uploadedFiles[0];
        expect(scope.selectUploadedImage).toHaveBeenCalledWith(file);
    });


    it("scope.selectUploadedImage set scope.selectedImage", function () {
    });





});