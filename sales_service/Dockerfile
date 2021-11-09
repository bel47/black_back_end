FROM openjdk:latest
 
ADD target/*.jar sales_service.jar

EXPOSE 8003
ENTRYPOINT ["java","-jar","sales_service.jar"]
