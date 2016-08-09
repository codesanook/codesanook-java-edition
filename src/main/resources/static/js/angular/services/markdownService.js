app.factory('markdownService', function ($q, $http) {

    var service = {};
    var markdownConverter = new showdown.Converter({noHeaderId: true});

    service.toHtml = function (markdownContent) {
        if (!markdownContent) return null;

        var htmlContent = markdownConverter.makeHtml(markdownContent);
        var $html = angular.element("<div>" + htmlContent + "</div>");
        $html.find("img").addClass('img-responsive post-image');
        $html.find("pre").addClass('prettyprint linenums');
        var html = $html.html();
        return html;
    };

    return service;

});
