app.factory('contentService', function (localStorageService, $sce) {

    var service = {};
    var markdownConverter = new showdown.Converter(
        {
            noHeaderId: true,
            extensions: ['youtube', 'demo']
        }
    );

    service.toHtml = function (markdownContent) {
        if (!markdownContent) return null;

        var htmlContent = markdownConverter.makeHtml(markdownContent);

        var $html = angular.element("<div>" + htmlContent + "</div>");
        $html.find("img").addClass('img-responsive post-image');
        $html.find("pre").addClass('prettyprint linenums');
        var html = $html.html();
        console.log("html : " + html);

        return html;
    };
    service.getStorageService = function () {
        return localStorageService;
    };

    service.saveToStorage = function (key, value) {
        return localStorageService.set(key, value);
    };

    service.getFromStorage = function (key) {
        return localStorageService.get(key);
    };

    service.removeFromStorage = function (key) {
        return localStorageService.remove(key);
    };

    return service;
});

