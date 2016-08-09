app.directive("lineLoading", function () {
    var directive = {
        restrict: 'E',
        replace :true,

        template: '<div class="la-line-scale">' +
        '<div></div>' +
        '<div></div>' +
        '<div></div>' +
        '<div></div>' +
        '<div></div>' +
        '</div>',

        link: function ($scope, $element, $attrs) {

        }
    };

    return directive;
});
