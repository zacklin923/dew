FROM openjdk:8-alpine

COPY ./run-java.sh ./
COPY ./serv.jar ./

RUN chmod 777 ./run-java.sh

EXPOSE 8080

CMD [ "./run-java.sh" ]
