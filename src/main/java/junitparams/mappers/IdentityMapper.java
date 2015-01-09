package junitparams.mappers;

import java.io.*;
import java.util.*;

/**
 * A mapper, that maps contents of a file to a set of parameters for test
 * methods. Basically a CSV with no header and ordering of columns exactly like
 * the one in the test methods.
 * 
 * It uses the logic from &#064;Parameters({}) for parsing lines of file, so be sure
 * the columns in the file match exactly the ordering of arguments in the test
 * method.
 * 
 * @author Pawel Lipinski
 * 
 */
public class IdentityMapper extends BufferedReaderDataMapper{

}
