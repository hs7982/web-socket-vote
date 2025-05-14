# Vote🗳️

## 소개
WebSocket을 활용한 실시간 투표 프로젝트입니다. 사용자는 투표방을 생성하고 참여할 수 있으며, 투표 결과는 실시간으로 화면에 반영됩니다.

## 주요 기능
- 사용자 인증 (회원가입/로그인)
- 투표방 생성 및 관리
- 실시간 투표 참여
- 투표 결과 실시간 업데이트
- 투표 수정 기능

## 기술 스택
### Backend
- **Framework:** Spring Boot 3.4.3
- **Security:** Spring Security + JWT
- **Database:** MariaDB
- **WebSocket:** Spring WebSocket
- **ORM:** Spring Data JPA
- **Build Tool:** Gradle
- **Java Version:** Java 21

### Frontend
- **Template Engine:** Thymeleaf
- **JavaScript:** Vanilla JS
- **WebSocket:** Native WebSocket API
- **CSS:** Custom CSS

## 시스템 아키텍처
- **인증 방식:** JWT (Access Token + Refresh Token)
- **실시간 통신:** WebSocket
- **데이터베이스:** MariaDB (JPA를 통한 ORM)

## 프로젝트 구조
```
src/main/
├── java/com/hseok/vote/
│   ├── controller/     # REST API 컨트롤러
│   ├── domain/        # 엔티티 클래스
│   ├── dto/           # 데이터 전송 객체
│   ├── repository/    # 데이터 접근 계층
│   ├── service/       # 비즈니스 로직
│   ├── security/      # 보안 설정 및 JWT 관련
│   ├── websocket/     # WebSocket 설정 및 핸들러
│   └── user/          # 사용자 관련 기능
└── resources/
    ├── static/        # 정적 리소스 (JS, CSS)
    └── templates/     # Thymeleaf 템플릿
```

## 실행 방법
1. MariaDB 설치 및 데이터베이스 생성
```sql
CREATE DATABASE vote;
```

2. 애플리케이션 설정
- `application.properties` 파일에서 데이터베이스 접속 정보 설정
- JWT 설정 파일(`jwt.yml`) 구성

3. 프로젝트 빌드 및 실행
```bash
./gradlew build
java -jar build/libs/vote-0.0.1-SNAPSHOT.jar
```

## API 엔드포인트
### 인증
- POST `/api/user/join` - 회원가입
- POST `/api/login` - 로그인
- POST `/api/logout` - 로그아웃

### 투표
- GET `/api/vote` - 투표방 목록 조회
- GET `/api/vote/{roomId}` - 투표방 상세 정보 조회
- POST `/api/vote` - 투표방 생성
- POST `/api/vote/{roomId}/cast` - 투표하기
- PUT `/api/vote/{roomId}/cast` - 투표 수정
- DELETE `/api/vote/{roomId}` - 투표방 삭제

## WebSocket 엔드포인트
- `/api/ws/{roomId}` - 투표방별 WebSocket 연결

## 보안
- CORS 설정이 되어있어 허용된 도메인에서만 API 접근 가능
- JWT 기반의 인증 시스템 구현
- WebSocket 연결 시 사용자 인증 검증

