package junitparams;

import java.io.*;
import java.util.*;

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
