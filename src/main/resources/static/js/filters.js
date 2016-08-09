var codesanookFilters = angular.module('codesanookFilters', []);

codesanookFilters
    .filter('trustAsResourceUrl', ['$sce', function($sce) {
        return function(val) {
            return $sce.trustAsResourceUrl(val);
        };
    }])
    .filter('htmlToPlaintext', function() {
        return function (text) {
            return String(text).replace(/<[^>]+>/gm, '');
        };
    });

function htmlToPlaintext(text) {
    return String(text).replace(/<[^>]+>/gm, '');
}
