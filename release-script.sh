#!/usr/bin/env bash

# Argument validation
if [[ -z "$1" ]];
 then
   echo "Tag version 입력 누락";
   exit 1;
  else
    echo "Tag version: $1";
  esle;
fi

# java build
echo "## 기존 빌드 파일 삭제 ##"
./gradlew clean

## QueryDsl 까지 빌드
echo "## 프로젝트 빌드 시작 ##"
./gradlew bootJar

# docker image 생성
echo "## 도커 이미지 생성 ##"
docker build -t "flowertaekk/teamdev6:$1" --platform linux/amd64 .

# docker image 업로드
docker push "flowertaekk/teamdev6:$1"

# 종료
exit 0;