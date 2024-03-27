package genesis;

import java.util.HashMap;

public class ComponentService {
    private String formData, formDataUpdate, foreignList;
    private HashMap<String, String> componentServiceName, specName, moduleName, routingModuleName;
    private HashMap<String, String> componentServiceContent, specContent, moduleContent, routingModuleContent;
    private HashMap<String, String> componentServiceSavePath;
    private String componentGetDataForeign, callGetDataForeign;
    private String fileExtension;

    public String getForeignList() {
        return this.foreignList;
    }

    public void setForeignList(String foreignList) {
        this.foreignList = foreignList;
    }

    public String getComponentGetDataForeign() {
        return this.componentGetDataForeign;
    }

    public void setComponentGetDataForeign(String componentGetDataForeign) {
        this.componentGetDataForeign = componentGetDataForeign;
    }

    public String getCallGetDataForeign() {
        return this.callGetDataForeign;
    }

    public void setCallGetDataForeign(String callGetDataForeign) {
        this.callGetDataForeign = callGetDataForeign;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getFormDataUpdate() {
        return formDataUpdate;
    }

    public void setFormDataUpdate(String formDataUpdate) {
        this.formDataUpdate = formDataUpdate;
    }

    public HashMap<String, String> getComponentServiceName() {
        return componentServiceName;
    }

    public void setComponentServiceName(HashMap<String, String> componentServiceName) {
        this.componentServiceName = componentServiceName;
    }

    public HashMap<String, String> getSpecName() {
        return specName;
    }

    public void setSpecName(HashMap<String, String> specName) {
        this.specName = specName;
    }

    public HashMap<String, String> getComponentServiceContent() {
        return componentServiceContent;
    }

    public void setComponentServiceContent(HashMap<String, String> componentServiceContent) {
        this.componentServiceContent = componentServiceContent;
    }

    public HashMap<String, String> getSpecContent() {
        return specContent;
    }

    public void setSpecContent(HashMap<String, String> specContent) {
        this.specContent = specContent;
    }

    public HashMap<String, String> getComponentServiceSavePath() {
        return componentServiceSavePath;
    }

    public void setComponentServiceSavePath(HashMap<String, String> componentServiceSavePath) {
        this.componentServiceSavePath = componentServiceSavePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public HashMap<String, String> getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(HashMap<String, String> moduleName) {
        this.moduleName = moduleName;
    }

    public HashMap<String, String> getModuleContent() {
        return this.moduleContent;
    }

    public void setModuleContent(HashMap<String, String> moduleContent) {
        this.moduleContent = moduleContent;
    }

    public HashMap<String, String> getRoutingModuleName() {
        return this.routingModuleName;
    }

    public void setRoutingModuleName(HashMap<String, String> routingModuleName) {
        this.routingModuleName = routingModuleName;
    }

    public HashMap<String, String> getRoutingModuleContent() {
        return this.routingModuleContent;
    }

    public void setRoutingModuleContent(HashMap<String, String> routingModuleContent) {
        this.routingModuleContent = routingModuleContent;
    }

}
