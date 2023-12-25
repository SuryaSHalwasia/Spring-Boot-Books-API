#!/bin/bash

# Function to check if PostgreSQL is ready to accept connections
wait_for_postgres() {
    until PGPASSWORD=password psql -h localhost -p 5432 -U postgres -d postgres -c '\q'
    do
        echo "Waiting for PostgreSQL to start..."
        sleep 5
    done
    echo "PostgreSQL is up and running!"
}

# Check PostgreSQL availability
wait_for_postgres

# Start your Java application
java -jar /path/to/your/java/application.jar
