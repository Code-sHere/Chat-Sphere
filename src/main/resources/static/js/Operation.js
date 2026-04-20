
let socket;
let currentReceiver ="";

const username = "[[${#authentication.name}]]";

function connectWebsocket(){

    socket = new socket(
        "ws://localhost:8080/private" + username
    );

    socket.onopen = function(){
        console.log("connected");
    };

    socket.onmessage = function(e){
        console.log("Message received: " + e.data);

        showReceivedMessage(e.data);
    };

    socket.onclose = function(e){
        console.log("Connection closed");
    }

}

connectWebsocket();

function selectUser(user){
    currentReceiver = user;

    console.log("Selected user: " + user);
}

function sendMessage(){
    let input = document.querySelector("input");

    const text = input.value;

    if(!text || !currentReceiver){
        return;
    }

    const message = 
    currentReceiver + ":" + text;

    socket.send(message);
    showSentMessage(text);
    input.value = "";

}

function showSentMessage(text){

    const container = document.querySelector(".overflow-y-auto");

    const div = document.createElement("div");

    div.className =
        "flex justify-end";

    div.innerHTML =
        '<div class="bg-red-600 px-4 py-2 rounded-2xl max-w-xs">'
        + text +
        '</div>';

    container.appendChild(div);

}

function showReceivedMessage(text){

    const container = document.querySelector(".overflow-y-auto");

    const div = document.createElement("div");

    div.className =
        "flex justify-start";

    div.innerHTML = 
        '<div class="bg-gray-600 px-4 py-2 rounded-2xl max-w-xs">' + text + '</div>'

    container.appendChild(div);
}
