import { api_createVoteRoom } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    // 현재 날짜를 기본값으로 설정 (하루 뒤)
    setDefaultEndTime();

    // 이벤트 리스너 등록
    const form = document.getElementById('createVoteForm');
    const addOptionBtn = document.getElementById('addOption');
    const optionsContainer = document.getElementById('optionsContainer');


    addOptionBtn.addEventListener('click', () => addNewOption(optionsContainer));
    form.addEventListener('submit', handleSubmit);

    // 옵션 삭제 버튼 이벤트 위임
    optionsContainer.addEventListener('click', (e) => {
        if (e.target.classList.contains('remove-option')) {
            removeOption(e.target.parentElement);
            updateRemoveButtons();
        }
    });

    // 초기 삭제 버튼 상태 설정
    updateRemoveButtons();
});

function setDefaultEndTime() {
    const endTimeInput = document.getElementById('endTime');
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    tomorrow.setHours(23, 59, 59, 0);

    // YYYY-MM-DDThh:mm 형식으로 변환
    const year = tomorrow.getFullYear();
    const month = String(tomorrow.getMonth() + 1).padStart(2, '0');
    const day = String(tomorrow.getDate()).padStart(2, '0');
    const hours = String(tomorrow.getHours()).padStart(2, '0');
    const minutes = String(tomorrow.getMinutes()).padStart(2, '0');

    endTimeInput.value = `${year}-${month}-${day}T${hours}:${minutes}`;
}

function addNewOption(container) {
    const optionCount = container.children.length + 1;
    const optionItem = document.createElement('div');
    optionItem.className = 'option-item';
    optionItem.innerHTML = `
        <input type="text" class="option-input" placeholder="옵션 ${optionCount}" required>
        <button type="button" class="remove-option">삭제</button>
    `;

    container.appendChild(optionItem);
    updateRemoveButtons();
}

function removeOption(optionElement) {
    optionElement.remove();

    // 옵션 placeholder 번호 재설정
    const options = document.querySelectorAll('.option-input');
    options.forEach((option, index) => {
        option.placeholder = `옵션 ${index + 1}`;
    });
}

function updateRemoveButtons() {
    const removeButtons = document.querySelectorAll('.remove-option');
    const optionCount = removeButtons.length;

    // 최소 2개의 옵션은 유지해야 함
    removeButtons.forEach(button => {
        button.disabled = optionCount <= 2;
    });
}

async function handleSubmit(e) {
    e.preventDefault();

    // 폼 데이터 수집
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    const endTime = document.getElementById('endTime').value;
    const optionInputs = document.querySelectorAll('.option-input');

    const optionNames = [];
    optionInputs.forEach(input => {
        if (input.value.trim()) {
            optionNames.push(input.value.trim());
        }
    });

    // 유효성 검사
    if (!title || !content || !endTime || optionNames.length < 2) {
        alert('모든 필드를 입력하고 최소 2개의 옵션을 추가해주세요.');
        return;
    }

    // API 요청 데이터 준비
    const voteRoomData = {
        title,
        content,
        endTime: `${endTime}:00`, // 초 추가
        optionNames
    };

    try {
        const submitBtn = document.getElementById('submitBtn');
        submitBtn.disabled = true;
        submitBtn.textContent = '생성 중...';

        // API 호출
        const response = await api_createVoteRoom(voteRoomData);

        if (response.ok) {
            alert('투표방이 성공적으로 생성되었습니다.');
            window.location.href = '/';
        }
    } catch (error) {
        alert(`투표방 생성 실패: ${error.message || '알 수 없는 오류가 발생했습니다.'}`);
        console.error('투표방 생성 오류:', error);
    } finally {
        const submitBtn = document.getElementById('submitBtn');
        submitBtn.disabled = false;
        submitBtn.textContent = '투표방 생성';
    }
}