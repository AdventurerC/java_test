package records;
import java.util.*;
import java.util.stream.*;

public class Table {

    private final String ID = "ID";
    //private int idIndex = -1;

    private List<String> colHeaders;
    private List<List<String>> rows;
    private Integer numCols; 

    public Table() { 
        this.colHeaders = new ArrayList<String>();
        this.rows = new ArrayList<List<String>>();
        numCols = 0;
    }

    public Table( List<String> columns) { 
        this.colHeaders = columns;
        this.rows = new ArrayList<List<String>>();
        this.numCols = columns.size();
    }

    public Table( String[] columns) { 
        this.colHeaders = new ArrayList<String>();
        Collections.addAll(this.colHeaders, columns);
        this.rows = new ArrayList<List<String>>();
        this.numCols = this.colHeaders.size();
    }

    public void setColumns(List<String> columns){
        this.colHeaders = columns;
        this.numCols = columns.size();
    }

    public void addRow(List<String> data){
        this.rows.add(data);
    }

    public void addRow(String[] data){
        List<String> row = new ArrayList<String>();
        Collections.addAll(row, data);
        this.rows.add(row);
    }

    public void printTable(String separator){

        //fallback to comma separator by default
        if (separator == null){
            separator = ", ";
        }
        String cols = String.join(separator, this.colHeaders);
        System.out.println(cols);

        for (List<String> row : this.rows){
            String rowStr = String.join(separator, row);
            System.out.println(rowStr);
        }

        System.out.println();
    }

    public List<String> getColumns(){
        return new ArrayList<String>(this.colHeaders);
    }

    public List<List<String>> getRows(){
        return new ArrayList<List<String>>(this.rows);
    }

    public Boolean sort(String pKey){

        //specified primary key is not a column in this table
        if (!this.colHeaders.contains(pKey)){
            return false;
        }

        Integer pKeyIdx = this.colHeaders.indexOf(pKey);

        //sort with comparator that compares the value at the [pKey] column
        this.rows.sort((List<String> r1, List<String> r2)->{
            String r1key = r1.get(pKeyIdx);
            String r2key = r2.get(pKeyIdx);
            try {
                return Integer.parseInt(r1key)-Integer.parseInt(r2key);
            } catch (NumberFormatException e){
                return r1key.compareTo(r2key);
            }
        });

        return true;
        
    }

    // joins on columns with the same name
    public Boolean join(Table other, List<String> pKeys){
        List<String> otherCols = other.getColumns();
        List<List<String>> thisRows = this.getRows();
        List<List<String>> otherRows = other.getRows();

        //merge column headers
        List<String> columns = new ArrayList<String>(this.colHeaders);
        List<Integer> toBeAdded = new ArrayList<Integer>(); //indexes on otherRows
        List<Integer> pKeyIdx = new ArrayList<Integer>();
        List<Integer> pKeyIdxOther = new ArrayList<Integer>();

        for (String pKey : pKeys){
            if (columns.contains(pKey)){
                pKeyIdx.add(columns.indexOf(pKey));
            }

            if (otherCols.contains(pKey)){
                pKeyIdxOther.add(otherCols.indexOf(pKey));
            }
        }

        if (pKeyIdx.size() != pKeyIdxOther.size()) {
            //should NEVER happen
            return false;
        }

        for (String col : otherCols){
            if (pKeys.contains(col)) continue;
            if (!columns.contains(col)){
                toBeAdded.add(otherCols.indexOf(col));
                columns.add(col);
            }
        }


        this.colHeaders = columns;


        //dupes and dupesOther should be same length, with corresponding columns in the same order

        //=============merge columns==================================

        //loop throw this table's rows and merge in rows from other table where the primary keys match
        //removes merged rows from otherRow
        for (int i = 0; i < thisRows.size(); i++){//List<String> row : thisRows){
            List<String> row = thisRows.get(i);
            
            Boolean match = true;
            
            for (List<String> otherRow : otherRows){
                //find the primary keys in the other row
                match = true;

                for (int k = 0; k < pKeyIdx.size(); k++){
                    Integer idx = pKeyIdx.get(k);
                    Integer otherIdx = pKeyIdxOther.get(k);

                    if (!row.get(idx).equals(otherRow.get(otherIdx))){
                        match = false;
                        break;
                    }
                }

                //if (Collections.indexOfSubList(pKeys, otherRow) != -1){
                //if primary key matched
                if (match){

                    //add columns on other table but not this table
                    for (Integer addIdx : toBeAdded){
                        row.add(otherRow.get(addIdx));
                    }
                    otherRows.remove(otherRow);
                    break;
                } 
                    
            }

            if (!match){
                for (Integer addIdx : toBeAdded){
                        row.add("");
                }
            }
        
            thisRows.set(i, row);
        }

        //if there are rows remaining in otherRow, append them to thisRows with columns belonging to thisRow as ""

        for (List<String> otherRow : otherRows){
            List<String> newRow = new ArrayList<String>(columns.size());

            for (int i = 0; i < this.numCols; i++){
                newRow.add("");
            }

            //fill primary key columns
            for (int k = 0; k < pKeyIdx.size(); k++){
                Integer idx = pKeyIdx.get(k);
                Integer otherIdx = pKeyIdxOther.get(k);

                newRow.set(idx, otherRow.get(otherIdx));
            }

            //add columns from other table

            for (Integer addIdx : toBeAdded){
                newRow.add(otherRow.get(addIdx));
            }

            thisRows.add(newRow);
            
        }

        this.rows = thisRows;
        this.numCols = this.colHeaders.size();
        //System.out.println("Columns after join: "+this.numCols);
        return true;
    }

    
}