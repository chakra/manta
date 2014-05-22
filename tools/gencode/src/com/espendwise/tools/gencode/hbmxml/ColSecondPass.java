package com.espendwise.tools.gencode.hbmxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.cfg.Mappings;
import org.hibernate.mapping.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public abstract class ColSecondPass implements org.hibernate.cfg.SecondPass {

    private static final Log log = LogFactory.getLog(ColSecondPass.class);

    public Mappings mappings;
    public Collection collection;
    private Map localInheritedMetas;

    public ColSecondPass(Mappings mappings, Collection collection, java.util.Map inheritedMetas) {
        this.collection = collection;
        this.mappings = mappings;
        this.localInheritedMetas = inheritedMetas;
    }

    public ColSecondPass(Mappings mappings, Collection collection) {
        this(mappings, collection, Collections.EMPTY_MAP);
    }

    public void doSecondPass(java.util.Map persistentClasses)  throws MappingException {

        log.debug("Second pass for collection: " + collection.getRole());

        secondPass( persistentClasses, localInheritedMetas ); // using local since the inheritedMetas at this point is not the correct map since it is always the empty map
        collection.createAllKeys();

        String msg = "Mapped collection key: " + columns( collection.getKey() );
        if ( collection.isIndexed() )
            msg += ", index: " + columns( ( (IndexedCollection) collection ).getIndex() );
        if ( collection.isOneToMany() ) {
            msg += ", one-to-many: "
                    + ( (OneToMany) collection.getElement() ).getReferencedEntityName();
        }
        else {
            msg += ", element: " + columns( collection.getElement() );
        }
        log.debug(msg);
    }

    abstract public void secondPass(java.util.Map persistentClasses, java.util.Map inheritedMetas) throws MappingException;

    private static String columns(Value val) {
        StringBuilder columns = new StringBuilder();
        Iterator iter = val.getColumnIterator();
        while ( iter.hasNext() ) {
            columns.append( ( (Selectable) iter.next() ).getText() );
            if ( iter.hasNext() ) columns.append( ", " );
        }
        return columns.toString();
    }
}
