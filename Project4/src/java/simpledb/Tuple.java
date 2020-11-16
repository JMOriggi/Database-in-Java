package simpledb;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    //Field object that specify the content of each field of the tuple
    private Field[] dataFields;
    //Specify the field schema of the tuple
    private TupleDesc tupleDesc;
    //Record id that represent the location of this tuple on disk.
    private RecordId recordId;

    private static final long serialVersionUID = 1L;

    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    public Tuple(TupleDesc td) {
        // some code goes here
        //If argument make sense and initialize the class var
        if(td.numFields() >= 0 && td instanceof TupleDesc){
            tupleDesc = td;
            dataFields = new Field[td.numFields()];
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return tupleDesc;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        return recordId;
    }

    /**
     * Set the RecordId information for this tuple.
     *
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
        recordId = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
        // No need to instantiate interface Field because the info come from outside and given as param to the method
        //Check if i in the correct interval
        if(0 <= i && i < dataFields.length){
            dataFields[i] = f;
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
        //Check if i in the correct interval
        if(0 <= i && i < dataFields.length){
            return dataFields[i];
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN
     *
     * where \t is any whitespace (except a newline)
     */
    public String toString() {
        // some code goes here
        //Concatenate every string result of the field content
        String res = "";
        for (int i = 0; i < dataFields.length; i++) {
            res += dataFields[i].toString() + "\t";
        }
        return res;
    }

    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        // some code goes here
        return new FieldsIterator();
    }
    //Iterator class that check for the next field in the fieldsDescList, and return it
    private class FieldsIterator implements Iterator<Field> {
        private int fieldPosition;
        @Override
        public boolean hasNext() {
            //Check if I'm in the end of the fieldsDescList
            if(dataFields.length == fieldPosition){
                return false;
            }else{
                return true;
            }
        }
        @Override
        public Field next() {
            //Check next element exist and return it
            if (hasNext()) {
                return dataFields[fieldPosition++];
            }else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * reset the TupleDesc of thi tuple
     * */
    public void resetTupleDesc(TupleDesc td)
    {
        // some code goes here
        tupleDesc = td;
    }
}
