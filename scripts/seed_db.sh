#!/bin/bash

if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
    echo "DB credentials loaded."
else
    echo "Error loading DB credentials."
    exit 1
fi

LOCAL_TMP_CONF=".tmp_mysql_creds.cnf"
CONTAINER_TMP_PATH="/tmp/.my.cnf"

echo "Starting database seed process for: $MYSQL_DATABASE"

printf "[client]\nuser=%s\npassword=%s\nhost=localhost\n" \
       "$MYSQL_USER" "$MYSQL_ROOT_PASSWORD" > "$LOCAL_TMP_CONF"

if docker cp "$LOCAL_TMP_CONF" "$MYSQL_HOST":"$CONTAINER_TMP_PATH"; then
    echo "Credentials transferred."
else
    echo "Error: container is not running."
    rm "$LOCAL_TMP_CONF"
    exit 1
fi

echo "Injecting seed data from dev_seed.sql"
docker exec -i "$MYSQL_HOST" mysql --defaults-extra-file="$CONTAINER_TMP_PATH" "$MYSQL_DATABASE" < dev_seed.sql

if [ $? -eq 0 ]; then
    echo "Database seeded successfully."
else
    echo "Error seed failed."
fi

docker exec -i "$MYSQL_HOST" rm "$CONTAINER_TMP_PATH"
rm "$LOCAL_TMP_CONF"

echo "Exiting process."