FROM gradle:8.8.0-jdk22

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew installDist

RUN ls -l build/install/app/bin
RUN file build/install/app/bin/app
RUN head -20 build/install/app/bin/app
RUN chmod +x build/install/app/bin/app

CMD ["./build/install/app/bin/app"]


