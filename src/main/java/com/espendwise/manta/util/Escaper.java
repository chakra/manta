package com.espendwise.manta.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utility methods for escaping special characters in various contexts.
 */
public final class Escaper {

  private static final int ASCII_ESCAPE_ARRAY_LENGTH = 127;
  
  private static String EMPTY_STRING = "";

  /*
   * These arrays are used by the escaping methods to speed their
   * processing.  They are initialized in the static initializer below.
   * The escaping rules are different for different situations.  For
   * example, escaping for JavaScript depends on whether the characters
   * appear in a JavaScript literal that is in a &lt;script&gt; tag or
   * that is in the attribute value of an XML/HTML tag's attribute (such as
   * an <code>onclick</code> attribute).
   */
  private static final boolean _jsEscapeCharIsSpecialInScriptTag[];
  private static final String _jsEscapeSpecialCharReplacementInScriptTag[];
  private static final boolean _jsEscapeCharIsSpecialInXMLAttr[];
  private static final String _jsEscapeSpecialCharReplacementInXMLAttr[];

  private static final boolean _htmlEscapeCharIsSpecial[];
  private static final String _htmlEscapeSpecialCharReplacement[];
  private static final boolean _htmlEscapeAndPreserveWhitespaceCharIsSpecial[];
  private static final String _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement[];
  private static final boolean _htmlPreserveWhitespaceCharIsSpecial[];
  private static final String _htmlPreserveWhitespaceSpecialCharReplacement[];

  private static final boolean _regExpEscapeCharIsSpecial[];
  private static final String _regExpEscapeSpecialCharReplacement[];

  static {
    // Initialize the escape helper arrays.
    //
    {
      {
        _jsEscapeCharIsSpecialInScriptTag = new boolean[ASCII_ESCAPE_ARRAY_LENGTH];
        _jsEscapeSpecialCharReplacementInScriptTag = new String[ASCII_ESCAPE_ARRAY_LENGTH];
        for (int i = 0; i < ASCII_ESCAPE_ARRAY_LENGTH; i++) {
          _jsEscapeCharIsSpecialInScriptTag[i] = false;
        }
        _jsEscapeCharIsSpecialInScriptTag['\\'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\\'] = "\\\\";
        _jsEscapeCharIsSpecialInScriptTag['\n'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\n'] = "\\n";
        _jsEscapeCharIsSpecialInScriptTag['\t'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\t'] = "\\t";
        _jsEscapeCharIsSpecialInScriptTag['\r'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\r'] = "\\r";
        _jsEscapeCharIsSpecialInScriptTag['\b'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\b'] = "\\b";
        _jsEscapeCharIsSpecialInScriptTag['\f'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\f'] = "\\f";
        // See the comments on jsEscapeInScriptTag for why we treat '/' as a special character.
        _jsEscapeCharIsSpecialInScriptTag['/'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['/'] = "\\/";
        _jsEscapeCharIsSpecialInScriptTag['\"'] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\"'] = "\\\"";
        _jsEscapeCharIsSpecialInScriptTag['\''] = true;
        _jsEscapeSpecialCharReplacementInScriptTag['\''] = "\\\'";
      }

      {
        _jsEscapeCharIsSpecialInXMLAttr = new boolean[ASCII_ESCAPE_ARRAY_LENGTH];
        _jsEscapeSpecialCharReplacementInXMLAttr = new String[ASCII_ESCAPE_ARRAY_LENGTH];
        for (int i = 0; i < ASCII_ESCAPE_ARRAY_LENGTH; i++) {
          _jsEscapeCharIsSpecialInXMLAttr[i] = false;
        }
        _jsEscapeCharIsSpecialInXMLAttr['\\'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\\'] = "\\\\";
        _jsEscapeCharIsSpecialInXMLAttr['\n'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\n'] = "\\n";
        _jsEscapeCharIsSpecialInXMLAttr['\t'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\t'] = "\\t";
        _jsEscapeCharIsSpecialInXMLAttr['\r'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\r'] = "\\r";
        _jsEscapeCharIsSpecialInXMLAttr['\b'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\b'] = "\\b";
        _jsEscapeCharIsSpecialInXMLAttr['\f'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\f'] = "\\f";
        _jsEscapeCharIsSpecialInXMLAttr['&'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['&'] = "&amp;";
        _jsEscapeCharIsSpecialInXMLAttr['\"'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\"'] = "\\&quot;";
        _jsEscapeCharIsSpecialInXMLAttr['\''] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['\''] = "\\&#39;";
        _jsEscapeCharIsSpecialInXMLAttr['<'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['<'] = "&lt;";
        _jsEscapeCharIsSpecialInXMLAttr['>'] = true;
        _jsEscapeSpecialCharReplacementInXMLAttr['>'] = "&gt;";
      }

      {
        // **** Currently this array (_htmlEscapeCharIsSpecial) is used for both XML and HTML
        // **** escaping.  If it is ever changed to something that is only appropriate for HTML,
        // **** a new array will have to be added for XML and the xmlEscape methods will need
        // **** to be changed to use the new array.

        _htmlEscapeCharIsSpecial = new boolean[ASCII_ESCAPE_ARRAY_LENGTH];
        _htmlEscapeSpecialCharReplacement = new String[ASCII_ESCAPE_ARRAY_LENGTH];
        for (int i = 0; i < ASCII_ESCAPE_ARRAY_LENGTH; i++) {
          _htmlEscapeCharIsSpecial[i] = false;
        }
        _htmlEscapeCharIsSpecial['&'] = true;
        _htmlEscapeSpecialCharReplacement['&'] = "&amp;";
        _htmlEscapeCharIsSpecial['\"'] = true;
        _htmlEscapeSpecialCharReplacement['\"'] = "&quot;";
        _htmlEscapeCharIsSpecial['\''] = true;
        _htmlEscapeSpecialCharReplacement['\''] = "&#39;";
        _htmlEscapeCharIsSpecial['<'] = true;
        _htmlEscapeSpecialCharReplacement['<'] = "&lt;";
        _htmlEscapeCharIsSpecial['>'] = true;
        _htmlEscapeSpecialCharReplacement['>'] = "&gt;";
      }

      {
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial = new boolean[ASCII_ESCAPE_ARRAY_LENGTH];
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement =
          new String[ASCII_ESCAPE_ARRAY_LENGTH];
        for (int i = 0; i < ASCII_ESCAPE_ARRAY_LENGTH; i++) {
          _htmlEscapeAndPreserveWhitespaceCharIsSpecial[i] = false;
        }
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['&'] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['&'] = "&amp;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['\"'] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['\"'] = "&quot;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['\''] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['\''] = "&#39;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['<'] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['<'] = "&lt;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['>'] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['>'] = "&gt;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial[' '] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement[' '] = "&nbsp;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['\t'] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['\t'] = "&nbsp;&nbsp;&nbsp;&nbsp;";
        _htmlEscapeAndPreserveWhitespaceCharIsSpecial['\n'] = true;
        _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement['\n'] = "<br>";
      }

      {
        _htmlPreserveWhitespaceCharIsSpecial = new boolean[ASCII_ESCAPE_ARRAY_LENGTH];
        _htmlPreserveWhitespaceSpecialCharReplacement = new String[ASCII_ESCAPE_ARRAY_LENGTH];
        for (int i = 0; i < ASCII_ESCAPE_ARRAY_LENGTH; i++) {
          _htmlPreserveWhitespaceCharIsSpecial[i] = false;
        }
        _htmlPreserveWhitespaceCharIsSpecial[' '] = true;
        _htmlPreserveWhitespaceSpecialCharReplacement[' '] = "&nbsp;";
        _htmlPreserveWhitespaceCharIsSpecial['\t'] = true;
        _htmlPreserveWhitespaceSpecialCharReplacement['\t'] = "&nbsp;&nbsp;&nbsp;&nbsp;";
        _htmlPreserveWhitespaceCharIsSpecial['\n'] = true;
        _htmlPreserveWhitespaceSpecialCharReplacement['\n'] = "<br>";
      }
      
      {
        _regExpEscapeCharIsSpecial = new boolean[ASCII_ESCAPE_ARRAY_LENGTH];
        _regExpEscapeSpecialCharReplacement = new String[ASCII_ESCAPE_ARRAY_LENGTH];
        for (int i = 0; i < ASCII_ESCAPE_ARRAY_LENGTH; i++) {
          _regExpEscapeCharIsSpecial[i] = false;
        }
        _regExpEscapeCharIsSpecial['('] = true;
        _regExpEscapeSpecialCharReplacement['('] = "\\(";
        _regExpEscapeCharIsSpecial['['] = true;
        _regExpEscapeSpecialCharReplacement['['] = "\\[";
        _regExpEscapeCharIsSpecial['{'] = true;
        _regExpEscapeSpecialCharReplacement['{'] = "\\{";
        _regExpEscapeCharIsSpecial['\\'] = true;
        _regExpEscapeSpecialCharReplacement['\\'] = "\\\\";
        _regExpEscapeCharIsSpecial['^'] = true;
        _regExpEscapeSpecialCharReplacement['^'] = "\\^";
        _regExpEscapeCharIsSpecial['-'] = true;
        _regExpEscapeSpecialCharReplacement['-'] = "\\-";
        _regExpEscapeCharIsSpecial['$'] = true;
        _regExpEscapeSpecialCharReplacement['$'] = "\\$";
        _regExpEscapeCharIsSpecial['|'] = true;
        _regExpEscapeSpecialCharReplacement['|'] = "\\|";
        _regExpEscapeCharIsSpecial[']'] = true;
        _regExpEscapeSpecialCharReplacement[']'] = "\\]";
        _regExpEscapeCharIsSpecial['}'] = true;
        _regExpEscapeSpecialCharReplacement['}'] = "\\}";
        _regExpEscapeCharIsSpecial[')'] = true;
        _regExpEscapeSpecialCharReplacement[')'] = "\\)";
        _regExpEscapeCharIsSpecial['?'] = true;
        _regExpEscapeSpecialCharReplacement['?'] = "\\?";
        _regExpEscapeCharIsSpecial['*'] = true;
        _regExpEscapeSpecialCharReplacement['*'] = "\\*";
        _regExpEscapeCharIsSpecial['+'] = true;
        _regExpEscapeSpecialCharReplacement['+'] = "\\+";
        _regExpEscapeCharIsSpecial['.'] = true;
        _regExpEscapeSpecialCharReplacement['.'] = "\\.";
      }
    }
  }

  /**
   * The constructor is private to prevent instantation as all methods are static.
   */
  private Escaper() {
  }

  /**
   * Escapes special characters in a given string, appending the result
   * to a specified StringBuffer.  What characters are considered to
   * be special are defined by the characterIsSpecial array, an array
   * of booleans indexed by character codes.  A character is considered to
   * be special if its corresponding array entry is true.  Any characters
   * whose codes fall outside the range of valid indices into the
   * characterIsSpecial array are considered non-special.
   *
   * <p>The escape sequence to replace special characters by is defined by
   * the specialCharacterReplacement array.  For each index in
   * characterIsSpecial whose value is true, the corresponding element
   * of specialCharacterReplacement must contain the string to replace
   * that character with.  Elements of specialCharacterReplacement that
   * don't correspond to special characters need not have any particular
   * value and will never be accessed.
   * 
   * <p>If the specialWhitepaceHandling flag is true, then a space character
   * will only be replaced with its replacement character if the prior
   * character is also a space.  Another way to say this is that when this
   * flag is true, a space will never be treated as a special character that
   * needs to be replaced unless the prior character was also a space (the
   * first space in a series of spaces won't be replaced).  If this flag
   * is true, the other thing that happens is that any \r characters that
   * are part of a \r\n sequence are discarded, but only if the \n character
   * is marked as being special in the characterIsSpecial array.
   * These are disturbingly special-case rules in what is otherwise a
   * very data-driven method, but they are needed to support
   * {@link #htmlEscapeAndPreserveWhitespace(String)} and
   * {@link #htmlPreserveWhitespace(String)}, and it isn't obvious what
   * a useful generalization of these concepts might be.
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   * @param characterIsSpecial  the array defining which characters must
   *    be replaced by an escape sequence
   * @param specialCharacterReplacement  the array defining the replacement
   *    strings for special characters
   * @param specialWhitespaceHandling if true, handle whitespace characters
   *    specially as described above
   */
  private static void escapeSpecialCharacters(
    String str,
    StringBuffer buf,
    boolean[] characterIsSpecial,
    String[] specialCharacterReplacement,
    boolean specialWhitespaceHandling) {

    if (!Utility.isSet(str)) {
      return;
    }

    int strlen = str.length();
    int characterIsSpecialLength = characterIsSpecial.length;
    char[] chars = str.toCharArray();
    int prevSpecial = -1;
    int lengthOrdinary = 0;
    char priorChar = 0;

    for (int i = 0; i < strlen; i++) {
      char c = chars[i];

      boolean charIsSpecial = ((c < characterIsSpecialLength) && characterIsSpecial[c]);

      if (charIsSpecial && specialWhitespaceHandling && (c == ' ') && (priorChar != ' ')) {
        charIsSpecial = false;
      }

      if (charIsSpecial) {
        // It is a special character.
        // Flush any contiguous sequence of non-special characters we've
        // encountered, then append the character's replacement.
        //
        lengthOrdinary = i - prevSpecial - 1;
        // If we're doing special whitespace handling, discard the
        // \r in a \r\n sequence.
        //
        if (specialWhitespaceHandling && (c == '\n') && (priorChar == '\r')) {
          lengthOrdinary = lengthOrdinary - 1;
        }
        if (lengthOrdinary > 0) {
          buf.append(chars, prevSpecial + 1, lengthOrdinary);
        }
        prevSpecial = i;
        buf.append(specialCharacterReplacement[c]);
      } // end if current character is special

      priorChar = c;
    } // end for each character

    lengthOrdinary = strlen - prevSpecial - 1;
    if (lengthOrdinary > 0) {
      // Append any ordinary characters we haven't appended yet.
      buf.append(chars, prevSpecial + 1, lengthOrdinary);
    }
  }

  /**
   * Escapes characters in the input string so that the string can be used
   * as part of a JavaScript string literal within an HTML &lt;script&gt; tag.
   * See {@link #jsEscapeInScriptTag(String, StringBuffer)} for details.  Use that version
   * when possible, as in many situations appending the results to a StringBuffer
   * is much more time and space efficient than returning a String, which is what this
   * method does.
   */
  public static String jsEscapeInScriptTag(String str) {
    // See the comments in the jsEscapeinXMLAttr(String, StringBuffer) method
    // for how to use the JavaScript escaping methods in different contexts.

    if (!Utility.isSet(str)) {
      return EMPTY_STRING;
    }

    StringBuffer buf = new StringBuffer((int) (1.1 * str.length()));
    jsEscapeInScriptTag(str, buf);
    return buf.toString();
  }

  /**
   * Escapes characters in the input string so that the string can be used
   * as part of a JavaScript string literal within an HTML &lt;script&gt; tag.
   * It does additional escaping so that the resulting JavaScript can appear
   * within an HTML SCRIPT tag without causing problems (this means that
   * "&lt;/" must be escaped as "&lt;\/").  This does NOT escape JavaScript
   * correctly for inclusion as the value of an HTML attribute.  To do that,
   * use {@link #jsEscapeInXMLAttr(String, StringBuffer)}.  For simplicity and
   * to ensure correctness in all pathological cases, we ALWAYS escape "/"
   * as "\/".  One such pathological case is when calling this on two different
   * strings in succession, and the first one ends with "&lt;" and the second
   * one begins with "/".
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void jsEscapeInScriptTag(String str, StringBuffer buf) {
    // See the comments in the jsEscapeinXMLAttr(String, StringBuffer) method
    // for how to use the JavaScript escaping methods in different contexts.

    if (!Utility.isSet(str)) {
      return;
    }

    escapeSpecialCharacters(
      str,
      buf,
      _jsEscapeCharIsSpecialInScriptTag,
      _jsEscapeSpecialCharReplacementInScriptTag,
      false);
  }

  /**
   * Escapes characters in the input string so that the string can be used
   * as part of a JavaScript string literal within the attribute value of
   * an XML/HTML tag's attribute (such as an <code>onclick</code> attribute).
   * See {@link #jsEscapeInXMLAttr(String, StringBuffer)} for details.  Use that version
   * when possible, as in many situations appending the results to a StringBuffer
   * is much more time and space efficient than returning a String, which is what this
   * method does.
   */
  public static String jsEscapeInXMLAttr(String str) {
    // See the comments in the jsEscapeinXMLAttr(String, StringBuffer) method
    // for how to use the JavaScript escaping methods in different contexts.

    if (!Utility.isSet(str)) {
      return EMPTY_STRING;
    }

    StringBuffer buf = new StringBuffer((int) (1.1 * str.length()));
    jsEscapeInXMLAttr(str, buf);
    return buf.toString();
  }

  /**
   * Escapes characters in the input string so that the string can be used
   * as part of a JavaScript string literal within the attribute value of
   * an XML/HTML tag's attribute (such as an <code>onclick</code> attribute).
   * This does NOT correctly escape strings for use in an HTML SCRIPT tag.
   * To do that, use {@link #jsEscapeInScriptTag(String, StringBuffer)}.
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void jsEscapeInXMLAttr(String str, StringBuffer buf) {
    // Here's some sample code to illustrate how to use the jsEscape*
    // functions in different contexts:
    //
    // StringBuffer sb = new StringBuffer();
    // sb.append("<script type=\"text/javascript\">\n  x = '");
    // Escaper.jsEscapeInScriptTag("It\'s \"</script>\"", sb);
    // sb.append("';\n</script>\n\n");
    // sb.append("<span onclick=\"alert('");
    // Escaper.jsEscapeInXMLAttr("It\'s \"</script>\"", sb);
    // sb.append(");\">My Span</span>");
    //
    // System.out.println(sb.toString());

    if (!Utility.isSet(str)) {
      return;
    }

    escapeSpecialCharacters(
      str,
      buf,
      _jsEscapeCharIsSpecialInXMLAttr,
      _jsEscapeSpecialCharReplacementInXMLAttr,
      false);
  }

  /**
   * Replace characters that have special meaning in XML with their entity
   * equivalents.
   * 
   * <p>Use {@link #xmlEscape(String, StringBuffer)} instead when
   * possible, as in many situations appending the results to a
   * StringBuffer is much more time and space efficient than
   * returning a String, which is what this method does.
   * 
   * @param str the string to escape
   * @return the modified string
   */
  public static String xmlEscape(String str) {
    return htmlEscape(str);
  }

  /**
   * Replace characters that have special meaning in XML with their entity
   * equivalents.  The result is appended to the supplied StringBuffer
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void xmlEscape(String str, StringBuffer buf) {
    htmlEscape(str, buf);
  }

  /**
   * Replace characters that have special meaning in HTML with their entity
   * equivalents.
   * 
   * <p>Use {@link #htmlEscape(String, StringBuffer)} instead when
   * possible, as in many situations appending the results to a
   * StringBuffer is much more time and space efficient than
   * returning a String, which is what this method does.
   * 
   * @param str the string to escape
   * @return the modified string
   */
  public static String htmlEscape(String str) {
    if (!Utility.isSet(str)) {
      return EMPTY_STRING;
    }

    StringBuffer buf = new StringBuffer((int) (1.1 * str.length()));
    htmlEscape(str, buf);
    return buf.toString();
  }

  /**
   * Replace characters that have special meaning in HTML with their entity
   * equivalents.  The result is appended to the supplied StringBuffer
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void htmlEscape(String str, StringBuffer buf) {
    if (!Utility.isSet(str)) {
      return;
    }

    escapeSpecialCharacters(
      str,
      buf,
      _htmlEscapeCharIsSpecial,
      _htmlEscapeSpecialCharReplacement,
      false);
  }

  /**
   * Replace characters that have special meaning in HTML with their entity
   * equivalents and whitespace is preserved.
   * 
   * <p>Use {@link #htmlEscapeAndPreserveWhitespace(String, StringBuffer)}
   * instead when possible, as in many situations appending the results to a
   * StringBuffer is much more time and space efficient than
   * returning a String, which is what this method does.
   * 
   * @param str the string to escape
   * @return the modified string
   */
  public static String htmlEscapeAndPreserveWhitespace(String str) {
    if (!Utility.isSet(str)) {
      return EMPTY_STRING;
    }

    StringBuffer buf = new StringBuffer((int) (1.1 * str.length()));
    htmlEscapeAndPreserveWhitespace(str, buf);
    return buf.toString();
  }

  /**
   * Replace characters that have special meaning in HTML with their entity
   * equivalents and whitespace is preserved.  The result is appended to
   * the supplied StringBuffer
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void htmlEscapeAndPreserveWhitespace(String str, StringBuffer buf) {

    if (!Utility.isSet(str)) {
      return;
    }

    escapeSpecialCharacters(
      str,
      buf,
      _htmlEscapeAndPreserveWhitespaceCharIsSpecial,
      _htmlEscapeAndPreserveWhitespaceSpecialCharReplacement,
      true);
  }

  /**
   * Replace characters with either their HTML entity equivalents or
   * suitable HTML elements.  Specifically:
   *
   * <ul>
   * <li>a space is converted to &amp;nbsp;</li>
   * <li>a tab is converted to 4 &amp;nbsp;</li>
   * <li>a carriage return is converted to &amp;br&amp;</li>
   * </ul>
   * 
   * <p>Use {@link #htmlPreserveWhitespace(String, StringBuffer)}
   * instead when possible, as in many situations appending the results to a
   * StringBuffer is much more time and space efficient than
   * returning a String, which is what this method does.
   * 
   * @param str the string to escape
   * @return the modified string
   */
  public static String htmlPreserveWhitespace(String str) {
    if (!Utility.isSet(str)) {
      return EMPTY_STRING;
    }

    StringBuffer buf = new StringBuffer((int) (1.1 * str.length()));
    htmlPreserveWhitespace(str, buf);
    return buf.toString();
  }

  /**
   * Replace characters with either their HTML entity equivalents or
   * suitable HTML elements.  Specifically:
   *
   * <ul>
   * <li>a space is converted to &amp;nbsp;</li>
   * <li>a tab is converted to 4 &amp;nbsp;</li>
   * <li>a carriage return is converted to &amp;br&amp;</li>
   * </ul>
   * 
   * <p>The result is appended to the supplied StringBuffer
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void htmlPreserveWhitespace(String str, StringBuffer buf) {
    if (!Utility.isSet(str)) {
      return;
    }

    escapeSpecialCharacters(
      str,
      buf,
      _htmlPreserveWhitespaceCharIsSpecial,
      _htmlPreserveWhitespaceSpecialCharReplacement,
      true);
  }

  /**
   * A version of URLEncoder.encode that we can use without having to always specify
   * the character encoding.  This translates a string into
   * <code>application/x-www-form-urlencoded</code> format using a specific encoding scheme.
   * This method uses the UTF-8 encoding scheme to obtain the bytes for unsafe
   * characters, as suggested in the <a href=
   * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
   * World Wide Web Consortium Recommendation</a>.
   * 
   * @param   s   <code>String</code> to be translated.
   * @return  the translated <code>String</code>.
   * 
   * @see URLEncoder#encode(String, String)
   */
  public static String urlEncode(String s) {
    String result = null;
    try {
      result = URLEncoder.encode(s, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  /**
   * Replace characters that have special meaning in Java regular expressions
   * with their entity equivalents.
   * 
   * <p>Use {@link #regExpEscape(String, StringBuffer)} instead when
   * possible, as in many situations appending the results to a
   * StringBuffer is much more time and space efficient than
   * returning a String, which is what this method does.
   * 
   * @param str the string to escape
   * @return the modified string
   */
  public static String regExpEscape(String str) {
    return regExpEscape(str, EMPTY_STRING);
  }

  /**
   * Replace characters that have special meaning in Java regular expressions
   * with their entity equivalents.
   * 
   * <p>Use {@link #regExpEscape(String, StringBuffer)} instead when
   * possible, as in many situations appending the results to a
   * StringBuffer is much more time and space efficient than
   * returning a String, which is what this method does.
   * 
   * @param str the string to escape
   * @param nonEscapedCharacters a string of characters not to be escaped
   * @return the modified string
   */
  public static String regExpEscape(String str, String nonEscapedCharacters) {
    if (!Utility.isSet(str)) {
      return EMPTY_STRING;
    }

    StringBuffer buf = new StringBuffer((int) (1.1 * str.length()));
    regExpEscape(str, buf, nonEscapedCharacters);
    return buf.toString();
  }

  /**
   * Replace characters that have special meaning in Java regular expressions
   * with their entity equivalents.  The result is appended to the supplied StringBuffer
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   */
  public static void regExpEscape(String str, StringBuffer buf) {
    regExpEscape(str, buf, EMPTY_STRING);
  }

  /**
   * Replace characters that have special meaning in Java regular expressions
   * with their entity equivalents.  The result is appended to the supplied StringBuffer
   *
   * @param str  the string to escape
   * @param buf  the buffer to append the result to
   * @param nonEscapedCharacters a string of characters not to be escaped
   */
  public static void regExpEscape(String str, StringBuffer buf, String nonEscapedCharacters) {
    if (!Utility.isSet(str)) {
      return;
    }
    
    boolean [] charIsSpecial = null;
    
    //if the called specified any characters to be left unescaped, handle that
    if (Utility.isSet(nonEscapedCharacters)) {
      int strlen = nonEscapedCharacters.length();
      charIsSpecial = new boolean[_regExpEscapeCharIsSpecial.length];
      System.arraycopy(_regExpEscapeCharIsSpecial, 0, charIsSpecial, 0, _regExpEscapeCharIsSpecial.length);
      int characterIsSpecialLength = charIsSpecial.length;
      char[] chars = nonEscapedCharacters.toCharArray();
  
      for (int i = 0; i < strlen; i++) {
        char c = chars[i];
        charIsSpecial[c] = false;
      }
    }
    else {
      charIsSpecial = _regExpEscapeCharIsSpecial;
    }

    escapeSpecialCharacters(
      str,
      buf,
      charIsSpecial,
      _regExpEscapeSpecialCharReplacement,
      false);
  }

}
