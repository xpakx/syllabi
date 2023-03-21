# Syllabi

## General info
Web-app for managing university courses written in Java + Spring (backend) and Typescript + Angular (frontend).

![gif](readme_files/screen.gif)

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)

## Technologies
Project is created with:
* Java 
* Spring Boot
* Typescript
* Angular
* PostgreSQL

## Setup
In order to run project locally you need to clone this repository and build project with Docker Compose:

```
$ git clone https://github.com/xpakx/syllabi.git
$ cd syllabi
$ docker-compose up --build -d
```

To stop:
```
$ docker-compose stop
```

## Features
- [x] Managing courses
- [x] Managing programs
- [x] Managing institutes
- [x] Managing students and teachers
- [x] Adding and editing for moderators
- [x] Admissions
	- [x] Opening admissions
	- [x] Applying for students
	- [x] Reviewing for moderators
	- [x] Closing admissions

