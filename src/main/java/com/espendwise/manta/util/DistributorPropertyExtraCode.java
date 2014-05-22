package com.espendwise.manta.util;


public enum DistributorPropertyExtraCode implements PropertyExtraCode {

	DISTRIBUTOR_TYPE(RefCodeNames.DISTRIBUTOR_TYPE);

    private String nameExtraCode;

    DistributorPropertyExtraCode(String NameExtraCode) {
        this.nameExtraCode = NameExtraCode;
    }

    public String getNameExtraCode() {
        return nameExtraCode;
    }
}
