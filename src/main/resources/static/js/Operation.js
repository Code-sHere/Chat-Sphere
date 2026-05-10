let socket;
let currentReceiver = "";
let onlineUsers = [];
let username = "";
let userMap = {};
let chatHistory = {};


const emojiBtn = document.getElementById("emojiBtn");
const pickerContainer = document.getElementById("emojiPickerContainer");
const messageInput = document.getElementById("messageInput");

emojiBtn.addEventListener("click", () => {
    pickerContainer.classList.toggle("hidden");
});

document
    .querySelector("emoji-picker")
    .addEventListener("emoji-click", event => {

        messageInput.value += event.detail.unicode;

        messageInput.focus();
    });


async function getUsername() {
    try {
        const res = await fetch("/current-user");

        data = await res.json();
        username = data.email;

        console.log("Logged in:", username);

        connectWebsocket();

        loadAllUsers();

        loadChats();

    } catch (error) {
        console.error("Error fetching username:", error);
    }
}

getUsername();


function connectWebsocket() {

    if (!username) {
        console.error("Username not set");
        return;
    }

    socket = new WebSocket(
        "ws://localhost:8080/private/" + username
    );

    socket.onopen = function () {
        console.log("Connected", username);

        if (currentReceiver) {
            loadMessages(currentReceiver);
        }

        loadAllUsers();
    };

    socket.onmessage = function (event) {

        const message =
            event.data;

        console.log("Received:", message);

        if (message.startsWith("Typing:")) {

            const sender = message.replace("Typing:", "").trim();

            if (sender === currentReceiver) {
                showTypingIndicator(sender);
            }
            return;
        }

        if (
            message.startsWith(
                "Online users:"
            )
        ) {

            onlineUsers =
                message
                    .replace(
                        "Online users:",
                        ""
                    )
                    .split(",")
                    .map(u => u.trim());

            loadAllUsers();

            return;
        }

        const parts = message.split(":", 2);

        if (parts.length === 2) {
            const sender = parts[0];
            const text = parts[1];

            if (sender === currentReceiver) {
                showReceivedMessage(text);
            } else {
                // Store the message in chat history
                if (!chatHistory[sender]) {
                    chatHistory[sender] = [];
                }
                chatHistory[sender].push({
                    type: "received",
                    text: text
                });

            }
        }

        try {

            const data = JSON.parse(message);

            if (data.type === "IMAGE") {

                showImage(data.fileUrl);

            } else if (data.type === "VIDEO") {

                showVideo(data.fileUrl);

            } else if (data.type === "AUDIO") {

                showAudio(data.fileUrl);

            } else if (data.type === "FILE") {

                showFile(
                    data.fileUrl,
                    data.fileName
                );
            }

        } catch (error) {

            console.log("Normal text message");
        }

    };

    socket.onclose = function () {
        console.log(
            "Connection closed"
        );
    };

}


function selectUser(email, chatId) {

    currentReceiver = email;

    document.getElementById("chatHeader").innerText =
        "Chat with " + email;

    const container = document.getElementById("messageContainer");
    container.innerHTML = "";

    if (!chatHistory[email]) {
        chatHistory[email] = [];
    }

    if (chatHistory[email].length === 0) {
        loadMessages(email);
    }

    chatHistory[email].forEach(msg => {
        if (msg.type === "sent") {
            showSentMessage(msg.text);
        } else {
            showReceivedMessage(msg.text);
        }
    });

    if (chatId !== undefined && chatId !== null) {
        seenMessage(chatId);
    }
}

function sendMessage() {

    const input =
        document
            .getElementById(
                "messageInput"
            );

    const text =
        input.value.trim();


    if (!currentReceiver) {

        alert(
            "Select user first"
        );

        return;
    }

    if (!socket ||
        socket.readyState !==
        WebSocket.OPEN
    ) {

        alert(
            "Socket not connected"
        );

        return;
    }

    const message =
        currentReceiver +
        ":" +
        text;

    socket.send(
        message
    );

    if (!chatHistory[currentReceiver]) {
        chatHistory[currentReceiver] = [];
    }

    chatHistory[currentReceiver].push({
        type: "sent",
        text: text
    });


    showSentMessage(text,);

    input.value = "";

}

messageInput.addEventListener("input", () => {
    sendTyping();
});

messageInput.addEventListener("keypress", (event) => {
    if (event.key === "Enter") {
        sendMessage();
    }
});


async function seenMessage(chatId) {
    const response = await fetch(`/message/seen?chatId=${chatId}`, {
        method: "PUT"
    });

    const container = document.getElementById("messageContainer");

    container.innerHTML = "";

    chatHistory[currentReceiver] = [];

    loadMessages(currentReceiver);

}

function showSentMessage(text, seen = false) {

    const container = document.getElementById("messageContainer");

    const status = document.querySelector(".message-status");

    if (status) {
        status.remove();
    }

    const div = document.createElement("div");

    div.className = "flex felx-col items-end mb-2";

    div.innerHTML = `
        <div class="sent-msg text-white px-4 py-2 rounded-2xl max-w-xs">
            ${text}
        </div>

        <span class="message-status text-xs text-gray-400 mt-1">${seen ? "Seen" : "Sent"}</span>
        `;

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
        <div class="received-msg text-white px-4 py-2 rounded-2xl max-w-xs">
            ${text}
        </div>
        `;

    container.appendChild(
        div
    );

    container.scrollTop =
        container.scrollHeight;

}

async function loadAllUsers() {

    const response = await fetch("/users");
    const users = await response.json();

    console.log("Users from API:", users);
    console.log("Logged in:", username);
    console.log("Online:", onlineUsers);

    renderUserList(users);
}


function renderUserList(users) {

    const userList = document.getElementById("userList");
    userList.innerHTML = "";

    users.forEach(user => {

        if (user.email === username) return;

        const isOnline = onlineUsers.includes(user.email);

        const dot = isOnline ? "bg-green-500" : "bg-red-400";

        const preview = lastMessagePreview(chatHistory[user.email]);

        const div = document.createElement("div");

        div.className = "user-card flex items-center gap-3 p-3 cursor-pointer";

        div.onclick = () => selectUser(user.email, user.chatId);

        div.innerHTML = `
            <div class="flex flex-col flex-1 rounded-lg p-2 bg-transparent">

                <div class="flex items-center gap-2">
                    <span class="status-dot ${dot} w-3 h-3 rounded-full"></span>
                    <span class="font-semibold">
                        ${user.username}
                    </span>
                </div>

                <div class="flex items-center gap-2 mt-1"> 
                    <span class="user-preview text-sm text-[#9aa4c3] truncate max-w-[150px]">
                        ${preview}
                    </span> 
                </div>

            </div>
        `;

        userList.appendChild(div);
    });
}

function searchUser() {

    const keyword =
        document
            .getElementById(
                "searchUser"
            )
            .value
            .toLowerCase();

    const users =
        document
            .querySelectorAll(
                "#userList div"
            );

    users.forEach(
        user => {

            const text =
                user.innerText
                    .toLowerCase();

            user.style.display =
                text.includes(
                    keyword
                )
                    ? "flex"
                    : "none";

        }

    );

}


async function loadMessages(user) {

    const response =
        await fetch(
            `/messages?sender=${username}&receiver=${user}`
        );

    const messages =
        await response.json();

    if (!chatHistory[user]) {
        chatHistory[user] = [];
    }

    messages.forEach(msg => {

        if (msg.senderId === username) {

            chatHistory[user].push({
                type: "sent",
                text: msg.messageText,
                seen: msg.seen
            });

            showSentMessage(msg.messageText, msg.seen);

        } else {

            chatHistory[user].push({
                type: "received",
                text: msg.messageText
            });

            showReceivedMessage(msg.messageText);

        }

    });

}


async function loadChats() {

    const response =
        await fetch(
            `/chat/chats?email=${username}`
        );

    const chats =
        await response.json();

    console.log("Chats:", chats);

    Object.keys(chatHistory).forEach(chat => {
        const otherUser = chat.chatName.replace(username, "").replace("_", "");

        if (otherUser) {
            loadMessages(otherUser);
        }
    })

}


loadChats();

function lastMessagePreview(chat) {
    if (!chat || !chat.messages || chat.messages.length === 0) return "";

    const lastMsg = chat[chat.length - 1];

    if (!lastMsg) return "";

    return lastMsg.text.length > 30 ? lastMsg.text.substring(0, 30) + "..." : lastMsg.text;
}

function Groups() {
    window.location.href = "/group";
}


function sendTyping() {
    if (socket && socket.readyState === WebSocket.OPEN && currentReceiver) {
        socket.send("Typing:" + currentReceiver);
    }
}

function showTypingIndicator(user) {

    const header = document.getElementById("chatHeader");

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
    }, 800);
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
        fileType: data.fileType
    };

    socket.send(JSON.stringify(attachment));
    console.log("Uploaded:", attachment);
}

function showImage(url) {
    const container = document.getElementById("messageContainer");

    const div = document.createElement("div");

    div.innerHTML = `<img src="${encodeURI(url)}" class="w-64 rounded-lg object-cover">`;

    container.appendChild(div);

    container.scrollTop = container.scrollHeight;
}

function showVideo(url) {

    const video = document.createElement("video");

    video.src = url;

    video.controls = true;

    video.className = "w-64 rounded-lg object-cover";

    document.getElementById("messageContainer").appendChild(video);
}


function showAudio(url) {
    const audio = document.createElement("audio");

    audio.src = url;

    audio.controls = true;

    audio.className = "w-64 h-20 rounded-lg object-cover";

    document.getElementById("messageContainer").appendChild(audio);
}

function showFile(url, name) {
    const link = document.createElement("a");

    link.href = url;

    link.innerText = name;

    link.target = "_blank";

    link.className = "text-red-400 underline";

    document.getElementById("messageContainer").appendChild(link);
}