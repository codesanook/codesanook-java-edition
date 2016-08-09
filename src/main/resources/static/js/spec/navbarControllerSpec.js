describe('navbarController', function () {


    var scope = null;
    var facebookService = null;

    var $rootScope = null;
    var $controller = null;
    var $q;
    var trackingService;
    beforeEach(function () {

        angular.mock.module('codesanook');
        angular.mock.inject(function (_$controller_, _$rootScope_, _facebookService_, _$q_, _trackingService_) {
            $controller = _$controller_;
            $rootScope = _$rootScope_;
            facebookService = _facebookService_;
            $q = _$q_;
            trackingService = _trackingService_;
        });

        scope = $rootScope.$new();
        $controller("navbarController",
            {
                $scope: scope,
                facebookService: facebookService,
                trackingService: trackingService
            });
    });


    it('scope should be defined', function () {
        expect(scope).toBeDefined();
    });

    it('user logged in should redirect to new question url', function () {
        spyOn(scope, 'redirect');
        var deferred = $q.defer();
        spyOn(trackingService, 'trackMenu').and.returnValue(deferred.promise);
        scope.user = {id: 1};
        scope.newQuestion();

        deferred.resolve({});
        $rootScope.$apply();

        var url = "/post/create?type=1";
        expect(scope.redirect).toHaveBeenCalledWith(url);
    });

    it('user not logged in facebookService.showIntroLogInModal called once', function () {
        spyOn(scope, 'redirect');
        spyOn(facebookService, 'showIntroLogInModal');

        var deferred = $q.defer();
        spyOn(trackingService, 'trackMenu').and.returnValue(deferred.promise);


        var logInDeferred = $q.defer();
        spyOn(facebookService, 'requestUserLogIn').and.returnValue(logInDeferred.promise);

        scope.user = null;
        scope.newQuestion();

        deferred.resolve({});
        logInDeferred.resolve({});
        $rootScope.$apply();

        expect(facebookService.requestUserLogIn).toHaveBeenCalledTimes(1);
    });

});