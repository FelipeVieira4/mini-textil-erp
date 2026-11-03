FROM maven:3.9-eclipse-temurin-25-alpine
WORKDIR /app

RUN apk add --no-cache curl tar nodejs npm && \
    curl -Lo /tmp/jbr.tar.gz \
      https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25.0.4-linux-musl-x64-b508.27.tar.gz && \
    mkdir -p /opt/jbr && \
    tar -xzf /tmp/jbr.tar.gz -C /opt/jbr --strip-components=1 && \
    rm /tmp/jbr.tar.gz && \
    mkdir -p /opt/jbr/lib/hotswap && \
    curl -Lo /opt/jbr/lib/hotswap/hotswap-agent.jar \
          https://repo1.maven.org/maven2/org/hotswapagent/hotswap-agent/2.0.3/hotswap-agent-2.0.3.jar

ENV JAVA_HOME=/opt/jbr
ENV PATH="$JAVA_HOME/bin:$PATH"

COPY pom.xml .
RUN mvn dependency:go-offline

EXPOSE 8080 5005

ENTRYPOINT ["mvn", "spring-boot:run", \
  "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -XX:+AllowEnhancedClassRedefinition -XX:HotswapAgent=fatjar"]