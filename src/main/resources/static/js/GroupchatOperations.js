let socket;
let username = "";
let currentSGroupId = "";
let typingTimeout;


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
        if (message.startsWith("Typing:")) {
            const sender = message.replace("Typing:", "").trim();

            if (sender !== username) {
                showTypingIndicator(sender);
            }
            return;
        }
        else if (message.startsWith("System:")) {
            const text = message.replace("System:", "");
            showSystemMessage(text);
        }

        else if (message.startsWith("Chat:")) {
            const parts = message.replace("Chat:", "").split(": ");
            const sender = parts[0];
            const text = parts.slice(1).join(": ");
            const time = new Date().toLocaleTimeString();

            if (sender === username) {
                ShowSentMessages("You: " + text, time);
            } else {
                showReceivedMessage(sender, text, time);
            }

        }

        try {

            const data = JSON.parse(message);

           if(data.type === "IMAGE" || data.type === "VIDEO" || data.type === "AUDIO" || data.type === "FILE"){
               showAttachment(data);
           }

        } catch (error) {
            console.log(error);

        }


    }

    socket.onclose = function () {
        console.log("Disconnected from group chat");
    }

}


function showSystemMessage(text) {

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

function ShowSentMessages(text, time) {

    const container = document.getElementById("messageContainer");

    const div = document.createElement("div");

    div.className = "flex justify-end";

    div.innerHTML = `
        <div class = "sent-msg text-white px-4 py-2 rounded-2xl max-w-xs">
        ${text}
        </div> 
        <div class="text-[10px] text-gray-300 text-right mt-1">
                You • ${time}
            </div>`

    container.appendChild(div);

    container.scrollTop = container.scrollHeight;
}

function showReceivedMessage(sender, text, time) {

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

            <div class="font-semibold text-green-300 text-sm">
                ${sender}
            </div>

            <div class="mt-1 text-sm">
                ${text}
            </div>

            <div class="text-[10px] text-gray-300 text-right mt-1">
                ${time}
            </div>

        </div>
        `;

    container.appendChild(
        div
    );

    container.scrollTop =
        container.scrollHeight;

}

async function createRoom() {

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

function joinRoom() {
    const roomId = document.getElementById("roomIdInput").value;

    if (!roomId) {
        alert("Please enter a valid room ID");
        return;
    }

    openGroupPage(roomId);
}

function openGroupPage(roomId) {

    window.location.href =
        `/group?roomId=${roomId}`;
}

function leaveRoom() {
    if (socket) {
        socket.close();
    }
    alert("You have left the group chat");
    window.location.href = "/private";
}

window.onload = async function () {

    const params = new URLSearchParams(window.location.search);

    const roomId = params.get("roomId");

    const input = document.getElementById("messageInput");

    input.addEventListener("input", () => {
        if (socket && socket.readyState === WebSocket.OPEN) {

            socket.send("Typing:");

        }
    });

    if (roomId) {

        await connectWebSocket(roomId);

        document.getElementById("roomIdDisplay").innerText = "Group ID: " + roomId;
    }
}

function showTypingIndicator(user) {

    const header = document.getElementById("typingStatus");

    let dots = 0;

    clearInterval(window.typingAnim);

    window.typingAnim = setInterval(() => {
        dots = (dots + 1) % 4;
        header.innerText = user + " is typing" + ".".repeat(dots);
    }, 300);

    clearTimeout(window.typingTimer);

    window.typingTimer = setTimeout(() => {
        clearInterval(window.typingAnim);
        header.innerText = "Chat with " + user;
    }, 1200);
}


function getFileType(file) {
    if (file.type.startsWith("image/")) {
        return "IMAGE";
    }
    if (file.type.startsWith("video/")) {
        return "VIDEO";
    }
    if (file.type.startsWith("audio/")) {
        return "AUDIO";
    }
    return "FILE";
}

async function uploadAttachment() {
    const file = document.getElementById("fileInput").files[0];

    if (!file) return;

    const formData = new FormData();

    formData.append("file", file);

    const response = await fetch("/upload", {
        method: "POST",
        body: formData
    });

    const data = await response.json();

    const attachment = {
        type: getFileType(file),
        fileName: data.fileName,
        fileUrl: data.fileUrl,
        fileType: data.fileType,

        sender: username,

        time : new Date().toLocaleTimeString([],{
            hour: '2-digit',
            minute: '2-digit'
        })
    };

    socket.send(JSON.stringify(attachment));
    console.log("Uploaded:", attachment);

}


function showAttachment(data) {

    const container =
        document.getElementById("messageContainer");

    const div =
        document.createElement("div");

    div.className = data.sender === username
        ? "flex justify-end my-2"
        : "flex justify-start my-2";

    let content = "";

    if (data.type === "IMAGE") {

        content = `
            <img src="${encodeURI(data.fileUrl)}"
                 class="w-64 rounded-lg object-cover">
        `;

    } else if (data.type === "VIDEO") {

        content = `
            <video controls
                   class="w-64 rounded-lg">
                <source src="${data.fileUrl}">
            </video>
        `;

    } else if (data.type === "AUDIO") {

        content = `
            <audio controls class="w-64">
                <source src="${data.fileUrl}">
            </audio>
        `;

    } else {

        content = `
            <a href="${data.fileUrl}"
               target="_blank"
               class="text-blue-400 underline">
               ${data.fileName}
            </a>
        `;
    }

    div.innerHTML = `
        <div class="bg-gray-700 text-white p-3 rounded-2xl max-w-xs">

            <div class="font-semibold text-green-300 text-sm">
                ${data.sender}
            </div>

            <div class="mt-2">
                ${content}
            </div>

            <div class="text-[10px] text-gray-300 text-right mt-2">
                ${data.time}
            </div>

        </div>
    `;

    container.appendChild(div);

    container.scrollTop =
        container.scrollHeight;
}