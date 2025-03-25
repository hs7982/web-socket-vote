//백엔드 완료 전까지 임시로 사용할 데이터
const tmpData = {
    title: "투표 제목",
    content: "투표 내용",
    isVoted: true,
    option: {
        1: { name: "옵션1" },
        2: { name: "옵션2" },
        3: { name: "옵션3" },
        4: { name: "옵션4" },
        5: { name: "옵션5" },
        6: { name: "옵션6" },
        7: { name: "옵션7" },
        8: { name: "옵션8" },
        9: { name: "옵션9" },
        10: { name: "옵션10" },
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

//투표 옵션 선택창 생성 함수
function createVoteOption(voteData) {
    const divContainer = document.getElementsByClassName("container")[0];

    const divVoteOption = document.createElement("div");
    divVoteOption.className = voteData.isVoted ? "resultOption" : "voteOption";

    Object.keys(voteData.option).forEach(key => {
        const divOptionRow = document.createElement("div");
        divOptionRow.className = "optionRow";

        if (!voteData.isVoted) {
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
        } else {
            const divOptionTitle = document.createElement("div")
            divOptionTitle.className = "optionTitle";
            divOptionTitle.innerText = voteData.option[key].name;

            const divChart = document.createElement("div");
            divChart.className="chart";
            divChart.id = "chart"+key;

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

function updateResult(optionId, percentage) {
    const optionChart = document.getElementById("chart"+optionId);
    optionChart.style.setProperty('--percentage', `${percentage}%`);
    console.log(optionChart);
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
    updateResult(1, 40)
}

//dom로드 완료시 페이지 초기화 실행
document.addEventListener("DOMContentLoaded", initPage);



