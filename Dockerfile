FROM openjdk:11
COPY ./build /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "./libs/leadpet-0.0.1-SNAPSHOT.jar"]