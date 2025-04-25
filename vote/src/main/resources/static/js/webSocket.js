export function webSocket(roomId) {
// WebSocket 연결 생성
    const socket = new WebSocket(`ws://${window.location.host}/api/ws/${roomId}`);

// 연결이 열렸을 때 이벤트 처리
    socket.addEventListener('open', (event) => {
        console.log('WebSocket 연결이 열렸습니다.');

        // 서버에 메시지 보내기
        socket.send("hello");
    });

// 메시지 수신 이벤트 처리
    socket.addEventListener('message', (event) => {
        console.log('서버로부터 메시지를 받았습니다:', event.data);

        // 여기서 받은 데이터를 처리
    });

// 에러 처리
    socket.addEventListener('error', (event) => {
        console.error('WebSocket 에러 발생:', event);
    });


    return socket;
}
