describe('imageService', function () {

    var imageService;
    var files;
    beforeEach(function () {

        files = [];
        angular.mock.module("codesanook");
        angular.mock.inject(function (_imageService_) {
            imageService = _imageService_;
        });

    });

    it('to be defined', function () {
        expect(imageService).toBeDefined();
    });


    it('files set', function () {
        var uploadedFile = {
            id: 1,
            contextReferenceId: 0,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "https://s3/aa.jpg",
            width: 500,
            height: 400
        };

        imageService.addOrUpdateUploadedFile(files, uploadedFile);
        expect(files.length).toBe(1);
        expect(files[0].contextReferenceId).toBe(1);
    });


    it('set 1 existing file', function () {
        files = [
            {
                id: 1,
                contextReferenceId: 1,
                name: "aa.jpg",
                relativePath: "aa.jpg",
                absoluteUrl: "https://s3/aa.jpg",
                width: 500,
                height: 400
            }];

        var uploadedFile = {
            id: 2,
            contextReferenceId: 0,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "https://s3/aa.jpg",
            width: 500,
            height: 400
        };

        imageService.addOrUpdateUploadedFile(files, uploadedFile);
        expect(files.length).toBe(2);
        expect(files[0].contextReferenceId).toBe(1);
        expect(files[1].contextReferenceId).toBe(2);


    });


});


describe('imageService removeFile', function () {

    var imageService;
    var files;
    var file1;
    var file2;
    var file3;
    var file4;

    beforeEach(function () {

        files = [];
        angular.mock.module("codesanook");
        angular.mock.inject(function (_imageService_) {
            imageService = _imageService_;
        });


        file1 =
        {
            id: 1,
            contextReferenceId: 1,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "https://s3/aa.jpg",
            width: 500,
            height: 400
        };

        file2 =
        {
            id: 2,
            contextReferenceId: 2,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "https://s3/aa.jpg",
            width: 500,
            height: 400

        };

        file3 =
        {
            id: 3,
            contextReferenceId: 3,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "https://s3/aa.jpg",
            width: 500,
            height: 400
        };

        file4 = {
            id: 4,
            contextReferenceId: 4,
            name: "aa.jpg",
            relativePath: "aa.jpg",
            absoluteUrl: "https://s3/aa.jpg",
            width: 500,
            height: 400
        };

        files = [file1, file2, file3, file4];

    });

    it('remove first file', function () {

        imageService.removeFile(files, file1);
        expect(files.length).toBe(3);
        expect(file2.contextReferenceId).toBe(1);
        expect(file3.contextReferenceId).toBe(2);
        expect(file4.contextReferenceId).toBe(3);

        expect(imageService.latestContextReferenceId(files)).toBe(3);
    });


    it('remove first file', function () {

        imageService.removeFile(files, file2);
        expect(files.length).toBe(3);
        expect(file1.contextReferenceId).toBe(1);
        expect(file3.contextReferenceId).toBe(2);
        expect(file4.contextReferenceId).toBe(3);
        expect(imageService.latestContextReferenceId(files)).toBe(3);
    });

    it('remove first file', function () {
        imageService.removeFile(files, file3);
        expect(files.length).toBe(3);
        expect(file1.contextReferenceId).toBe(1);
        expect(file2.contextReferenceId).toBe(2);
        expect(file4.contextReferenceId).toBe(3);
        expect(imageService.latestContextReferenceId(files)).toBe(3);
    });


    it('remove first file', function () {
        imageService.removeFile(files, file4);
        expect(files.length).toBe(3);
        expect(file1.contextReferenceId).toBe(1);
        expect(file2.contextReferenceId).toBe(2);
        expect(file3.contextReferenceId).toBe(3);
        expect(imageService.latestContextReferenceId(files)).toBe(3);
    });


    it('remove first file', function () {
        var file5 = {id: 5};
        imageService.removeFile(files, file5);
        expect(files.length).toBe(4);
        expect(file1.contextReferenceId).toBe(1);
        expect(file2.contextReferenceId).toBe(2);
        expect(file3.contextReferenceId).toBe(3);
        expect(file4.contextReferenceId).toBe(4);
        expect(imageService.latestContextReferenceId(files)).toBe(4);
    });


});
