# N26 Test
N26's Transactions & Statistics test

## Technology Stack Used

* Java 1.8 with Spring Boot and an embedded Tomcat
* Maven for dependency management
* Logback for logging

## Overview
The purpose of this assignment is to create a RESTful API to the create transactions and get statistics. 

The API exposes 4 endpoints :

POST /transactions/

GET /statistics

Create a transaction with amount and timestamp

Get aggregated statistics of transactions happened in last 60 seconds

How to run
```
$ git clone https://github.com/kinjal63/N26Test.git
$ cd N26Test
$ mvn clean install
```
