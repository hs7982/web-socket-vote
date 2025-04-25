//백엔드 완료 전까지 임시로 사용할 데이터
import {api_castVote, api_getVoteDetail, api_updateVote} from "./api.js";
import {webSocket} from "./websocket.js";

async function getData(id) {
    const response = await api_getVoteDetail(id);
    const data = await response.json();
    return data.data;
}

function BtnUpdateClick() {
    viewType = "vote"
    reCreate()
}

//투표 버튼을 눌렀을때 실행될 함수
async function BtnVoteClick() {
    let checkName = null;
    let checkId = null;
    try {
        checkName = document.querySelector('input[name="vote"]:checked').alt;
        checkId = document.querySelector('input[name="vote"]:checked').id;
    } catch (error) {
        alert("투표할 항목을 선택해주세요!");
        return;
    }

    //투표처리 api 호출 추가
    if (!hasVoted) { //투표 안한사람이 투표할때
        if (!confirm(`'${checkName}' 항목에 투표하시겠습니까?`)) {
            return
        }
        await api_castVote(id, checkId).then(() => {
            alert("투표하였습니다!");
        }).catch((error) => {
            alert(error.message)
            console.log(error);
        });
    } else { //투표한사람이 수정할때
        if (!confirm(`'${checkName}' 항목으로 변경하시겠습니까?`)) {
            return
        }
        await api_updateVote(id, checkId).then(() => {
            alert("투표를 변경하였습니다!");
        }).catch((error) => {
            alert(error.message)
            console.log(error);
        });
    }
    initPage();
}

//url 쿼리에서 id 추출
function getVoteIdFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

//vote 데이터를 기반으로 기본적 컨테이너와 info 앨리먼트를 생성하는 함수
function createVoteInfoContainer() {
    const wrap = document.getElementsByClassName("voteWrap")[0];
    wrap.innerHTML = "";

    const divContainer = document.createElement("div");
    divContainer.className = "container";

    const divInfo = document.createElement("div");
    divInfo.className = "info";

    const divH2 = document.createElement("h2");
    divH2.textContent = voteData.title;

    const divP = document.createElement("p");
    divP.textContent = voteData.content;

    divInfo.appendChild(divH2)
    divInfo.appendChild(divP)

    divContainer.appendChild(divInfo);
    wrap.appendChild(divContainer);
}

//생성된 컨터이너에 투표 옵션 선택창 or 결과화면 생성 함수
//백엔드에서 데이터를 불러왔을때 투표 미참여 -> 옵션선택, 투표참여->결과화면 표시
function createVoteOption() {
    const divContainer = document.getElementsByClassName("container")[0];

    const divVoteOption = document.createElement("div");
    divVoteOption.className = viewType === "result" ? "resultOption" : "voteOption";

    const divMySelect = document.createElement("img");
    divMySelect.src = "/img/check.svg"
    divMySelect.alt = "나의 선택"

    Object.keys(voteData.options).forEach(key => {
        const divOptionRow = document.createElement("div");
        divOptionRow.className = "optionRow";
        divOptionRow.id = "id" + voteData.options[key].id;

        const divInner = document.createElement("div")
        divInner.className = "inner";

        if (viewType === "vote") { //투표 화면일경우
            const inputRadio = document.createElement("input");
            inputRadio.type = "radio";
            inputRadio.name = "vote";
            inputRadio.id = voteData.options[key].id;
            inputRadio.alt = voteData.options[key].name;

            if (hasVoted && (voteData.options[key].id === voteData.userVoteStatus.selectedOptionId))
                inputRadio.checked = true;

            const lable = document.createElement("label");
            lable.className = "optionTitle";
            lable.htmlFor = voteData.options[key].id;
            lable.innerText = voteData.options[key].name;

            divOptionRow.appendChild(inputRadio);
            divOptionRow.appendChild(lable);
            divVoteOption.appendChild(divOptionRow);
        } else { //투표 참여했을 경우
            if (voteData.userVoteStatus.selectedOptionId === voteData.options[key].id)
                divInner.appendChild(divMySelect);
            const divOptionTitle = document.createElement("div")
            divOptionTitle.className = "optionTitle";
            divOptionTitle.innerText = voteData.options[key].name;

            const divOptionVoteNum = document.createElement("div")
            divOptionVoteNum.className = "optionVoteNum";
            divOptionVoteNum.innerText = voteData.options[key].voteCount + "표";

            const divChart = document.createElement("div");
            divChart.className = "chart";
            divChart.id = "chart" + key;

            updateResult(divChart, voteData.voteCount, voteData.options[key])

            const divVoters = document.createElement("div")
            if (voteData.options[key].voters.length !== 0) {
                divVoters.className = "voters"
                divVoters.id = "voters" + key

                divVoters.innerHTML += "<p>투표자</p>"
                Object.keys(voteData.options[key].voters).forEach(votersKey => {
                    console.log(voteData.options[key].voters[votersKey].username)
                    divVoters.innerHTML += `<span>${voteData.options[key].voters[votersKey].username}</span>`;
                })
            }

            divInner.appendChild(divChart);
            divInner.appendChild(divOptionTitle);
            divInner.appendChild(divOptionVoteNum);
            divOptionRow.appendChild(divInner)
            divOptionRow.appendChild(divVoters);
            divVoteOption.appendChild(divOptionRow);
        }

    });
    divContainer.appendChild(divVoteOption);

    const divBtnBox = document.createElement("div");
    divBtnBox.className = "btnBox";

    if (viewType === "vote") {
        const voteBtn = document.createElement("button");
        voteBtn.classList = "btn btn_pr btn_lg";
        voteBtn.id = "btn_vote"
        if (hasVoted) {
            voteBtn.innerText = "수정하기"
            voteBtn.addEventListener("click", BtnVoteClick);
        } else {
            voteBtn.innerText = "투표하기"
            voteBtn.addEventListener("click", BtnVoteClick);
        }

        divBtnBox.appendChild(voteBtn);
    } else if (viewType === "result") {
        const voteBtn = document.createElement("button");
        voteBtn.classList = "btn btn_gh btn_sm";
        voteBtn.style.width = "120px"
        voteBtn.id = "btn_vote_update"
        voteBtn.innerText = "다시 투표하기"
        voteBtn.addEventListener("click", BtnUpdateClick);

        divBtnBox.appendChild(voteBtn);
    }

    divVoteOption.appendChild(divBtnBox);
}

function updateResult(divChart, totalCount, optionData) {
    if (typeof (divChart) == "number") //엘리먼트 객체가 아니라 옵션id가 들어왔을때 처리
        divChart = document.getElementById("chart" + divChart);

    console.log(optionData)

    let percentage = (optionData.voteCount / totalCount) * 100;
    //업데이트 하기 전 현재 퍼센트
    const prevPercentage = divChart.style.width || 0;

    // 리플로우 강제 실행 (브라우저가 다시 계산하도록 유도)
    divChart.style.animation = "none";
    divChart.offsetHeight;
    divChart.style.animation = "fillChart 0.5s ease";

    //퍼센트 업데이트
    divChart.style.setProperty('--prev-percentage', `${prevPercentage}`);
    divChart.style.setProperty('--percentage', `${percentage}%`);
    divChart.style.width = `${percentage}%`;

}

function updateVoteResults() {
    // 각 옵션에 대해 차트 및 정보 업데이트
    Object.keys(voteData.options).forEach(key => {
        const optionElement = document.getElementById("id" + voteData.options[key].id);
        if (optionElement) {
            // 차트 업데이트
            const chartElement = optionElement.querySelector(".chart");
            if (chartElement) {
                updateResult(chartElement, voteData.voteCount, voteData.options[key]);
            }

            // 투표 수 업데이트
            const voteNumElement = optionElement.querySelector(".optionVoteNum");
            if (voteNumElement) {
                voteNumElement.innerText = voteData.options[key].voteCount + "표";
            }

            // 투표자 목록 업데이트
            const votersElement = optionElement.querySelector(".voters");
            if (votersElement) {
                if (voteData.options[key].voters.length !== 0) {
                    votersElement.innerHTML = "<p>투표자</p>";
                    Object.keys(voteData.options[key].voters).forEach(votersKey => {
                        votersElement.innerHTML += `<span>${voteData.options[key].voters[votersKey].username}</span>`;
                    });
                } else {
                    votersElement.innerHTML = "";
                }
            }
        }
    });
}

// 데이터를 가져와 업데이트하는 함수
async function updateVoteData() {
    try {
        // 최신 데이터 가져오기
        const response = await getData(id);
        // voteData 업데이트
        voteData = response;
        // 차트와 정보 업데이트
        updateVoteResults();
        return true;
    } catch (error) {
        console.error("투표 데이터 업데이트 실패:", error);
        return false;
    }
}


// 초기 페이지 초기화 영역
function initPage() {
    getData(id).then((response) => {
        voteData = response;
        hasVoted = voteData.userVoteStatus.hasVoted;
        if (hasVoted) viewType = "result"
        createVoteInfoContainer();
        createVoteOption();
    });
}

function reCreate() {
    createVoteInfoContainer();
    createVoteOption();
}

const id = getVoteIdFromURL();
if (!id) {
    alert("투표 ID null!")
}
let hasVoted = null;
let viewType = "vote";
let voteData = null;

//dom로드 완료시 페이지 초기화 실행
document.addEventListener("DOMContentLoaded", initPage);

const getSocket = () => webSocket(id);
let socket = getSocket();

socket.addEventListener('message', (event) => {
    if (viewType === "result") {
        if (event.data === "updateVote" || event.data === "newVote") {
            // 함수화된 updateVoteData 호출
            updateVoteData();
        }
    }
});

socket.addEventListener('close', (event) => {
    console.log('WebSocket 연결이 종료되었습니다. 코드:', event.code, '이유:', event.reason);
    socket = getSocket();
});


