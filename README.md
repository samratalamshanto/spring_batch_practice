# spring_batch_practice

## Key Points:

1. Batch processing in Java applications using the Spring Batch framework
2. CSV file upload via Rest API (Dynamic file path, On the fly) and parse information from the file. Then upload to the DB.
3. Parallel processing and chunk-wise save in the DB.
4. File change notification and call JobLauncher (static file path) for batch processing using WatchService API from java.nio.file package

## Batch Processing Summary: 
1. Without Parallel Processing <br /> 
    1000 rows --> 3.5 sec <br /> 
    10000 rows--> 3 min 10sec <br /> 

2. Using Chunk-wise Parallel Processing <br /> 
    1000 rows--> 356 ms <br /> 
    10000 rows--> 11s 689ms <br /> 
