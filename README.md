# 📚 개인 프로젝트 - 온라인 서점 웹사이트, 북마켓(BookMarket)

본 프로젝트는 Spring Boot를 활용한 백엔드 중심의 개인 포트폴리오 프로젝트입니다.
<br/>
<br/>
<br/>
* * *
## 📑 목차
[__1. 프로젝트 개요__](#-프로젝트-개요)
- [주제 및 목표](#-주제-및-목표)
- [역할](#-역할)
  <br/>

[__2. 주요 구현 기능__](#-주요-구현-기능)
- [도서 검색 API](#-도서-검색-API)
- [도서 구매 신청 API](#-도서-구매-신청-API)
- [리뷰 CRUD API](#-리뷰-CRUD-API)
- [희망 도서 신청 API](#-희망-도서-신청-API)
- [추천 도서 API](#-추천-도서-API)
  <br/>
  <br/>
  <br/>
  <br/>

* * *

## 📌 프로젝트 개요
#### 📅 기간 (예정)
2025.06.01 ~ 2025.07.20 (7주)

- 1~2주차 : DB 설계(ERD), API 설계(Swagger), Spring Boot 프로젝트 설정 및 검색 API 구현.
- 3~4주차 : 구매 신청 API, 리뷰 CRUD API, 희망 도서 신청 API 구현 및 테스트(Postman).
- 5주차 : 추천 도서 API 및 관리자 기능 추가, 전체 기능 테스트.
- 6주차 : AWS EC2에 배포
- 7주차: GitHub Actions로 CI/CD 설정, 포트폴리오 문서 작성
  <br/>

#### 🖍 주제 및 목표
__[주제]__
- 온라인 서점 웹사이트
  <br/>

__[목표]__
1. Spring Boot, JPA, QueryDSL 기반 REST API 백엔드 시스템 구현.
2. AWS 배포 및 CI/CD 파이프라인 구축 경험.
3. Git/GitHub로 버전 관리 및 브랜치 전략 숙지.
   <br/>

#### 👥 역할
- 개인 프로젝트: 기획, 설계, 개발, 테스트, 배포 전 과정 담당.
  <br/>

__[사용기술 및 개발환경]__
- OS : Mac
- Tools  :  IntelliJ IDEA, SQL Developer, Git, GitHub, Postman, Swagger
- Back-end  :  JDK21, Spring Boot, JPA, QueryDSL, MariaDB 11.7.2
- Library  :  Lombok 1.18.38, Spring Security, JUnit 5
- 배포  :  AWS EC2, GitHub Actions (CI/CD)
  <br/>
  <br/>
  <br/>

* * *

## 🙋🏻‍♀ 주요 구현 기능
#### 🔍 도서 검색 API

- QueryDSL 활용 동적 쿼리로 제목, 저자 검색 지원.
- 페이징 처리 및 조건별 필터링 가능.
<details>
	<summary>코드보기</summary>

</details> 
<br/>

#### 🎖 도서 구매 신청 API

- 재고 확인 및 중복 구매 방지 로직 추가.
- 트랜잭션 관리로 데이터 정합성 보장.
<details>
	<summary>코드보기</summary>

</details>
<br/>

#### __🔎 리뷰 CRUD API__

- 작성자 권한 검증 로직 추가.
<details>
	<summary>코드보기</summary>

 </details>
<br/>

#### __📖 희망 도서 신청 API__

- 사용자가 새로운 도서 입고 요청 가능.
- 관리자 페이지에서 요청 목록 확인 후 도서 등록 가능.
<details>
	<summary>코드보기</summary>
</details>
<br/>

#### __📊 추천 도서 API__

- 구매/조회수 기준 상위 5권 조회.
- 관리자 지정 추천 도서 등록/삭제 기능.
  <details>
  <summary>코드보기</summary>
</details>
<br/>
<br/>
<br/>
