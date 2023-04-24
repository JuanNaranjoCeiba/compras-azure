FROM openjdk:11-jdk-slim
#Directorio de trabajo dentro del contenedor
WORKDIR /app

#Copia los archivos de Gradle y las dependencias del proyecto
COPY build.gradle gradlew ./
COPY gradle gradle
RUN chmod +x gradlew
RUN ./gradlew dependencies

#Copia el proyecto en el directorio de trabajo
COPY . .

#Construye el proyecto con Gradle y crea el archivo .jar
RUN ./gradlew build

#Expone el puerto de la aplicación
EXPOSE 8080

# Comando que se ejecuta cuando el contenedor se inicia
CMD ["java", "-jar", "./build/libs/microservicios-0.0.1-SNAPSHOT.jar"]