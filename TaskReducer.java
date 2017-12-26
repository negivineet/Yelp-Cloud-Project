package InvertedIndex;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class TaskReducer extends Reducer<Text,Text,Text,Text> {
    @Override
    public void reduce(Text word, Iterable<Text> counts, Context context)
            throws IOException, InterruptedException {
        int sup = 3;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for(Text t: counts){
            if(i == 0){
                sb.append(t.toString());
            } else {
                sb.append(",");
                sb.append(t.toString());
            }
            i++;
        }
        context.write(word,new Text(sb.toString()));
    }
}