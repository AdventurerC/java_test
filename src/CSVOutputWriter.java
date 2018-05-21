package records;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.*;

public class CSVOutputWriter extends BaseOutputWriter{

    public CSVOutputWriter(String filename){
        super(filename);
    }

    public void write(Table table) throws IOException, FileNotFoundException{

        try(
            Writer writer = Files.newBufferedWriter(Paths.get(filename));

            CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        ) {
            String[] header = table.getColumns().toArray( new String[table.getColumns().size()]);
            csvWriter.writeNext(header);

            List<List<String>> rows = table.getRows();

            for (List<String> row : rows){
                String[] r = row.toArray(new String[row.size()]);
                csvWriter.writeNext(r);
            }
        }

    }
}