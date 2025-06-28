FROM gradle:8.8.0-jdk22

WORKDIR /app

COPY . .

COPY src/main/resources/keys /app/src/main/resources/keys

RUN chmod +x ./gradlew

RUN ./gradlew installDist

CMD ["./build/install/app/bin/app"]


