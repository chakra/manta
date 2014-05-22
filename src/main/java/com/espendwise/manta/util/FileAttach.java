package com.espendwise.manta.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAttach implements java.io.Serializable{
    private String name;
    private byte[] fileData;
    private String contentType;
    private int fileSize;

    private static final long serialVersionUID = 8823952771380003037L;

    public FileAttach(String name, byte[] fileData, String contentType, int fileSize) {
        this.name = name;
        this.fileData = fileData;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public File fromAttachToFile() throws IOException {
        File file = Utility.createTempFileAttachment(getName());

        if(file.exists()) {
            file.delete();
        }
        if (file.createNewFile()) {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(getFileData());
            bos.flush();
            bos.close();
        }
        return file;
    }

    static public byte[] fromFileToByte(File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        byte[] byteData;
        int i;
        do{ i=bis.read();
            if(i!=-1){baos.write(i);
            }
        }while(i!=-1);
        baos.flush();
        byteData=baos.toByteArray();

        baos.close();
        bis.close();

        return byteData;
    }
}
