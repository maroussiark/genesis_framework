package genesis;

public class Service {
    private String serviceSavePath;
    private String serviceName;
    private String serviceExtension;
    private String specName;
    private String specContent;
    private String serviceContent;

    public String getServiceContent() {
        return this.serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getSpecName() {
        return this.specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecContent() {
        return this.specContent;
    }

    public void setSpecContent(String specContent) {
        this.specContent = specContent;
    }

    public String getServiceExtension() {
        return this.serviceExtension;
    }

    public void setServiceExtension(String serviceExtension) {
        this.serviceExtension = serviceExtension;
    }

    public String getServiceSavePath() {
        return this.serviceSavePath;
    }

    public void setServiceSavePath(String serviceSavePath) {
        this.serviceSavePath = serviceSavePath;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
