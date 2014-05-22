package com.espendwise.manta.web.tags;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.AppI18nUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;


public class LocateLayerTag extends TagSupport {

    private String titleLabel;
    private String closeLabel;
    private String layer;
    private String idGetter;
    private String nameGetter;
    private String targetNames;
    private String targetIds;
    private String var;
    private String layerName;
    private String action;
    private String postHandler;
    private String finallyHandler;

    public String getTitleLabel() {
        return titleLabel;
    }

    public String getVar() {
        return var;
    }

    public void setName(String var) {
        this.var = var;
    }

    public void setTitleLabel(String titleLabel) {
        this.titleLabel = titleLabel;
    }

    public String getCloseLabel() {
        return closeLabel;
    }

    public void setCloseLabel(String closeLabel) {
        this.closeLabel = closeLabel;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getIdGetter() {
        return idGetter;
    }

    public void setIdGetter(String idGetter) {
        this.idGetter = idGetter;
    }

    public String getTargetNames() {
        return targetNames;
    }

    public void setTargetNames(String targetNames) {
        this.targetNames = targetNames;
    }

    public String getNameGetter() {
        return nameGetter;
    }

    public void setNameGetter(String nameGetter) {
        this.nameGetter = nameGetter;
    }

    public String getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(String targetIds) {
        this.targetIds = targetIds;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPostHandler() {
        return postHandler;
    }

    public void setPostHandler(String postHandler) {
        this.postHandler = postHandler;
    }

    public String getFinallyHandler() {
		return finallyHandler;
	}

	public void setFinallyHandler(String finallyHandler) {
		this.finallyHandler = finallyHandler;
	}

	public String getLayerName() {
        return Utility.strNN(layerName, getVar());
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    @Override
    public int doEndTag() throws JspException {

        try {
            pageContext.getOut().write(buildJsForLayer());
        } catch (IOException e) {
            pageContext.setAttribute(getVar(), "{}");
            return super.doEndTag();
        }

        pageContext.setAttribute(getVar(), "return locateLayerManager.locate('" + getLayerName() + "', this, createLocate" + getVar() + "LayerData());");

        return super.doEndTag();
    }

    private String buildJsForLayer() {

        return  "<link href=\""+pageContext.findAttribute("resources")+"/ocean/css/locate.css\" rel=\"Stylesheet\" type=\"text/css\" media=\"all\"/>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "\n" +
                "    locateLayerManager.init();\n" +
                "\n" +
                "    function createLocate"+getVar()+"LayerData() {\n" +
                "\n" +
                "        var m = {};\n" +
                "\n" +
                "        m.labelClose = '"+ AppI18nUtil.getMessage(getCloseLabel())+"';\n" +
                "        m.labelTitle = '"+ AppI18nUtil.getMessage(getTitleLabel())+"';\n" +
                "        m.layer = \""+getLayer()+"\";\n" +
                "        m.returnSelectedAction = \""+ getAction()+"\";\n" +
                (Utility.isSet(getPostHandler())? "        m.postHandler = "+getPostHandler()+";\n":"") +
                (Utility.isSet(getFinallyHandler())? "        m.finallyHandler = "+getFinallyHandler()+";\n":"") +
                (Utility.isSet(getIdGetter()) ? "         m.idGetter = \""+getIdGetter()+"\";":"")+"\n" +
                (Utility.isSet(getNameGetter()) ? "        m.nameGetter = \""+getNameGetter()+"\";":"")+"\n" +


                (Utility.isSet(getTargetNames()) || Utility.isSet(getTargetIds())
                        ? ("        m.target = {" +
                        (Utility.isSet(getTargetNames()) ? "names:\"" + getTargetNames() + "\"" : "") +
                        (Utility.isSet(getTargetNames()) && Utility.isSet(getTargetIds()) ? "," : "") +
                        (Utility.isSet(getTargetIds()) ? "ids:\"" + getTargetIds() + "\"" : "") + "};")
                        : "") +
                "\n" +
                "        return  m;\n" +
                "    }\n" +
                "\n" +
                "</script>";

    }

    @Override
    public void release() {
        super.release();
        this.titleLabel = null;
        this.closeLabel= null;
        this.layer= null;
        this.idGetter= null;
        this.nameGetter= null;
        this.targetNames= null;
        this.targetIds= null;
        this.var= null;
        this.action = null;
        this.postHandler = null;
        this.finallyHandler = null;
        this.layerName= null;
    }
}
