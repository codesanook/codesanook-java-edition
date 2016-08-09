app.controller('chatIndexController', function ($scope, $http, $sanitize) {

    $scope.username = "";//must be with domain
    $scope.password = "";
    $scope.logOutput = "";
    $scope.connection = null;
    $scope.allChatRooms = [];
    $scope.connectionStatus = null;

    $scope.log = function (message) {
        console.log("logging new message");
        $scope.logOutput += sprintf("<br/>%s", message);
        console.log("logOutput %s", $scope.logOutput);
    };

    $scope.connectServer = function () {
        if ($scope.connectionStatus === Strophe.Status.CONNECTED) return;

        console.log($pres().toString());
        var serverUrl = "http://codesanook.com:7070/http-bind/";
        $scope.connection = new Strophe.Connection(serverUrl);
        $scope.connection.connect($scope.username, $scope.password, $scope.connectServerCallback);
    };

    $scope.disconnectServer = function () {
        $scope.connection.disconnect();
        $scope.connectionStatus = null;
    };

    $scope.pingHandler = function (iq) {
        console.log("from handler %o", iq);
        $scope.$apply(function () {
            $scope.log("got ping result ok");
        });
        return true;//still connect
    };

    $scope.presenceHandler = function (presenceResponse) {
        console.log("presenceHandler")
        if (console.dirxml)console.dirxml(presenceResponse);
        //Strophe.getBareJidFromJid

        var presence = $(presenceResponse);
        var pType = presence.attr('type');
        if (pType === 'error') return true;

        var from = presence.attr('from');
        var fromJid = Strophe.getBareJidFromJid(from);
        console.log(fromJid);

        var isOnline = pType !== "unavailable";
        var foundAtIndex = $scope.searchChatRoomIndex(fromJid);
        if (foundAtIndex >= 0) {
            $scope.delay(function () {
                $scope.$apply(function() {
                    var chatRoom = $scope.allChatRooms[foundAtIndex];
                    chatRoom.isOnline = isOnline;
                });
            });
        }

        return true;//reuse
    };

    $scope.connectServerCallback = function (status) {
        console.log("status code %o, get status %o", Strophe.Status.CONNECTED, status);
        switch (status) {
            case Strophe.Status.CONNECTED:
                console.log("connected");
                var handler = $scope.pingHandler;
                var ns = null;
                var name = "iq";
                var type = null;
                var id = "ping1";
                var from = null;
                var options = null;
                $scope.connection.addHandler(handler, ns, name, type, id, from, options);
                $scope.connection.addHandler($scope.presenceHandler, null, 'presence');


                $scope.$apply(function () {
                    $scope.log("connected");
                    $scope.getAllChatRooms();
                    $scope.connection.send($pres());
                });
                break;

            case Strophe.Status.DISCONNECTED:
                $scope.$apply(function () {
                    $scope.log("disconnected");
                });
                break;

            case Strophe.Status.CONNECTING:
                $scope.log("connecting");
                break;

            default :
                console.log("error %o", status);
                break;
        }
    };


    $scope.sendPing = function () {
        var to = Strophe.getDomainFromJid($scope.connection.jid);
        console.log("to %s", to);

        var ping = $iq({to: to, type: 'get', id: 'ping1'})
            .c("ping", {xmlns: "urn:xmpp:ping"});
        console.log("send ping xml %s", ping.toString());
        $scope.connection.send(ping);
    };


    $scope.closeTab = function (event, chatRoom) {
        console.log("closing tab chatRoom jid %d", chatRoom.jid);
        event.preventDefault();
        event.stopPropagation();

        chatRoom.isActive = false;
        var activeInChatRoomCount = 0;
        for (var i = 0; i < $scope.allChatRooms.length; i++) {
            var currentChatRoom = $scope.allChatRooms[i];
            if (currentChatRoom.isActive) {
                activeInChatRoomCount++;
            }
        }

        if (activeInChatRoomCount == 0) {
            $scope.delay(function () {
                $("#linkChatRooms").tab('show');
            });
        }

    };

    $scope.activateChatRoom = function (chatRoom) {
        chatRoom.isActive = true;
        $scope.delay(function () {
            $("#linkTab" + chatRoom.jid).tab('show');
        });
    };

    $scope.searchChatRoomIndex = function (chatRoomJid) {

        var foundAtIndex = -1;
        for (var i = 0; i < $scope.allChatRooms.length; i++) {
            var currentItem = $scope.allChatRooms[i];
            if (currentItem.jid === chatRoomJid) {
                foundAtIndex = i;
                return foundAtIndex;
            }
        }

        return foundAtIndex;
    }

    $scope.delay = function (delayExpression) {

        var delay = 100;
        window.setTimeout(function () {
            console.log("delay of %d milliseconds", delay);
            delayExpression();
        }, delay);

    };

    $scope.sortChatRoom = function () {

    };

    $scope.addChatRooms = function (iq) {
        if (console.dirxml)console.dirxml(iq);
        $(iq).find('item').each(function (index, element) {
            var jid = $(element).attr('jid');
            var name = $(element).attr('name');
            name = name ? name : jid;

            var foundAtIndex = $scope.searchChatRoomIndex(jid);
            console.log("found at index %d", foundAtIndex);
            if (foundAtIndex === -1) {
                var chatRoom = {jid: jid, name: name, content: ""};
                $scope.allChatRooms.push(chatRoom);
            }
        });

        console.log("$scope.allChatRooms.length = %d", $scope.allChatRooms.length);
        $scope.delay(function () {
            $scope.$apply();
        });
    };


    $scope.getAllChatRooms = function () {
        var iq = $iq({type: 'get'}).c('query', {xmlns: 'jabber:iq:roster'});
        $scope.connection.sendIQ(iq, $scope.addChatRooms);
    };


    $scope.activeChatRoomFilter = function (chatRoom) {
        return chatRoom.isActive;
    };


});//end controller
