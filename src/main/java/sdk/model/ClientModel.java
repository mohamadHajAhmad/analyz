package sdk.model;

import java.util.List;
import java.util.Map;

public class ClientModel extends AbstractModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getId() {
        return get("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getClientId() {
        return get("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getDescription() {
        return get("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public String getRootUrl() {
        return get("rootUrl");
    }

    public void setRootUrl(String rootUrl) {
        set("rootUrl", rootUrl);
    }

    public String getAdminUrl() {
        return get("adminUrl");
    }

    public void setAdminUrl(String adminUrl) {
        set("adminUrl", adminUrl);
    }

    public String getBaseUrl() {
        return get("baseUrl");
    }

    public void setBaseUrl(String baseUrl) {
        set("baseUrl", baseUrl);
    }

    public Boolean getSurrogateAuthRequired() {
        return getBoolean("surrogateAuthRequired");
    }

    public void setSurrogateAuthRequired(Boolean surrogateAuthRequired) {
        set("surrogateAuthRequired", surrogateAuthRequired);
    }

    public Boolean getEnabled() {
        return getBoolean("enabled");
    }

    public void setEnabled(Boolean enabled) {
        set("enabled", enabled);
    }

    public String getClientAuthenticatorType() {
        return get("clientAuthenticatorType");
    }

    public void setClientAuthenticatorType(String clientAuthenticatorType) {
        set("clientAuthenticatorType", clientAuthenticatorType);
    }

    public String getSecret() {
        return get("secret");
    }

    public void setSecret(String secret) {
        set("secret", secret);
    }

    public String getRegistrationAccessToken() {
        return get("registrationAccessToken");
    }

    public void setRegistrationAccessToken(String registrationAccessToken) {
        set("registrationAccessToken", registrationAccessToken);
    }

    public List<String> getDefaultRoles() {
        return get("defaultRoles");
    }

    public void setDefaultRoles(List<String> defaultRoles) {
        set("defaultRoles", defaultRoles);
    }

    public List<String> getRedirectUris() {
        return get("redirectUris");
    }

    public void setRedirectUris(List<String> redirectUris) {
        set("redirectUris", redirectUris);
    }

    public List<String> getWebOrigins() {
        return get("webOrigins");
    }

    public void setWebOrigins(List<String> webOrigins) {
        set("webOrigins", webOrigins);
    }

    public Integer getNotBefore() {
        return getInteger("notBefore");
    }

    public void setNotBefore(Integer notBefore) {
        set("notBefore", notBefore);
    }

    public Boolean getBearerOnly() {
        return getBoolean("bearerOnly");
    }

    public void setBearerOnly(Boolean bearerOnly) {
        set("bearerOnly", bearerOnly);
    }

    public Boolean getConsentRequired() {
        return getBoolean("consentRequired");
    }

    public void setConsentRequired(Boolean consentRequired) {
        set("consentRequired", consentRequired);
    }

    public Boolean getStandardFlowEnabled() {
        return getBoolean("standardFlowEnabled");
    }

    public void setStandardFlowEnabled(Boolean standardFlowEnabled) {
        set("standardFlowEnabled", standardFlowEnabled);
    }

    public Boolean getImplicitFlowEnabled() {
        return getBoolean("implicitFlowEnabled");
    }

    public void setImplicitFlowEnabled(Boolean implicitFlowEnabled) {
        set("implicitFlowEnabled", implicitFlowEnabled);
    }

    public Boolean getDirectAccessGrantsEnabled() {
        return getBoolean("directAccessGrantsEnabled");
    }

    public void setDirectAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        set("directAccessGrantsEnabled", directAccessGrantsEnabled);
    }

    public Boolean getServiceAccountsEnabled() {
        return getBoolean("serviceAccountsEnabled");
    }

    public void setServiceAccountsEnabled(Boolean serviceAccountsEnabled) {
        set("serviceAccountsEnabled", serviceAccountsEnabled);
    }

    public Boolean getDirectGrantsOnly() {
        return getBoolean("directGrantsOnly");
    }

    public void setDirectGrantsOnly(Boolean directGrantsOnly) {
        set("directGrantsOnly", directGrantsOnly);
    }

    public Boolean getPublicClient() {
        return getBoolean("publicClient");
    }

    public void setPublicClient(Boolean publicClient) {
        set("publicClient", publicClient);
    }

    public Boolean getFrontchannelLogout() {
        return getBoolean("frontchannelLogout");
    }

    public void setFrontchannelLogout(Boolean frontchannelLogout) {
        set("frontchannelLogout", frontchannelLogout);
    }

    public String getProtocol() {
        return get("protocol");
    }

    public void setProtocol(String protocol) {
        set("protocol", protocol);
    }

    public Map<String, String> getAttributes() {
        return get("attributes");
    }

    public void setAttributes(Map<String, String> attributes) {
        set("attributes", attributes);
    }

    public Boolean getFullScopeAllowed() {
        return getBoolean("fullScopeAllowed");
    }

    public void setFullScopeAllowed(Boolean fullScopeAllowed) {
        set("fullScopeAllowed", fullScopeAllowed);
    }

    public Integer getNodeReRegistrationTimeout() {
        return getInteger("nodeReRegistrationTimeout");
    }

    public void setNodeReRegistrationTimeout(Integer nodeReRegistrationTimeout) {
        set("nodeReRegistrationTimeout", nodeReRegistrationTimeout);
    }

    public Map<String, Integer> getRegisteredNodes() {
        return get("registeredNodes");
    }

    public void setRegisteredNodes(Map<String, Integer> registeredNodes) {
        set("registeredNodes", registeredNodes);
    }

    public List<ProtocolMapperModel> getProtocolMappers() {
        return get("protocolMappers");
    }

    public void setProtocolMappers(List<ProtocolMapperModel> protocolMappers) {
        set("protocolMappers", protocolMappers);
    }

    public String getClientTemplate() {
        return get("clientTemplate");
    }

    public void setClientTemplate(String clientTemplate) {
        set("clientTemplate", clientTemplate);
    }

    public Boolean getUseTemplateConfig() {
        return getBoolean("useTemplateConfig");
    }
}
