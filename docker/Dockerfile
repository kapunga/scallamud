FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S scallamud && adduser -S -G scallamud scallamud

WORKDIR /opt/scallamud

COPY --chown=scallamud:scallamud ./server/target/universal/stage/ /opt/scallamud/

USER scallamud

EXPOSE 8080

ENTRYPOINT ["/opt/scallamud/bin/scallamud-server"]