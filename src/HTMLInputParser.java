package records;
import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.select.*;
import org.jsoup.nodes.*;

public class HTMLInputParser extends BaseInputParser{
    public HTMLInputParser(){ }

    public Table extractTable(String fileName) throws IOException {
        return extractTable(fileName, "UTF-8");
    }

    public Table extractTable(String fileName, String format) throws IOException, FileNotFoundException {

            File file = new File(fileName);
            Document doc = Jsoup.parse(file, format);

            Element htmlTable = doc.select("table").first();
            Elements htmlRows = htmlTable.select("tr");

            Element colHeaders = htmlRows.get(0);
            List<String> cols = this.rowToList(colHeaders, "th");

            Table table = new Table(cols);

            // System.out.println(htmlRows.size());

            for (Element row : htmlRows){
                if (!row.equals(colHeaders)){
                    table.addRow(this.rowToList(row, "td"));
                }
            }

            return table;

    }

    private List<String> rowToList(Element row, String selector){
        Elements colValues = row.select(selector);
        //  System.out.println(colValues.size());
        List<String> data = new ArrayList<String>();

        for (Element col: colValues){
            data.add(col.text());
            // System.out.println(col.text());
        }

        return data;

    }
} 
