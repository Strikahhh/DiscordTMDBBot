FROM openjdk:17-alpine
COPY target/DiscordTMDBBot-1.0-SNAPSHOT-jar-with-dependencies.jar DiscordTMDBApp.jar
ENTRYPOINT ["java", "-jar", "DiscordTMDBApp.jar"]
