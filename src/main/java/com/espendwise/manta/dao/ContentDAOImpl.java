package com.espendwise.manta.dao;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.model.data.ContentData;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Locale;

import org.springframework.web.multipart.commons.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class ContentDAOImpl extends DAOImpl implements ContentDAO {

    private static final Logger logger = Logger.getLogger(ContentDAOImpl.class);


    public ContentDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public byte[] findBestLogo(Long busEntityId, String path, Locale userLocale) {

        logger.info("findBestLogo()=> busEntityId: " + busEntityId +
                ", path: " + path +
                ", userLocale: " + userLocale
        );

        List<String> locales = Utility.toList(userLocale.toString(),
                userLocale.getLanguage(),
                Constants.DEFAULT_LOCALE.toString(),
                Constants.DEFAULT_LOCALE.getLanguage(),
                Constants.CONTENT_DEFAULT_LOCALE_CD
        );

        Query q = em.createQuery("Select content.contentId, content.localeCd  from ContentData content" +
                " where content.contentTypeCd = (:contentTypeCd)    " +
                " and content.contentUsageCd = (:contentUsageCd)  " +
                " and content.contentStatusCd = (:contentStatusCd)" +
                " and content.path like :path                     " +
                " and content.localeCd in (:localeCd)");

        q.setParameter("contentTypeCd", RefCodeNames.CONTENT_TYPE_CD.IMAGE);
        q.setParameter("contentUsageCd", RefCodeNames.CONTENT_USAGE_CD.LOGO_IMAGE);
        q.setParameter("path", QueryHelp.endWith(path));
        q.setParameter("localeCd", locales);
        q.setParameter("contentStatusCd", RefCodeNames.CONTENT_STATUS_CD.ACTIVE);

        List contents = q.getResultList();
        if(contents.isEmpty()) {
            return new byte[0];
        }

        Long bestContentId = (long) Constants.ZERO;

        for (String locale : locales) {
            for (Object obj : contents) {
                Object[] c = (Object[]) obj;
                if (locale.equals(c[1])) {
                    bestContentId = (Long) c[0];
                    break;
                }
            }
        }

        q = em.createQuery("Select content.binaryData from ContentData content" +
                " where content.contentId = (:bestContentId)");

        q.setParameter("bestContentId", bestContentId);

        List datas = q.getResultList();


        return datas.isEmpty() ? new byte[0] : (byte[]) datas.get(0);


    }

    @Override
    public void addContentSaveImage(String path, String imageType, CommonsMultipartFile imageFile) {
        if (imageFile == null) return;
        addContentSaveImage(path, imageType, imageFile.getBytes());
    }

    @Override
    public void addContentSaveImage(String path, String imageType, byte[] imageFile) {
        logger.info("addContentSaveImage()=> path: " + path );
           
        // remove old content
        path = "." + path;
        removeContent(path);
        
        // create and insert content
        ContentData d = new ContentData();
        d.setShortDesc(imageType);
        d.setContentTypeCd("Image");
        d.setContentStatusCd(RefCodeNames.CONTENT_STATUS_CD.ACTIVE);
        d.setLocaleCd("x");
        d.setLanguageCd("x");
        d.setContentUsageCd(imageType);
        d.setSourceCd("xsuite-app");
        d.setPath(path);
        d.setBinaryData(imageFile);
        d = super.create(d);
    }


    public void copyContentImage(String oldPath, String newPath, String imageType) {
        Query q = em.createQuery("Select content  from ContentData content" +
                " where content.path like :path  ");
        q.setParameter("path", QueryHelp.endWith(oldPath));
        List<ContentData> res = q.getResultList();
        for (ContentData oldD : res) {
            ContentData d = new ContentData();
            d.setShortDesc(oldD.getShortDesc());
            d.setContentTypeCd(oldD.getContentTypeCd());
            d.setContentStatusCd(RefCodeNames.CONTENT_STATUS_CD.ACTIVE);
            d.setLocaleCd(oldD.getLocaleCd());
            d.setLanguageCd(oldD.getLanguageCd());
            d.setContentUsageCd(oldD.getContentUsageCd());
            d.setSourceCd(oldD.getSourceCd());
            d.setPath(newPath);
            byte[] bindata = oldD.getBinaryData();
            d.setBinaryData(bindata);
            d = super.create(d);
        }

    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeContent(String path) {
        Query q = em.createQuery("Select content  from ContentData content" +
                " where content.path like :path  ");
        q.setParameter("path", QueryHelp.endWith(path));
        List<ContentData> res = q.getResultList();
        for (ContentData d : res) {
            em.remove(d);
        }
    }

    @Override
    public byte[] getImage(String path, String contentType, String contentUsage) {
        Query q = em.createQuery("Select content.binaryData from ContentData content" +
               // " where content.contentTypeCd = (:contentTypeCd)    " +
               // " and content.contentUsageCd = (:contentUsageCd)  " +
                " where content.path like (:path) " +
                " and content.contentStatusCd = (:contentStatusCd)");
               // " and content.localeCd in (:localeCd)");

      //  q.setParameter("contentTypeCd", contentType);
      //  q.setParameter("contentUsageCd", contentUsage);
        q.setParameter("path", QueryHelp.endWith(path));
        q.setParameter("contentStatusCd", RefCodeNames.CONTENT_STATUS_CD.ACTIVE);

        List datas = q.getResultList();
        byte[]  res = datas.isEmpty() ? new byte[0] : (byte[]) datas.get(0);
        return res;

    }
}
