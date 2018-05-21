package records;
import java.util.*;
import java.io.*;

public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */ 

	public static void main(final String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}

		// your code starts here.
		System.out.println("Input files: "+args.length);

		List<Table> tables = new ArrayList<Table>();

		try {

			for (String arg : args){
				BaseInputParser parser;
				int dotIndex = arg.lastIndexOf(".");
				String ext = arg.substring(dotIndex + 1);

				if ("html".equals(ext)){
					parser = new HTMLInputParser();
				} else if ("csv".equals(ext)){
					parser = new CSVInputParser();
				} else { //default case
					parser = new CSVInputParser();
				}

				if (parser != null){
					Table table = parser.extractTable(arg);
					if (table != null){
						tables.add(table);
						//table.printTable(" | ");
					}
				}

			}
		} catch (FileNotFoundException fe){
            System.err.println("File not found");
            System.exit(1);
        } catch (IOException e){
            System.err.println("IOException");
            System.exit(1);
        }

		System.out.println();

		Table joined = tables.get(0);

		for (int i = 1; i < tables.size(); i++){
			Table table = tables.get(i);
			List<String> pKeys = Arrays.asList("ID", "Name");
			joined.join(table, pKeys);
		}

		joined.sort("ID");

		writeTable(joined);
		joined.printTable(",");
		//tables.get(1).printTable(" | ");
	}

	public static void writeTable(Table table){
		try {
			BaseOutputWriter writer = new CSVOutputWriter(FILENAME_COMBINED);

			writer.write(table);
		} catch (FileNotFoundException fe){
            System.err.println("Output File not found");
            System.exit(1);
        } catch (IOException e){
            System.err.println("IOException");
		}
            
	}
}
