import { api_signup } from './api.js';

document.addEventListener('DOMContentLoaded', function() {
    // 폼 및 요소 참조
    const signupForm = document.getElementById('signupForm');
    const signupButton = document.getElementById('btn_submit');
    const username = document.getElementById('username');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const successModal = document.getElementById('successModal');
    const closeModal = document.getElementById('closeModal');

    // 에러 메시지 요소
    const usernameError = document.getElementById('username-error');
    const emailError = document.getElementById('email-error');
    const passwordError = document.getElementById('password-error');
    const confirmPasswordError = document.getElementById('confirm-password-error');

    // 입력 검증 함수
    function validateUsername() {
        if (username.value.trim() === '') {
            usernameError.textContent = '사용자 이름을 입력해주세요.';
            return false;
        } else if (username.value.length < 3 || username.value.length > 50) {
            usernameError.textContent = '사용자 이름은 3자 이상 50자 이하여야 합니다.';
            return false;
        } else {
            usernameError.textContent = '';
            return true;
        }
    }

    function validateEmail() {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email.value.trim() === '') {
            emailError.textContent = '이메일을 입력해주세요.';
            return false;
        } else if (!emailRegex.test(email.value)) {
            emailError.textContent = '유효한 이메일 주소를 입력해주세요.';
            return false;
        } else {
            emailError.textContent = '';
            return true;
        }
    }

    function validatePassword() {
        if (password.value === '') {
            passwordError.textContent = '비밀번호를 입력해주세요.';
            return false;
        } else if (password.value.length < 8) {
            passwordError.textContent = '비밀번호는 8자 이상이어야 합니다.';
            return false;
        } else {
            passwordError.textContent = '';
            return true;
        }
    }

    function validateConfirmPassword() {
        if (confirmPassword.value === '') {
            confirmPasswordError.textContent = '비밀번호 확인을 입력해주세요.';
            return false;
        } else if (password.value !== confirmPassword.value) {
            confirmPasswordError.textContent = '비밀번호가 일치하지 않습니다.';
            return false;
        } else {
            confirmPasswordError.textContent = '';
            return true;
        }
    }

    // 실시간 유효성 검사
    username.addEventListener('blur', validateUsername);
    email.addEventListener('blur', validateEmail);
    password.addEventListener('blur', validatePassword);
    confirmPassword.addEventListener('blur', validateConfirmPassword);
    confirmPassword.addEventListener('input', validateConfirmPassword);

    // 회원가입 버튼 클릭 처리
    signupButton.addEventListener('click', function() {
        console.log('회원가입 버튼 클릭됨');

        // 모든 유효성 검사 실행
        const isUsernameValid = validateUsername();
        const isEmailValid = validateEmail();
        const isPasswordValid = validatePassword();
        const isConfirmPasswordValid = validateConfirmPassword();

        // 모든 검증이 통과되면 API 호출
        if (isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
            const userData = {
                username: username.value,
                email: email.value,
                password: password.value
            };

            // API 호출
            submitForm(userData);
        }
    });

    // 폼 제출 처리 (백업용 - 직접 제출을 막음)
    signupForm.addEventListener('submit', function(e) {
        // 이벤트 기본 동작 방지
        e.preventDefault();
        console.log('폼 제출 이벤트 발생 - 기본 동작 방지됨');
        return false;
    });

    // API 호출 함수
    async function submitForm(userData) {
        try {
            // api.js의 api_signup 함수 호출
            const response = await api_signup(userData);
            const data = await response.json();

            console.log('회원가입 성공:', data);
            showSuccessModal();

            // 폼 초기화
            signupForm.reset();
        } catch (error) {
            handleApiError(error);
        }
    }

    // 오류 처리 함수
    function handleApiError(error) {
        console.error('회원가입 오류:', error);

        // 오류 메시지에 따른 필드별 오류 표시
        if (error.message.includes('username') || (error.data && error.data.field === 'username')) {
            usernameError.textContent = error.message;
        } else if (error.message.includes('email') || (error.data && error.data.field === 'email')) {
            emailError.textContent = error.message;
        } else if (error.message.includes('password') || (error.data && error.data.field === 'password')) {
            passwordError.textContent = error.message;
        } else {
            // 일반 오류 메시지 표시
            alert('회원가입 처리 중 오류가 발생했습니다: ' + error.message);
        }
    }

    // 성공 모달 관련 함수
    function showSuccessModal() {
        successModal.style.display = 'flex';
    }

    closeModal.addEventListener('click', function() {
        successModal.style.display = 'none';
        // 로그인 페이지로 리디렉션
        window.location.href = './login';
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (event.target === successModal) {
            successModal.style.display = 'none';
            window.location.href = './login';
        }
    });
});