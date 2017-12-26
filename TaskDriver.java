package InvertedIndex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TaskDriver {
    public static void main(String[] args) throws Exception {

        Path input1 = new Path(args[0]);
        Path output1 = new Path(args[1]);


        // Create a Configuration object that is used to set other options
        Configuration conf1 = new Configuration();

        // Create the object representing the job
        Job job1 = Job.getInstance(conf1, "Task1");

        // Set the name of the main class in the job jar file
        job1.setJarByClass(TaskDriver.class);

        // Set the mapper and reducer class
        job1.setMapperClass(TaskMapper.class);

        // job1.setCombinerClass(Task1Reducer.class);
        job1.setReducerClass(TaskReducer.class);

        // Set the types for the final output key and value
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);

        // Set input and output file paths
        FileInputFormat.addInputPath(job1, input1);
        FileOutputFormat.setOutputPath(job1, output1);


        // Execute the job and wait for it to complete
        System.exit(job1.waitForCompletion(true) ? 0 : 1);
    }
}