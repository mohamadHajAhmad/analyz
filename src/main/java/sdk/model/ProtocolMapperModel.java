package sdk.model;

import java.util.HashMap;
import java.util.Map;

public class ProtocolMapperModel extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String protocol;
    protected String protocolMapper;
    protected boolean consentRequired;
    protected String consentText;
    protected Map<String, String> config = new HashMap<String, String>();

    public String getId() {
        return get("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getProtocol() {
        return get("protocol");
    }

    public void setProtocol(String protocol) {
        set("protocol", protocol);
    }

    public String getProtocolMapper() {
        return get("protocolMapper");
    }

    public void setProtocolMapper(String protocolMapper) {
        set("protocolMapper", protocolMapper);
    }

    public Map<String, String> getConfig() {
        return get("config");
    }

    public void setConfig(Map<String, String> config) {
        set("config", config);
    }

    public boolean isConsentRequired() {
        return getBoolean("consentRequired");
    }

    public void setConsentRequired(boolean consentRequired) {
        set("consentRequired", consentRequired);
    }

    public String getConsentText() {
        return get("consentText");
    }

    public void setConsentText(String consentText) {
        set("consentText", consentText);
    }
}
