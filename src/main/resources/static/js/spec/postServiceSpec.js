describe('postService', function () {

    var postService;
    beforeEach(function () {

        files = [];
        angular.mock.module("codesanook");
        angular.mock.inject(function (_postService_) {
            postService = _postService_
        });

    });

    it('must extend serviceBase with toHtml defined', function () {
        expect(postService.toHtml).toBeDefined();
    });


    it('toHtmlReturn correctly', function () {
        var markdown = "#Hello";
        var html = postService.toHtml(markdown);
        expect(html).toBe("<h1>Hello</h1>");
    });

    it('toHtmlReturn correctly', function () {
        var markdown = "# Hello";
        var html = postService.toHtml(markdown);
        expect(html).toBe("<h1>Hello</h1>");
    });

    it('getLocalStorage should not be null', function () {
        var localStorage = postService.getStorageService();
        expect(localStorage).not.toBeNull();
    });

})
;