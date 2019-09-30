FROM java:8  
COPY ./target/*shaded.jar /opt/heigvd/server.jar  
WORKDIR /opt/heigvd
CMD ["java", "-jar", "server.jar"]  