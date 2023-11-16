# images

A distributed async image processing service.

## TODO

### Infrastructure

* [ ] Multi-project skeleton.
* [x] Setup Postgresql database.
* [ ] Setup database migrations.
* [ ] Setup messaging broker.

### API Server

* [ ] Add register endpoint.
* [ ] Add get token endpoint.
* [ ] Add image upload endpoint with validation.
* [ ] Add image GET endpoint.
* [ ] Add image resize endpoint.
* [ ] Add image rotate endpoint.
* [ ] Add tests.

### Worker

* [ ] Add worker skeleton.

### Documentation

* [ ] Add architecture diagram.
* [ ] Add CHANGELOG.md.
* [ ] Add CONTRIBUTING.md.

## Features

## Tech Stack

* Kotlin - primary language
* PostgreSQL - database
* Spring Boot - web framework
* Gradle - build tool
* Docker - containerization
* Minio - S3 compatible object storage

## How to build and test

See: CONTRIBUTING.md

## How to run

Start the database:

```shell    
docker-compose up -d
```

## How to deploy



