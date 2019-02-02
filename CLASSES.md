# Class reference

## Data

[DataSource](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/data/DataSource.java)   
Simple interface for flowable datasource

[CSVDataSource](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/data/CSVDataSource.java)  
CSV implementation of DataSource - it reads CSV file line by line and streams parsed Candles  
  
[Candle](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/data/Candle.java)  
Data object representing Candle.

## Studies

All studies are implemented as a flowable transformers so they can be chained and won't break a stream

[StudyEnvelope](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/StudyEnvelope.java)  
Wrapper object that contain all studies calculated during pipeline and original Candle object.
  
[MA](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/MA.java)  
Moving Average implementation
  
[StdDev](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/StdDev.java)  
Standard Deviation implementation - it makes use of MA

[BollingerBands](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/studies/BollingerBands.java)  
Bollinger Bands implementation - it makes use of MA and StdDev putting them into stream chain

## Strategy

The strategy concept is also implemented as a flowable transformer. It has to stream signals to the chain so they can be processed by executors

[Side](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/strategy/Side.java)  
Simple Enum representing order market Side

[Signal](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/strategy/Signal.java~~~~~~~~)  
Data object representing signal - this will be pushed into stream by strategy transformer

[SimpleBollingerStrategy](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/strategy/SimpleBollingerStrategy.java)  
Implementation of Simple Bollinger Bands strategy

## Execution

[ExecutionStatus](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/execution/ExecutionStatus.java)  
Simple Enum representing the resulting state of signal execution

[Execution](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/execution/Execution.java)  
Data Object representing singnal execution summary (if no closing order - cacheBalance = -1)

[StrategyExecutor](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/execution/StrategyExecutor.java)  
The flowable transformer that executes signals in a stream chain using Algotrader's simulator

## Application

[App](https://github.com/harmony1358/algointerview/blob/master/src/main/java/com/algotrader/interview/App.java)  
Main application class - building a stream chain from all above. Should print out cache balances when closing positions
