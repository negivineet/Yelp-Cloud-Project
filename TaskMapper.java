package InvertedIndex;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class TaskMapper extends Mapper<LongWritable, Text, Text, Text> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private long numRecords = 0;
    public void map(LongWritable offset, Text lineText, Context context)
            throws IOException, InterruptedException {


        String line = lineText.toString();
        Text currentWord = new Text();
        String[] cols = line.split("\\|");
        Text reviewId = new Text(cols[0]);

        //1_review_stars
        String value = lmh(cols[1], 3.0, 4.0);
        Text output = new Text("1_" + value);
        context.write(output, reviewId);
        //2_review_count,avg_stars
        value = personality(cols[2], cols[6]);
        output = new Text("2_" + value);
        context.write(output, reviewId);
        //3_friends_count
        value = social(cols[3]);
        output = new Text("3_" + value);
        context.write(output, reviewId);
        //4_fans,elite_years
        value = influence(cols[4], cols[5]);
        output = new Text("4_" + value);
        context.write(output, reviewId);
        //5_city
        output = new Text("5_" + cols[7]);
        context.write(output, reviewId);

        //6_business_stars, review_count
        value = businessCategory(cols[8], cols[9]);
        output = new Text("6_" + value);
        context.write(output, reviewId);
    }



    public static String lmh(String value, double l, double m) {
        double num = Double.parseDouble(value);
        if(num < l) {
            return "low";
        } else if (num <= m) {
            return "medium";
        } else {
            return "high";
        }
    }

    public static String personality(String c, String s) {
        String count = lmh(c, 50, 150);
        String stars = lmh(s, 3.0, 4.0);
        if(count.equals("low") || stars.equals("medium")) {
            return "diplomats";
        } else if (stars.equals("low")) {
            return "cry_babies";
        } else {
            return "crowd_pleasers";
        }
    }

    public static String social(String c) {
        String count = lmh(c, 50, 150);
        if(count.equals("low")) {
            return "not_social";
        } else {
            return "social";
        }
    }

    public static String influence(String f, String e) {
        String fans = lmh(f, 50, 150);
        String elite = lmh(e, 2.0, 4.0);
        if(elite.equals("low") && !fans.equals("high")) {
            return "non_influencer";
        } else {
            return "influencer";
        }
    }

    public static String businessCategory(String s, String c) {
        String count = lmh(c, 50, 150);
        String stars = lmh(s, 3.0, 4.0);
        if(count.equals("low")) {
            return "explore";
        } else if (stars.equals("high") && count.equals("high")) {
            return "highly_recommended";
        } else if (stars.equals("high") && count.equals("medium")) {
            return "recommended";
        } else if (stars.equals("medium") && count.equals("high")) {
            return "above_average";
        } else if (stars.equals("medium") && count.equals("medium")) {
            return "average";
        } else if (stars.equals("low")) {
            return "not_recommended";
        } else {
            return "not_recommended";
        }
    }
}