package junitparams.mappers;

import java.io.*;

/**
 * Interface to be used by FileParameters'ized test methods. If you want to read
 * your own format of data from file, implement the map method appropriately.
 * For CSV files, just skip it.
 * 
 * @author Pawel Lipinski
 * 
 */
public interface DataMapper {
    /**
     * Maps file contents to parameters. In your implementation read the data
     * from the reader. The reader is closed in the framework, so just read it
     * :)
     * 
     * While reading transform the data into Object[][], where external
     * dimension are different parameter sets, and internal dimension is the set
     * of params per single test call
     * 
     * You can optionally return Object[] with Strings inside, but each String
     * must be a string in the same format as what you would normally pass to
     * &#064;Parameters({})
     * 
     * @param reader
     * @return an array with all parameter sets
     */
    Object[] map(Reader reader);
}
