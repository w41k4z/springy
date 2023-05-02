# Introduction

This is a java framework inspired by the famous Spring MVC, for building web applications

## Getting Started

Welcome to this project. Here is a guideline to help you setup the framework workspace.

> This guide is for any text editor. If you use an IDE, just import the jar file to your project and then create the 'views' and 'modelControllers' folder inside your source project

## Installation

    1- Download the springy-cli
    2- Execute the `install.sh` script

## Springy-cli

- link : [springy-cli](https://github.com/w41k4z/springy-cli.git)

## Pre-requisites

- Java 20
- Tomcat 9 or higher
- CATALINA_HOME environment variable
- Springy-cli

## Folder Structure

Your project would have thow main folders: 'src' and 'lib'
The 'src' will contain two main folders, where:

- `views`: the folder to store all your jsp files
- `modelControllers`: the folder for your model-controller

> You can also add your custom folder inside the src directory

## Documentation

> A ModelController must have an empty constructor

- Annotations:
  - @DatePattern
    > This annotation is used to specify the date pattern for the date field in the model-controller
  - @HttpParam
    > This annotation is used to specify the name of the request parameter and its type (request variable || path variable) that will be used to set the value of the field in the model-controller
  - @ModelController
    > This annotation tells the framework that the class is a model-controller. It is also used for routing
  - @UrlMapping
    > This annotation is for methods mapped with an url. It is used to specify the url and the http method (GET, POST, PUT, DELETE)
- Helpers:
  - @DateHelper
    > This helper is used to convert a string to a date
  - @StringHelper
    > This helper is an extension of the java String class (more features like converting a string to camel case, etc.)
