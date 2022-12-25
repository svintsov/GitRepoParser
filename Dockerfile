FROM openjdk:17-alpine

COPY ./target/git-repo-parser.jar /app/application.jar
COPY ./distribution/docker/run.sh /app/run.sh

RUN find /app -type f -exec chmod 750 {} \;

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["/app/run.sh"]