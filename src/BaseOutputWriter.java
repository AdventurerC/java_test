package records;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;

public abstract class BaseOutputWriter {
    
    protected String filename;

    public BaseOutputWriter(String filename){
        this.filename = filename;
    }

    public void write(Table table) throws IOException, FileNotFoundException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));

        String cols = String.join(",", table.getColumns());
        writer.write(cols);
        writer.newLine();

        List<List<String>> rows = table.getRows();

        for (List<String> row : rows){
            String r = String.join(",", row);
            writer.write(r);
            writer.newLine();
        }
        writer.close();      
    }
}