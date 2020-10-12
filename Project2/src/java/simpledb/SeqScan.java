package simpledb;

import java.util.*;

/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements DbIterator {

    //tableId that correspond to the file Id in the DB
    private int tableId;
    //table name alias required in this method
    private String tableAlias;
    //SeqScan is an iterator itself on the tuple of a specific page of a db file
    private DbFileIterator tupleIterator;


    private static final long serialVersionUID = 1L;

    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     *
     * @param tid
     *            The transaction this scan is running as a part of.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        // some code goes here
        this.tableAlias = tableAlias;
        this.tableId = tableid;
        //I need also an iterator on the tuples
        tupleIterator = Database.getCatalog().getDatabaseFile(tableid).iterator(tid);
    }

    /**
     * @return
     *       return the table name of the table the operator scans. This should
     *       be the actual name of the table in the catalog of the database
     * */
    public String getTableName() {
        return null;
    }

    /**
     * @return Return the alias of the table this operator scans.
     * */
    public String getAlias()
    {
        // some code goes here
        return tableAlias;
    }

    /**
     * Reset the tableid, and tableAlias of this operator.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public void reset(int tableid, String tableAlias) {
        // some code goes here
        this.tableId = tableid;
        this.tableAlias = tableAlias;
    }

    public SeqScan(TransactionId tid, int tableid) {
        this(tid, tableid, Database.getCatalog().getTableName(tableid));
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
        tupleIterator.open();
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.  The alias and name should be separated with a "." character
     * (e.g., "alias.fieldName").
     *
     * @return the TupleDesc with field names from the underlying HeapFile,
     *         prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        //Retrieve the tuple description to have the specific size of the fields
        TupleDesc tDesc = Database.getCatalog().getTupleDesc(tableId);
        //Create 2 new arrays that will contain the type and alias names
        Type[] fieldsTypes = new Type[tDesc.numFields()];
        String[] fieldsNames = new String[tDesc.numFields()];
        //for each field trasform the name in alias and copy the type
        for (int i = 0; i < tDesc.numFields(); i++) {
            fieldsTypes[i] = tDesc.getFieldType(i);
            //If alias null I do not used it
            if(getAlias() == null){
                fieldsNames[i] = tDesc.getFieldName(i);
            }else{
                fieldsNames[i] = getAlias() + "." + tDesc.getFieldName(i);
            }
        }
        return new TupleDesc(fieldsTypes, fieldsNames);
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        // some code goes here
        return tupleIterator.hasNext();
    }

    public Tuple next() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // some code goes here
        return tupleIterator.next();
    }

    public void close() {
        // some code goes here
        tupleIterator.close();
    }

    public void rewind() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // some code goes here
        tupleIterator.rewind();
    }
}
