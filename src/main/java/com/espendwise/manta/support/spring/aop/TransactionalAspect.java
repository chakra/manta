package com.espendwise.manta.support.spring.aop;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.service.DatabaseAccess;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.support.spring.ApplicationTransactionManager;
import com.espendwise.manta.util.TxUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;

@Aspect
public class TransactionalAspect extends TransactionAspectSupport {

    public TransactionalAspect() {
        setTransactionAttributeSource(new AnnotationTransactionAttributeSource(false));
        logger.info("TransactionalAspect aspect created");
    }

    @Pointcut(" execution(public * ((@org.springframework.transaction.annotation.Transactional *)+).*(..)) && within(@org.springframework.transaction.annotation.Transactional *)")
    public void executionOfAnyPublicMethodInAtTransactionalType() {
    }

    @Pointcut("execution(@org.springframework.transaction.annotation.Transactional * *(..))")
    public void executionOfTransactionalMethod() {
    }

    @Pointcut(value = "(executionOfAnyPublicMethodInAtTransactionalType() || executionOfTransactionalMethod()) && this(txObject)", argNames = "txObject")
    public void transactionalMethodExecution(Object txObject) {
    }

    @Before(value = "transactionalMethodExecution(txObject)", argNames = "jp, txObject")
    public void executionTransactionBefore(JoinPoint jp, Object txObject) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        createTransactionIfNecessary(methodSignature, jp.getArgs(), jp.getTarget());
    }

    @SuppressAjWarnings("adviceDidNotMatch")
    @AfterThrowing(pointcut = "transactionalMethodExecution(Object)", throwing = "t", argNames = "t")
    public void executionTransactionAfterThrowing(Throwable t) {
        logger.info("<rollback>");
        try {
            completeTransactionAfterThrowing(TransactionAspectSupport.currentTransactionInfo(), t);
        } catch (Throwable t2) {
            logger.error("Failed to close transaction after throwing in a transactional method", t2);
        }
    }

    @SuppressAjWarnings("adviceDidNotMatch")
    @AfterReturning(pointcut = "transactionalMethodExecution(Object)")
    public void executionTransactionAfterReturning() {
        TransactionInfo txInfo = TransactionAspectSupport.currentTransactionInfo();
        if (txInfo != null && txInfo.hasTransaction()) {
            logger.info("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
            logger.info("<commit>");
             commitTransactionAfterReturning(txInfo);
       } else {
            logger.info("no tx info");
        }
    }

    @Around("transactionalMethodExecution(Object)")
    public Object transactionalMethodExecute(ProceedingJoinPoint pjp) throws Throwable {

        logger.info("");
        logger.info("----------------------------------------------------------------");
        logger.info("BEGIN TRANSACTIONAL METHOD (" + pjp.getSignature().getName() + "), USER '"+ getUserName() + "' ");
        logger.info("----------------------------------------------------------------");
        logger.info("");

        StopWatch sw = new StopWatch(getClass().getSimpleName());

        try {

            sw.start(pjp.getSignature().getName());
            return pjp.proceed();

        } finally {

            try {

                cleanupTransactionInfo(TransactionAspectSupport.currentTransactionInfo());

            } catch (NoTransactionException e) {

                logger.error("Failed to cleanup transaction, Error - "+e.getMessage(), e);

            } finally {

                sw.stop();

                logger.info(sw.toString());
                logger.info("");
                logger.info("----------------------------------------------------------------");
                logger.info("END TRANSACTIONAL METHOD (" + pjp.getSignature().getName() + ")    ");
                logger.info("----------------------------------------------------------------");
                logger.info("");

            }

        }
    }

    protected TransactionInfo createTransactionIfNecessary(MethodSignature signature, Object[] args, Object target) throws NoSuchMethodException {

        try {
            Method method = signature.getMethod();

            String methodName = methodIdentification(method, target.getClass());
            TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, target.getClass());

            PlatformTransactionManager tm = determineTransactionManager(signature,
                    args,
                    txAttr,
                    target
            );


            return createTransactionIfNecessary(tm, txAttr, methodName);
       
        } catch (Throwable e) {

            logger.error("Failed to open transaction, Error: "+e.getMessage());

        }

        return null;
    }

    private PlatformTransactionManager determineTransactionManager(MethodSignature signature, Object[] args, TransactionAttribute txAttr, Object target) throws SQLException {

        PlatformTransactionManager userTxManager = null;
        Object datasource = null;

        PlatformTransactionManager txManager = getTransactionManager();

        if (txManager instanceof ApplicationTransactionManager) {

            ApplicationTransactionManager appTxManager = (ApplicationTransactionManager) txManager;

            Method method = BridgeMethodResolver.findBridgedMethod(
                    ClassUtils.getMostSpecificMethod(
                            signature.getMethod(),
                            ClassUtils.getUserClass(target.getClass())
                    )
            );

            datasource = determineDataSource(method, args);
            if (datasource == null) {
                logger.info("determineTransactionManager()=>" +
                        " no specific data source," +
                        " will be used datasource from application context");
                datasource = Auth.getApplicationDataSource();
            }

            userTxManager = appTxManager.txManager(datasource);

        }

        if (userTxManager == null) {
            userTxManager = determineTransactionManager(txAttr);
        }


        print(userTxManager, datasource);

        return userTxManager;

    }

    private void print(PlatformTransactionManager userTxManager, Object applicationDataSource) throws SQLException {

        JpaTransactionManager specificTxManager = null;
        if (userTxManager != null && userTxManager instanceof JpaTransactionManager) {
            specificTxManager = ((JpaTransactionManager) userTxManager);
        }

        logger.info("");
        logger.info("tx.manager = '" + specificTxManager+"'");
        logger.info("tx.unit = " + (applicationDataSource == null ? "null" : applicationDataSource.toString())+"'");
        logger.info("tx.url = '" + (specificTxManager != null ? TxUtil.getDataSourceUrl(specificTxManager.getDataSource())  : null));

        logger.info("");

    }

    private Object determineDataSource(Method method, Object[] args) {
        Object datasource = null;
        int i = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof AppDS) {
                    if (args[i] instanceof String) {
                        datasource = (String) args[i];
                    }
                }
            }
            i++;
        }
        return datasource;
    }

    public String getUserName() {
        AppUser user = Auth.getAppUser();
        return  user == null ? "system" : user.getUserName();
    }
}
