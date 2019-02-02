[![Build Status](https://travis-ci.com/harmony1358/algointerview.svg?branch=master)](https://travis-ci.com/harmony1358/algointerview)
[![Coverage Status](https://coveralls.io/repos/github/harmony1358/algointerview/badge.svg?service=github)](https://coveralls.io/github/harmony1358/algointerview)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8fc3d9420b744e59a65bbf854da42511)](https://www.codacy.com/app/harmony1358/algointerview?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=harmony1358/algointerview&amp;utm_campaign=Badge_Grade)

# Algotrader Strategy

This project contains example strategy built for Algotrader.
It implements basic BollingerBands strategy and executed orders on Algotrader simulator

## Releases

[[v1.1.0]](https://github.com/harmony1358/algointerview/releases)
Second interation based on suggestions from Andy. [[Backlog]](https://github.com/harmony1358/algointerview/milestone/1?closed=1) 
[[Pull request]](https://github.com/harmony1358/algointerview/pull/12)  
[[v1.0.0]](https://github.com/harmony1358/algointerview/releases) 
First attempt released at 28.01.2019
  
## Concept

The concept is to make strategy work in the flow of constant stream of candles provided by data source.
All studies (Moving Average, Standard Deviation, Bollinger Bands) are calculated on the flow using FlowableTransformer from
RxJava library in order to handle backpressure stream.
The strategy itself and orders executor are also implemented as a stream transformers.

## Classes

Class layout changed since last release. Additional classes are introduces. 
Some changed their names and/or package.  
[[Class reference]](./CLASSES.md) New Classes description

## Testing

Project uses JUnit for testing. Most of tests have own sample data files in "resources" folder 
under the "test" scope of classpath.
Those files contains data that is expected in assertions. 
For example - Moving Average test loads "TEST_CASE_MA.csv" where opening price is expected assertion value. 
  
To launch tests type:  
`mvn test`

## Reporting

Project using JaCoCo library for coverage reporting. 
All JaCoCo reports are saved in "target/site" after tests.

## Automatic Code Review

Project makes automatic code review and QA check using Codacy.
Code reviews are performed after each commit during CI Pipeline.

## CI Pipeline

Project has CI Pipeline configured with "Travis-CI". 
After each commit pipeline starts - pipeline includes:  
- building project
- testing project
- reporting to coveralls.io
- performing automatic QA tests with Codacy


## Building
  
Project is built by "maven" build system.  
Caution - you have to have algotraders simulator installed on your local maven!  

`mvn verify`

## Running
  
Project can be launched locally with maven task:    
  
`mvn exec:java`  

## Roadmap

In case someone would like to push this project further (its on github anyway ;-) 
here are my thoughts about what could be done in the future to grow it into sth really useful.

[[Wishlist]](https://github.com/harmony1358/algointerview/milestone/2)