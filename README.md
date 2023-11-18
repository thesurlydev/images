# images

A distributed async image processing service.

## Components

- **api** - REST API server which serves as the entry point for the system.
- **core** - Core library which contains shared code between the api and worker.
- **worker** - Worker which processes image processing jobs.

## TODO

### Infrastructure

* [x] Multi-project skeleton.
* [x] Setup Postgresql database.
* [x] Setup database migrations.
* [ ] Setup messaging broker.
* [ ] Setup minio or local file storage.

### API Server

* [ ] Add register endpoint.
* [ ] Add get token endpoint.
* [ ] Add image upload endpoint with validation.
* [ ] Add image GET endpoint.
* [ ] Add image resize endpoint.
* [ ] Add image rotate endpoint.
* [ ] Add tests.

### Worker

* [x] Add worker skeleton.
* [ ] Add image processing library.
* [ ] Add image resize operation.
* [ ] Add image rotate operation.

### Documentation

* [ ] Add architecture diagram.
* [ ] Add CHANGELOG.md.
* [ ] Add CONTRIBUTING.md.

## Highlighted Features

* Selectable storage implementation based on Spring profiles.
* Batched image processing - perform multiple operations in a single request.
* Horizontal image processing scalability via "worker" nodes.
* Robust mime type validation via Apache Tika.
* All components are containerized and can be run locally for a great developer experience.

### Tech Stack

* Docker - containerization
* Docker Compose - container orchestration
* Kotlin - primary language
* PostgreSQL - database
* Spring Boot - web framework
* Gradle - build tool
* Docker - containerization
* NATS - messaging broker

## How to build and test

See: CONTRIBUTING.md (TODO)

## How to run

Start the database:

```shell    
docker-compose up -d
```

## How to deploy

TODO?

## Project Overview

A distributed and asynchronous image processing service. You may use whatever development and testing tools you are comfortable with. Feel free to use as many or as few cloud services as you wish to solve the business requirements.

Please submit the following:

1. A working implementation of the application described below.
2. Instructions on how to run the application as well as install and configure any external dependencies.
3. The suite of tests that go along with the application.

## Feature Requirements

The business requires a new service to handle and process images with the following requirements:

* Allow authenticated users to upload image files. Validate the files are only images.
* Allow authenticated users to download a 1280x720 sized and 180 degrees rotated version of the image.
* Allow authenticated users to download the original image.
* Allow authenticated users to see a log of the uploaded images with status of processing, any failure details and which user uploaded it.
* Allow external systems to download the modified image on request. The external systems will provide a fixed token to access the API.

Some things to consider while solving the business requirements:

* Today, images are only resized and rotated. You know this is a core feature of the business and is likely to expand in the future to encompass more outputs variants and features. How will you design the system to ensure new jobs can be easily added.
* How will you deal with security around this service? These images should only be accessible by authenticated users and external systems with the correct tokens.
* You are aware the external systems requesting a copy of the modified image will potentially do this at a high rate. How would you cache the output to increase throughput if required?


