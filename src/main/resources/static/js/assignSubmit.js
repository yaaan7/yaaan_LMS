document.addEventListener("DOMContentLoaded", function() {
    const path = window.location.pathname;
    //현재 페이지 url의 경로값을 path에 저장
    const isEditMode = path.includes('/homework/edit/');
    //현재 페이지의 경로에 /homework/edit/가 포함되어있는지 여부를 확인
    if (isEditMode) {
        //만약 true라면
        const assignmentId = path.split('/').pop();
        //pop() : 배열의 마지막 요소를 반환하는 메서드
        //마지막 요소를 assingmentId에 저장
        loadAssignmentData(assignmentId);
        //과제Id를 받아 데이터를 불러오는 함수
        document.getElementById('submitBtn').textContent = '과제 수정하기';
        document.getElementById('pageTitle').textContent = '과제 수정하기';
        //submitBtn, pageTitle의 텍스트를 '과제 수정하기'로 바꿈
    }
});

function submitAssignment() {
    const cate = document.getElementById('assignment_cate').value;
    //getElementById() : html 문서 내에서 주어진 문자열과 값이 일치하는 id 속성을 가진 요소 객체를 반환하는 함수
    //->만약 없다면 null 을 반환
    const title = document.getElementById('assignment_title').value;
    const description = document.getElementById('assignment_description').value;
    const dueDate = document.getElementById('assignment_due_date').value;
    const dueTime = document.getElementById('assignment_due_time').value;


    if (!title || !description || !dueDate || !dueTime) {
        // null 값을 가진 항목이 있다면 alert() 메서드를 이용해 경고창을 띄움
        alert('모든 항목을 입력해주세요.');
        return;
    }

    const assignmentData = {
        cate: cate,
        title: title,
        description: description,
        dueDate: dueDate,
        dueTime: dueTime
        // html에 입력된 정보를 저장?
    };

    const path = window.location.pathname; //현재 페이지 url의 경로값을 path에 저장
    const isEditMode = path.includes('/homework/edit/');
    //현재 페이지의 경로에 /homework/edit/가 포함되어있는지 여부를 확인
    const method = isEditMode ? 'PUT' : 'POST'; //삼항연산자??!
    //isEditMode가 참값을 가지면 method에 'PUT'을 저장, 거짓값을 가지면 'POST'를 저장
    const url = isEditMode ? `/homework/post/${path.split('/').pop()}` : '/homework/post';
    //isEditMode가 참값을 가지면 url에 '/homework/post/{id}(id변할 수 있음)'로 저장하고  (pop메서드 이용해서 id추출)
    //거짓값을 가지면 '/homework/post'를 저장

    fetch(url, {
        //HTTP 요청 전송 기능을 제공하는 Web API
        //브라우저가 네트워크 요청을 보내고 Promise 타입의 객체를 반환
        method: method, //HTTP 방식
        headers: { //HTTP 요청 헤더
            'Content-Type': 'application/json' //JSON 포맷을 사용한다고 알려주기
        },
        body: JSON.stringify(assignmentData) //HTTP 요청 전문
        //fetch() 함수의 두번째 인자인 옵션 객체를 설정
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('과제 부여에 실패했습니다.');
        })
        .then(data => {
            alert(isEditMode ? '과제가 성공적으로 수정되었습니다.' : '과제가 성공적으로 부여되었습니다.');
            window.location.href = '/list/1';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('과제 부여 중 오류가 발생했습니다.');
        });
}
