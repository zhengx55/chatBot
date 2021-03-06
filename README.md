# Team 7 Chatbot

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Build and Installation](#build-and-installation)
- [Usages](#usage)
- [Major libraries and dependencies](#major-libraries-and-dependencie)

## Features

As a chatbot, our product has basic funtionality of giving out information based on user given queries. It is designed to be closed-domain chatbot with information-fetching system. It also has the features specifically for admin and normal users.


*  Admins have the ability to view all the anonymous user feedbacks and capabilities to start cralwer, upload and check the documents held on IBM Watson. 
    *  The crawler is now configured to be single-thread but has the option of multithread, other features can also be configured in __backend/src/main/com.cscc01.chatbot.backend.crawlerCrawlerConfiguration__ class, i.e. MAX_PAGES_FETCH for total amount of pages that the crawler will fetech and MAX_DEPTH_CRAWLING for crawling depth
*  Security of the communication between static frontend and backend is ensured by JWT Oauth token.
*  Logger is enable on backend as well to ensure backlog if something on backend fails in someway.


## Getting Started

### Prerequisites

1. Maven

2. JDK 1.8

3. npm

### Build and Installation
You can either run provided script or commands below.

At directory `Backend/` 
```
mvn clean install
mvn spring-boot:run
```
Open up a new terminal console, at directory `frontend/UI/ui` 
```

npm install
npm start
```
As a result, the frontend UI will be automatically open in the browser.

To run the provided script run following commands in different terminals at root dir.
Start backend:
``
sh backend-build.sh
``
Start frontend:
``
sh frontend-build.sh
``

As a result, the frontend UI will be automatically open in the browser.

### Testing
Backend tests is under `backend/src/test`, to run backend tests, run command `mvn test`.

Frontend tests is under `frontend/UI/ui/src/__test__`, to run backend tests, run command `npm run test`.

### Usage

When open the page, users(including admin and normal user) need to either login or signup to has access on their main pages.


*  For admins, they can login to have access on the admin dashboard, consisting of two main parts as following:
    *  Documents management
        *  Cralwer option
        *  Document upload
        *  Documents list
    *  User feedback list
    *  Note: we have provided few admin accounts
        *  Username: __dfiadmin__ Password: __admin123__
        *  Username: __admin__ Password: __n^:E6JDTR?dhn:#f__
        *  Username: __abbas__ Password: __557?ZcZcQ?}L$n$[__
*  For normal users, they can login to use chatbot main functionality by querying the information. If the query is not recognized by backend, we will let users know about it. If a document is returned, users can preview the document as well.

### API
RESTful API is followed on backend. If API call is needed, do as following:

* #### Authentication

get an access token by following:
```
curl chatbot:L80eUZjHnafVOG5TWRenSGfiMkPL2j03@localhost:8080/oauth/token -d grant_type=password -d username=<username> -d password=<password>
```
you will receive token representation like following:
```
{
"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY2hhdGJvdFJlc3RBcGkiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTYyODM5NzkyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiJmYTUyYTcxMy02OTQ3LTQ0ODMtOTNmMy03M2QyNzE4YzkzOGEiLCJjbGllbnRfaWQiOiJjaGF0Ym90In0.J85XvcjXP0qdBAGEcKEGrkAb29KdQ_b49fENKtaqciA",
"token_type":"bearer",
"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY2hhdGJvdFJlc3RBcGkiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZmE1MmE3MTMtNjk0Ny00NDgzLTkzZjMtNzNkMjcxOGM5MzhhIiwiZXhwIjoxNTY1Mzg4NTkyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiIxODJiNzgwYi1jNmI2LTRiYjgtYjk0Mi00OTY5ZjQ3YmQxOGEiLCJjbGllbnRfaWQiOiJjaGF0Ym90In0.wV-FQji61Ou7sgBBuIK-tR4aKMT5a4onCaUo_QdjBmo",
"expires_in":43199,
"scope":"read write",
"jti":"fa52a713-6947-4483-93f3-73d2718c938a"}%
```

* #### API request with Authorization header: Bearer <access_token>
```
curl http://localhost:8080/query/ -H “Authorization: Bearer GENERATED_AUTH_TOKEN” -d ......
```

## Major libraries and Dependencie

* Frontend
    * React
* Backend
    * Spring Boot
    * Spring JPA & Hibernate
    * Crawler4j
    * JWT
    * Apache Lucene
    * Tika
    * Slf4j
    * IBM Watson SDK

## Video link
https://www.youtube.com/watch?v=CHGvs_rvlfI&feature=youtu.be
