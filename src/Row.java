package records;
import java.util.*;

public class Row implements Comparable<Row>{
    List<String> data;
    
    int id = 0;
    int idx = 0;

    public Row(int idIndex, List<String> data){
        this.idx = idIndex;
        this.id = Integer.parseInt(data.get(idIndex));
        this.data = data;
    }


    @Override
    public int compareTo(Row other){
        if (this.id < other.id){
            return -1;
        }
        if (this.id == other.id){
            return 0;
        }
        return 1;
    }
}