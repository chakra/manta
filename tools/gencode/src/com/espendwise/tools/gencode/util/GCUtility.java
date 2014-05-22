package com.espendwise.tools.gencode.util;


import java.io.*;

public class GCUtility {

    public static String loadFile(File pFile) throws IOException {
        BufferedReader rdr = null;
        try {
            rdr = new BufferedReader(new FileReader(pFile));
            return loadFile(rdr);
        } finally {
            if (rdr != null) {
                rdr.close();
            }
        }
    }

    private static String loadFile(BufferedReader pReader) throws IOException {
        String line = pReader.readLine();
        StringBuilder data = new StringBuilder();
        while (line != null) {
            data.append(line).append("\n");
            line = pReader.readLine();
        }
        return data.toString();
    }

    public static String loadFile(String file) throws IOException {
        return loadFile(new File(file));
    }
}
