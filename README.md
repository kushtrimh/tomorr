# tomorr
[![build](https://github.com/kushtrimh/tomorr/actions/workflows/maven.yml/badge.svg)](https://github.com/kushtrimh/tomorr/actions/workflows/maven.yml)

## Associated projects

Tomorr Terraform (for provisioning the resources on AWS) - https://github.com/kushtrimh/tomorr-terraform
Tomorr AMI (for making the AMI requested by tomorr instances on AWS) - https://github.com/kushtrimh/tomorr-ami

## Running the project

### Using Docker

#### Building the image

Run `docker build -t kushtrimh/tomorr .` inside the project directory to build the image.

#### Running the container

Run

`docker container run -p 8098:8098 -d --name tomorr kushtrimh/tomorr`

or

`docker container run -p 8098:8098 -d --name tomorr -v /home/config/application.yml:/etc/tomorr/application.yml kushtrimh/tomorr`

in case you want to use an externalized configuration file.

#### Running with `docker-compose`

When running with `docker-compose`, create an `.env` file in the project directory, and add the environment variables specified below.

```shell
TOMORR_EXTERNAL_CONFIG=<DIRECTORY-TO-EXTERNAL-CONFIG>/application.yml
```

The external `application.yml` properties should contain the properties to needed to run the application when using `docker-compose`.
Please check [external configuration](#external-configuration) for a ready-to-use configuration file for this case.

### Without `docker`

It is required either to define the environment variables specified below, or create a new `application.yml` and use it as external configuration.
For an example of how the external configuration file should look like, please check [external configuration](#external-configuration) section.

#### Environment variables

| Environment variable           | Description            |
|--------------------------------|------------------------|
| `TOMORR_PID_FILE`              | PID file location.     |
| `TOMORR_MAIL_HOST`             | Mail host.             |
| `TOMORR_MAIL_USERNAME`         | Mail username.         |
| `TOMORR_MAIL_PASSWORD`         | Mail password.         |
| `TOMORR_MAIL_PORT`             | Mail port.             |
| `TOMORR_DB_URL`                | Database URL.          |
| `TOMORR_DB_USERNAME`           | Database username.     |
| `TOMORR_DB_PASSWORD`           | Database password.     |
| `TOMORR_SPOTIFY_CLIENT_ID`     | Spotify client id.     |
| `TOMORR_SPOTIFY_CLIENT_SECRET` | Spotify client secret. |
| `TOMORR_REDIS_HOST`            | Redis host.            |
| `TOMORR_REDIS_PORT`            | Redis port.            |
| `TOMORR_RABBITMQ_HOST`         | RabbitMQ host.         |
| `TOMORR_RABBITMQ_PORT`         | RabbitMQ port.         |

## External configuration

The `application.yml` below shows an example of an external configuration file, and it also serves as a ready-to-use external configuration
when using `docker-compose`. Please keep in mind updating `client-id` and `client-secret`.

```yaml
spring:
  pid:
    file: /var/run/tomorr.pid
  mail:
    host: mail
    port: 1025
datasource:
  url: jdbc:postgresql://postgres:5432/tomorr
  username: postgres
  password: tomorrpostgres
spotify:
  client-id: <SPOTIFY_CLIENT_ID>
  client-secret: <SPOTIFY_CLIENT_SECRET>
redis:
  host: redis
  port: 6379
rabbitmq:
  host: rabbitmq
  port: 5672
  username: guest
  password: guest
  use-ssl: false
```

### Additional configuration for other environments

Properties below are properties which either are missing on the local/development environment, or their values needs to be changed
in order to work as intended in production environments.

```yaml
spring:
  mail:
    properties:
      mail:
        auth: true
        starttls:
          enable: true
          required: true
```
