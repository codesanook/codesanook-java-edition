describe("postAddUpdateControllerSpec", function () {

    var $controller = null;
    var controller = null;
    var scope = null;
    window.postId = null;
    window.postTypeId = 2;
    var element = {};
    var postService;
    var $q = null;
    window.apiEndpoint = "http://api";

    beforeEach(function () {
        angular.mock.module('codesanook');

        angular.mock.inject(function (_$controller_, $rootScope, $http, _$q_,  _postService_) {
            scope = $rootScope.$new();
            $controller = _$controller_;
            postService = _postService_;
            $q = _$q_;
        });

        controller = $controller("postAddUpdateController",
            {
                $scope: scope,
                $element: element,
                postService: postService
            });

        var editor = {};
        spyOn(scope, 'getEditor').and.returnValue(editor);
        spyOn(scope, 'getUser').and.returnValue({id: 1});
        spyOn(scope, 'showEditMultipartFormModal');

    });

    it("scope should be defined", function () {
        expect(scope).toBeDefined();
    });


    it("window.postType 2 scope.postTypeId return 2", function () {
        expect(scope.post.postType.id).toBe(2);
    });

    it("window.postType 2 scope.formModel.isArticle return true", function () {
        expect(scope.post.isArticle).toBe(true);
    });


    it("save new post valid form, scope.addPost() called", function () {

        spyOn(scope, 'addPost');

        var form = {
            $valid: true,
            $setPristine: jasmine.createSpy()
        };
        scope.savePost(form);

        expect(scope.addPost).toHaveBeenCalledTimes(1);
    });

    it("save new post valid form, new post added", function () {


        var addPostDeferred = $q.defer();
        var addPost = spyOn(postService, 'addPost');
        addPost.and.returnValue(addPostDeferred.promise);

        var form = {
            $valid: true,
            $setPristine: jasmine.createSpy()
        };
        scope.savePost(form);

        var savedPostId = 1;
        addPostDeferred.resolve({data: savedPostId});
        scope.$apply();

        expect(scope.post.id).toBe(savedPostId);
        expect(scope.showEditMultipartFormModal).toHaveBeenCalledTimes(1);
        expect(postService.addPost).toHaveBeenCalledTimes(1);
        expect(postService.addPost).toHaveBeenCalledWith(scope.post);
    });


    it("save new post content get converted to HTML correctly", function () {
        var addPostDeferred = $q.defer();
        var addPost = spyOn(postService, 'addPost');
        addPost.and.returnValue(addPostDeferred.promise);

        var form = {
            $valid: true,
            $setPristine: jasmine.createSpy()
        };

        var content = "#Hello";
        scope.post.content = content;


        scope.savePost(form);

        var savedPostId = 1;
        addPostDeferred.resolve({data: savedPostId});
        scope.$apply();

        expect(scope.post.content).toEqual(content);
        expect(scope.post.htmlContent).toEqual("<h1>Hello</h1>");
    });

    it("save new post uploadedFiles saved", function () {
        var addPostDeferred = $q.defer();
        var addPost = spyOn(postService, 'addPost');
        addPost.and.returnValue(addPostDeferred.promise);

        var form = {
            $valid: true,
            $setPristine: jasmine.createSpy()
        };

        var uploadedFile = {
            id: 1,
            contextReferenceId: 1,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 500,
            height: 400
        };
        scope.post.uploadedFiles = [uploadedFile];

        scope.savePost(form);

        var savedPostId = 1;
        addPostDeferred.resolve({data: savedPostId});
        scope.$apply();

        expect(scope.post.uploadedFiles.length).toBe(1);
        expect(scope.post.uploadedFiles[0].id).toBe(1);
        expect(scope.post.uploadedFiles[0].contextReferenceId).toBe(1);
    });

    it("post id not null, get post get call and return existing post", function () {

        var getPostDeferred = $q.defer();
        var getPost = spyOn(postService, 'getPost');
        getPost.and.returnValue(getPostDeferred.promise);


        var uploadedFile = {
            id: 1,
            contextReferenceId: 1,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "http://s3/aa.jpg",
            width: 500,
            height: 400
        };

        var existingPost = {
            uploadedFiles: [uploadedFile],
            title: "hello world",
            tags: ["c#", "java"],
            alias: "hello-world"
        };

        var postId = 1;
        scope.getPost(postId);

        getPostDeferred.resolve({data: existingPost});
        scope.$apply();

        //expect(scope.post).toBe(existingPost);
        expect(scope.post.uploadedFiles.length).toBe(1);
        expect(scope.post.tags).toBe("c# java");
        expect(scope.post.alias).toBe("hello world");
        expect(postService.getPost).toHaveBeenCalledWith(postId);
    });


});