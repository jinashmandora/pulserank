FROM quay.io/debezium/connect-base:3.6.0.Final

ARG DEBEZIUM_VERSION=3.6.0.Final

# Download and extract ONLY the PostgreSQL connector
RUN mkdir -p /kafka/connect/debezium-connector-postgres && \
    cd /kafka/connect/debezium-connector-postgres && \
    curl -sO https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/${DEBEZIUM_VERSION}/debezium-connector-postgres-${DEBEZIUM_VERSION}-plugin.tar.gz && \
    tar -xzf debezium-connector-postgres-${DEBEZIUM_VERSION}-plugin.tar.gz && \
    rm debezium-connector-postgres-${DEBEZIUM_VERSION}-plugin.tar.gz