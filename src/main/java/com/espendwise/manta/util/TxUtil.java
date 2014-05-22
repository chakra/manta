package com.espendwise.manta.util;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

public class TxUtil {

    private static final Logger logger = Logger.getLogger(TxUtil.class);

    public static boolean isAliveEntityManager(String unit, EntityManager entityManager) {

        try {

            if (entityManager != null) {

                String dsUrl = getDataSourceUrl(entityManager);

                if (dsUrl != null) {
                    javax.persistence.Query q = entityManager.createNativeQuery("Select sysdate from dual");
                    Date sysdate = (Date) q.getSingleResult();
                    logger.info("isAliveEntityManager()=> unit[" + unit + "] database system time: " + sysdate);
                    return sysdate != null;
                }
            }

        } catch (Exception ex) {  //ignore
            logger.error("isAliveEntityManager()=> unit[" + unit + "] ERROR: " + ex.getMessage());
        } catch (Throwable ex) {  //ignore
            logger.error("isAliveEntityManager()=> unit[" + unit + "] ERROR: " + ex.getMessage());
        }

        logger.info("isAliveEntityManager()=> unit[" + unit + "] entity manager is unavailable");

        return false;

    }

    public static String getDataSourceUrl(EntityManager em) {
        return em != null
                ? getDataSourceUrl(getDataSource(em))
                : null;
    }

    private static DataSource getDataSource(EntityManager em) {

        try {

            if (Proxy.isProxyClass(em.getEntityManagerFactory().getClass())) {

                Class userClass = ClassUtils.getUserClass(em.getEntityManagerFactory());
                Method method = BridgeMethodResolver.findBridgedMethod(
                        ClassUtils.getMostSpecificMethod(
                                ClassUtils.getMethod(userClass, "getDataSource"),
                                userClass
                        )
                );

                return (DataSource) method.invoke(em.getEntityManagerFactory());

            } else {

                Method method = ClassUtils.getMethod(em.getEntityManagerFactory().getClass(), "getDataSource");
                return (DataSource) method.invoke(em);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getDataSourceUrl(DataSource dataSource) {
        return (dataSource instanceof BasicDataSource)
                ? ((BasicDataSource) dataSource).getUrl()
                : (dataSource instanceof ComboPooledDataSource)
                ? ((ComboPooledDataSource) dataSource).getJdbcUrl()
                : null;
    }
}
