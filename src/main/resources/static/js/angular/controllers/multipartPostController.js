app.controller('multipartPostManagerController',
    function ($scope, $http, $element, $document, multipartPostService, postService) {

        $scope.user = window.loggedInUser;

        $scope.searchedMultipartPost = {
            title: null,
            isSearching: false
        };

        $scope.searchedMultipartPostResult = [];

        $scope.newMultipartPost = {
            title: null,
            isSaving: false
        };


        $scope.selectedMultipartPost = {
            id: null,
            title: null,
            multipartPostItems: [],
            isLoading: false,
            isUpdating: false
        };

        $scope.searchedPost = {
            title: null
        };

        $scope.getExistingMultipartPost = function () {
            if (!window.multipartPostId)return;
            $scope.selectMultipartPost(window.multipartPostId);
        };


        $scope.searchMultipartPost = function (form) {
            if (!form.$valid) return;
            form.$setPristine();
            $scope.searchedMultipartPost.isSearching = true;

            multipartPostService.searchedMultipartPost($scope.searchedMultipartPost.title)
                .then(function (response) {
                    $scope.searchedMultipartPostResult = response.data;
                })
                .catch(function (response) {
                    console.error(response);
                }).finally(function () {
                    $scope.searchedMultipartPost.isSearching = false;
                });
        };


        $scope.createMultipartPost = function (form) {
            if (!form.$valid) return;
            form.$setPristine();
            console.log("valid create input");

            $scope.newMultipartPost.isSaving = true;
            multipartPostService.addMultipartPost($scope.newMultipartPost)
                .then(function (response) {
                    $scope.getMultipartPost(response.data);
                    alert("Multipart post created, please go to step 2.");

                }).catch(function (response) {
                    console.error(response);
                    alert("!!! Error creating new multipart post, please try again.");
                }).finally(function () {
                    $scope.newMultipartPost.title = null;
                    $scope.newMultipartPost.isSaving = false;
                });
        };


        $scope.getMultipartPost = function (multipartPostId) {
            $scope.selectedMultipartPost.isLoading = true;

            multipartPostService.getMultipartPost(multipartPostId)
                .then(function (response) {
                    $scope.selectedMultipartPost.id = response.data.id;
                    $scope.selectedMultipartPost.title = response.data.title;
                    $scope.selectedMultipartPost.multipartPostItems =
                        response.data.multipartPostItems;
                })
                .catch(function (response) {
                    console.log(response);
                })
                .finally(function () {
                    $scope.selectedMultipartPost.isLoading = false;
                    $scope.searchedMultipartPostResult = [];
                    $scope.searchedMultipartPost.title = null;
                });
        };


        $scope.selectMultipartPost = function (multipartPostId) {
            console.log("multipartPostId %d", multipartPostId);
            $scope.getMultipartPost(multipartPostId);
        };


        $scope.sortableOptions = {
            update: function (e, ui) {
            },
            stop: function (e, ui) {
                console.log("stop");

                var newOrderIndexs = $scope.selectedMultipartPost.multipartPostItems.map(function (item) {
                    return item.post.id;
                });

                console.log("%o", newOrderIndexs);

                for (var index = 0; index < $scope.selectedMultipartPost.multipartPostItems.length; index++) {
                    var item = $scope.selectedMultipartPost.multipartPostItems[index];
                    item.orderIndex = newOrderIndexs.indexOf(item.post.id);
                }

            }
        };


        $scope.searchPost = function (form) {
            if (!form.$valid) return;
            form.$setPristine();
            console.log("valid search input");

            $scope.searchedPost.isLoading = true;
            postService.searchPost($scope.searchedPost.title)
                .then(function (response) {
                    $scope.searchedPostResult = response.data;
                    $scope.searchedPost.title = null;
                    $scope.searchedPost.isLoading = false;
                })
                .catch(function (response) {
                    console.error(response);
                });
        };


        $scope.getLastOrderIndex = function () {

            var orderIndexs = $scope.selectedMultipartPost.multipartPostItems.map(function (item) {
                return item.orderIndex;
            });

            console.log("orderIndexs.length %d", orderIndexs.length);
            if (orderIndexs.length == 0)return -1;
            if (orderIndexs.length == 1) return 0;

            orderIndexs.sort(function (a, b) {
                return a - b;
            });
            return orderIndexs[orderIndexs.length - 1];
        };


        $scope.addToMultipartPostItem = function (post) {
            var lastOrderIndex = $scope.getLastOrderIndex();

            var multipartPostItem = {
                post: post,
                user: {
                    id: $scope.user.id
                },
                orderIndex: lastOrderIndex + 1
            };
            $scope.selectedMultipartPost.multipartPostItems.push(multipartPostItem);
            $scope.searchedPostResult = [];
        };


        $scope.removeMultipartPostItem = function (multipartPostItem) {
            console.log("post.id %d", multipartPostItem.post.id);
            var foundIndex = $scope.indexOf(multipartPostItem);
            if (foundIndex >= 0) {
                $scope.selectedMultipartPost.multipartPostItems.splice(foundIndex, 1);

                //update index
                for (var startIndex = foundIndex;
                     startIndex < $scope.selectedMultipartPost.multipartPostItems.length;
                     startIndex++) {
                    var item = $scope.selectedMultipartPost.multipartPostItems[startIndex];
                    item.orderIndex--;
                }
            }
        };


        $scope.updateMultipartPost = function (form) {
            if (!form.$valid)return;

            $scope.selectedMultipartPost.isUpdating = true;
            console.log("updateMultipartPost");
            multipartPostService.updateMultipartPost($scope.selectedMultipartPost)
                .then(function (response) {

                })
                .catch(function (response) {
                    console.log("catch block");
                    console.log(response);
                    if (response.data.exception == 'DataIntegrityViolationException') {
                        alert("!!! Failed update, one post in list belongs to other multipart post");
                    }
                }).finally(function (response) {
                    console.log("finally %o", response);
                    $scope.selectedMultipartPost.isUpdating = false;
                });
        };


        $scope.indexOf = function (multipartPostItem) {

            var multipartPostItemIds = $scope.selectedMultipartPost.multipartPostItems.map(function (item) {
                return item.post.id;
            });


            var foundIndex = multipartPostItemIds.indexOf(multipartPostItem.post.id);
            return foundIndex;
        };


        $scope.closeModal = function () {

            var multipartPostModal = $document.find("#multipartPostModal");
            multipartPostModal.modal('hide');
            var waitingModal = $document.find(".waiting-modal");
            waitingModal.modal('show');
            location.href = "/";



            return;
        };

        $scope.getExistingMultipartPost();

    });


