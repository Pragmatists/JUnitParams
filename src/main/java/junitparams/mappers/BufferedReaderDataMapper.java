package junitparams.mappers;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * A Data Mapper based on Buffered Reader.
 */
class BufferedReaderDataMapper implements DataMapper {

    private final int linesToSkip;

    BufferedReaderDataMapper() {
        this(0);
    }

    BufferedReaderDataMapper(int linesToSkip) {
        this.linesToSkip = linesToSkip;
    }

    @Override
    public Object[] map(Reader reader) {
        BufferedReader br = new BufferedReader(reader);
        String line;
        List<String> result = new LinkedList<String>();
        int lineNo = 0;
        try {
            while ((line = br.readLine()) != null) {
                if (++lineNo > linesToSkip) {
                    result.add(line);
                }
            }
            return result.toArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
