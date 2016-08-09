//http://www.bradoncode.com/blog/2015/07/13/unit-test-promises-angualrjs-q/
//http://tobyho.com/2011/12/15/jasmine-spy-cheatsheet/
describe("facebookLogInController logIn", function () {

    var $controller = null;
    var $http = null;
    var facebookLogInController = null;
    var scope = {};
    var facebookService;
    var $q;

    var fbLogInDeferred;
    var fbGetLogInStatusDeferred;
    var fbGetUserInfoDeferred;
    var fbIsExistingUserDeferred;
    var $document;

    beforeEach(function () {
        angular.mock.module('codesanook');
        angular.mock.inject(function (_$controller_, _$rootScope_, _$q_, _facebookService_, _$http_, _$document_) {
            $controller = _$controller_;
            scope = _$rootScope_.$new();
            facebookService = _facebookService_;
            $q = _$q_;
            $http = _$http_;
            $document = _$document_;
        });

        fbLogInDeferred = $q.defer();
        spyOn(facebookService, 'logIn').and.returnValue(fbLogInDeferred.promise);

        fbGetLogInStatusDeferred = $q.defer();
        spyOn(facebookService, 'getLogInStatus').and.returnValue(fbGetLogInStatusDeferred.promise);

        fbGetUserInfoDeferred = $q.defer();
        spyOn(facebookService, 'getUserInfo').and.returnValue(fbGetUserInfoDeferred.promise);

        fbIsExistingUserDeferred = $q.defer();
        spyOn(facebookService, 'isExistingUser').and.returnValue(fbIsExistingUserDeferred.promise);

        facebookLogInController = $controller("facebookLogInController",
            {
                $scope: scope, facebookService: facebookService,
                $document: $document
            });

        spyOn(scope, 'logInExistingUser');
        spyOn(scope, 'registerNewUser');
        spyOn(scope, 'hideIntroLogInModal');
        spyOn(scope, 'showWaitingModal');
    });


    it('with existing user, scope.logInExistingUser called once', function () {

        scope.logIn();
        fbLogInDeferred.resolve({});

        var facebookId = 111;
        var facebookToken = "token";
        fbGetLogInStatusDeferred.resolve({
            data: {
                facebookAppScopeUserId: facebookId,
                facebookAccessToken: facebookToken
            }
        });

        var email = "theeranitp@gmail.com";
        var profileUrl = "aaron.jpg";
        var name = "aaron";
        fbGetUserInfoDeferred.resolve({
            data: {
                profileUrl: profileUrl,
                name: name,
                email: email
            }
        });

        fbIsExistingUserDeferred.resolve({data: {existingUser: true}});
        scope.$apply();


        expect(scope.user.facebookAppScopeUserId).toBe(facebookId);
        expect(scope.user.facebookAccessToken).toBe(facebookToken);
        expect(scope.user.profileUrl).toBe(profileUrl);
        expect(scope.user.name).toBe(name);
        expect(scope.user.email).toBe(email);

        expect(scope.logInExistingUser).toHaveBeenCalled();
        expect(scope.logInExistingUser).toHaveBeenCalledTimes(1);
    });

    it('new user, scope.registerNewUser called once', function () {

        scope.logIn();
        fbLogInDeferred.resolve({});

        var facebookId = 111;
        var facebookToken = "token";
        fbGetLogInStatusDeferred.resolve({
            data: {
                facebookAppScopeUserId: facebookId,
                facebookAccessToken: facebookToken
            }
        });

        var email = "theeranitp@gmail.com";
        var profileUrl = "aaron.jpg";
        var name = "aaron";
        fbGetUserInfoDeferred.resolve({
            data: {
                profileUrl: profileUrl,
                name: name,
                email: email
            }
        });

        fbIsExistingUserDeferred.resolve({data: {existingUser: false}});
        scope.$apply();

        expect(scope.registerNewUser).toHaveBeenCalled();
        expect(scope.registerNewUser).toHaveBeenCalledTimes(1);
    });

});

describe("facebookLogInController logInExistingUser", function () {

    var $controller = null;
    var $http = null;
    var facebookLogInController = null;
    var scope = {};
    var facebookService;
    var $q;
    var $document;
    var fbLogInWithNewFacebookToken;

    beforeEach(function () {
        angular.mock.module('codesanook');
        angular.mock.inject(function (_$controller_, _$rootScope_, _$q_, _facebookService_, _$http_, _$document_) {
            $controller = _$controller_;
            scope = _$rootScope_.$new();
            facebookService = _facebookService_;
            $q = _$q_;
            $http = _$http_;
            $document = _$document_;
        });

        fbLogInWithNewFacebookToken = $q.defer();
        spyOn(facebookService, 'logInWithNewFacebookToken').and.returnValue(fbLogInWithNewFacebookToken.promise);

        facebookLogInController = $controller("facebookLogInController",
            {
                $scope: scope, facebookService: facebookService,
                $document: $document
            });

        spyOn(scope, 'logInWithCookieToken');
    });


    it('facebookService.logInWithNewFacebookToken called once', function () {

        var facebookId = 111;
        var facebookToken = "token";
        var email = "theeranitp@gmail.com";
        var profileUrl = "aaron.jpg";
        var name = "aaron";
        scope.user = {
            facebookAppScopeUserId: facebookId,
            facebookAccessToken: facebookToken,
            profileUrl: profileUrl,
            name: name,
            email: email
        };
        var apiToken = "API_TOKEN";
        fbLogInWithNewFacebookToken.resolve({data: {apiToken: apiToken}});
        scope.logInExistingUser();

        scope.$apply();

        expect(facebookService.logInWithNewFacebookToken).toHaveBeenCalled();
        expect(facebookService.logInWithNewFacebookToken.calls.count()).toBe(1);
        expect(facebookService.logInWithNewFacebookToken).toHaveBeenCalledWith(scope.user);

        expect(scope.logInWithCookieToken).toHaveBeenCalled();
        expect(scope.logInWithCookieToken.calls.count()).toBe(1);
        expect(scope.logInWithCookieToken).toHaveBeenCalledWith(apiToken);

    });

});

describe("facebookLogInController registerNewUser", function () {

    var $controller = null;
    var $http = null;
    var facebookLogInController = null;
    var scope = {};
    var facebookService;
    var $q;
    var $document;

    var fbIsExistingUserWithEmailDeferred;
    var fbUpdateUserWithFacebookDeferred;
    var fbRegisterNewUserDeferred;

    beforeEach(function () {
        angular.mock.module('codesanook');
        angular.mock.inject(function (_$controller_, _$rootScope_, _$q_, _facebookService_, _$http_, _$document_) {
            $controller = _$controller_;
            scope = _$rootScope_.$new();
            facebookService = _facebookService_;
            $q = _$q_;
            $http = _$http_;
            $document = _$document_;
        });


        fbIsExistingUserWithEmailDeferred = $q.defer();
        spyOn(facebookService, 'isExistingUserWithEmail')
            .and.returnValue(fbIsExistingUserWithEmailDeferred.promise);

        fbUpdateUserWithFacebookDeferred = $q.defer();
        spyOn(facebookService, 'updateUserWithFacebook')
            .and.returnValue(fbUpdateUserWithFacebookDeferred.promise);

        fbRegisterNewUserDeferred = $q.defer();
        spyOn(facebookService, 'registerNewUser')
            .and.returnValue(fbRegisterNewUserDeferred.promise);

        facebookLogInController = $controller("facebookLogInController",
            {
                $scope: scope, facebookService: facebookService,
                $document: $document
            });

        spyOn(scope, 'logInExistingUser');
    });


    it('IsExistingUserWithEmail is true, facebookService.updateUserWithFacebook called once', function () {

        var facebookId = 111;
        var facebookToken = "token";
        var email = "theeranitp@gmail.com";
        var profileUrl = "aaron.jpg";
        var name = "aaron";
        scope.user = {
            facebookAppScopeUserId: facebookId,
            facebookAccessToken: facebookToken,
            profileUrl: profileUrl,
            name: name,
            email: email
        };

        scope.registerNewUser();

        fbIsExistingUserWithEmailDeferred.resolve({data: {existingUser: true}});
        fbUpdateUserWithFacebookDeferred.resolve({data: {}});

        scope.$apply();

        expect(facebookService.updateUserWithFacebook).toHaveBeenCalledWith(scope.user);
        expect(facebookService.updateUserWithFacebook).toHaveBeenCalledTimes(1);

        expect(scope.logInExistingUser).toHaveBeenCalledTimes(1);
    });

    it('logIn with user who has registered with email, facebookService.registerNewUser called once ', function () {

        var facebookId = 111;
        var facebookToken = "token";
        var email = "theeranitp@gmail.com";
        var profileUrl = "aaron.jpg";
        var name = "aaron";
        scope.user = {
            facebookAppScopeUserId: facebookId,
            facebookAccessToken: facebookToken,
            profileUrl: profileUrl,
            name: name,
            email: email
        };

        scope.registerNewUser();

        fbIsExistingUserWithEmailDeferred.resolve({data: {existingUser: false}});
        fbRegisterNewUserDeferred.resolve({data: {userId: 1}});

        scope.$apply();

        expect(facebookService.registerNewUser).toHaveBeenCalledWith(scope.user);
        expect(facebookService.registerNewUser).toHaveBeenCalledTimes(1);

        expect(scope.logInExistingUser).toHaveBeenCalledTimes(1);
    });

});
