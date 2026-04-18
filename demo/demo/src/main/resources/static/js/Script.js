const btn = document.getElementById("menu-btn");
const menu = document.getElementById("menu");
const overlay = document.getElementById("overlay");

/* Toggle menu */

btn.addEventListener("click", () => {

    btn.classList.toggle("menu-open");

    menu.classList.toggle("-translate-y-full");
    menu.classList.toggle("translate-y-0");

    overlay.classList.toggle("opacity-0");
    overlay.classList.toggle("opacity-100");

    overlay.classList.toggle("pointer-events-none");

});

/* Close when clicking overlay */

overlay.addEventListener("click", () => {

    btn.classList.remove("menu-open");

    menu.classList.add("-translate-y-full");
    menu.classList.remove("translate-y-0");

    overlay.classList.add("opacity-0");
    overlay.classList.add("pointer-events-none");

});



//login registeration

const registerBtn = document.getElementById("registerBtn");
const loginBtn = document.getElementById("loginBtn");
const container = document.getElementById("container");

registerBtn.addEventListener("click", () => {
    container.classList.add("active");
});

loginBtn.addEventListener("click", () => {
    container.classList.remove("active");
});