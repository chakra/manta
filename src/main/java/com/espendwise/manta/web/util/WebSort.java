package com.espendwise.manta.web.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.ObjectGetter;
import com.espendwise.manta.util.PathObjectGetter;
import com.espendwise.manta.util.SimpleObjectGetter;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.FilterResult;

/**
 * Simple solution for universal sorting
 */
public class WebSort {

    private static final Logger logger = Logger.getLogger(WebSort.class);

    public static void sort(Object obj, String field) {
    	sort(obj, field, null);
    }

    public static void sort(Object obj, String field, Boolean asc) {
    	sort(obj, field, asc, null);
    }

    public static void sort(Object obj, String field, Boolean asc, Class clazz) {

        logger.info("sort()=>  BEGIN," + "\n form: " + obj + "\n field: " + field);

        if (obj instanceof FilterResult) {
            SortHistory history = ((FilterResult) obj).getSortHistory();
            sort((FilterResult) obj, field, history, clazz);
        }
        else if (obj instanceof List){
            sort((List)obj, field, asc, clazz);
        }  
        else {
            logger.info("sort()=> reject sorting, obj is neither a FilterResult or List");
        }

        logger.info("sort()=> END.");
    }
    
    private static void sort(List collection, String pathExp, Boolean asc) {
    	sort(collection, pathExp, asc, null);
    }
    
    /**
     * @param collection elements for sort
     * @param pathExp    java bean path
     * @param asc        sort order
     * @param clazz		 the common class of all objects in the collection
     */
    private static void sort(List<Object> collection, String pathExp, Boolean asc, Class clazz) {

        logger.debug("sort()=> BEGIN");

        try {

            if (Utility.isSet(collection)) {
            	
            	if (asc == null) {
            		asc = new Boolean(true);
            	}

                String[] path = Utility.split(pathExp, ".");

                logger.debug("sort()=> path: " + Arrays.asList(path));

                final ObjectGetter getter;

                // determine the ObjectGetter instance to use
                if (path.length > 1) {
                    getter = new PathObjectGetter(collection.get(0), path);
                } 
                else {
                    getter = new SimpleObjectGetter(collection.get(0), pathExp);
                }

                if (clazz != null) {
                	getter.init(clazz);
                }
                else {
                	getter.init();
                }

                logger.debug("sort()=> getter: " + getter);

                if (getter.isInit()) {

                    logger.debug("sort()=> sort " + collection.size() + " items ... ");

                    long startSortTime = System.currentTimeMillis();
                    final int dir = asc.booleanValue() ? 1 : -1;  //asc or desc

                    Collections.sort(collection, new Comparator<Object>() {

                        @Override
                        public int compare(Object o1, Object o2) {

                            try {

                                Object v1 = getter.get(o1);
                                Object v2 = getter.get(o2);

                                if (v1 == null && v2 == null) {
                                    return 0;
                                } else if (v1 == null) {
                                    return dir * -1;
                                } else if (v2 == null) {
                                    return dir;
                                } else {
                                    if (v1 instanceof String) {
                                        return dir * ((String) v1).toLowerCase().compareTo(((String) v2).toLowerCase());
                                    } else if (v1 instanceof Comparable) {
                                        return dir * ((Comparable) v1).compareTo(v2);
                                    } else {
                                        throw new IllegalArgumentException();
                                    }
                                }

                            } catch (InvocationTargetException e) {
                                throw new IllegalArgumentException(e);
                            } catch (NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    long endSortTime = System.currentTimeMillis();

                    logger.debug("sort()=> sort OK!, duration: " + (endSortTime - startSortTime) + " ms");

                    getter.destroy();

                }
            } 
            else {
                logger.debug("sort()=>  reject sorting, list is empty");
            }

        } 
        catch (Exception e) {
            logger.error("sort()=> ERROR: " + e.getMessage(), e);
        }
        logger.debug("sort()=> END.");
    }

    /**
     * sort elements of action foem which implemented FilterResult interface
     *
     * @param form    struts action form
     * @param field   field name of object fom form result collection
     * @param history sort histor (@see SortHistory)
     */
    private static void sort(FilterResult form, String field, SortHistory history, Class clazz) {

        logger.info("sort()=> BEGIN," + "\n form: " + form + "\n field: " + field + "\n history: " + history);

        //determine sort order
        boolean dir = history == null || (!Utility.strNN(history.getSortField()).equals(field) || !history.isAsc());
        logger.info("sort()=> dir: " + dir);

        WebSort.sort(form.getResult(), field, new Boolean(dir), clazz);

        if (history == null) {
            history = new SortHistory();
        }

        history.setSortField(field);
        history.setAsc(dir);

        // refresh history
        form.setSortHistory(history);

        logger.info("sort()=> END.");
    }

}
