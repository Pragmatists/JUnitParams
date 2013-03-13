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
public class CsvWithHeaderMapper implements DataMapper {
    public Object[] map(Reader reader) {
        BufferedReader br = new BufferedReader(reader);
        String line;
        List<String> result = new LinkedList<String>();
        try {
            try {
                br.readLine(); // skip the header
                while ((line = br.readLine()) != null) {
                    result.add(line);
                }
                return result.toArray();
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
