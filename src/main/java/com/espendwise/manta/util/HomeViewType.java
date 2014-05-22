package com.espendwise.manta.util;


import org.apache.log4j.Logger;

public enum HomeViewType {

    SEARCH_VIEW(new ViewCriteria(11, null)), LIST_VIEW(new ViewCriteria(0,10));

    private static final Logger logger = Logger.getLogger(HomeViewType.class);

    private ViewCriteria viewCriteria;

    HomeViewType(ViewCriteria viewCriteria) {
         this.viewCriteria =  viewCriteria;
    }

    public boolean isApplied(Integer userStoreSize) {
        int testSize = Utility.intNN(userStoreSize);
       return viewCriteria.getRangeFrom() <= testSize
               && testSize <= viewCriteria.getRangeTo();
    }


    private static class ViewCriteria {

        private Integer rangeFrom;
        private Integer rangeTo;

        public ViewCriteria(Integer from, Integer to) {
            rangeFrom = from == null ? Integer.MIN_VALUE : from;
            rangeTo = to== null ? Integer.MAX_VALUE : to;
        }

        public Integer getRangeFrom() {
            return rangeFrom;
        }

        public Integer getRangeTo() {
            return rangeTo;
        }
    }


}
