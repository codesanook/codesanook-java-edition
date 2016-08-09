describe("markdownToolbarDirective button", function () {

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
            window.buildNumber = 1;
            $rootScope.formModel = {
                uploadedFiles: [],
                content: null
            };
            $rootScope.loaded = function () {
            };

            var htmlElement = angular.element(
                '<markdown-toolbar form-model="formModel" editor-id="' +
                editorId +
                '" loaded="loaded">' +
                '</markdown-toolbar>');
            el = $compile(htmlElement)($rootScope);
            $rootScope.$digest();

            controller = el.controller("markdownToolbar", {imageService: imageService});
            scope = el.isolateScope();

            spyOn(scope,'makeImageEditable');
        });
    });


    it("button fontSize h1 click, scope.fontSize called", function () {
        spyOn(scope, 'fontSize');
        var button = el.find(".font-size-h1");
        button.trigger("click");

        expect(scope.fontSize).toHaveBeenCalledTimes(1);
        expect(scope.fontSize).toHaveBeenCalledWith("h1");
    });


    it("button fontSize h1 called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.callFake(function () {
            return "hello";
        });
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.fontSize("h1");

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith('\n# hello\n');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button fontSize h2 click, scope.fontSize called", function () {
        spyOn(scope, 'fontSize');
        var button = el.find(".font-size-h2");
        button.trigger("click");

        expect(scope.fontSize).toHaveBeenCalledTimes(1);
        expect(scope.fontSize).toHaveBeenCalledWith("h2");
    });


    it("button fontSize h2 called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.callFake(function () {
            return "hello";
        });
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.fontSize("h2");

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith('\n## hello\n');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button fontSize h3 click, scope.fontSize called", function () {
        spyOn(scope, 'fontSize');
        var button = el.find(".font-size-h3");
        button.trigger("click");

        expect(scope.fontSize).toHaveBeenCalledTimes(1);
        expect(scope.fontSize).toHaveBeenCalledWith("h3");
    });


    it("button fontSize h3 called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.callFake(function () {
            return "hello";
        });
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.fontSize("h3");

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith('\n### hello\n');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button italic click, scope.italic called", function () {
        spyOn(scope, 'italic');
        var button = el.find(".btn-italic");
        button.trigger("click");
        expect(scope.italic).toHaveBeenCalledTimes(1);
    });

    it("button italic called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.returnValue("hello");
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.italic();

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith(' *hello* ');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button bold click, scope.italic called", function () {
        spyOn(scope, 'bold');
        var button = el.find(".btn-bold");
        button.trigger("click");
        expect(scope.bold).toHaveBeenCalledTimes(1);
    });


    it("button bold called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.returnValue("hello");
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.bold();

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith(' **hello** ');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button quote click, scope.italic called", function () {
        spyOn(scope, 'quote');
        var button = el.find(".btn-quote");
        button.trigger("click");
        expect(scope.quote).toHaveBeenCalledTimes(1);
    });


    it("button quote called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.returnValue("hello");
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.quote();

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith('\n>hello\n');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button insert code click, scope.italic called", function () {
        spyOn(scope, 'insertCode');
        var button = el.find(".btn-insert-code");
        button.trigger("click");
        expect(scope.insertCode).toHaveBeenCalledTimes(1);
    });


    it("button insert code called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.returnValue("hello");
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');

        scope.insertCode();

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith('\n```\nhello\n```\n');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });


    it("button insert link click, scope.italic called", function () {
        spyOn(scope, 'insertLink');
        var button = el.find(".btn-insert-link");
        button.trigger("click");
        expect(scope.insertLink).toHaveBeenCalledTimes(1);
    });

    it("button insert link called, other functions called correctly ", function () {
        spyOn(scope, 'getSelectedText').and.returnValue("hello");
        spyOn(scope, 'insertTextAtCursor').and.returnValue(0);
        spyOn(scope, 'highlightTextAtPosition');
        spyOn(scope, 'getUrl').and.returnValue("http://codesanook.com");

        scope.insertLink();

        expect(scope.getSelectedText).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledTimes(1);
        expect(scope.insertTextAtCursor).toHaveBeenCalledWith(' [hello](http://codesanook.com) ');
        expect(scope.highlightTextAtPosition).toHaveBeenCalledTimes(1);
    });

});