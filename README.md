# images

A distributed async image processing service.

## Highlighted Features

* Selectable storage implementation based on Spring profiles.
* Batched image processing - perform multiple operations in a single request.
* Horizontal image processing scalability via "worker" nodes.
* Robust mime type validation via Apache Tika.
* All components are containerized and can be run locally for a great developer experience.

## Forward Looking Statements

1. The project supports easily adding multiple storage implementations. The current implementation uses the file system
   for simplicity. A more robust implementation would use a cloud storage provider such as AWS S3 for durability and
   subsequently AWS CloudFront as a CDN.
1. Additional image types could be supported by leveraging libraries such as Apache Batik to support SVG.
1. As of now, the authentication is a simplified version of what should be used in a production environment.
1. The API should be versioned and documented via OpenAPI. This would ease supporting multiple languages by leveraging
   code generation for clients.

## Architecture

### Components

- **api** - REST API server which serves as the entry point for the system.
- **core** - Core library which contains shared code between the api and worker.
- **worker** - Worker which processes image processing jobs.
- **message broker** - NATS messaging broker which is used for communication between the api and worker.

### Component Diagram

TODO

### Tech Stack

* Docker - containerization
* Docker Compose - container orchestration
* Kotlin - primary language
* PostgreSQL - database
* Spring Boot - web framework
* Gradle (Kotlin DSL) - build tool
* Docker - containerization
* NATS - messaging broker

## How to build and test

See: [CONTRIBUTING.md](CONTRIBUTING.md)

## How to run

Start the database:

```shell    
docker-compose up -d
```


