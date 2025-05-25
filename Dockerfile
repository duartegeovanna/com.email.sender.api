# Imagem base com Java 21 JDK (slim para menor tamanho)
FROM eclipse-temurin:21-jdk-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado pela aplicação para o container
COPY ./email-sender-api/target/email.sender.api*.jar app.jar

# Expõe a porta da aplicação (ajuste conforme necessário)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]