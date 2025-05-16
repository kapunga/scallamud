#!/bin/bash

# Get the directory where the script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Path to the client jar
CLIENT_JAR="$SCRIPT_DIR/client/target/scala-3.3.5/scallamud-client.jar"

# Check if the jar exists, if not, build it
if [ ! -f "$CLIENT_JAR" ]; then
  echo "Client jar not found. Building it now..."
  cd "$SCRIPT_DIR" && sbt buildClient
  
  # Check if the build was successful
  if [ ! -f "$CLIENT_JAR" ]; then
    echo "Failed to build client jar. Exiting."
    exit 1
  fi
fi

# Run the client with all passed arguments
java -jar "$CLIENT_JAR" "$@"