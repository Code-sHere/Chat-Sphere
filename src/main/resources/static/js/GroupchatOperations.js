let socket;
let username = "";
let currentSGroupId = "";


async function getUsername() {
    const res = await fetch("/current-user");
    const data = await res.json();
    username = data.username;
    console.log("Logged in:", username);
}

async function connectWebSocket(groupId) {
   
    currentSGroupId = groupId;

    await getUsername();

    socket = new WebSocket(
        `ws://localhost:8080/group/${groupId}/${username}`
    );

    socket.onopen = function () {
        console.log("connected to group chat");
    }

    socket.onmessage = function (event) {

        const message = event.data;

        showReceivedMessage(message);
    }

    socket.onclose = function () {
        console.log("Disconnected from group chat");
    }

}

function sendMessage(groupId, message) {
    const input = document.getElementById("messageInput");

    const text = input.value.trim();

    if (!text) {
        alert("Message cannot be empty");
        return;
    }

    if (!socket || socket.readyState !== WebSocket.OPEN) {
        alert("WebSocket not connected");
        return;
    }

    socket.send(text);

    ShowSentMessages(text);
    input.value = "";
}

function ShowSentMessages(text) {

    const container = document.getElementById("messageContainer");

    const div = document.createElement("div");

    div.className = "flex justify-end";

    div.innerHTML = `
        <div class = "bg-blue-600 text-white px-4 py-2 rounded-2xl max-w-xs">
        ${text}
        </div>`

    container.appendChild(div);

    container.scrollTop = container.scrollHeight;
}

function showReceivedMessage(text) {



    const container =
        document
            .getElementById(
                "messageContainer"
            );

    const div =
        document
            .createElement(
                "div"
            );

    div.className =
        "flex justify-start";

    div.innerHTML =
        `
        <div class="bg-gray-600 text-white px-4 py-2 rounded-2xl max-w-xs">
            ${text}
        </div>
        `;

    container.appendChild(
        div
    );

    container.scrollTop =
        container.scrollHeight;

}

async function createRoom(){
    const response = await fetch("/api/group/create",{
        method:"POST"
    });

    const roomId = await response.text();

    alert("Group created with ID: " + roomId);
    openGroupPage(roomId);

}

function joinRoom(){
    const roomId = document.getElementById("roomIdInput").value;

    if(!roomId){
        alert("Please enter a valid room ID");
        return;
    }

    openGroupPage(roomId);
}

function openGroupPage(roomId) {

    window.location.href =
        `/group?roomId=${roomId}`;
}

function leaveRoom(){
    if(socket){
        socket.close();
    }
    alert("You have left the group chat");
    window.location.href = "/private";
}   

window.onload = async function(){

    const params = new URLSearchParams(window.location.search);

    const roomId = params.get("roomId");

    if(roomId){

        await connectWebSocket(roomId);

        document.getElementById("roomIdDisplay").innerText = "Group ID: " + roomId;
    }
}