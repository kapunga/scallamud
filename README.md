# ScallaMUD

A new modern MUD

## Building and Running the Client

The client can be packaged as a fat JAR using the SBT assembly plugin:

```bash
# Build client using the provided SBT alias
sbt buildClient
```

This creates a standalone JAR file at `client/target/scala-3.3.5/scallamud-client.jar`.

You can run the client directly with:

```bash
java -jar client/target/scala-3.3.5/scallamud-client.jar
```

Or use the provided shell script, which will build the client if needed:

```bash
# Run the client (builds it first if necessary)
./whisker.sh
```

## Building and Running the Server

Build and stage the server using:

```bash
# Stage the server for local execution or Docker deployment
sbt buildServer
```

This prepares the server in `server/target/universal/stage/`.

You can run the server locally after staging:

```bash
./server/target/universal/stage/bin/scallamud-server
```

The server can be run in a Docker container:

1. First, stage the server:
```bash
sbt buildServer
```

2. Then build and run the Docker container:
```bash
cd docker
docker-compose up
```

This will start the server container and expose it on port 8080.

To run in detached mode:
```bash
docker-compose up -d
```

To stop the container:
```bash
docker-compose down
```
