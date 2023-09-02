# RoadMaker

로드메이커는 누구나 학습 로드맵을 만들고, 공유할 수 있는 플랫폼 입니다.

![image](https://github.com/road-maker/road-maker-spring/assets/60874549/9bcc467d-9963-48c1-9e61-afb6722c7bff)

본 문서는 RoadMaker의 백엔드에 초점을 맞춰 작성됐습니다.

## 목차
1. [프로젝트 개요](#overview)
2. [서비스 소개](#intro)
3. [기술적 issue 해결 과정](#issue)
4. [프로젝트 아키텍처](#frontend)
5. [프로젝트 포스터](#poster)
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

### 1.3. 웹사이트

[바로 가기](http://roadmaker.site)

- demo 계정:
- demo 계정:
- 시연 연상:
- 현장 발표 영상:

<a id="intro"></a>
## 2. 서비스 소개

개발자들은 개발 공부를 할 때 로드맵을 참고한다. developer-roadmap 리포지토리는 24만개의 star를 받았을 정도로 인기가 많다.

기존 로드맵을 사용하다보니 나도 로드맵을 만들고 공유할 수 있으면 좋겠다는 생각이 들었고 그렇게 RoadMaker를 개발하게 되었습니다.

### 2.1. 주요 기능

1. 로드맵 자동 생성
   - GPT API를 이용하여 로드맵 초안을 자동 생성할 수 있습니다.
2. 로드맵 참여 및 진행 
3. 댓글 작성
4. 검색 기능

<a id="issue"></a>
## 3. 기술적 issue 해결 과정

- Blue/Green 배포 자동화 구축하기
- Thread Pool을 이용하여 GPT API 응답 속도를 줄인 과정
- lambda와 cloudfront를 이용하여 온디맨드 이미지 리사이징을 구축한 과정
- MySQL full text search를 이용하여 검색 기능을 구현한 과정

<a id="architecture"></a>
## 4. 프로젝트 아키텍처

<a id="poster"></a>
## 5. 프로젝트 포스터

<a id="commit-convention"></a>
## 6. Commit Convention

본 프로젝트에서는 AngularJS commit Convention을 채택했습니다.

### 6.1. Commit Message & Description
> [#Issue Number] Type: Commit Title Description goes on here

### 6.2. Commit Rules

> Reference: [커밋을 잘게 쪼개자 - 커밋은 언제 하는 것이 가장 좋을까?](https://jaeheon.kr/257)

1. 커밋 단위는 메세지 한 줄로 설명할 수 있는 행동
2. 간단한 커밋을 지향해야 한다: 한 커밋에 한 액션


