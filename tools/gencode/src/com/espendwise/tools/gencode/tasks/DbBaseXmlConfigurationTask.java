package com.espendwise.tools.gencode.tasks;

import com.espendwise.tools.gencode.dbxml.DbXmlAssist;
import com.espendwise.tools.gencode.hbmxml.DbBaseConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.tool.ant.ConfigurationTask;
import org.hibernate.util.ReflectHelper;

import java.io.File;
import java.lang.reflect.Constructor;

public class DbBaseXmlConfigurationTask extends ConfigurationTask {

    private final static Log log = LogFactory.getLog(DbBaseXmlConfigurationTask.class);

    private String namesStrategy;
    private String packageName;
    private String xmlFile;
    private Path namesStrategyFiles;
    private String dialect;

    private boolean preferBasicCompositeIds = true;
    private boolean detectManyToMany = true;
    private boolean detectOptimisticLock = true;

    public DbBaseXmlConfigurationTask() {
        setDescription("DBBase Configuration");
    }

    protected Configuration createConfiguration() {
        return new DbBaseConfiguration();
    }

    protected void doConfiguration(Configuration configuration) {

        DbBaseConfiguration jmdc = (DbBaseConfiguration) configuration;
        super.doConfiguration(jmdc);

        jmdc.setPreferBasicCompositeIds(isPreferBasicCompositeIds());
        jmdc.setDatabase(DbXmlAssist.parseDbXml(getXmlFile()));
        jmdc.setDialect(getDialect());

        DefaultReverseEngineeringStrategy defaultStrategy = new DefaultReverseEngineeringStrategy();

        ReverseEngineeringStrategy strategy = defaultStrategy;

        if(getNamesStrategyFiles()!=null) {

            OverrideRepository or = new OverrideRepository();

            String[] fileNames = getNamesStrategyFiles().list();
            for (String fileName : fileNames) {
                or.addFile(new File(fileName));
            }

            strategy = or.getReverseEngineeringStrategy(defaultStrategy);

        }

        if(getNamesStrategy()!=null) {
            strategy = loadreverseEngineeringStrategy(getNamesStrategy(), strategy);
        }

        ReverseEngineeringSettings settings =
                new ReverseEngineeringSettings(strategy).setDefaultPackageName(getPackageName())
                        .setDetectManyToMany(isDetectManyToMany())
                        .setDetectOptimisticLock(isDetectOptimisticLock());

        defaultStrategy.setSettings(settings);
        strategy.setSettings(settings);

        jmdc.setReverseEngineeringStrategy(strategy);

        jmdc.read();
    }


    private ReverseEngineeringStrategy loadreverseEngineeringStrategy(final String className, ReverseEngineeringStrategy delegate) throws BuildException {

        try {

            Class clazz = ReflectHelper.classForName(className);
            Constructor constructor = clazz.getConstructor(new Class[] { ReverseEngineeringStrategy.class });

            return (ReverseEngineeringStrategy) constructor.newInstance(delegate);

        } catch (NoSuchMethodException e) {
            try {

                log.info("loadreverseEngineeringStrategy()=> Could not find public " + className + "(ReverseEngineeringStrategy delegate) constructor on ReverseEngineeringStrategy. Trying no-arg version.");

                Class clazz = ReflectHelper.classForName(className);
                ReverseEngineeringStrategy rev = (ReverseEngineeringStrategy) clazz.newInstance();

                log.info("loadreverseEngineeringStrategy()=> Using non-delegating strategy, thus packagename and revengfile will be ignored.");

                return rev;

            } catch (Exception eq) {
                throw new BuildException("Could not create or find " + className + " with default no-arg constructor", eq);
            }
        } catch (Exception e) {
            throw new BuildException("Could not create or find " + className + " with one argument delegate constructor", e);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isDetectManyToMany() {
        return detectManyToMany;
    }

    public boolean isPreferBasicCompositeIds() {
        return preferBasicCompositeIds;
    }

    public Path getNamesStrategyFiles() {
        return namesStrategyFiles;
    }

    public boolean isDetectOptimisticLock() {
        return detectOptimisticLock;
    }

    public String getNamesStrategy() {
        return namesStrategy;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public void setNamesStrategy(String namesStrategy) {
        this.namesStrategy = namesStrategy;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void setPreferBasicCompositeIds(boolean preferBasicCompositeIds) {
        this.preferBasicCompositeIds = preferBasicCompositeIds;
    }

    public void setDetectManyToMany(boolean detectManyToMany) {
        this.detectManyToMany = detectManyToMany;
    }

    public void setNamesStrategyFiles(Path namesStrategyFiles) {
        this.namesStrategyFiles = namesStrategyFiles;
    }

    public void setDetectOptimisticLock(boolean detectOptimisticLock) {
        this.detectOptimisticLock = detectOptimisticLock;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
