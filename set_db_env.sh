#!/bin/zsh

# Database connection environment variables
export DB_HOST="your_database_host"
export DB_PORT="your_database_port"
export DB_NAME="your_database_name"
export DB_USERNAME="your_database_username"
export DB_PASSWORD="your_database_password"

# Application port environment variable
export APPLICATION_PORT="your_application_port"

# JWT configuration
export JWT_SECRET="your_jwt_secret"
export JWT_EXPIRATION="your_jwt_expiration"  # 15 minutes in milliseconds

# Print confirmation
echo "PostgreSQL environment variables set:"
echo "DB_HOST: $DB_HOST"
echo "DB_PORT: $DB_PORT"
echo "DB_NAME: $DB_NAME"
echo "DB_USERNAME: $DB_USERNAME"
echo "DB_PASSWORD: ${DB_PASSWORD:0:1}****"
echo "APPLICATION_PORT: $APPLICATION_PORT"
echo "JWT_SECRET: ${JWT_SECRET:0:10}****"
echo "JWT_EXPIRATION: $JWT_EXPIRATION (15 minutes)"

# Instructions for application launch
echo ""
echo "To use these variables with Spring Boot, run:"
echo "./gradlew bootRun --args='--spring.profiles.active=prod --spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} --spring.datasource.username=${DB_USERNAME} --spring.datasource.password=${DB_PASSWORD} --jwt.secret=${JWT_SECRET} --jwt.expiration=${JWT_EXPIRATION}'"