package com.espendwise.manta.web.util;


public interface UrlPathKey {

    public static final String INSTANCE_PREFIX = "/instance";
    public static final String INSTANCE = "/instance/{globalStoreId}";

    public static final String DISPLAY_CONTROL = "/display";

    public interface STORE_MESSAGE {
        public static final String FILTER          = UrlPathKey.INSTANCE + "/storeMessage";
        public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/storeMessage/{storeMessageId}";
        public static final String CONFIGURATION   = UrlPathKey.INSTANCE + "/storeMessage/{storeMessageId}/configuration";
    }

    public interface ACCOUNT {
        public static final String FILTER         = UrlPathKey.INSTANCE + "/account";
        public static final String IDENTIFICATION = UrlPathKey.INSTANCE + "/account/{accountId}";
    }

    public interface EMAIL_TEMPLATE {
        public static final String FILTER         = UrlPathKey.INSTANCE + "/emailTemplate";
        public static final String IDENTIFICATION = UrlPathKey.INSTANCE + "/emailTemplate/{emailTemplateId}";
    }


}
