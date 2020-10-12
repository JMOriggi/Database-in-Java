package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    //List of Field elements (structure description) in the Tuple
    private TDItem[] fieldsDescList;
    //Number of Fields in the Tuple (same for every tuple of a Table)
    private int numFields;

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return new ItemsIterator();
    }
    //Iterator class that check for the next field in the fieldsDescList, and return it
    private class ItemsIterator implements Iterator<TDItem> {
        private int fieldPosition;
        @Override
        public boolean hasNext() {
            //Check if I'm in the end of the fieldsDescList
            if(fieldPosition == numFields){
                return false;
            }else{
                return true;
            }
        }
        @Override
        public TDItem next() {
            //Check next element exist and return it
            if (hasNext()) {
                return fieldsDescList[fieldPosition++];
            }else {
                throw new NoSuchElementException();
            }
        }
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        //check Valid param
        if(typeAr.length > 0) {
            //Initialize vars of TupleDesc class
            numFields = typeAr.length;
            fieldsDescList = new TDItem[numFields];
            //For each position of the list insert a new Field Item element with particular type and name
            for (int i = 0; i < numFields; i++) {
                fieldsDescList[i] = new TDItem(typeAr[i], fieldAr[i]);
            }
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        //check Valid param
        if(typeAr.length > 0) {
            //Initialize vars of TupleDesc class
            numFields = typeAr.length;
            fieldsDescList = new TDItem[numFields];
            //For each position of the list insert a new Field Item element with particular type and empty name
            for (int i = 0; i < numFields; i++) {
                fieldsDescList[i] = new TDItem(typeAr[i], null);
            }
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return numFields;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        //Check if i is in the correct interval
        if(0 <= i && i < numFields){
            return fieldsDescList[i].fieldName;
        }else{
            throw new NoSuchElementException();
        }
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        //Check if i is in the correct interval
        if(0 <= i && i < numFields){
            return fieldsDescList[i].fieldType;
        }else{
            throw new NoSuchElementException();
        }
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        //Check if the field name equals the entry for each element of the list, otherwise throw exception
        for (int i = 0; i < numFields; i++) {
            if(fieldsDescList[i].fieldName != null && fieldsDescList[i].fieldName.equals(name)){
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        //Sum of the sizes in bytes of each type of field in the list
        int totSize=0;
        for (int i = 0; i < numFields; i++) {
            totSize += fieldsDescList[i].fieldType.getLen();
        }
        return totSize;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        //I create 2 arrays for the Type and Name of the fields for the concatenated tuples,
        //I will use the 2 arrays to instantiate a new TupleDesc
        int mergeNumField = td1.numFields+td2.numFields;
        Type[] mergeTDItemsType = new Type[mergeNumField];
        String[] mergeTDItemsName = new String[mergeNumField];
        int i=0;
        for(; i<td1.numFields; i++){
            mergeTDItemsType[i] = td1.fieldsDescList[i].fieldType;
            mergeTDItemsName[i] = td1.fieldsDescList[i].fieldName;
        }
        for(int j=0; j<td2.numFields; j++, i++){
            mergeTDItemsType[i] = td2.fieldsDescList[j].fieldType;
            mergeTDItemsName[i] = td2.fieldsDescList[j].fieldName;
        }
        return new TupleDesc(mergeTDItemsType, mergeTDItemsName);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
        //First level check on generic characteristic of object o
        if(o == null || !(o instanceof TupleDesc) || ((TupleDesc) o).numFields != numFields) {
            return false;
        }else{
            //check field by field if the type is the same (if only 1 is different return false)
            for (int i = 0; i < numFields(); i++) {
                if (fieldsDescList[i].fieldType != ((TupleDesc) o).fieldsDescList[i].fieldType) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        //Concatenate the result of the toString() Item mehtod for each Item in fieldsDescList
        String res = "";
        for (int i = 0; i < numFields(); i++) {
            res = fieldsDescList[i].toString() + ", ";
        }
        return res;
    }
}
