let socket;
let currentReceiver = "";
let onlineUsers = [];
let username = "";
let chatHistory = {};

async function getUsername() {
    try {
        const res = await fetch("/current-user");

        data = await res.json();
        username = data.username;

        console.log("Logged in:", username);
        connectWebsocket();
        loadAllUsers();
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
                    .split(",");

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

                showNotification(sender, text);
            }
        }


    };

    socket.onclose = function () {
        console.log(
            "Connection closed"
        );
    };

}

function selectUser(user) {

    currentReceiver = user;

    document
        .getElementById("chatHeader")
        .innerText =
        "Chat with " + user;

    const container = document.getElementById("messageContainer");

    container.innerHTML = "";

    if (chatHistory[user]) {

        chatHistory[user].forEach(msg => {

            if (msg.type === "sent") {

                showSentMessage(
                    msg.text
                );

            } else {

                showReceivedMessage(
                    msg.text
                );

            }

        });

    }

    loadMessages(user);

}

function sendMessage() {

    const input =
        document
            .getElementById(
                "messageInput"
            );

    const text =
        input.value.trim();

    if (!text) {

        alert(
            "Type message"
        );

        return;
    }

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

    showSentMessage(text);

    input.value = "";

}

function showSentMessage(text) {

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
        "flex justify-end";

    div.innerHTML =
        `
        <div class="bg-blue-600 text-white px-4 py-2 rounded-2xl max-w-xs">
            ${text}
        </div>
        `;

    container.appendChild(
        div
    );

    container.scrollTop =
        container.scrollHeight;

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

async function loadAllUsers() {

    const response = await fetch("/users");
    const users = await response.json();

    console.log("Users from API:", users);
    console.log("Logged in:", username);
    console.log("Online:", onlineUsers);

    renderUserList(users);
}


function renderUserList(users) {

    const userList =
        document
            .getElementById(
                "userList"
            );

    userList.innerHTML =
        "";

    users.forEach(
        user => {

            if (
                user.email.trim() ===
                username.trim()
            )
                return;

            const isOnline =
                onlineUsers.includes(
                    user.email
                );

            const dot =
                isOnline
                    ? "bg-green-500"
                    : "bg-gray-400";

            const div =
                document
                    .createElement(
                        "div"
                    );

            div.className =
                "flex items-center gap-3 p-3 cursor-pointer hover:bg-gray-100";

            div.onclick =
                () =>
                    selectUser(
                        user.email
                    );

            div.innerHTML =
                `
                <span class="w-3 h-3 ${dot} rounded-full"></span>
                <span>${user.email}</span>
                `;

            userList.appendChild(
                div
            );

        }

    );

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

    // chatHistory[user] = [];

    messages.forEach(msg => {

        if (msg.senderEmail === username) {

            chatHistory[user].push({
                type: "sent",
                text: msg.text
            });

            showSentMessage(msg.text);

        } else {

            chatHistory[user].push({
                type: "received",
                text: msg.text
            });

            showReceivedMessage(msg.text);

        }

    });

}


let unreadCount = {};

function showNotification(sender, text) {

    if (Notification.permission === "granted") {
        new Notification(
            "New message from " + sender,
            {
                body: text
            }
        );

    } else {

        Notification.requestPermission();

    }

    unreadCount[sender] =
        (unreadCount[sender] || 0) + 1;
}


async function loadChats() {

    const response =
        await fetch(
            `/chat/chats?email=${username}`
        );

    const chats =
        await response.json();

    console.log("Chats:", chats);

}


loadChats();