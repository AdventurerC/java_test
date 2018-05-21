package records;
import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;

public class CSVInputParser extends BaseInputParser{
    public CSVInputParser(){ }

    public Table extractTable(String fileName) throws IOException, FileNotFoundException{
        
       
        CSVReader reader = new CSVReader(new FileReader(fileName));
        
        String[] colHeaders = reader.readNext();
        Table table = new Table(colHeaders);

        //System.out.println(colHeaders.toString());

        String[] row;

        while ((row = reader.readNext()) != null){
            table.addRow(row);
        }

        return table;
        
    }
}