# Requirements

A distributed and asynchronous image processing service. You may use whatever development and testing tools you are
comfortable with. Feel free to use as many or as few cloud services as you wish to solve the business requirements.

Please submit the following:

* [ ] A working implementation of the application described below.
* [ ] Instructions on how to run the application as well as install and configure any external dependencies.
* [ ] The suite of tests that go along with the application.

## Feature Requirements

The business requires a new service to handle and process images with the following requirements:

* [ ] Allow authenticated users to upload image files. Validate the files are only images.
* [ ] Allow authenticated users to download a 1280x720 sized and 180 degrees rotated version of the image.
* [ ] Allow authenticated users to download the original image.
* [ ] Allow authenticated users to see a log of the uploaded images with status of processing, any failure details and which
  user uploaded it.
* [ ] Allow external systems to download the modified image on request. The external systems will provide a fixed token to
  access the API.

Some things to consider while solving the business requirements:

* Today, images are only resized and rotated. You know this is a core feature of the business and is likely to expand in
  the future to encompass more outputs variants and features. How will you design the system to ensure new jobs can be
  easily added.
* How will you deal with security around this service? These images should only be accessible by authenticated users and
  external systems with the correct tokens.
* You are aware the external systems requesting a copy of the modified image will potentially do this at a high rate.
  How would you cache the output to increase throughput if required?

