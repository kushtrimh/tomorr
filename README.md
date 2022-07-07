# tomorr

## Associated projects

*Tomorr Terraform (_for provisioning the resources on AWS_)* - https://github.com/kushtrimh/tomorr-terraform

## Running the project

### Using Docker

#### Building the image

Run `docker build -t <IDENTIFIER>/tomorr:<TAG> .` inside the project directory to build the image.

#### Running the container

Run

`docker container run -p 8098:8098 -d --name tomorr <IDENTIFIER>/tomorr:<TAG>`

or

`docker container run -p 8098:8098 -d --name tomorr -v /home/config/application.yml:/etc/tomorr/application.yml <IDENTIFIER>/tomorr:<TAG>`

in case you want to use an externalized configuration file.

#### Running with `docker-compose`

When running with `docker-compose`, create an `.env` file in the project directory, and add the environment variables
specified below.

```shell
TOMORR_EXTERNAL_CONFIG=<DIRECTORY-TO-EXTERNAL-CONFIG>/application.yml
```

The external `application.yml` properties should contain the properties to needed to run the application when
using `docker-compose`. Please check [external configuration](#external-configuration) for a ready-to-use configuration
file for this case.

### Environment variables

If you do not plan on using an external `application.yml` file, 
then you can use environment variables to configure the application.

| Environment variable           | Description                          | Type    |
|--------------------------------|--------------------------------------|---------|
| `TOMORR_PID_FILE`              | PID file location.                   | string  |
| `TOMORR_MAIL_HOST`             | Mail host.                           | string  |
| `TOMORR_MAIL_USERNAME`         | Mail username.                       | string  |
| `TOMORR_MAIL_PASSWORD`         | Mail password.                       | string  |
| `TOMORR_MAIL_AUTH`             | Enable authentication for mail.      | boolean |
| `TOMORR_MAIL_TLS`              | Enable TLS for mail.                 | boolean |
| `TOMORR_MAIL_PORT`             | Mail port.                           | integer |
| `TOMORR_MAIL_FROM`             | Address where the email is sent from | string  |
| `TOMORR_DB_URL`                | Database URL.                        | string  |
| `TOMORR_DB_USERNAME`           | Database username.                   | string  |
| `TOMORR_DB_PASSWORD`           | Database password.                   | string  |
| `TOMORR_SPOTIFY_CLIENT_ID`     | Spotify client id.                   | string  |
| `TOMORR_SPOTIFY_CLIENT_SECRET` | Spotify client secret.               | string  |
| `TOMORR_REDIS_HOST`            | Redis host.                          | string  |
| `TOMORR_REDIS_PORT`            | Redis port.                          | integer |
| `TOMORR_RABBITMQ_HOST`         | RabbitMQ host.                       | string  |
| `TOMORR_RABBITMQ_PORT`         | RabbitMQ port.                       | integer |
| `TOMORR_RABBITMQ_USERNAME`     | RabbitMQ username.                   | string  |
| `TOMORR_RABBITMQ_PASSWORD`     | RabbitMQ password.                   | string  |
| `TOMORR_RABBITMQ_SSL`          | RabbitMQ SSL status.                 | boolean |

## External configuration

The `application.yml` below shows an example of an external configuration file, and it also serves as a ready-to-use
external configuration when using `docker-compose`. Please keep in mind updating `client-id` and `client-secret`.

```yaml
spring:
  pid:
    file: /var/run/tomorr.pid
  mail:
    host: mail
    port: 1025
    username: 
    password: 
    properties:
      mail:
        auth: false
        starttls:
          enable: false
          required: false
mail:
  from: noreply@localhost.com
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
