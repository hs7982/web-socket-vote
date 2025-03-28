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
api_test();
document.getElementById("btn_login").addEventListener("click", btn_login);