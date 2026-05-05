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

        if(message.startsWith("System:")){
            const text = message.replace("System:","");
            showSystemMessage(text);
        }else if(message.startsWith("Chat:")){
            const parts = message.replace("Chat:", "").split(": ");
            const sender = parts[0];
            const text = parts.slice(1).join(": ");

            if(sender === username){
                ShowSentMessages(text);
            }else{
                showReceivedMessage(text);
            }
            
        }
    }

    socket.onclose = function () {
        console.log("Disconnected from group chat");
    }

}


function showSystemMessage(text){

    const container = document.getElementById("messageContainer");

    const div = document.createElement("div");
    
    div.className = "flex justify-center my-2";

    div.innerHTML = `
    <div class="text-white px-4 py-2 bg-transparent text-xs border border-none opacity-75">
        ${text}
    </div>`

    container.appendChild(div);
    container.scrollTop = container.scrollHeight;

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
    input.value = "";
}

function ShowSentMessages(text) {

    const container = document.getElementById("messageContainer");

    const div = document.createElement("div");

    div.className = "flex justify-end";

    div.innerHTML = `
        <div class = "sent-msg text-white px-4 py-2 rounded-2xl max-w-xs">
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
        <div class="received-msg bg-gray-600 text-white px-4 py-2 rounded-2xl max-w-xs">
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

    const response = await fetch("/api/group/create", {
        method: "POST"
    });

    // DEBUG: see what backend returns
    const text = await response.text();
    console.log("Server response:", text);

    // check if HTML came
    if (text.startsWith("<!DOCTYPE")) {
        alert("ERROR: Backend returned HTML, not roomId");
        return;
    }

    alert("Group created with ID: " + text);

    openGroupPage(text);
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