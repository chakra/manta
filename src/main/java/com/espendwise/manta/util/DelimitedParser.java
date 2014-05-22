package com.espendwise.manta.util;

import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.StringCharacterIterator;

import org.apache.log4j.Logger;

import com.espendwise.manta.loader.IInputStreamParser;


/**
 * Provides functions to parse a string into a vector fields. The separator is allowed
 * to appear within quoted strings, leading and trailing quotes will be stripped,
 * adjacent quotes within a quoted string will be converted to a single double quote and
 * will appear in the string, leading and trailing spaces on other fields will be stripped.
 * Here are some examples of the results of the parsing operation:
 * <PRE>
 * 1: "hello, world",hello world,"",,"","hello, world",   hello "world"   ,"   hello world    ",
 * [hello, world]
 * [hello world]
 * [null]
 * [null]
 * [null]
 * [hello, world]
 * [hello "world"]
 * [   hello world    ]
 * 
 * 2: "hello, world",hello world,"",,"","""hello, world""",   "hello ""world"""   ,"   hello world    ",
 * [hello, world]
 * [hello world]
 * [null]
 * [null]
 * [null]
 * ["hello, world"]
 * ["hello ""world"""]
 * [   hello world    ]
 * 
 * 3: "hello, world",hello world,"",,"",""hello, world"",   hello ""world""   ,"   hello world    ",
 * Exception: At position: 36 Trailing quote followed by something other than a quote, a separator or the end of the string
 * Program terminating...
 * </PRE>
 * 
 * @author Ken Mawhinney
 * @since April 11, 2001
 */
public class DelimitedParser {
	protected Logger log = Logger.getLogger(this.getClass());

    /** Holds a string used to handle quoted strings in the parse method */
    protected static String aQuote = "\"";
    
    BufferedReader reader = null;
    
    public DelimitedParser(InputStream pInput) throws Exception{
    	reader = new BufferedReader(new InputStreamReader(pInput, "UTF-8"));
    }
    
    /**
     * When the first field is missing or there are two separators adjacent, the value
     * will be null, so make sure a null is added to the vector. If string starts and ends
     * with a quote, then strip them both, otherwise trim leading and trailing spaces.
     * 
     * @param theVector The vector of fields.
     * @param value
     */
    static void AddElement (Vector theVector, StringBuffer value) {
        if (value == null) {
            theVector.addElement(null);
        } else {
            String tempStr = value.toString().trim();
            String valueStr;
            if (tempStr.startsWith(aQuote) && tempStr.endsWith(aQuote)) {
                valueStr = tempStr.substring(1, tempStr.length()-1);
            } else {
                valueStr = tempStr;
            }
            if (valueStr.length() > 0) {
                theVector.addElement(valueStr);
            } else {
                theVector.addElement(null);
            }
        }
    }

    static public Vector parse (String aRecord, char lFs) 
    throws NoSuchElementException {
        return parse(aRecord,lFs,'\"',true);
     }
    
    /**
     * Replaced the parsing function. Needs to handle quoted strings, two separators
     * back to back, separators inside quoted strings, must treat two quotes together inside
     * a quoted string as single quote. Simplified the problem by required that the separator
     * is a character rather than a string as it is for the other parse function.
     * 
     * @param aRecord     The String to be split into fields at each occurrence of the separator.
     * @param lFs         The separator character
     * @param kQuote      The charachter to interpret as a quote
     * @param stripQuotes True if the quotes should not be returned in the parsed Vector
     * @return A vector containing the fields, each field is held in a string
     * @exception NoSuchElementException
     *                   Thrown if:
     *                   <uL>
     *                   <LI>Field starts with a quote, but there is no trailing quote</LI>
     *                   <LI>Field starts and ends with a quote, but the trailing quote is followed by
     *                   something other than a separator or the end of the string</LI>
     *                   <LI>The specified separator is a quote</LI>
     *                   <UL>
     */
    static public Vector parse (String aRecord, char lFs,char kQuote,boolean stripQuotes) 
    throws NoSuchElementException {
        Vector mFields = new Vector(32);
        StringCharacterIterator sci = new StringCharacterIterator (aRecord);
        final int kSeekNextToken      = 0;
        final int kSeekTrailingFs     = 1;
        final int kSeekTrailingQuote  = 2;
        final int kAllDone            = 99;
        char lCursor = sci.first();
        int state;
        StringBuffer aField = new StringBuffer();
        // Initialize to this state because the first field might not be preceded by a separator
        if (lCursor == kQuote) {
            if(!stripQuotes){
                aField.append (kQuote);
            }
            lCursor = sci.next();
            state = kSeekTrailingQuote;
        } else {
            state = kSeekTrailingFs;
        }
        while (state != kAllDone) {
            switch (state) {
            case kSeekNextToken:
                // Looking for the start of the next token.
                // If the character is a separator, means there are no 
                // characters found since the last token or the start of the string
                // If the character is a quote, then switch to parsing an quoted string
                // Otherwise, start a new string buffer, store the character and switch
                // to looking for the trailing separator.
                if (lCursor == lFs) {
                    AddElement (mFields, null);
                } else if (lCursor == kQuote) {
                    aField = new StringBuffer();
                    if(!stripQuotes){
                        aField.append (kQuote);
                    }
                    state = kSeekTrailingQuote;
                } else if (lCursor == StringCharacterIterator.DONE) {
                    // Found end of string, just exit. A trailing separator is allowed
                    state = kAllDone;
                } else {
                    aField = new StringBuffer();
                    aField.append (lCursor);
                    state = kSeekTrailingFs;
                }
                lCursor = sci.next();
                break;
            case kSeekTrailingFs:
                // Looking for the end of the current token.
                // If the character is a separator, then store the field being built
                // as the next element of the vector and switch state to looking for
                // the next token.
                if (lCursor == lFs) {
                    AddElement (mFields, aField);
                    state = kSeekNextToken;
                } else if (lCursor == StringCharacterIterator.DONE) {
                    AddElement (mFields, aField);
                    state = kAllDone;
                } else {
                    aField.append (lCursor);
                }
                lCursor = sci.next();
                break;
            case kSeekTrailingQuote:
            	// Quotes can be escaped by placing two together
            	if (lCursor == kQuote) {
            		char lPrevious = sci.previous();
            		sci.next(); //because previous actually moves cursor increment it to get us
            		//back to whence we came
            		if(lPrevious == '\\'){
            			//delete the last char as it is just escaping the quote
            			if(aField.length() > 1){
            				aField.deleteCharAt(aField.length()-1);
            			}
            			aField.append (kQuote);
            			lCursor = sci.next();
            		}else{
            			lCursor = sci.next();
            			if (lCursor == kQuote ) {
            				aField.append (kQuote);
            			} else if (lCursor == StringCharacterIterator.DONE) {
            				if(!stripQuotes){
            					aField.append(kQuote);
            				}
            				AddElement (mFields, aField);
            				state = kAllDone;
            			} else if (lCursor == lFs) {
            				if(!stripQuotes){
            					aField.append(kQuote);
            				}
            				AddElement (mFields, aField);
            				state = kSeekNextToken;
            			} else if (lCursor == ' ') {
            				//continue seeking...
            			} else {                                      
            				throw new 
            				NoSuchElementException ("At position: " +
            						sci.getIndex() +
            						" Trailing quote followed by something other than " +
            						"a quote, a separator or the end of the string  ["+lCursor+"]::"+sci.previous());
            			}
            		}
            	} else if (lCursor == StringCharacterIterator.DONE) {
                	
            		throw new NoSuchElementException ("At position: " +
            				sci.getIndex() +
            				" Non-terminated quoted string ["+lCursor+"]::"+sci.previous()+"::"+aField);
                		
            	} else {
            		// If the separator appears inside a quoted string, it is accepted
            		// as a regular character
                    aField.append (lCursor);
            	}
            	lCursor = sci.next();
            	break;
            default:
                throw new NoSuchElementException ("At position: " +
                                                  sci.getIndex() +
                                                  " Invalid state: " +
                                                  state +
                                                  " while parsing: " +
                                                  aRecord);
            }
        }
        return mFields;
    }


    /**
     * Concatenate each field into a string of fields separated by the given separator.
     * If a field is null, don't append it, just append the separator.
     * 
     * @param lFields    The vector of fields to turn into a string.
     * @param lSeparator The separator character, e.g. a comma, to be placed between each field AND at the end of the
     *                   string.
     * @return A string containing all the non-null fields.
     */
    static public String toString (Vector lFields, char lSeparator) {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i < lFields.size(); i++) {
            if (lFields.elementAt(i) != null) {
                buf.append (lFields.elementAt(i));
                buf.append (lSeparator);
            } else {
                buf.append (lSeparator);
            }

        }
        return buf.toString();
    }

	public void processFile(IInputStreamParser listener, char sepertorChar, char quoteChar) throws Exception {
		int currentLineNumber = 0;
		try{
			String line = null;
			while ((line = reader.readLine()) != null) {
				List l = DelimitedParser.parse(line,sepertorChar, quoteChar, true);
				listener.parseLine(l);
				currentLineNumber++;
			}
		}catch(Exception e){
			log.error("Error parsing line: "+(currentLineNumber +1));
			e.printStackTrace();
			throw e;
		}
		
	}
}
