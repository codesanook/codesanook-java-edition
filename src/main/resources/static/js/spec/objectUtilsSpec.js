describe("objectUtilsSpec", function () {


    it("xx", function () {
        var obj = {a: 1, b: 2};
        var obj2 = {};

        for (var property in obj) {
            //console.log(property + " : " + obj[property]);
            obj2[property] = obj[property];
        }

        //console.log(angular.mock.dump(obj2));
    });

});