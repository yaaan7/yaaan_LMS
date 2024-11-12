const loginBox = document.getElementById('login-box');
const toggleButton = document.getElementById('toggle-button');
const loginButton = document.getElementById('login-button');

// 마우스가 버튼 위로 올라올 때 로그인 박스를 보여줌
toggleButton.addEventListener('mouseenter', function() {
    loginBox.classList.add('show'); // 로그인 박스 보이기
});

// 마우스가 버튼을 벗어날 때 로그인 박스를 숨김
toggleButton.addEventListener('mouseleave', function() {
    loginBox.classList.remove('show'); // 로그인 박스 숨기기
});

// 로그인 박스에 마우스가 올라가면 숨김을 방지
loginBox.addEventListener('mouseenter', function() {
    loginBox.classList.add('show'); // 로그인 박스 계속 보이기
});

// 로그인 박스에서 마우스가 벗어날 때 숨김
loginBox.addEventListener('mouseleave', function() {
    loginBox.classList.remove('show'); // 로그인 박스 숨기기
});

// 페이지 로드 시 로그인 박스를 숨깁니다.
document.addEventListener('DOMContentLoaded', function() {
    loginBox.style.display = 'block'; // 기본적으로 보여주기
});


