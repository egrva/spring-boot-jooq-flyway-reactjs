#!/bin/bash

# Immediately exits if any error occurs during the script
# execution. If not set, an error could occur and the
# script would continue its execution.
set -o errexit


# Creating an array that defines the environment variables
# that must be set. This can be consumed later via arrray
# variable expansion ${REQUIRED_ENV_VARS[@]}.
readonly REQUIRED_ENV_VARS=(
  "FILLA_DB_USER"
  "FILLA_DB_PASSWORD"
  "FILLA_DB_DATABASE"
  "POSTGRES_USER")


# Main execution:
# - verifies if all environment variables are set
# - runs the SQL code to create user and database
main() {
  check_env_vars_set
  init_user_and_db
  init_structure
}


# Checks if all of the required environment
# variables are set. If one of them isn't,
# echoes a text explaining which one isn't
# and the name of the ones that need to be
check_env_vars_set() {
  for required_env_var in ${REQUIRED_ENV_VARS[@]}; do
    if [[ -z "${!required_env_var}" ]]; then
      echo "Error:
    Environment variable '$required_env_var' not set.
    Make sure you have the following environment variables set:
      ${REQUIRED_ENV_VARS[@]}
Aborting."
      exit 1
    fi
  done
}


# Performs the initialization in the already-started PostgreSQL
# using the preconfigured POSTGRE_USER user.
init_user_and_db() {
  echo "Creating database..."
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
     CREATE USER $FILLA_DB_USER WITH SUPERUSER PASSWORD '$FILLA_DB_PASSWORD';
     CREATE DATABASE $FILLA_DB_DATABASE;
EOSQL
}

# Performs the initialization in the already-started PostgreSQL
# using the preconfigured POSTGRE_USER user.
init_structure() {
  echo "Creating structure..."
#  psql -v ON_ERROR_STOP=1 --username "${FILLA_DB_USER}" -d "${FILLA_DB_DATABASE}" -f /structure.sql
  psql -U ${POSTGRES_USER} -c "ALTER DATABASE ${FILLA_DB_DATABASE} OWNER TO ${FILLA_DB_USER};"
  psql -U ${POSTGRES_USER} -c "ALTER ROLE $FILLA_DB_USER WITH NOSUPERUSER;"

}

# Executes the main routine with environment variables
# passed through the command line.
main "$@"
