[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8fc3d9420b744e59a65bbf854da42511)](https://www.codacy.com/app/harmony1358/algointerview?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=harmony1358/algointerview&amp;utm_campaign=Badge_Grade)

# Algotrader Strategy

This project contains example strategy built for Algotrader.
It implements basic BollingerBands strategy and executed orders on Algotrader simulator
  
## Concept

The concept is to make strategy work in the flow of constant stream of candles provided by data source.
All studies (Moving Average, Standard Deviation, Bollinger Bands) are calculated on the flow using FlowableTransformer from
RxJava library in order to handle backpressure stream.
The strategy itself and orders executor are also implemented as a stream transformers.

## Classes

### Data

[DataSource](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/data/DataSource.java)   
Simple interface for flowable datasource

[CSVDataSource](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/data/CSVDataSource.java)  
CSV implementation of DataSource - it reads CSV file line by line and streams parsed Candles  
  
[Candle](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/data/Candle.java)  
Data object representing Candle. It also contains a HashMap that can store studies calculated in further processes.

### Studies

All studies are implemented as a flowable transformers so they can be chained and won't break a stream
  
[MA](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/MA.java)  
Moving Average implementation
  
[StdDev](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/StdDev.java)  
Standard Deviation implementation

[BollingerBands](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/BollingerBands.java)  
Bollinger Bands implementation - it makes use of MA and StdDev putting them into stream chain

### Strategy

The strategy concept is also implemented as a flowable transformer. It has to stream signals to the chain so they can be processed by executors

[Side](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/strategy/Side.java)  
Simple Enum representing order market Side

[Signal](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/strategy/Signal.java~~~~~~~~)  
Data object representing signal - this will be pushed into stream by strategy transformer

[SimpleBollingerStrategy](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/strategy/SimpleBollingerStrategy.java)  
Implementation of Simple Bollinger Bands strategy

### Execution

[ExecutionResult](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/execution/ExecutionResult.java)  
Simple Enum representing the resulting state of signal execution

[Execution](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/execution/Execution.java)  
Data Object representing singnal execution summary (if no closing order - cacheBalance = -1)

[StrategyExecutor](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/execution/StrategyExecutor.java)  
The flowable transformer that executes signals in a stream chain using Algotrader's simulator

### Application

[App](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/App.java)  
Main application class - building a stream chain from all above. Should print out cache balances when closing positions

## Building
  
Project is built by "maven" build system.  
Caution - you have to have algotraders simulator installed on your local maven!  

`mvn verify`

## Running
  
Project can be launched locally with maven task:    
  
`mvn exec:java`  
  
