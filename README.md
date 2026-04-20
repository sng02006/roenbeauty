# Roen Beauty

네일샵 예약 및 관리 시스템 프로젝트입니다.  
사용자는 시술 메뉴, 갤러리, 예약 페이지를 확인할 수 있고,  
관리자는 추후 시술 메뉴와 갤러리, 예약 내역을 관리할 수 있도록 개발 중입니다.

---

## 프로젝트 소개

Roen Beauty는 네일샵 홈페이지를 넘어,  
실제 운영까지 고려한 예약 및 관리 시스템을 목표로 하는 프로젝트입니다.

현재는 사용자용 화면(UI)을 먼저 구현했으며,  
이후 예약 기능, 시술 메뉴 관리, 갤러리 관리, 관리자 기능까지 확장할 예정입니다.

---

## 개발 목적

이 프로젝트는 단순한 학습용 토이 프로젝트가 아니라,  
공부를 목적으로 시작했지만 완성도를 높여 실제 서비스로 사용할 수 있는 수준까지 개발하는 것을 목표로 합니다.

- Spring Boot 기반 백엔드 설계
- 도메인 중심 패키지 구조 구성
- MySQL + JPA를 활용한 데이터 관리
- 사용자 페이지와 관리자 페이지 분리
- 실제 운영을 고려한 기능 설계

---

## 주요 기능

### 사용자
- 메인 페이지 조회
- 시술 메뉴 조회
- 갤러리 조회
- 예약 페이지 조회
- 예약 요청 기능 (구현 중)

### 관리자 (예정)
- 시술 메뉴 등록 / 수정 / 삭제
- 갤러리 등록 / 수정 / 삭제
- 예약 목록 조회 및 상태 관리

---

## 기술 스택

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Gradle

### Database
- MySQL

### Frontend
- HTML
- CSS
- JavaScript

### Tool
- VS Code
- Git / GitHub

---

## 프로젝트 구조

```text
src/main/java/com/example/roenbeauty
 ┣ gallery
 ┣ global
 ┣ menu
 ┣ reservation
 ┗ RoenBeautyApplication.java

src/main/resources
 ┣ static
 ┃ ┣ index.html
 ┃ ┣ services.html
 ┃ ┣ gallery.html
 ┃ ┗ reservation.html
 ┗ application.properties
```

---

## 구현 현황

### 완료
- Spring Boot 프로젝트 초기 세팅
- MySQL 연동
- 메인 페이지 UI 구현
- 시술 메뉴 페이지 UI 구현
- 갤러리 페이지 UI 구현
- 예약 페이지 UI 구현
- GitHub 저장소 생성 및 프로젝트 업로드
- reservation 도메인 구조 생성

### 진행 중
- 예약 요청 API 연결
- 예약 DB 저장 기능 구현

### 예정
- 시술 메뉴 DB 기반 관리 기능
- 갤러리 DB 기반 관리 기능
- 관리자 페이지 구현
- 예약 상태 관리 기능
- 로그인 / 권한 기능

---

## 실행 방법

1. MySQL 실행
2. 데이터베이스 생성

```sql
CREATE DATABASE roenbeauty;
```

3. `application.properties`에 DB 설정 입력
4. 프로젝트 실행

```bash
./gradlew bootRun
```

Windows PowerShell에서는 아래 명령어를 사용합니다.

```powershell
.\gradlew.bat bootRun
```

---

## 향후 계획

- reservation 기능 완성
- menu 도메인을 DB 기반으로 전환
- gallery 도메인 기능 추가
- 관리자 기능 구현
- 실제 서비스 운영 가능 수준으로 구조 개선

---

## 프로젝트 메모

이 프로젝트는 UI를 먼저 구현한 뒤,  
핵심 데이터를 점진적으로 DB 기반으로 전환하는 방식으로 개발하고 있습니다.

정적인 화면 구현에 그치지 않고,  
실제 사장님이 메뉴와 갤러리를 수정하고 예약을 관리할 수 있는 구조를 목표로 합니다.
