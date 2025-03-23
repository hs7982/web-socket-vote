import { api_login } from "./api.js";

const btn_login = () => {
    let username = document.getElementById("username").value
    let password = document.getElementById("password").value

    if (!(username && password)) {
        alert("아이디와 비밀번호를 모두 입력해주세요.")
        return;
    }

    // api_login(username, password).then((result) => {
    //     window.location.href = "./index.html"
    // })
        window.location.href = "./index.html"
}

document.getElementById("btn_login").addEventListener("click", btn_login);