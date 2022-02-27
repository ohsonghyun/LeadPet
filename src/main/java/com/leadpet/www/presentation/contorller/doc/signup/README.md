## UserController API 사양 정리

### 1) POST : /v1/user/signup

#### REQUEST

| 필드명                | 변수 타입  | 필수     | 예시                          | 비고               |
|--------------------|--------|--------|-----------------------------|------------------|
| type               | String | o      | kakao, apple, email, google | 공통               |
| uid                | String | o      | -                           | 공통               |
| email              | String | 조건부 필수 | -                           | 공통: 이메일로그인이라면 필수 |
| password           | String | 조건부 필수 | -                           | 공통: 이메일로그인이라면 필수 |
| profileImage       | String | x      | -                           | 공통               |
| name               | String | o      | -                           | 공통               |
| userType           | String | o      | normal, shelter             | 공통               |
| shelterName        | String | x      | -                           | 보호소 전용           |
| shelterAddress     | String | x      | -                           | 보호소 전용           |
| shelterPhoneNumber | String | x      | -                           | 보호소 전용           |
| shelterManager     | String | x      | -                           | 보호소 전용           |
| shelterHomePage    | String | x      | -                           | 보호소 전용           |

#### RESPONSE

##### 정상

| 필드명 | 변수 타입  | 예시  | 비고                 |
|-----|--------|-----|--------------------|
| uid | String | -   | 가입 성공한 유저의 uid를 반환 |

##### 에러 : 이미 등록되어 있는 유저인 경우

| 필드명           | 변수 타입      | 예시                | 비고  |
|---------------|------------|-------------------|-----|
| error/        | Object     | -                 | -   |
| error/code    | HttpStatus | 409               | -   |
| error/message | String     | CONFLICT          | -   |
| error/detail  | String     | Error: 이미 존재하는 유저 | -   |

##### 에러 : 필수 데이터 입력이 누락된 경우

| 필드명           | 변수 타입      | 예시                  | 비고  |
|---------------|------------|---------------------|-----|
| error/        | Object     | -                   | -   |
| error/code    | HttpStatus | 400                 | -   |
| error/message | String     | BAD_REQUEST         | -   |
| error/detail  | String     | Error: 필수 입력 데이터 누락 | -   |