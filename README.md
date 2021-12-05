# tomorr
[![build](https://github.com/kushtrimh/tomorr/actions/workflows/maven.yml/badge.svg)](https://github.com/kushtrimh/tomorr/actions/workflows/maven.yml)

## Required environment variables/properties

It is required either to define the environment variables specified below, or use the properties below to create a new `application.yml` and use it as external configuration.
Some files as well, that do not require environment should be updated.

```yaml
spring:
  pid:
    file: ${TOMORR_PID_FILE}
  mail:
    host: ${TOMORR_MAIL_HOST}
    username: ${TOMORR_MAIL_USERNAME}
    password: ${TOMORR_MAIL_PASSWORD}
    port: ${TOMORR_MAIL_PORT}
    properties:
      mail:
        auth: true
        starttls:
          enable: true
          required: true
datasource:
  url: ${TOMORR_DB_URL}
  username: ${TOMORR_DB_USERNAME}
  password: ${TOMORR_DB_PASSWORD}
spotify:
  client-id: ${TOMORR_SPOTIFY_CLIENT_ID}
  client-secret: ${TOMORR_SPOTIFY_CLIENT_SECRET}
redis:
  host: ${TOMORR_REDIS_HOST}
  port: ${TOMORR_REDIS_PORT}
rabbitmq:
  host: ${TOMORR_RABBITMQ_HOST}
  port: ${TOMORR_RABBITMQ_PORT}
```

## Using Docker

When using *docker*, use `TOMORR_ADDITIONAL_CONFIG` environment variable in addition, to specify the location of other `application.yml` files.

### Running the container
`docker container run -p 8098:8098 -d --name tomorr -v /home/config/application.yml:/config/application.yml kushtrimh/tomorr`