package com.espendwise.manta.web.util.smac;


import com.espendwise.manta.util.Hierarchy;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SmacMapping {

    private static final Logger logger = Logger.getLogger(SmacMapping.class);

    public static final String SESSION_KEY = SmacMapping.class.getName();

    private Hierarchy<String, Map<String, SmacDesc>> mappings;
    private HashMap<String, String> hadlerPaths;

    public SmacMapping() {
        this.mappings = new Hierarchy<String, Map<String, SmacDesc>>();
        this.hadlerPaths = new HashMap<String, String>();
    }

    public void bindSma(String path, SmacDesc desc) {

        logger.debug("bindSma()=> path: " + path + ", " + desc);

        String[] pathParts = path.split("/");

        if (pathParts != null) {

            logger.debug("bindSma()=> pathParts: " + Arrays.asList(pathParts));

            Hierarchy<String, Map<String, SmacDesc>> pathState = providePath(pathParts, mappings);
            if (pathState != null) {
                pathState.getValue().put(desc.getName(), desc);
                hadlerPaths.put(desc.getController(), desc.getHandlerPath());
            }


            logger.debug("bindSma()=> mappings: " + mappings);

        }

    }

    Hierarchy<String, Map<String, SmacDesc>> providePath(String[] pathParts, Hierarchy<String, Map<String, SmacDesc>> startWith) {

        if (pathParts == null || pathParts.length == 0) {
            return null;
        }

        Hierarchy<String, Map<String, SmacDesc>> obj = startWith;

        for (int i = 0; i < pathParts.length; i++) {

            String partOfPath = pathParts[i];

            logger.debug("bindSma()=> pathPath.[" + i + "].partOfPath: " + partOfPath);

            Hierarchy<String, Map<String, SmacDesc>> h = providePath(partOfPath, obj);
            if (h == null) {
                logger.debug("bindSma()=> no desc found, create new");
                h = new Hierarchy<String, Map<String, SmacDesc>>(partOfPath, new HashMap<String, SmacDesc>());
                obj.addChild(h);
            }



            obj = h;

            if (i == pathParts.length - 1) {
                logger.debug("bindSma()=> go next");
            }
        }

        logger.debug("bindSma()=> END, hierarchy for path : " + obj);

        return obj;
    }

    Hierarchy<String, Map<String, SmacDesc>> providePath(String path, Hierarchy<String, Map<String, SmacDesc>> hierarchy) {

        for (Hierarchy<String, Map<String, SmacDesc>> child : hierarchy.values()) {
            if (child.getKey().equalsIgnoreCase(path)) {
                return child;
            }
        }

        return null;

    }

    public Hierarchy<String, Map<String, SmacDesc>> getMappings() {
        return mappings;
    }

    public void setMappings(Hierarchy<String, Map<String, SmacDesc>> mappings) {
        this.mappings = mappings;
    }

    public String getHandlerPath(String handler) {
        logger.debug("getHandlerPath()=> hadlerPaths: " + hadlerPaths);
        return hadlerPaths.get(handler);
    }
}

