# tomorr

## Associated projects

*Tomorr Terraform (_for provisioning the resources on AWS_)* - https://github.com/kushtrimh/tomorr-terraform

## The project

Tomorr is a service that will notify you by email when an artist you follow on _tomorr_ has a new release.
It uses Spotify behind the scenes to get the artist's data and its releases.

This project is mostly done for the purpose of learning different technologies,
and will be updated with different technologies in the future. I will not be deploying this project for other people to
use.

You need to generate a _client id_ and _client secret_ from Spotify in order to use the service.
For more information about that, please check https://developer.spotify.com/dashboard/applications.

## API

### Searching for artists

    GET /api/v1/artist/search

    Parameters:
        name: The artist's name
        external: true/false flag that defines whether only the local database should be used or not

    Response:
        {
            "artists": [
                {
                    "id": "1Xyo4u8u745435MpatF05PJ",
                    "name": "Artist Name 1"
                },
                {
                    "id": "0Y5tJX7345656lOH1tJY",
                    "name": "Artist Name 2"
                }
            ]
        }

### Following an artist

    POST /api/v1/artist/follow

    Request:
        {
            "user": "youremail@example.com",
            "artistId": "0Y5tJX7345656lOH1tJY"
        }

    Response status code:
        200 - Success
        400 - Bad request
        404 - Artist not found
        409 - Artist already followed

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

| Environment variable           | Description                           | Type    |
|--------------------------------|---------------------------------------|---------|
| `TOMORR_PID_FILE`              | PID file location.                    | string  |
| `TOMORR_MAIL_HOST`             | Mail host.                            | string  |
| `TOMORR_MAIL_USERNAME`         | Mail username.                        | string  |
| `TOMORR_MAIL_PASSWORD`         | Mail password.                        | string  |
| `TOMORR_MAIL_AUTH`             | Enable authentication for mail.       | boolean |
| `TOMORR_MAIL_TLS`              | Enable TLS for mail.                  | boolean |
| `TOMORR_MAIL_PORT`             | Mail port.                            | integer |
| `TOMORR_MAIL_FROM`             | Address where the email is sent from. | string  |
| `TOMORR_DB_URL`                | Database URL.                         | string  |
| `TOMORR_DB_USERNAME`           | Database username.                    | string  |
| `TOMORR_DB_PASSWORD`           | Database password.                    | string  |
| `TOMORR_SPOTIFY_CLIENT_ID`     | Spotify client id.                    | string  |
| `TOMORR_SPOTIFY_CLIENT_SECRET` | Spotify client secret.                | string  |
| `TOMORR_REDIS_HOST`            | Redis host.                           | string  |
| `TOMORR_REDIS_PORT`            | Redis port.                           | integer |
| `TOMORR_RABBITMQ_HOST`         | RabbitMQ host.                        | string  |
| `TOMORR_RABBITMQ_PORT`         | RabbitMQ port.                        | integer |
| `TOMORR_RABBITMQ_USERNAME`     | RabbitMQ username.                    | string  |
| `TOMORR_RABBITMQ_PASSWORD`     | RabbitMQ password.                    | string  |
| `TOMORR_RABBITMQ_SSL`          | RabbitMQ SSL status.                  | boolean |

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
