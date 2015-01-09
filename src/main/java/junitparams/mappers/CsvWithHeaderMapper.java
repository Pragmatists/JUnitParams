package junitparams.mappers;

import java.io.*;
import java.util.*;

/**
 * Reads a CSV file starting from the second line - the first one is supposed to
 * be a header. If you don't want to skip the first line, use &#064;FilePatameters
 * without any mapper.
 * 
 * @author Pawel Lipinski
 * 
 */
public class CsvWithHeaderMapper extends BufferedReaderDataMapper {

    public CsvWithHeaderMapper() {
        super(1);
    }
}
