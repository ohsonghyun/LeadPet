## UserController API 사양 정리

### 1) POST : /v1/user/login

#### REQUEST

| 필드명                | 변수 타입  | 필수     | 예시                          | 비고               |
|--------------------|--------|--------|-----------------------------|------------------|
| type               | String | o      | kakao, apple, email, google | 공통               |
| uid                | String | o      | -                           | 공통               |
| email              | String | 조건부 필수 | -                           | 공통: 이메일로그인이라면 필수 |
| password           | String | 조건부 필수 | -                           | 공통: 이메일로그인이라면 필수 |

#### RESPONSE

##### 정상

| 필드명          | 변수 타입  | 예시  | 비고                          |
|--------------|--------|-----|-----------------------------|
| uid          | String | -   | 가입 성공한 유저의 uid를 반환          |
| profileImage | String | -   | 가입 성공한 유저의 profileImage를 반환 |

##### 에러 : 존재하지 않는 유저

| 필드명           | 변수 타입      | 예시                | 비고  |
|---------------|------------|-------------------|-----|
| error/        | Object     | -                 | -   |
| error/code    | HttpStatus | 404               | -   |
| error/message | String     | NOT_FOUND         | -   |
| error/detail  | String     | Error: 존재하지 않는 유저 | -   |

##### 에러 : 필수 데이터 누락

| 필드명           | 변수 타입      | 예시                  | 비고  |
|---------------|------------|---------------------|-----|
| error/        | Object     | -                   | -   |
| error/code    | HttpStatus | 400                 | -   |
| error/message | String     | BAD_REQUEST         | -   |
| error/detail  | String     | Error: 필수 입력 데이터 누락 | -   |
