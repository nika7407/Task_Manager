FROM gradle:8.3.0-jdk22

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew installDist

CMD ./build/install/secondvers/bin/secondvers
