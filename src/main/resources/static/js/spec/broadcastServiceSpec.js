describe('broadcastService', function () {

    var broadcast1Controller;
    var broadcast2Controller;
    var $controller;
    var $rootScope;
    var scope1;
    var scope2;

    beforeEach(function () {

        angular.mock.module('codesanook');
        angular.mock.inject(function (_$controller_, _$rootScope_) {
            $controller = _$controller_;
            $rootScope = _$rootScope_;

        });
        scope1 = $rootScope.$new();
        broadcast1Controller = $controller("broadcast1Controller",
            {$scope: scope1});

        scope2 = $rootScope.$new();
        broadcast2Controller = $controller("broadcast2Controller",
            {$scope: scope2});

    });


    it('controllers should be defined', function () {
        expect(broadcast1Controller).toBeDefined();
        expect(broadcast2Controller).toBeDefined();
    });


    it('', function () {
        scope2.receiveNotify();
        scope1.notify(["hello","hi"]);
    });


});