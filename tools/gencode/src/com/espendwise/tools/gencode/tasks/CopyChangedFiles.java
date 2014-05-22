package com.espendwise.tools.gencode.tasks;

import com.espendwise.tools.gencode.util.GCUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.taskdefs.Copy;

import java.io.IOException;
import java.util.Iterator;

public class CopyChangedFiles extends Copy {

    private final static Log log = LogFactory.getLog(CopyChangedFiles.class);

    @Override
    protected void doFileOperations() {

        log.info("doFileOperations()=> Total files:" + fileCopyMap.size());

        Iterator it = fileCopyMap.keySet().iterator();
        while (it.hasNext()) {
            String fromFile = (String) it.next();
            String[] toFiles = (String[]) fileCopyMap.get(fromFile);
            try {
                if (!wasChanged(fromFile, toFiles)) {
                    it.remove();
                }
            } catch (IOException e) {   //ignore
                log.debug("doFileOperations()=> ERROR: " + e.getMessage());
            }
        }

        log.info("doFileOperations()=>  Changed files:" + fileCopyMap.size());
        super.doFileOperations();
    }

    private boolean wasChanged(String fromFile, String[] toFiles) throws IOException {
        final String srcText = GCUtility.loadFile(fromFile);
        String toText = null;
        for (int i = 0; toFiles != null && i < toFiles.length; i++) {
            toText = GCUtility.loadFile(toFiles[i]);
            if (!srcText.equals(toText)) {
                return true;
            }
        }
        return false;
    }

  
}