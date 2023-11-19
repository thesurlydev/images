# User Stories

The intent of this document is to provide a high-level overview of the user stories that are implemented in the project. 

## Health check

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

## Who Am I?

Get information about the currently logged in user.

```shell
curl -v http://localhost:8080/api/whoami
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

## Get available operations

Get a list of available operations.

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

## Get accepted image formats

Get a list of accepted image formats (mime types).

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

## Upload an image (valid mime-type)

```shell
curl -X POST --location "http://localhost:8080/api/images/upload" \
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
  "created_at": null
}
```

## Upload an image (unsupported mime-type)

An example of uploading an invalid image mime type (SVG) which is not supported.

```shell
POST http://localhost:8080/api/images/upload
Content-Type: multipart/form-data; boundary=WebAppBoundary
Content-Length: 73497
Connection: Keep-Alive
User-Agent: Apache-HttpClient/4.5.14 (Java/17.0.9)
Accept-Encoding: br,deflate,gzip,x-gzip

--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="brain-1552824775.svg"

< etc/test-images/brain-1552824775.svg
--WebAppBoundary--
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

## Download an image (found)

Download an image by id.

```shell
curl -v http://localhost:8080/api/images/92099701-604a-4048-9325-107bf44dc463/download
```

Example response:
```shell
HTTP/1.1 200 OK
Content-Type: image/jpeg
Content-Disposition: attachment; filename=4eea80da-3062-4164-9dcb-877b1e910d7e.jpeg
Content-Length: 351993
```
![test1-2016x1512-360k.jpg](etc/test-images/test1-2016x1512-360k.jpg)

## Download an image (not found)

Download an image by id that does not exist.
```shell
curl -v http://localhost:8080/api/images/92099701-604a-4048-9325-107bf44dc463/download
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


## Get Logs

Get an audit of the operations performed by the current user.

```shell
curl -v http://localhost:8080/api/logs
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


## Get images

Get a list of images that belong to the currently logged in user.

```shell
curl -v http://localhost:8080/api/images
```

Example response:

```shell
HTTP/1.1 200 OK
transfer-encoding: chunked
Content-Type: application/json

[
  {
    "id": "7a2448de-65b5-43de-b5b6-b923b4875aa3",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "path": "c611519d-4784-46b8-9f9b-3d27e1b9e98b.jpeg",
    "status": "processing",
    "type": "image/jpeg",
    "file_size": 351993,
    "width": 2016,
    "height": 1512,
    "original_image_id": null,
    "created_at": "2023-11-19T20:42:01.212528Z"
  },
  {
    "id": "2a8d17be-dc64-4e5e-80dc-bb8a4e86188c",
    "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
    "path": "fb686222-f00f-4474-a653-aa570317bd8a.jpeg",
    "status": "complete",
    "type": "image/jpeg",
    "file_size": 38364,
    "width": 1280,
    "height": 720,
    "original_image_id": "7a2448de-65b5-43de-b5b6-b923b4875aa3",
    "created_at": "2023-11-19T20:42:01.637001Z"
  }
]
```

## Get image by id

```shell
curl -v http://localhost:8080/api/images/92099701-604a-4048-9325-107bf44dc463
```

Example response:
```shell
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 331

{
  "id": "2a8d17be-dc64-4e5e-80dc-bb8a4e86188c",
  "user_id": "1aeabbac-84a5-11ee-9c3b-37b635df60b6",
  "path": "fb686222-f00f-4474-a653-aa570317bd8a.jpeg",
  "status": "complete",
  "type": "image/jpeg",
  "file_size": 38364,
  "width": 1280,
  "height": 720,
  "original_image_id": "7a2448de-65b5-43de-b5b6-b923b4875aa3",
  "created_at": "2023-11-19T20:42:01.637001Z"
}
```