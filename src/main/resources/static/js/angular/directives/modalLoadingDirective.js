app.directive("waitingModal", function () {
    var directive = {
        restrict: 'E',
        replace: true,
        scope: {
            show: "="
        },
        templateUrl: '/js/angular/templates/waiting-modal.html?bno=' + window.buildNumber,

        link: function ($scope, $element, $attrs) {

            var modal = $element.find(".waiting-modal");
            scope.$watch('show', function (newValue, oldValue) {
                if (newValue) {
                    modal.modal('show');
                } else {
                    modal.modal('hide');
                }
            });

        }
    };

    return directive;
});
