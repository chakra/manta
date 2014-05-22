/*
 * StreamedInput.java
 *
 * Created on July 10, 2003, 8:11 PM
 */

package com.espendwise.manta.loader;
import java.util.List;

/**
 * Implementing Classes for dealing with binary files (excel, pdf etc).
 * @author  deping
 */
public interface IInputStreamParser {
	/**
     *Gets called once for each line of text in the file.  
     */
    public abstract void parseLine(List pParsedLine) throws Exception;
}
