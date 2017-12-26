CLOUD PROJECT STEPS
--------------------
--------------------

PREREQUISITES
-------------
1. Cloudera Quikstart vm CDH-5.12 on EC2
2. Java 1.7


DATASET
-------

1. Download dataset from https://www.yelp.com/dataset/challenge
2. You will be presented with a form. Fill required information and click Download.
3. Choose JSON format for download.

FILE STRUCTURE
---------------

1.  Local File System
    ------------------
    You need to create following file structure in the home folder of the user,

    CloudProject/loadJoinGenerate.pig
    
    CloudProject/workflow.xml

    CloudProject/config/job.properties

    CloudProject/lib/CloudFinalProject.jar
    CloudProject/lib/InvertedIndex.jar

    CloudProject/output-data
    
    CloudProject/pigoutput
    
    CloudProject/rules
    
    CloudProject/support

    CloudProject/yelp/business.json
    CloudProject/yelp/review.json
    CloudProject/yelp/user.json

2. HDFS
   ----
    You need to create following file structure in the home folder of the user in HDFS,

    CloudProject/config/job.properties
    
    CloudProject/lib/CloudFinalProject.jar
    CloudProject/lib/InvertedIndex.jar
    
    CloudProject/loadJoinGenerate.pig --> pig program to format data across JSON files in /yelp    

    CloudProject/output-data --> Final Output of InvertedIndex MapReduce program

    CloudProject/pigoutput  --> Pig output which is also input for MapReduce

    CloudProject/rules

    CloudProject/support

    CloudProject/workflow.xml --> oozie workflow files


APPROACH
--------

We have used Oozie to run the following

1. pig script (loadJoinGenerate.pig) to load the json file and join by columns.
2. mapreduce job(InvertedIndex.jar) on the pig output to generate Inverted Index for Apriori calaculations.

RUNNING OOZIE
-------------

1. Run the following commands one after the other:

   
   oozie job -oozie http://<nameNodeIP>:11000/oozie -config CloudProject/job.properties -submit
   
   oozie job -oozie http://<nameNodeIP>:11000/oozie-start <job-id>

2. In the above commands,

   nameNodeIP -> IP address of nameNode (example: ec2-54-161-67-163.compute-1.amazonaws.com)
   11000 -> Default port number for Ooozie 
   job-id -> Job ID returned by the first command

3. In case oozie fails,

   RUNNING PIG
   -----------

   1. Goto Grunt mode by running
      
      pig 

   
   2. Enter into project folder,

      cd CloudProject   

   3. Execute pig script by running the command,
      
      exec loadJoinGenerate.pig


   RUNNING MAPREDUCE
   -----------------
   1. hadoop jar InvertedIndex.jar InvertedIndex CloudProject/pigoutput CloudProject/output-data

JAVA PROGRAM
------------

We have used the java program to run the balanced sequential logic on apriori. To perform that copy the pig output and outputdata folder from HDFS to local and run following command

java Apriori -m 0.05 0.3 < cat pigoutput/*

