package com.espendwise.manta.web.validator;


public abstract class AbstractFormFieldValidator  extends SpringFieldValidatorSupport implements FormFieldValidator {

    public String fieldKey;
    public String fieldIdKey;
    public String fieldName;
    public String fieldLabel;

    protected AbstractFormFieldValidator(String fieldKey, String fieldIdKey, String fieldName, String fieldLabel) {
        super();
        this.fieldKey = fieldKey;
        this.fieldIdKey = fieldIdKey;
        this.fieldName = fieldName;
        this.fieldLabel = fieldLabel;
    }

    protected AbstractFormFieldValidator(String fieldKey, String fieldIdKey,  String fieldName) {
        super();
        this.fieldKey = fieldKey;
        this.fieldIdKey = fieldIdKey;
        this.fieldName = fieldName;
    }
    protected AbstractFormFieldValidator(String fieldKey, String fieldIdKey) {
        super();
        this.fieldKey = fieldKey;
        this.fieldIdKey = fieldIdKey;
    }
    protected AbstractFormFieldValidator(String fieldKey) {
        super();
        this.fieldKey = fieldKey;
    }

/*    protected AbstractFormFieldValidator(String fieldName) {
        super();
        this.fieldName = fieldKey;
    }
*/
    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldIdKey() {
		return fieldIdKey;
	}

	public void setFieldIdKey(String fieldIdKey) {
		this.fieldIdKey = fieldIdKey;
	}

	public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }
}
