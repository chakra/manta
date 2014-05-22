package com.espendwise.manta.util.crypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class BASE64Encoder {

    public String encode(byte[] raw) {
        return Base64.encode(raw);
    }

    public String encode(byte[] raw, int length) {
        return Base64.encode(raw,length);
    }

    public void encode (String inputFilename, String outputFilename) throws IOException {
        FileInputStream in = new FileInputStream(inputFilename);
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFilename));
        byte [] buf = new byte [48];
        int bytesRead;
        while ((bytesRead = in.read(buf)) > 0) {
            out.println(encode(buf, bytesRead));
        }
        in.close();
        out.flush();
        out.close();
    }
}