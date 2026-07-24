#!/bin/bash
set -e

# 1. Fetch the running Flink Job ID
JOB_ID=$(docker compose exec flink-jobmanager flink list | grep -E "RUNNING|INITIALIZING" | awk '{print $4}' | head -n 1 | tr -d '\r\n')

if [ -n "$JOB_ID" ]; then
    echo "Found active streaming job: $JOB_ID. Capturing state via Savepoint..."

    # Gracefully stop the job and capture the 30-min window state into our named volume
    SAVEPOINT_LINE=$(docker compose exec flink-jobmanager flink stop "$JOB_ID" --savepointPath /opt/flink/state/savepoints)

    # Extract the resulting savepoint directory string output
    SAVEPOINT_PATH=$(echo "$SAVEPOINT_LINE" | grep -o '/opt/flink/state/savepoints/savepoint-[a-zA-Z0-9.-]*' | head -n 1 | tr -d '\r\n')
    echo "Savepoint securely committed to: $SAVEPOINT_PATH"

    # Temporarily append the savepoint argument to the Docker Compose startup block
    export SAVEPOINT="--fromSavepoint $SAVEPOINT_PATH"
else
    echo "No active job found. Performing a clean start..."
    export SAVEPOINT=""
fi

# 2. Trigger Gradle 9 compilation and rebuild Docker image layers from root context
echo "Rebuilding Gradle artifacts and Docker containers..."
./gradlew streaming:ranking-job:clean shadowJar
docker compose build flink-jobmanager flink-taskmanager

# 3. Spin the infrastructure up with the updated execution command parameters
echo "Launching Flink topology cluster nodes..."
docker compose up -d --force-recreate flink-jobmanager flink-taskmanager

echo "Deployment update cycle completed successfully!"