// Karma configuration
// Generated on Thu Feb 18 2016 23:42:42 GMT+0700 (SE Asia Standard Time)

module.exports = function (config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '',

        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],


        // list of files / patterns to load in the browser
        //include jQuery

        files: [
            'showdown.min.js',
            'jquery-2.2.0.min.js', //full selector option
            'sprintf.js',
            'angular-1.4.8/angular.min.js',
            'angular-1.4.8/angular-mocks.js',

            'angular-1.4.8/angular-sanitize.min.js',
            'angular-1.4.8/angular-local-storage.min.js',
            'angular-1.4.8/sortable.min.js',
            'angular-toastr/angular-toastr.tpls.js',
            //'angular-toastr/*',

            'angular/**/*.js',
            'spec/**/*.js',
            'angular/templates/**/*.html'
        ],


        // list of files to exclude
        exclude: [],

        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        //*** 1
        preprocessors: {
            'angular/templates/**/*.html': ['ng-html2js']
        },

        ngHtml2JsPreprocessor: {
            cacheIdFromPath: function (filepath) {
                // example strips 'public/' from anywhere in the path
                // module(app/templates/template.html) => app/public/templates/template.html
                var buildNumber = 1;
                var cacheId = "/js/" + filepath + '?bno=' + buildNumber;
                return cacheId;
            },
            moduleName: 'ngTemplates'
        },


        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['progress'],


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'],


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false,

        // Concurrency level
        // how many browser should be started simultaneous
        concurrency: Infinity
    })
};

/*
 http://stackoverflow.com/questions/17230242/angular-element-vs-document-getelementbyid-or-jquery-selector-with-spin-busy-c

 You should read the angular element docs if you haven't yet, so you can understand what is supported by jqLite and what not -jqlite is a subset of jquery built into angular.

 Those selectors won't work with jqLite alone, since selectors by id are not supported.

 var target = angular.element('#appBusyIndicator');
 var target = angular.element('appBusyIndicator');

 So, either :

 you use jqLite alone, more limited than jquery, but enough in most of the situations.
 or you include the full jQuery lib in your app, and use it like normal jquery, in the places that you really need jquery.

 Edit: Note that jQuery should be loaded before angularJS in order to take precedence over jqLite:

 Real jQuery always takes precedence over jqLite, provided it was loaded before DOMContentLoaded event fired.

 Edit2: I missed the second part of the question before:

 The issue with <input type="number"> , I think it is not an angular issue, it is the intended behaviour of the native html5 number element.

 It won't return a non-numeric value even if you try to retrieve it with jquery's .val() or with the raw .value attribute.


 */
