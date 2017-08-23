var ws;
var pendingGetValueRequestId;
var pendingStatsRequestId;
var pendingPutRequestId;

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}

function sendRequest(method, params) {
    var request = {
        "id": guid(),
        "method": method,
        "params": params
    };
    var json = JSON.stringify(request);
    ws.send(json);
    console.log(json);
    return request;
}

function requestStats() {
    pendingStatsRequestId = sendRequest("stats", null).id;
}

init = function () {
    ws = new WebSocket("ws://localhost:8090/cache/websocket");
    ws.onopen = function (event) {
        console.log("Opened", event.data)
    }
    ws.onmessage = function (event) {
        console.log("Message", event.data)
        var response = JSON.parse(event.data);
        if (response.id == pendingGetValueRequestId) {
            pendingGetValueRequestId = null;
            var value = response.result;
            if (null != value) {
                document.getElementById("inputReadValue").value = value;
            } else {
                document.getElementById("inputReadValue").value = "<null>";
            }
            requestStats();
        } else if (response.id == pendingStatsRequestId) {
            pendingStatsRequestId = null;
        } else if (response.id == pendingPutRequestId) {
            pendingPutRequestId = null;
            requestStats();
        }
    }
    ws.onclose = function (event) {
       console.log("Close", event.data)
    }

    document.getElementById("btnNewValue")
            .addEventListener("click", function (event) {
        var $inputNewKey = document.getElementById("inputNewKey");
        var $inputNewValue = document.getElementById("inputNewValue");
        pendingPutRequestId = sendRequest("add", {
            "key": $inputNewKey.value,
            "value": $inputNewValue.value
        }).id;
    });

    document.getElementById("btnReadValue").addEventListener("click", function (event) {
        document.getElementById("inputReadValue").value = "";
        var $inputReadKey = document.getElementById("inputReadKey");
        pendingGetValueRequestId = sendRequest("get", {
            "key": $inputReadKey.value,
        }).id;
    });

};