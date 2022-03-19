FROM gradle:7.4.1-jdk11-alpine
COPY --chown=gradle:gradle . . 
WORKDIR .
RUN gradle build -x test --no-daemon
EXPOSE 8081
CMD ["gradle", "bootRun"]
