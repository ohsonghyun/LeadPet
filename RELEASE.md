# LeadPet_BE Release doc

## 로컬 작업

1. `docker login` 으로 도커에 로그인 되어 있는지 확인
    1. 안 되어 있으면 로그인 하기 (현재 `플라워데브`만 가능)
    2. `private repository`로 이미지를 저장하기 때문
2. Release tag version 확인하기
    1. dockerhub 에서!
3. `프로젝트 root`로 이동 후, `sh ./release-script.sh <릴리즈 번호 ex)v1.0>` 실행
4. `docker image ls` 커맨드로 생성된 이미지 + 태그 확인
5. DockerHub 에서도 확인 (tag 번호 제대로 확인하기!!)

## AWS 작업

1. EC2 접속
2. `docker login` 으로 도커에 로그인 되어 있는지 확인
3. EC2에 도커 설치

```shell
## update
sudo yum update -y
## install most recent package
sudo amazon-linux-extras install docker
## start the service docker
sudo service docker start
## add the ec2-docker user to the group
sudo usermod -a -G docker ec2-user
```

4. 도커 이미지 다운로드
5. 도커 이미지 실행
6. 동작 확인
