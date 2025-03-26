//백엔드 완료 전까지 임시로 사용할 데이터
const tmpData = {
    title: "투표 제목",
    content: "투표 내용",
    isVoted: true,
    option: {
        1: { name: "옵션1", vote: 8, percentage: 20 },
        2: { name: "옵션2", vote: 9, percentage: 40 },
        3: { name: "옵션3", vote: 2, percentage: 60 },
        4: { name: "옵션4", vote: 5, percentage: 80 },
        5: { name: "옵션5", vote: 1, percentage: 100 },
        6: { name: "옵션6", vote: 0, percentage: 2 },
        7: { name: "옵션7", vote: 0, percentage: 0 },
        8: { name: "옵션8", vote: 0, percentage: 0 },
        9: { name: "옵션9", vote: 8, percentage: 1.2 },
        10: { name: "옵션10", vote: 8, percentage: 98.9 },
    }
}

//투표 버튼을 눌렀을때 실행될 함수
function BtnVoteClick() {
    let check = null;
    try {
        check = document.querySelector('input[name="vote"]:checked').id;
    } catch (error) {
        alert("투표할 항목을 선택해주세요!");
        return;
    }

    if (!confirm(check + "번에 투표하시겠습니까?")) {
        return;
    }

    alert("투표됨ㅇㅇ")
    //투표처리 api 호출 추가

    //임시
    tmpData.isVoted = true;
    initPage();
}

//url 쿼리에서 id 추출
function getVoteIdFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

//id 데이터를 불러오는 함수
function loadData(id) {
    //임시 데이터 활용 추후 API로 대체 필요
    return tmpData
}

//vote 데이터를 기반으로 기본적 컨테이너와 info 앨리먼트를 생성하는 함수
function createVoteInfoContainer(voteData) {
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
function createVoteOption(voteData) {
    const divContainer = document.getElementsByClassName("container")[0];

    const divVoteOption = document.createElement("div");
    divVoteOption.className = voteData.isVoted ? "resultOption" : "voteOption";

    Object.keys(voteData.option).forEach(key => {
        const divOptionRow = document.createElement("div");
        divOptionRow.className = "optionRow";

        if (!voteData.isVoted) { //투표 미참여 했을경우
            const inputRadio = document.createElement("input");
            inputRadio.type = "radio";
            inputRadio.name = "vote";
            inputRadio.id = key;

            const lable = document.createElement("label");
            lable.className = "optionTitle";
            lable.htmlFor = key;
            lable.innerText = voteData.option[key].name;

            divOptionRow.appendChild(inputRadio);
            divOptionRow.appendChild(lable);
            divVoteOption.appendChild(divOptionRow);
        } else { //투표 참여했을 경우
            const divOptionTitle = document.createElement("div")
            divOptionTitle.className = "optionTitle";
            divOptionTitle.innerText = voteData.option[key].name;

            let divChart = document.createElement("div");
            divChart.className = "chart";
            divChart.id = "chart" + key;

            updateResult(divChart, voteData.option[key])

            divOptionRow.appendChild(divChart);
            divOptionRow.appendChild(divOptionTitle);
            divVoteOption.appendChild(divOptionRow);
        }

    });
    divContainer.appendChild(divVoteOption);

    const divBtnBox = document.createElement("div");
    divBtnBox.className = "btnBox";

    if (!voteData.isVoted) {
        const voteBtn = document.createElement("button");
        voteBtn.classList = "btn btn_pr btn_lg";
        voteBtn.id = "btn_vote"
        voteBtn.innerText = "투표하기"
        voteBtn.addEventListener("click", BtnVoteClick);

        divBtnBox.appendChild(voteBtn);
    }

    divVoteOption.appendChild(divBtnBox);
}

function updateResult(divChart, optionData) {
    if (typeof (divChart) == "number") //엘리먼트 객체가 아니라 옵션id가 들어왔을때 처리
        divChart = document.getElementById("chart" + divChart);

    let percentage = 0;
    if (typeof (optionData) == "number")
        percentage = optionData;
    else
        percentage = optionData.percentage;

    const prevPercentage = divChart.style.width;

    divChart.style.animation = "none";
    divChart.offsetHeight; // 리플로우 강제 실행 (브라우저가 다시 계산하도록 유도)
    divChart.style.animation = "fillChart 1s ease";

    divChart.style.setProperty('--prev-percentage', `${prevPercentage}%`);
    divChart.style.setProperty('--percentage', `${percentage}%`);
    divChart.style.width = `${percentage}%`;

}


// 초기 페이지 초기화 영역
function initPage() {
    const id = getVoteIdFromURL();
    if (!id) {
        alert("투표 ID null!")
        return;
    }

    const voteData = loadData(id);
    createVoteInfoContainer(voteData);
    createVoteOption(voteData);
    setInterval(() => {
        updateResult(1, 100)
    }, 3000)
}

//dom로드 완료시 페이지 초기화 실행
document.addEventListener("DOMContentLoaded", initPage);



