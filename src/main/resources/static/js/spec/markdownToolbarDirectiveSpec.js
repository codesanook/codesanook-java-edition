describe("markdownToolbarDirective", function () {

    var el = null;
    var controller = null;
    var scope = null;
    var imageService;
    var editorId = "editorId1";
    var $rootScope;

    beforeEach(function () {
        angular.mock.module('codesanook', 'ngTemplates');
        angular.mock.inject(function ($compile, _$rootScope_, $http, _imageService_) {
            imageService = _imageService_;
            $rootScope = _$rootScope_;

            //$httpBackend.whenGET('/js/angular/templates/markdown-toolbar.html?bno=' + window.buildNumber)
            //    .respond(200, '');

            window.buildNumber = 1;
            $rootScope.formModel = {
                uploadedFiles: []
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

            /*
             http://stackoverflow.com/questions/15314293/unit-testing-directive-controllers-in-angular-without-making-controller-global
             ames's method works for me. One small twist is though, when you have an external template, you would have to call $httpBackend.flush() before $rootScope.$digest() in order to let angular execute your controller.
             I guess this should not be an issue, if you are using https://github.com/karma-runner/karma-ng-html2js-preprocessor
             */
            //$httpBackend.flush();
            $rootScope.$digest();

            spyOn(imageService, 'uploadImage');
            controller = el.controller("markdownToolbar", {imageService: imageService});
            scope = el.isolateScope();
            //|| el.scope();

            spyOn(scope,'makeImageEditable');
        });
    });

    it("$scope.isUploadingImage should be false", function () {
        expect(scope.isUploadingImage).toBe(false);
    });

    //http://www.sitepoint.com/angular-testing-tips-testing-directives/
    //it("$scope.isUploadingImage should be false", function () {
    //    scope.uploadImage();
    //    expect(scope.isUploadingImage).toBe(true);
    //});


    it("find test hidden input should return test value", function () {
        var input = el.find("#testX");
        expect(input.val()).toBe("test");
    });


    it("controller is defined", function () {
        expect(controller).toBeDefined();
    });

    it("scope is defined", function () {
        expect(scope).toBeDefined();
    });

    it("editorId set correctly", function () {
        expect(scope.editorId).toBe(editorId);
    });

    it("scope.loaded to be defined", function () {
        expect(scope.loaded).toBeDefined();
    });


    it("rootScope.loaded get called back after directive loaded", function () {
        expect($rootScope.loaded).toHaveBeenCalledTimes(1);
    });


    it("$rootScope.formModel must be scope.formModel", function () {
        expect($rootScope.formModel).toBe(scope.formModel);
    });


});