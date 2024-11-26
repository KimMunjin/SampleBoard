# 게시판을 만들고 싶었지만 하지 못한 프로젝트

## 구성 환경
- **SpringBoot**: 3.3.5
- **Gradle**
- **Java**: 17
- **Database**: PostgreSQL 17

## 프로젝트 구조
프로젝트는 다음과 같은 구조로 구성되어 있습니다:

Controller - Service - ServiceImpl - Mapper  


## 현재 구현된 주요 기능
- 회원 가입
- 로그인
- 로그아웃
- 회원 정보 수정
- 회원 탈퇴

## 사용 기술
- **Spring Security**와 **JWT**를 활용하여 인증 및 권한 관리
- **RefreshToken**과 **AccessToken**으로 구성된 토큰 기반 인증 방식
- Token 및 Blacklist 저장을 위한 **Redis** 사용
- **Swagger**를 통한 API 문서화

## 환경 설정
- Redis와 PostgreSQL은 **Docker** 컨테이너로 구현

## 차후 계획
- [ ] 초기 목표였던 게시판 기능 완성 
- [x] **GlobalExceptionHandler** 완성
- [ ] **Mockito**를 활용한 `ServiceImpl` 테스트 코드 작성
- [x] 구체적인 주석 추가
- [ ] 필요한 부분 리팩토링 작업
