package com.espendwise.manta.dao;


import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Locale;

public interface ContentDAO {
    public byte[] findBestLogo(Long busEntityId, String path, Locale userLocale);

    public void addContentSaveImage(String path, String imageType, CommonsMultipartFile imageFile);

    public void addContentSaveImage(String path, String imageType,  byte[] imageFile);

    public void copyContentImage(String oldPath, String newPath, String imageType);

    public byte[] getImage(String path, String contentType, String contentUsage);

    public void removeContent(String path);

}
