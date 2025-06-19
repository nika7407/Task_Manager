FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew installDist

CMD ./build/install/secondvers/bin/secondvers
