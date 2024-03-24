# RoadMaker

로드메이커는 누구나 학습 로드맵을 만들고, 공유할 수 있는 플랫폼 입니다.

![image](https://github.com/road-maker/road-maker-spring/assets/60874549/9bcc467d-9963-48c1-9e61-afb6722c7bff)

본 문서는 RoadMaker의 백엔드에 초점을 맞춰 작성됐습니다.

## 목차
1. [프로젝트 개요](#overview)
2. [서비스 소개](#intro)
3. [기술적 issue 해결 과정](#issue)
4. [프로젝트 아키텍처](#frontend)
5. [커밋 컨벤션](#commit-convention)


<a id="overview"></a>
## 1. 프로젝트 개요

### 1.1. 기술 스택
- 주요 기술: Java, Spring Boot, JPA, MySQL
- 인프라: AWS, Nginx
- 협업: Git, Slack, Github Projects, Notion, IntelliJ
- 빌드, 배포: Github Actions

### 1.2. 팀원
- [강원영](https://github.com/onezerokakng)(팀장/BE)
- [표혜민](https://github.com/pyotato)(FE)
- [박주영](https://github.com/dearmysolitude)(BE)
- [전서인](https://github.com/Seo1n)(FE)
- [임희호](https://github.com/HH981010)(BE)

### 1.3. 시연 연상

<!--
[바로 가기](http://roadmaker.site)

- demo 계정: test@test.com
- demo 계정 비밀번호: test1234!
-->
- 시연 연상: https://youtu.be/RqMJgGFdEws?feature=shared

<a id="intro"></a>
## 2. 서비스 소개

프로그래머가 되고 싶어하는 사람들은 "로드맵"이나 "커리큘럼"이라는 키워드로 자료를 검색합니다.

이러한 정보가 모여있는 "[developer-roadmap](https://github.com/kamranahmedse/developer-roadmap)" 리포지토리가 24만 개의 star를 받은 것은 이러한 수요를 잘 보여주는 예입니다.

하지만, 기존의 로드맵을 사용하다 보면 '나도 로드맵을 만들어보고 싶다내가 만든 로드맵을 다른 사람과 공유하고 싶다'는 생각이 들게 됩니다.

이런 필요성에 응답하여, "RoadMaker"를 개발하게 되었습니다.

RoadMaker는 사용자가 자신만의 개발 로드맵을 쉽게 만들고 공유할 수 있는 플랫폼입니다.

### 2.1. 주요 기능

1. 로드맵 자동 생성
   - GPT API를 이용하여 로드맵 초안을 자동 생성합니다.
2. 로드맵 참여 및 진행
   - 로드맵에 참여하고 진행도를 업데이트 할 수 있습니다.
3. 댓글 작성
   - 로드맵에 대해 궁금한 내용을 댓글을 달아 질문할 수 있습니다.
4. 검색 기능
   - 원하는 로드맵을 검색을 통해 찾을 수 있습니다.

<a id="issue"></a>
## 3. 기술적 issue 해결 과정

- Blue/Green 배포 자동화 구축하기
- Lambda와 CloudFront, S3를 이용하여 온디맨드 이미지 리사이징을 구축한 과정

<a id="architecture"></a>
## 4. 프로젝트 아키텍처

![image](https://github.com/road-maker/road-maker-spring/assets/60874549/3a0a5263-fe33-4df0-827e-504b50dffbcc) 

[//]: # (<a id="poster"></a>)

[//]: # (## 5. 프로젝트 포스터)

[//]: # (![ROADMAKER POSTER]&#40;https://github.com/road-maker/road-maker-spring/assets/60874549/03e43fc1-e4ed-46ed-8330-d74cd11ff934&#41;)

<a id="commit-convention"></a>
## 5. 커밋 컨벤션

본 프로젝트에서는 AngularJS commit Convention을 채택했습니다.

> [#이슈 번호] 이슈 타입: 커밋 메시지

### 5.1. 타입 목록

Type | Description
-- | --
Feat | 새로운 기능 추가
Fix | 버그 수정 또는 typo
Refactor | 리팩토링
Design | CSS 등 사용자 UI 디자인 변경
Comment | 필요한 주석 추가 및 변경
Style | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우(함수/변수명 변경은 포함)
Test | 테스트(테스트 코드 추가/수정/삭제), 테스트 리팩토링
Perf | 성능 개선
Init | 프로젝트 초기 생성
Rename | 파일 혹은 폴더명을 수정하거나 옮기는 경우
Remove | 파일을 삭제하는 작업만 수행하는 경우
Docs | 문서 작업, 수정
Build | 빌드 관련 파일 수정
CI | CI 관련 설정 수정



