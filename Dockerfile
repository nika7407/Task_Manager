FROM gradle:8.8.0-jdk22

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew installDist

RUN ls -l build/install
RUN ls -l build/install/app/bin

CMD ["./build/install/app/bin/app"]


