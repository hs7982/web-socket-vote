import {api_getVoteList, api_test} from "../js/api.js";

//백엔드 완료 전까지 임시로 사용할 데이터
// const postData = [{ "id": "1", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "2", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "3", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "4", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "5", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "6", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "7", "title": "투표제목", "subtitle": "투표설명" },
// { "id": "8", "title": "투표제목", "subtitle": "투표설명" },];

async function getListData() {
    const result = await api_getVoteList();
    const voteData = await result.json();
    return voteData.data;
}

const postData =  await getListData();
console.log(postData)
const listBox = document.getElementsByClassName("listBox")[0]
for (const post of postData) {
    const postHTML = `
        <div class="listItem">
            <h3>${post.title}</h3>
            <p>${post.content}</p>
            <a href="./vote?id=${post.id}" class="btn btn_pr btn_sm" onclick="gotoVote(${post.id})">투표하기</a>
            <span>${post.voteCount}명 참여중</span>
        </div>
    `;

    listBox.insertAdjacentHTML('beforeend', postHTML);

}

