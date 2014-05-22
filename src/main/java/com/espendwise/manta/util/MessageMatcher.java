package com.espendwise.manta.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import org.apache.log4j.Logger;

public class MessageMatcher {
    private static Logger logger = Logger.getLogger(MessageMatcher.class);
    private String inputDirPath_ = "";
    private String outputDirPath_ = "";
    private File inputDir_ = null;
    private File outputDir_ = null;
    
    private Map<String, Pair<String, String>> inBaseMessages_ = null;
    private Map<String, Pair<String, String>> outBaseMessages_ = null;
    
    private List<String> outBaseMessagesKeys_ = null;
    
    public static String MESSAGE_ALL = "Message_All";
    public static String MESSAGES = "messages";
    public static String UTF_8 = "UTF-8";
    public static String FILE_SEPARATOR;
    public static String LINE_SEPARATOR;
    public static enum project_name {ORCA, MANTA};

    public MessageMatcher(String[] args) {
        FILE_SEPARATOR = System.getProperty("file.separator");
        LINE_SEPARATOR = System.getProperty("line.separator");
        inputDirPath_ = args[0];
        if (!inputDirPath_.endsWith(FILE_SEPARATOR)) {
            inputDirPath_ += FILE_SEPARATOR;
        }
        outputDirPath_ = args[1];
        if (!outputDirPath_.endsWith(FILE_SEPARATOR)) {
            outputDirPath_ += FILE_SEPARATOR;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            logger.error("Too few input parameters.");
            logger.info("Usage: inputDir destinationDir");
        }

        MessageMatcher matcher = new MessageMatcher(args);
        matcher.process();
    }

    public void process() {
        checkDirs();
        if (inputDir_ == null || outputDir_ == null) {
            return;
        }
        
        String baseFileNameOrca = composeFileName(project_name.ORCA, "_en");
        Set inputFileNames = getFileNames(project_name.ORCA, inputDir_);
        if (!inputFileNames.contains(baseFileNameOrca)) {
            logger.info("Can't find input file: (" + baseFileNameOrca + ")");
            return;
        }
        inputFileNames.remove(baseFileNameOrca);
        
        String baseFileNameManta = composeFileName(project_name.MANTA, "_en");
        Set outputFileNames = getFileNames(project_name.MANTA, outputDir_);
        if (!outputFileNames.contains(baseFileNameManta)) {
            logger.info("Can't find destination file: (" + baseFileNameManta + ")");
            return;
        }
        outputFileNames.remove(baseFileNameManta);
        
        // Collecting input mesages
        collectInBaseMessages(baseFileNameOrca, Charset.forName(UTF_8));
        
        // Collecting destination messages
        collectOutBaseMessages(baseFileNameManta, Charset.forName(UTF_8));
        
        // Composing Key Crossing Map (destinationKey to inputKey)
        Map<String, String> keyCrossings = new HashMap<String, String>();
        Iterator<String> it;
        if (!outBaseMessages_.isEmpty()) {
            it = outBaseMessages_.keySet().iterator();
            String destinationKey;
            while (it.hasNext()) {
                destinationKey = it.next();
                if (inBaseMessages_.containsKey(destinationKey)) {
                    if (!keyCrossings.containsKey(outBaseMessages_.get(destinationKey).getObject1())) {
                        keyCrossings.put(outBaseMessages_.get(destinationKey).getObject1(),
                                         inBaseMessages_.get(destinationKey).getObject1());
                    } else {
                        logger.error("Duplicate key: (" + outBaseMessages_.get(destinationKey).getObject1() + ")");
                    }
                }
            }
        } else {
            logger.info("Nothing to translate. file: (" + outputDirPath_ + baseFileNameManta + ")");
            return;
        }
        
        if (keyCrossings.isEmpty()) {
            logger.info("No matches found.");
            return;
        } else {
            logger.info(keyCrossings.size() + " matches found.");
        }
        
        // Processing internationalization files
        it = outputFileNames.iterator();
        String fileName;
        String pairedFileName;
        Map<String, String> currentInputMessages;
        while (it.hasNext()) {
            fileName = it.next();
            logger.info("Processing file: (" + outputDirPath_ + fileName + ")");
            
            pairedFileName = convertFileName(project_name.MANTA, fileName);
            if (inputFileNames.contains(pairedFileName)) { // Collect existing messages
                currentInputMessages = collectMessages(inputDirPath_, pairedFileName, Charset.forName(UTF_8));
            } else {
                logger.info("Can't find paired file with translated messages");
                continue;
            }

            try {
                // create temporary file for the new translated messages                    
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outputDirPath_ + fileName), Charset.forName(UTF_8)));

                // go through the existing message file
                String[] tokens;
                String str;
                String originalStr;
                StringBuilder output = new StringBuilder('\ufeff');
                boolean fileWasEmpty = true;
                while ((originalStr = in.readLine()) != null) {
                    if (originalStr.length() > 0) {
                        originalStr = (originalStr.charAt(0) == '\ufeff') ? originalStr.substring(1) : originalStr;
                    }
                    str = originalStr.trim();
                    if (str.startsWith("#") || str.length() == 0) {
                        output.append(originalStr);
                        output.append(LINE_SEPARATOR);
                        fileWasEmpty = false;
                        continue;
                    } else {
                        String message = null;
                        boolean messagesNotEqual = false;
                        tokens = tokenize (str, '=');
                        if (tokens != null && tokens.length > 1) {
                            tokens[0] = tokens[0].trim();
                            tokens[1] = tokens[1].trim();
                            if (keyCrossings.containsKey(tokens[0])) {
                                message = currentInputMessages.get(keyCrossings.get(tokens[0]));
                                if (Utility.isSet(message)) {
                                    if (!message.equals(tokens[1])) {
                                        messagesNotEqual = true;
                                    } else { // already present
                                        currentInputMessages.remove(keyCrossings.get(tokens[0]));
                                    }
                                }
                            }
                        } 
                        output.append(originalStr);
                        output.append(LINE_SEPARATOR);
                        if (messagesNotEqual) {
                            output.append("### WARNING! Duplicate key with different messages: (");
                            output.append(tokens[0]);
                            output.append("=");
                            output.append(tokens[1]);
                            output.append(")");
                            output.append(LINE_SEPARATOR);
                        }
                        fileWasEmpty = false;
                    }
                }
                in.close();

                if (!currentInputMessages.isEmpty()) {
                    boolean firstMatch = true;
                    for (String key : outBaseMessagesKeys_) {
                        if (currentInputMessages.containsKey(keyCrossings.get(key))) {
                            if (firstMatch && !fileWasEmpty) {
                                output.append(LINE_SEPARATOR);
                                firstMatch = false;
                            }
                            output.append(key);
                            output.append("=");
                            output.append(currentInputMessages.get(keyCrossings.get(key)));
                            output.append(LINE_SEPARATOR);
                        }
                    }
                }

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDirPath_ + fileName, false), Charset.forName(UTF_8)));

                out.write(output.toString());

                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                logger.error("process(): Can't read file: (" + outputDirPath_ + pairedFileName + ")");
            } catch (UnsupportedEncodingException e) {
                logger.error("process(): Unsupported encoding: (" + UTF_8 + ") file: (" + outputDirPath_ + pairedFileName + ")");
            } catch (IOException e) {
                logger.error("process(): Error reading file: (" + outputDirPath_ + fileName + ")");
            }
        }
        
    }
    
    private void checkDirs() {
        File dir = new File(inputDirPath_);
        if (dir.exists()) {
            inputDir_ = dir;
        } else {
            inputDir_ = null;
            logger.error("Can't find input directory: (" + inputDirPath_ + ")");
        }
        dir = new File(outputDirPath_);
        if (dir.exists()) {
            outputDir_ = dir;
        } else {
            outputDir_ = null;
            logger.info("Can't find destination directory: (" + inputDirPath_ + ")");
        }
    }
    
    private void collectInBaseMessages (String fileName, Charset charset) {
        inBaseMessages_ = new HashMap<String, Pair<String, String>>();
        
        Set<String> keySet = new HashSet<String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputDirPath_ + fileName), charset));
            
            String[] tokens;
            String str;
            while ((str = in.readLine()) != null) {
                if (str.length() > 0) {
                    str = (str.charAt(0) == '\ufeff') ? str.substring(1) : str;
                }
                str = str.trim();
                if (str.startsWith("#") || str.length() == 0) {
                    continue;
                } else {
                    tokens = tokenize (str, '=');
                    if (tokens != null && tokens.length > 1) {
                        tokens[0] = tokens[0].trim();
                        tokens[1] = tokens[1].trim();
                        if (keySet.contains(tokens[0])) {
                            logger.error("collectInBaseMessages(): WARNING! duplicate key: <<" + tokens[0] + ">>");
                        } else {
                            keySet.add(tokens[0]);
                            inBaseMessages_.put(tokens[0] + '=' + tokens[1], new Pair(tokens[0], tokens[1]));
                        }
                        
                    }
                }
            }
            
            in.close();   
        } catch (FileNotFoundException e) {
            logger.error("collectInBaseMessages(): Can't read file: (" + inputDirPath_ + fileName + ")");
        } catch (UnsupportedEncodingException e) {
            logger.error("collectInBaseMessages(): Unsupported encoding: (" + charset.displayName() + ") file: (" + inputDirPath_ + fileName + ")");
        } catch (IOException e) {
            logger.error("collectInBaseMessages(): Error reading file: (" + inputDirPath_ + fileName + ")");
        }
    }
    
    private void collectOutBaseMessages (String fileName, Charset charset) {
        outBaseMessages_ = new HashMap<String, Pair<String, String>>();
        outBaseMessagesKeys_ = new ArrayList<String>();
        
        Set<String> keySet = new HashSet<String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outputDirPath_ + fileName), charset));
            
            String[] tokens;
            String str;
            while ((str = in.readLine()) != null) {
                if (str.length() > 0) {
                    str = (str.charAt(0) == '\ufeff') ? str.substring(1) : str;
                }
                str = str.trim();
                if (str.startsWith("#") || str.length() == 0) {
                    continue;
                } else {
                    tokens = tokenize (str, '=');
                    if (tokens != null && tokens.length > 1) {
                        tokens[0] = tokens[0].trim();
                        tokens[1] = tokens[1].trim();
                        if (keySet.contains(tokens[0])) {
                            logger.error("collectOutBaseMessages(): WARNING! duplicate key: <<" + tokens[0] + ">>");
                        } else {
                            keySet.add(tokens[0]);
                            outBaseMessages_.put(tokens[0] + '=' + tokens[1], new Pair(tokens[0], tokens[1]));
                            outBaseMessagesKeys_.add(tokens[0]);
                        }
                    }
                }
            }
            
            in.close();   
        } catch (FileNotFoundException e) {
            logger.error("collectOutBaseMessages(): Can't read file: (" + outputDirPath_ + fileName + ")");
        } catch (UnsupportedEncodingException e) {
            logger.error("collectOutBaseMessages(): Unsupported encoding: (" + charset.displayName() + ") file: (" + outputDirPath_ + fileName + ")");
        } catch (IOException e) {
            logger.error("collectOutBaseMessages(): Error reading file: (" + outputDirPath_ + fileName + ")");
        }
    }
    
    private Map collectMessages (String dirPath, String fileName, Charset charset) {
        Map<String, String> messagesMap = new HashMap<String, String>();
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dirPath + fileName), charset));
            
            String[] tokens;
            String str;
            while ((str = in.readLine()) != null) {
                if (str.length() > 0) {
                    str = (str.charAt(0) == '\ufeff') ? str.substring(1) : str;
                }
                str = str.trim();
                if (str.startsWith("#") || str.length() == 0) {
                    continue;
                } else {
                    tokens = tokenize (str, '=');
                    if (tokens != null && tokens.length > 1) {
                        tokens[0] = tokens[0].trim();
                        tokens[1] = tokens[1].trim();
                        messagesMap.put(tokens[0], tokens[1]);
                    }
                }
            }
            
            in.close();   
        } catch (FileNotFoundException e) {
            logger.error("collectMessages(): Can't read file: (" + dirPath + fileName + ")");
        } catch (UnsupportedEncodingException e) {
            logger.error("collectMessages(): Unsupported encoding: (" + charset.displayName() + ") file: (" + dirPath + fileName + ")");
        } catch (IOException e) {
            logger.error("collectMessages(): Error reading file: (" + dirPath + fileName + ")");
        }
        
        return messagesMap;
    }
    
    private String[] tokenize (String line, char token) {
        String[] tokens = null;
        if (line != null) {
            int idx;
            if ((idx = line.indexOf(token)) != -1) {
                tokens = new String[2];
                tokens[0] = line.substring(0, idx);
                tokens[1] = line.substring(idx + 1, line.length());
            } else {
                tokens = new String[1];
                tokens[0] = line;
            }
        }
        return tokens;
    }
    
    private Set getFileNames (project_name projectName, File directory) {
        File[] files = directory.listFiles(); 
        Set fileNames = new HashSet();
        
        String prefix;
        String postfix;
        
        switch (projectName) {
            case ORCA:
                    prefix = MESSAGE_ALL;
                    postfix = ".txt";
                break;
            case MANTA:
                    prefix = MESSAGES;
                    postfix = ".properties";
                break;
            default:
                    prefix = "";
                    postfix = "";
                break;
        }

        String fileName;
        for (File file : files) {
            if (file.isFile()) {
                fileName = file.getName();
                if (fileName.startsWith(prefix) && (fileName.endsWith(postfix) || fileName.endsWith(postfix.toUpperCase()))) {
                    fileNames.add(fileName);
                }
            }
        }

        return fileNames;
    }
    
    private String convertFileName (project_name from, String fileName) {
        String resultFileName = fileName;

        int idx = 0;
        switch (from) {
            case ORCA:
                    if (fileName.startsWith(MESSAGE_ALL) && (idx = fileName.indexOf('.')) > -1) {
                        resultFileName = fileName.substring(MESSAGE_ALL.length(), idx);
                        resultFileName = MESSAGES + resultFileName + ".properties";
                    }
                break;
            case MANTA:
                    if (fileName.startsWith(MESSAGES) && (idx = fileName.indexOf('.')) > -1) {
                        resultFileName = fileName.substring(MESSAGES.length(), idx);
                        resultFileName = MESSAGE_ALL + resultFileName + ".txt";
                    }
                break;
            default:
                break;
        }
        
        return resultFileName;
    }
    
    private String composeFileName(project_name projectName, String postfix) {
        String result = null;
        switch (projectName) {
            case ORCA:
                    result = MESSAGE_ALL + postfix + ".txt";
                break;
            case MANTA:
                    result = MESSAGES + postfix + ".properties";
                break;
            default:
                break;
        }
        return result;
    }

}
