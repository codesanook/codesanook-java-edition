describe("commentAddController", function () {


    var $controller = null;
    var $rootScope = null;
    var $q;
    var commentAddController = null;
    var scope;
    var facebookService;
    var commentService;
    beforeEach(function () {
        angular.mock.module('codesanook');
        angular.mock.inject(function (_$controller_, _$rootScope_, _facebookService_, _commentService_) {
            $controller = _$controller_;
            $rootScope = _$rootScope_;
            facebookService = _facebookService_;
            commentService = _commentService_;
        });

        scope = $rootScope.$new();
        commentAddController = $controller('commentAddController',
            {$scope: scope, $element: {}, facebookService: facebookService});

        spyOn(facebookService, 'showIntroLogInModal');
    });


    it('should be defined', function () {
        expect(commentAddController).toBeDefined();
    });

    it('scope should be defined', function () {
        expect(scope).toBeDefined();
    });


    it('user not log in, facebookService called once', function () {
        scope.user = null;
        scope.addComment();
        expect(facebookService.showIntroLogInModal).toHaveBeenCalledTimes(1);
    });


    it('user not log in, new comment save to storage', function () {
        scope.user = null;
        spyOn(commentService, 'saveToStorage');
        scope.addComment();
        expect(commentService.saveToStorage).toHaveBeenCalledTimes(1);
    });


});
