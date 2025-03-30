import { api_login, api_test } from "./api.js";

const btn_login = () => {
    let username = document.getElementById("username").value
    let password = document.getElementById("password").value

    if (!(username && password)) {
        alert("아이디와 비밀번호를 모두 입력해주세요.")
        return;
    }

    api_login(username, password).then((result) => {
        console.log(result)
        if(result.status === 200){
        window.location.href = "/"
        }

    }).catch((error)=> {
        console.log(error)
        alert(error.message)
    });
    
        // window.location.href = "./index.html"
}

function getParam() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('logout');
}
document.getElementById("btn_login").addEventListener("click", btn_login);
document.getElementById("btn_join").addEventListener("click", ()=>window.location.href="/join");

const logoutResult = getParam();
const divAlert = document.getElementsByClassName("alert")[0]
if (logoutResult) {
    if (logoutResult === "self")
        divAlert.innerHTML = `<div class="info">로그아웃 되었습니다.</div>`;
    else if(logoutResult==="expire")
        divAlert.innerHTML = `<div class="error">로그인 정보가 없거나 만료되었습니다.</div>`;
}