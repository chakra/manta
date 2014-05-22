package com.espendwise.manta.i18n;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.IOUtility;
import com.espendwise.manta.util.ResourceNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.I18nResolvedOutput;
import com.espendwise.manta.util.alert.ResolvedOutput;
import com.espendwise.manta.util.parser.Parse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Resource(mappedName = ResourceNames.MESSAGE_RESOURCE)
public class MessageResourceImpl implements StoreMessageResource {

    private static Log logger = LogFactory.getLog(MessageResourceImpl.class);

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private String resourceDirectory;
    private String messagesPrefix;
    private String messagesExt;

    private String storeImplOn;
    private I18nMessages i18nMessages;
    private StoreLocales storeLocales;
    private ReentrantLock lock;

    private static final String DEFAULT_MESSAGE_PREFIX = "messages";
    private static final String DEFAULT_MESSAGE_EXT = ".properties";

    public void setResourceDirectory(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }

    public void setMessagesPrefix(String messagesPrefix) {
        this.messagesPrefix = messagesPrefix;
    }

    public String getMessagesPrefix() {
        return messagesPrefix == null ? DEFAULT_MESSAGE_PREFIX : messagesPrefix;
    }

    public String getMessagesExt() {
        return messagesExt == null ? DEFAULT_MESSAGE_EXT : messagesExt;
    }

    public void setMessagesExt(String messagesExt) {
        this.messagesExt = messagesExt;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getResourceDirectory() {
        return resourceDirectory;
    }

    public String getStoreImplOn() {
        return storeImplOn;
    }

    public void setStoreImplOn(String storeImplOn) {
        this.storeImplOn = storeImplOn;
    }

    public void init() throws IOException {

        this.i18nMessages = new I18nMessages();
        this.storeLocales = new StoreLocales();
        this.lock = new ReentrantLock();

        loadMessages();

    }

    public String getMessage(Long pStoreId,
                             Locale pLocale,
                             String key,
                             Object arg0,
                             Object arg1,
                             Object arg2,
                             Object arg3,
                             Object arg4,
                             String arg0Type,
                             String arg1Type,
                             String arg2Type,
                             String arg3Type,
                             String arg4Type) {


        Object args[] = new Object[]{arg0, arg1, arg2, arg3, arg4};
        String types[] = new String[]{arg0Type, arg1Type, arg2Type, arg3Type, arg4Type};

        return getMessage(pStoreId, pLocale, key, args, types);

    }


    public String getMessage(Locale locale, String key, Object[] args) {
        return getMessage(null, locale, key, args);
    }

    public String getMessage(Long storeId, Locale locale, String key) {
        return getMessage(storeId, locale, key, new Object[0], new String[0]);
    }

    public String getMessage(Long storeId, Locale locale, String key, Object[] args) {
        return getMessage(storeId, locale, key, args, new String[0]);
    }

    public String getMessage(Long pStoreId, Locale pLocale, String pKey, Object[] pArgs, String[] pTyps) {

        if (pArgs != null) {
            for (int i = 0; i < pArgs.length; i++) {
                if (pArgs[i] == null) {
                    pArgs[i] = Constants.EMPTY;
                }
            }
        }

        StoreLocaleKey scratchLocale = getStoreLocaleKey(pStoreId, pLocale);

        return getMessage(scratchLocale, pKey, pArgs);

    }

    public String getMessage(String pKey, Locale pLocale) {
        return getMessage(null, pLocale, pKey, new Object[0], new String[0]);
    }

    public String getMessage(String key, AppLocale locale) {
        return getMessage(null, locale == null ? null : locale.getLocale(), key, new Object[0], new String[0]);
    }

    public String getMessage(String pKey, Object[] pArgs) {
        return getMessage(null, null, pKey, pArgs, new String[0]);
    }

    public String getMessage(StoreLocaleKey pLocaleKey, String pKey, Object pArgs[]) {
        return getMessage(getI18nMessages(), pLocaleKey, pKey, pArgs);
    }

    private String getMessage(I18nMessages pI18nMessages, StoreLocaleKey pLocaleKey, String pKey, Object pArgs[]) {

        if (pI18nMessages == null || pKey == null) {
            return null;
        }

        logger.debug("getMessage()=> pLocaleKey=" + pLocaleKey + ", pKey=" + pKey);


        String message = null;

        I18nMessageValues<LocaleKey, String> map = pI18nMessages.get(pKey);

        if (map != null) {

            StoreLocaleKey.StoreLocaleKeyIterator keyIterator = pLocaleKey.getKeyIterator();

            while (keyIterator.hasNext()) {
                String lkey = keyIterator.next();
                logger.debug("getMessage()=> searching " + pKey + " for locale  " + lkey);
                if (map.containsKey(new LocaleKey(lkey))) {
                    message = map.get(new LocaleKey(lkey));
                    logger.debug("getMessage()=> message for key  " + pKey + " found in locale  " + lkey + ", value: " + message);
                    break;
                }
            }

        }

        Locale locale = new Locale(pLocaleKey.getKeyPartLanguage(),
                pLocaleKey.getKeyPartCountry(),
                pLocaleKey.getKeyPartDialect()
        );

        Object[] args = new Object[pArgs == null ? 0 : pArgs.length];

        if (pArgs != null) {
            int i = 0;
            for (Object arg : pArgs) {
                if (arg instanceof I18nResolvedOutput) {
                    args[i++] = ((I18nResolvedOutput) arg).resolve(new AppLocale(locale));
                } else if (arg instanceof ResolvedOutput) {
                    args[i++] = ((ResolvedOutput) arg).resolve();
                } else  {
                    args[i++] = String.valueOf(arg);
                }
            }

        }


        if (Utility.isSet(message)) {
            MessageFormat format = new MessageFormat(Utility.escape(message));
            format.setLocale(locale);
            return format.format(args);
        } else {
            return message;
        }

    }


    public synchronized void loadMessages() throws IOException {

        logger.info("loadI18nMessages()=> BEGIN");

        try {

            lock.lockInterruptibly();

            try {

                i18nMessages = new I18nMessages();

                File resourceDirectory = resourceLoader.getResource(getResourceDirectory()).getFile();

                logger.info("loadI18nMessages()=> resourceDirectory: " + resourceDirectory.getAbsolutePath());

                if (resourceDirectory.exists()) {

                    File[] i18nfiles = resourceDirectory.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            String ext = IOUtility.getFileExt(name);
                            return getMessagesExt().equalsIgnoreCase(ext) && name.startsWith(getMessagesPrefix());
                        }
                    });

                    logger.info("loadI18nMessages()=> i18nfiles.length: " + i18nfiles.length);

                    for (File file : i18nfiles) {

                        String ext = IOUtility.getFileExt(file.getName());

                        String key = file.getName().replaceAll(getMessagesPrefix(), Constants.EMPTY).replaceAll(ext, Constants.EMPTY);
                        key = Utility.isSet(key) ? key.substring(1) : null;

                        logger.info("loadI18nMessages()=> loading file: " + file.getName() + ", key: " + key);

                        if (Utility.isSet(key)) {

                            StoreLocaleKey storeLocaleKey = new StoreLocaleKey(key);

                            if (storeLocaleKey.isValid() && (!storeLocaleKey.isStore() || isStoreImplOn())) {

                                Properties props = new Properties();
                                try {
                                    props.load(new InputStreamReader(new FileInputStream(file), Constants.ENCODING.UTF8));
                                } catch (IOException e) {
                                    logger.error("loadI18nMessages()", e);
                                }

                                if (props.isEmpty()) {
                                    continue;
                                }

                                for (Object o : props.keySet()) {

                                    String mkey = (String) o;

                                    I18nMessageValues<LocaleKey, String> i18nValues = i18nMessages.get(mkey);
                                    if (i18nValues == null) {
                                        i18nValues = new I18nMessageValues<LocaleKey, String>();
                                        i18nMessages.put(mkey, i18nValues);
                                    }

                                    String messageValue = props.getProperty(mkey);
                                    i18nValues.put(new LocaleKey(key), messageValue);
                                }

                            }

                        }
                    }

                }

            } finally {

                lock.unlock();

            }

        } catch (InterruptedException e) {

            logger.error(e.getMessage(), e);

        }

        logger.info("loadI18nMessages()=> i18nMessages.size: " + i18nMessages.size());
        logger.info("loadI18nMessages()=> END.");

    }

    public I18nMessages getI18nMessages() {
        return i18nMessages;
    }

    private String getDefaultLocaleKey() {
        String defLocale = System.getProperty("i18n.messages.locale.default");
        return Utility.isSet(defLocale) ? defLocale : Locale.ENGLISH.toString();
    }

    private boolean isStoreImplOn() {
        return Utility.isTrue(getStoreImplOn());
    }

    public Map<Long, Map<String, StoreLocaleKey>> getStoreLocale() {
        return storeLocales;
    }

    public StoreLocaleKey getStoreLocaleKey(Long pStoreId, Locale pLocale) {

        logger.debug("getStoreLocaleKey()=> BEGIN. storeLocales: " + this.storeLocales + ", pLocale: " + pLocale);

        Map<String, StoreLocaleKey> m = this.storeLocales.get(isStoreImplOn() ? pStoreId : null);
        if (m == null) {
            m = new HashMap<String, StoreLocaleKey>();
            this.storeLocales.put(isStoreImplOn() ? pStoreId : null, m);
        }

        StoreLocaleKey v = m.get(pLocale.toString());
        if (v == null) {
            v = new StoreLocaleKey(isStoreImplOn() ? pStoreId : null, pLocale);
            m.put(pLocale.toString(), v);
        }

        logger.debug("getStoreLocaleKey()=> END.");
        return v;
    }

    @Override
    public String getMessage(String s, Object[] objects, String s1, Locale locale) {

        String message = getMessage(locale,
                s,
                objects
        );

        return message != null ? message : s1;

    }

    @Override
    public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {

        return getMessage(locale,
                s,
                objects
        );

    }


    public class LocaleKey {

        private String mKey;

        public LocaleKey(String pKey) {
            this.mKey = pKey;
        }

        public String toString() {
            return mKey;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LocaleKey that = (LocaleKey) o;

            return mKey.equals(that.mKey);

        }

        public int hashCode() {
            return mKey.hashCode();
        }
    }

    public interface II18nMessageValues<K, V> extends Serializable {

        public Set<K> keySet();

        public Collection<V> values();

        public V put(K key, V daKa);

        public V remove(K key);

        public V get(K key);

        public void clear();

        public int size();

        public boolean containsKey(K key);

    }

    public class I18nMessageValues<K, V> extends HashMap<K, V> implements II18nMessageValues<K, V> {

    }

    public class I18nMessages extends HashMap<String, I18nMessageValues<LocaleKey, String>> {

    }

    public class StoreLocales extends HashMap<Long, Map<String, StoreLocaleKey>> {

    }

    public class StoreLocaleKey {

        private static final String LECALE_DELIM = "_";

        Long mStoreId;
        String mLanguage;
        String mCountry;
        String mDialect;
        private List<String> mLocalesOrder;

        public StoreLocaleKey(Long pStoreId, String pLanguage, String pCountry, String pDialect) {
            init(pStoreId, pLanguage, pCountry, pDialect);
        }

        public StoreLocaleKey(String pStoreLocalKey) {
            this.init(pStoreLocalKey);
        }

        public StoreLocaleKey(Long pStoreId, String pLocaleKey) {
            init(pStoreId, pLocaleKey);
        }

        public StoreLocaleKey(Long pStoreId, Locale pLocale) {
            init(pStoreId, pLocale.getLanguage(), pLocale.getCountry(), pLocale.getVariant());
        }

        public String getKey() {
            return mLocalesOrder.get(0);
        }

        public StoreLocaleKeyIterator getKeyIterator() {
            return new StoreLocaleKeyIterator(getLocaleOrder());
        }

        public Long getStore() {
            return mStoreId;
        }

        public String getKeyPartLanguage() {
            return mLanguage;
        }

        public String getKeyPartCountry() {
            return mCountry;
        }

        public String getKeyPartDialect() {
            return mDialect;
        }

        public List<String> getLocaleOrder() {
            return mLocalesOrder;
        }

        private void init(String pStoreLocalKey) {

            String localeKey = null;
            Long storeId = null;

            if (pStoreLocalKey != null) {

                //break the locale key into the store local id and the remaining locale
                //ex: passed in value = 1_en_US
                //    posibleStoreId = 1
                //    remainingLocaleKey = en_US

                int pos = pStoreLocalKey.indexOf(LECALE_DELIM);
                String posibleStoreId;
                String remainingLocaleKey;
                if (pos > 0) {
                    //split the locale key
                    posibleStoreId = pStoreLocalKey.substring(0, pos);
                    remainingLocaleKey = pStoreLocalKey.substring(pos + 1);
                } else {
                    //assume that the store id is the only thing passed in
                    posibleStoreId = null;
                    remainingLocaleKey = pStoreLocalKey;
                }

                try {
                    if (Utility.isSet(posibleStoreId)) {
                        storeId = Parse.parseLong(posibleStoreId);
                    }
                    localeKey = remainingLocaleKey;
                } catch (Exception e) {
                    storeId = null;
                    localeKey = pStoreLocalKey;
                }

            }
            init(storeId, localeKey);
        }

        private void init(Long pStoreId, String pLocaleKey) {

            String language = null;
            String country = null;
            String dialect = null;

            StringTokenizer st = new StringTokenizer(pLocaleKey, LECALE_DELIM);
            if (st.hasMoreElements()) {
                int i = 0;
                while (st.hasMoreElements()) {
                    switch (i) {
                        case 0: language = (String) st.nextElement(); break;
                        case 1:country = (String) st.nextElement(); break;
                        case 2:dialect = (String) st.nextElement(); break;
                        default:  dialect += (Utility.isSet(dialect) ? LECALE_DELIM : Constants.EMPTY) + st.nextElement();
                    }
                    i++;
                }
            } else {
                language = pLocaleKey;
            }

            init(pStoreId, language, country, dialect);
        }

        private void init(Long pStoreId, String pLanguage, String pCountry, String pDialect) {

            this.mStoreId = pStoreId;
            this.mLanguage = pLanguage;
            this.mCountry = pCountry;
            this.mDialect = pDialect;
            this.mLocalesOrder = createLocaleOrder(pStoreId);

        }

        private List<String> createLocaleOrder(Long pStoreId) {

            List<String> list = new ArrayList<String>();

            String[] keyParts = getKeyParts();
            if (pStoreId != null && isStoreImplOn()) {
                fillList(list, pStoreId, true, keyParts);
                fillList(list, null, true, keyParts);
            } else {
                fillList(list, null, true, keyParts);
            }

            return list;
        }

        private void fillList(List<String> pList, Long pStoreId, boolean pIncludeDefault, String... pKeyParts) {

            for (int i = pKeyParts.length - 1; i >= 0; i--) {

                String m = (pStoreId != null && isStoreImplOn() ? pStoreId.toString() + LECALE_DELIM : Constants.EMPTY);
                String s = Constants.EMPTY;

                for (int j = 0; j < i; j++) {
                    if (Utility.isSet(pKeyParts[j])) {
                        s += (Utility.isSet(s) ? LECALE_DELIM : Constants.EMPTY) + pKeyParts[j];
                    }
                }

                if (Utility.isSet(s)) {
                    m += s;
                    if (!pList.contains(m)) {
                        pList.add(m);
                    }
                }
            }

            if (pIncludeDefault) {
                String defaultLocale = (pStoreId != null && isStoreImplOn() ? pStoreId.toString() + LECALE_DELIM : Constants.EMPTY);
                defaultLocale += getDefaultLocaleKey();
                if (!pList.contains(defaultLocale)) {
                    pList.add(defaultLocale);
                }
            }

        }

        public String[] getKeyParts() {
            return new String[]{getKeyPartLanguage(), getKeyPartCountry(), getKeyPartDialect()};
        }

        public boolean isStore() {
            return mStoreId != null;
        }

        public boolean isValid() {
            return Utility.isSet(mLanguage);
        }

        public class StoreLocaleKeyIterator implements Iterator {

            Iterator<String> mIterator;

            public StoreLocaleKeyIterator(List<String> pLocaleKeysOrder) {
                mIterator = pLocaleKeysOrder.iterator();
            }

            public boolean hasNext() {
                return mIterator.hasNext();
            }

            public String next() {
                return mIterator.next();
            }

            public void remove() {
                mIterator.remove();
            }
        }

        public String toString() {
            return getKey();
        }
    }

}
