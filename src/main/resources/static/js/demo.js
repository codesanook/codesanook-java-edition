(function () {

    var getIframe = function (videoId) {
        //var iframe =
        //    '<iframe width="560" height="315" ' +
        //    'src="https://www.youtube.com/embed/' +
        //    videoId +
        //    '" ' +
        //    'frameborder="0" allowfullscreen></iframe>';

        var youtubePreview = '<div  class="youtube-preview ' + videoId + '">' +
            'Your video will be shown here.' +
            '</div>';
        return youtubePreview;
    };

    var demo = function (converter) {
        console.log("demo");

        var pattern = "\\^\\[http[s]?://www\\.youtube\\.com/watch\\?v=(.+)\\]";
        return [
            // Replace escaped @ symbols
            {
                type: 'lang',
                regex: pattern,
                replace: getIframe('$1')
            }
        ];
    };

    // Client-side export
    if (typeof window !== 'undefined' && window.showdown && window.showdown.extensions) {
        window.showdown.extensions.demo = demo;
    }
    // Server-side export
    if (typeof module !== 'undefined') module.exports = demo;

}());