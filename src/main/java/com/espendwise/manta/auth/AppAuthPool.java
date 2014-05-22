package com.espendwise.manta.auth;


import java.util.HashMap;
import java.util.Map;

public class AppAuthPool extends HashMap<String, AppAuth> {

    protected AppAuthPool(AppAuth... list) {
        super(list != null ? list.length : 0);
        if (list != null) {
            for (AppAuth x : list) {
                put(x.getUsername(), x);
            }
        }
    }

    private AppAuthPool(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    private AppAuthPool(int initialCapacity) {
        super(initialCapacity);
    }

    private AppAuthPool() {
    }

    private AppAuthPool(Map<? extends String, ? extends AppAuth> m) {
        super(m);
    }
}
