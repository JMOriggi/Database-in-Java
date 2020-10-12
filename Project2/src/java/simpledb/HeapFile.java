package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

    //structure of the tuple
    private TupleDesc td;
    //File used to build the heap file
    private File f;

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
        //Initialize class vars
        this.f = f;
        this.td = td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return f;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
        return f.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
        //for random access, you are allowed to move your file cursor to the xst page and directly read that page
        try (RandomAccessFile raf = new RandomAccessFile(getFile(), "r")) {
            int pos = pid.pageNumber() * BufferPool.getPageSize();
            raf.seek(pos);
            byte[] data = new byte[BufferPool.getPageSize()];
            raf.read(data, 0, data.length);
            Page page = new HeapPage((HeapPageId) pid, data);
            return page;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        //file size over the page size give me the number of pages in the file
        int numPage = (int) (f.length() / BufferPool.getPageSize());
        return numPage;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
        return new FilesIterator();
    }
    //For that files it enables to open it and iterates to the tuples of the pages it contains
    //fileId and TableId are the same (important for the implementation)
    private class FilesIterator implements DbFileIterator {

        private int pgNo;
        private Iterator<Tuple> tuplesIterator;

        @Override
        public void open() throws DbException, TransactionAbortedException {
            //open the file and point to the first page (return the tuples iterator of the first page)
            pgNo = 0;
            //Take the page from bufferPool: if in memory return it otherwise fetch it from disk (all done by the bufferpool)
            HeapPageId pid = new HeapPageId(getId(), pgNo);
            HeapPage page = (HeapPage) Database.getBufferPool().getPage(new TransactionId(), pid, null);
            tuplesIterator = page.iterator();
        }
        @Override
        public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
            if (hasNext()) {
                return tuplesIterator.next();
            }else{
                throw new NoSuchElementException();
            }
        }
        @Override
        public void close() {
            //reset values
            tuplesIterator = null;
        }
        @Override
        public void rewind() throws DbException, TransactionAbortedException {
            //come back to the page 0
            open();
        }
        @Override
        public boolean hasNext() throws DbException, TransactionAbortedException {
            //Check if null pointer exception
            if (tuplesIterator == null) {
                return false;
            } else if (tuplesIterator.hasNext()) {
                //Check if I have next element directly on the current page
                return true;
            } else {
                //In the current page I have no next tuple
                //Check if I a next page exist
                pgNo++;//increment to select next page in file
                if (pgNo < numPages()) {
                    //Next page exist
                    //I reset the iterator to point to that particular page
                    HeapPageId pid = new HeapPageId(getId(), pgNo);
                    HeapPage page = (HeapPage) Database.getBufferPool().getPage(new TransactionId(), pid, null);
                    tuplesIterator = page.iterator();
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

}

