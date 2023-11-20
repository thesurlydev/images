# User Guide

The intent of this document is to provide a high-level overview and guide the user through the application.

For requirements to build and run the application, see [CONTIBUTING.md](CONTRIBUTING.md).

Note: For testing the image upload endpoint, there's some test images available in this repo under `etc/test-images`.

* [Unprotected Endpoints](#unprotected-endpoints)
    + [Health check](#health-check)
    + [Accepted Mime Types](#accepted-mime-types)
    + [Get available operations](#get-available-operations)
    + [Register a user](#register-a-user)
    + [Login](#login)
* [Protected Endpoints](#protected-endpoints)
    + [Who Am I?](#who-am-i)
    + [Upload an image (valid mime-type)](#upload-an-image-valid-mime-type)
    + [Upload an image (unsupported mime-type)](#upload-an-image-unsupported-mime-type)
    + [Download an image (found)](#download-an-image-found)
    + [Download an image (not found)](#download-an-image-not-found)    
    + [Get images](#get-images)
    + [Get image by id](#get-image-by-id)
    + [Get Logs](#get-logs)

## Unprotected Endpoints

The following endpoints are unprotected and do not require authentication.

### Health check

Verify the application is running.

```shell
curl http://localhost:8080/actuator/health
```

Example response:
```json
{
  "status": "UP"
}
```

### Accepted Mime Types

Get a list of accepted mime types.

```shell
curl -v http://localhost:8080/api/images/accepted
```

Example response:
```shell
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 60
        
{
  "types": [
    "image/jpg",
    "image/jpeg",
    "image/png",
    "image/gif"
  ]
}
```

### Get available operations

Get a list of available image operations.

```shell
curl -v http://localhost:8080/api/operations
```

Example response:
```json
[
  {
    "id": "c35e2ca6-706d-4411-8f50-8297a19b0685",
    "name": "resize",
    "description": "Resize an image given x and y dimensions"
  },
  {
    "id": "2197660f-abcd-45fb-ab96-5aeb8f7f9ad1",
    "name": "rotate",
    "description": "Rotate an image given degrees and direction parameters"
  }
]
```

### Register a user

```shell
curl -X POST "http://localhost:8080/register" \
    -H "Content-Type: application/json" \
    -d '{
          "username": "shane",
          "password": "shane",
          "email": "shane@surly.dev"
        }'
```

Example response:
```shell
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 90

{
  "id": "f052679a-87f9-4d24-b633-1ea5cef2d844",
  "username": "shane",
  "email": "shane@surly.dev"
}
```

### Login

Once a user is registered, they can login to the system.

```shell
curl -v -X POST "http://localhost:8080/login" \
    -H "Content-Type: application/json" \
    -d '{
          "username": "shane",
          "password": "shane"
        }'
```
Example response:
```shell
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 360

{
  "user": {
    "id": "f052679a-87f9-4d24-b633-1ea5cef2d844",
    "username": "shane",
    "passwordHash": "$2a$12$nW4JUa3CoDsYrSExqEpW8.28L5lYlOGUlH8KyYpptLZchAInW1cAy",
    "email": "shane@surly.dev"
  },
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmMDUyNjc5YS04N2Y5LTRkMjQtYjYzMy0xZWE1Y2VmMmQ4NDQiLCJpYXQiOjE3MDA1MDQzNDksImV4cCI6MTcwMDUwNTI0OX0.WoXeNgUAyR45l0Dcrq77QS9IACwnRBeWPY6UGb-oQUI"
}
```
Note: 
* The token included in the response can be used to authenticate the user for protected endpoints. 
* The default expiration is 15 minutes.


## Protected Endpoints

The following endpoints are protected and require authentication.

### Who Am I?

Get information about the currently logged in user.

```shell
curl -v "http://localhost:8080/whoami" \
     -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc"
```
Example response:
```shell
HTTP/1.1 200 OK
transfer-encoding: chunked
Content-Type: application/json

[
  {
    "id": "282d020d-9378-42f6-92a0-117edb4a6d3c",
    "name": "resize",
    "description": "Resize an image given x and y dimensions"
  },
  {
    "id": "4f76843e-11b3-4ae6-92c8-88b8559419ea",
    "name": "rotate",
    "description": "Rotate an image given degrees and direction parameters"
  }
]
```

### Upload an image (valid mime-type)

```shell
curl -X POST --location "http://localhost:8080/api/images/upload" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc" \
-H "Content-Type: multipart/form-data; boundary=WebAppBoundary" \
-F "file=@etc/test-images/test1-2016x1512-360k.jpg;filename=test1-2016x1512-360k.jpg;type=*/*"
```

Example response:
```shell
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 276

{
  "id": "ee657bc3-8b79-4477-b010-ff23b15e3a41",
  "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
  "path": "78cdcf04-7898-4e4e-91df-3a53db079d49.jpeg",
  "status": "processing",
  "type": "image/jpeg",
  "file_size": 351993,
  "width": 2016,
  "height": 1512,
  "original_image_id": null,
  "created_at": null,
  "original_image_id": null,
  "original_image_name": "test1-2016x1512-360k.jpg",
}
```

### Upload an image (unsupported mime-type)

An example of uploading an invalid image mime type (SVG) which is not supported.

```shell
curl -X POST --location "http://localhost:8080/api/images/upload" \
    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmMDUyNjc5YS04N2Y5LTRkMjQtYjYzMy0xZWE1Y2VmMmQ4NDQiLCJpYXQiOjE3MDA1MDQzNDksImV4cCI6MTcwMDUwNTI0OX0.WoXeNgUAyR45l0Dcrq77QS9IACwnRBeWPY6UGb-oQUI" \
    -H "Content-Type: multipart/form-data; boundary=WebAppBoundary" \
    -F "file=@/home/shane/projects/images/etc/test-images/brain-1552824775.svg;filename=brain-1552824775.svg;type=*/*"
```

Example response:
```shell
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 80

{
  "status": "Bad request",
  "message": "File is not an accepted type: image/svg+xml"
}
```

### Download an image (found)

Download an image by id.

```shell
curl -v http://localhost:8080/api/images/92099701-604a-4048-9325-107bf44dc463/download \
 -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc"
```

Example response:
```shell
HTTP/1.1 200 OK
Content-Type: image/jpeg
Content-Disposition: attachment; filename=4eea80da-3062-4164-9dcb-877b1e910d7e.jpeg
Content-Length: 351993
```
![test1-2016x1512-360k.jpg](etc/test-images/test1-2016x1512-360k.jpg)

### Download an image (not found)

Download an image by id that does not exist.
```shell
curl -v http://localhost:8080/api/images/92099701-604a-4048-9325-107bf44dc463/download \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc"
```

Example response:
```shell
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 88

{
  "status": "Not found",
  "message": "Image not found: 92099701-604a-4048-9325-107bf44dc463"
}
````

### Get a list of images

Get a list of images that belong to the currently logged in user.

Note:
* The `original_image_id` is populated when an image is transformed. This allows for a chain of transformations to be 
  tracked.
* The `original_image_name` is populated when an image is uploaded. This allows for the original image name to be 
  tracked.

```shell
curl -v http://localhost:8080/api/images \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc"
```

Example response:

```shell
HTTP/1.1 200 OK
transfer-encoding: chunked
Content-Type: application/json

[
  {
    "id": "55fa9f8a-5c6c-434d-9049-689176c7e3ad",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "path": "e0b71390-1cd0-468e-925a-d2184cd7dafc.jpeg",
    "status": "processing",
    "type": "image/jpeg",
    "file_size": 351993,
    "width": 2016,
    "height": 1512,
    "original_image_id": null,
    "original_image_name": "test1-2016x1512-360k.jpg",
    "created_at": "2023-11-19T13:23:43.470859-08:00"
  },
  {
    "id": "9c3580cf-9946-4a2c-993e-1277fc3a66b0",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "path": "d0dbb4bd-dc01-4d03-a7c1-e265e71337eb.jpeg",
    "status": "complete",
    "type": "image/jpeg",
    "file_size": 38364,
    "width": 1280,
    "height": 720,
    "original_image_id": "55fa9f8a-5c6c-434d-9049-689176c7e3ad",
    "original_image_name": null,
    "created_at": "2023-11-19T13:23:43.900394-08:00"
  }
]
```

### Get image by id

```shell
curl -v http://localhost:8080/api/images/92099701-604a-4048-9325-107bf44dc463 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc"
```

Example response:
```shell
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 363

{
  "id": "9c3580cf-9946-4a2c-993e-1277fc3a66b0",
  "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
  "path": "d0dbb4bd-dc01-4d03-a7c1-e265e71337eb.jpeg",
  "status": "complete",
  "type": "image/jpeg",
  "file_size": 38364,
  "width": 1280,
  "height": 720,
  "original_image_id": "55fa9f8a-5c6c-434d-9049-689176c7e3ad",
  "original_image_name": null,
  "created_at": "2023-11-19T13:23:43.900394-08:00"
}
```

### Get Logs

Get an audit of the operations performed by the current user.

```shell
curl -v http://localhost:8080/api/logs \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwODBkZWFhNi02ZmU2LTQyYjMtOTBmNC0yZmI4MDY1ZDU1MWIiLCJpYXQiOjE3MDA1MDE4OTEsImV4cCI6MTcwMDUwMjc5MX0.-FYBpupWP0kFEi-trSXtmLy7pktAHC4WRASJ74jjWOc"
```

Example response:
```shell
HTTP/1.1 200 OK
transfer-encoding: chunked
Content-Type: application/json

[
  {
    "id": "ac67c751-3778-40b6-90cb-4557c181c1fe",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "operation": "upload",
    "status": "SUCCESS",
    "message": "Uploaded image: f60f2648-7529-478e-a964-297b53c98a2c",
    "created_at": "2023-11-19T20:04:31.034585Z"
  },
  {
    "id": "47d40dfb-95fd-48a3-9941-c929043b4f65",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "operation": "upload",
    "status": "SUCCESS",
    "message": "Uploaded image: 7fbe1d8a-60fa-49be-ae2e-484ab39d8836",
    "created_at": "2023-11-19T20:09:54.468594Z"
  },
  {
    "id": "a00c7933-8c5a-4094-90de-f7171b977755",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "operation": "upload",
    "status": "SUCCESS",
    "message": "Uploaded image: 92099701-604a-4048-9325-107bf44dc463",
    "created_at": "2023-11-19T20:16:23.736075Z"
  },
  {
    "id": "251d8cdd-b340-4ea1-a46a-df23c0474d8d",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "operation": "upload",
    "status": "ERROR",
    "message": "File is not an accepted type: image/svg+xml",
    "created_at": "2023-11-19T20:26:19.926303Z"
  }
]
```