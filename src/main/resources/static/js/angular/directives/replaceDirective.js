app.directive("replace", function () {

    var directive = {
        restrict: 'E',
        scope: {
            src: '='
        },
        template: '<ng-include src="src" onload="replace()"></ng-include>',
        link: function (scope, el, iAttrs) {
            scope.replace = function () {
                el.replaceWith(e.children());
            };
        }
    };

    return directive;

});


app.directive('includeReplace', function () {
    return {
        require: 'ngInclude',
        restrict: 'A', /* optional */
        link: function (scope, el, attrs) {
            el.replaceWith(el.children());
        }
    };
});



