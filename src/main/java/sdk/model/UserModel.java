package sdk.model;

import sdk.util.restBusinessDataValidation.DataValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class UserModel extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public UserModel(){

    }
    public UserModel(Map<String, Object> token) {

        setId(DataValidation.getString(token.get("sub")));
//		String id = (String) token.get("sub");
//		setId(id);

//		String firstName = (String) token.get("given_name");
//		setFirstName(firstName);
        setFirstName(DataValidation.getString(token.get("given_name")));

//		String lastName = (String) token.get("family_name");
//		setLastName(lastName);
        setLastName(DataValidation.getString(token.get("family_name")));

//		String email = (String) token.get("email");
//		setEmail(email);
        setEmail(DataValidation.getString(token.get("email")));

        String username = (String) token.get("preferred_username");
        setUsername(username);
        setUsername(DataValidation.getString(token.get("preferred_username")));

        Boolean emailVerified = (Boolean) token.get("email_verified");
        setEmailVerified(emailVerified);
        setEmailVerified(DataValidation.getBoolean(token.get("email_verified")));

        String sessionState = (String) token.get("session_state");



        Map<String, Object> attributes = new HashMap<String, Object>();
//		List<String> attr = new ArrayList<String>();
        String partnerCode = DataValidation.getString(token.get("partnerCode"));
        String firstLogin = DataValidation.getString(token.get("firstLogin"));
        String tokenIssueDate = DataValidation.getString(token.get("tokenIssueDate"));
        String userType = DataValidation.getString(token.get("userType"));
        String accountNumber = DataValidation.getString(token.get("accountNumber"));
        String agentUser = DataValidation.getString(token.get("agentUser"));
        String passIssueDate = DataValidation.getString(token.get("passIssueDate"));
        String passTemp = DataValidation.getString(token.get("passTemp"));
        String locale = DataValidation.getString(token.get("locale"));
        String tokens = DataValidation.getString(token.get("token"));
        String ssoUser = DataValidation.getString(token.get("ssoUser"));

//		attr.add(partnerCode);
        attributes.put("partnerCode", partnerCode);
//		attr = new ArrayList<String>();
//		attr.add(firstLogin);
        attributes.put("firstLogin", firstLogin);
//		attr = new ArrayList<String>();
//		attr.add(tokenIssueDate);
        attributes.put("tokenIssueDate", tokenIssueDate);
//		attr = new ArrayList<String>();
//		attr.add(userType);
        attributes.put("userType", userType);
//		attr = new ArrayList<String>();
//		attr.add(accountNumber);
        attributes.put("accountNumber", accountNumber);
//		attr = new ArrayList<String>();
//		attr.add(agentUser);
        attributes.put("agentUser", agentUser);
//		attr = new ArrayList<String>();
//		attr.add(passIssueDate);
        attributes.put("passIssueDate", passIssueDate);
//		attr = new ArrayList<String>();
//		attr.add(passTemp);
        attributes.put("passTemp", passTemp);
//		attr = new ArrayList<String>();
//		attr.add(locale);
        attributes.put("locale", locale);
//		attr = new ArrayList<String>();
//		attr.add(tokens);
        attributes.put("token", tokens);

        attributes.put("sessionState", sessionState);

        attributes.put("ssoUser", ssoUser);

        setAttributess(attributes);

    }

    public String getSelf() {
        return get("self");
    }

    public void setSelf(String self) {
        set("self", self);
    }

    public String getId() {
        return get("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public Long getCreatedTimestamp() {
        return getLong("createdTimestamp");
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        set("createdTimestamp", createdTimestamp);
    }

    public String getFirstName() {
        return get("firstName");
    }

    public void setFirstName(String firstName) {
        set("firstName", firstName);
    }

    public String getLastName() {
        return get("lastName");
    }

    public void setLastName(String lastName) {
        set("lastName", lastName);
    }

    public String getEmail() {
        return get("email");
    }

    public void setEmail(String email) {
        set("email", email);
    }

    public String getUsername() {
        return get("username");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getUserName() {
        return get("userName");
    }

    public void setUserName(String userName) {
        set("userName", userName);
    }

    public Boolean isEnabled() {
        return getBoolean("enabled");
    }

    public void setEnabled(Boolean enabled) {
        set("enabled", enabled);
    }

    public Boolean isTotp() {
        return getBoolean("totp");
    }

    public void setTotp(Boolean totp) {
        set("totp", totp);
    }

    public Boolean isEmailVerified() {
        return getBoolean("emailVerified");
    }

    public void setEmailVerified(Boolean emailVerified) {
        set("emailVerified", emailVerified);
    }

    public Map<String, Object> getAttributes() {
        return get("attributes");
    }

    // set("method can be removed once we can remove backwards compatibility with Keycloak 1.3 (then getAttributes() can be changed to return
    // Map<String, List<String>> )
    // @JsonIgnore
    public Map<String, Object> getAttributesAsListValuess() {
        return get("attributes");
    }

    public void setAttributess(Map<String, Object> attributes) {
        set("attributes", attributes);
    }

    // set("method can be removed once we can remove backwards compatibility with Keycloak 1.3 (then getAttributes() can be changed to return
    // Map<String, List<String>> )
    // @JsonIgnore
    public Map<String, List<String>> getAttributesAsListValues() {
        return get("attributes");
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        set("attributes", attributes);
    }



    public List<String> getRequiredActions() {
        return get("requiredActions");
    }

    public void setRequiredActions(List<String> requiredActions) {
        set("requiredActions", requiredActions);
    }

    public List<String> getRealmRoles() {
        return get("realmRoles");
    }

    public void setRealmRoles(List<String> realmRoles) {
        set("realmRoles", realmRoles);
    }

    public Map<String, List<String>> getClientRoles() {
        return get("clientRoles");
    }

    public void setClientRoles(Map<String, List<String>> clientRoles) {
        set("clientRoles", clientRoles);
    }

    @Deprecated
    public Map<String, List<String>> getApplicationRoles() {
        return get("applicationRoles");
    }

    public String getFederationLink() {
        return get("federationLink");
    }

    public void setFederationLink(String federationLink) {
        set("federationLink", federationLink);
    }

    public String getServiceAccountClientId() {
        return get("serviceAccountClientId");
    }

    public void setServiceAccountClientId(String serviceAccountClientId) {
        set("serviceAccountClientId", serviceAccountClientId);
    }

    public List<String> getGroups() {
        return get("groups");
    }

    public void setGroups(List<String> groups) {
        set("groups", groups);
    }

    public String getPassword() {
        return get("password");
    }

    public void setPassword(String password) {
        set("password", password);
    }

    public String getClientSecret() {
        return get("clientSecret");
    }

    public void setClientSecret(String clientSecret) {
        set("clientSecret", clientSecret);
    }

    public String getClientName() {
        return get("clientName");
    }

    public void setClientName(String clientName) {
        set("clientName", clientName);
    }

    public String getCurrentPassword() {
        return get("currentPassword");
    }

    public void setCurrentPassword(String currentPassword) {
        set("currentPassword", currentPassword);
    }

    public String getNewPassword() {
        return get("newPassword");
    }

    public void setNewPassword(String newPassword) {
        set("newPassword", newPassword);
    }

    public String getNewPasswordConfirm() {
        return get("newPasswordConfirm");
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        set("newPasswordConfirm", newPasswordConfirm);
    }

    public String getConfirmPassword() {
        return get("confirmPassword");
    }

    public void setConfirmPassword(String confirmPassword) {
        set("confirmPassword", confirmPassword);
    }

    public String getToken() {
        return get("token");
    }

    public void setToken(String token) {
        set("token", token);
    }

    public String getPassIssueDate() {
        return get("passIssueDate");
    }

    public void setPassIssueDate(String passIssueDate) {
        set("passIssueDate", passIssueDate);
    }

    public String getPartnerCode() {
        return get("partnerCode");
    }

    public void setPartnerCode(String partnerCode) {
        set("partnerCode", partnerCode);
    }

    public String getLocale() {
        return get("locale");
    }

    public void setLocale(String locale) {
        set("locale", locale);
    }

    public String getFirstLogin() {
        return get("firstLogin");
    }

    public void setFirstLogin(String firstLogin) {
        set("firstLogin", firstLogin);
    }

    public String getAgentUser() {
        return get("agentUser");
    }

    public void setAgentUser(String agentUser) {
        set("agentUser", agentUser);
    }

    public String getPassTemp() {
        return get("passTemp");
    }

    public void setPassTemp(String passTemp) {
        set("passTemp", passTemp);
    }

    public String getTokenIssueDate() {
        return get("tokenIssueDate");
    }

    public void setTokenIssueDate(String tokenIssueDate) {
        set("tokenIssueDate", tokenIssueDate);
    }

    public String getUserType() {
        return get("userType");
    }

    public void setUserType(String userType) {
        set("userType", userType);
    }

    public String getAccountNumber() {
        return get("accountNumber");
    }

    public void setAccountNumber(String accountNumber) {
        set("accountNumber", accountNumber);
    }

    public String getParentAccount() {
        return get("parentAccount");
    }

    public void setParentAccount(String parentAccount) {
        set("parentAccount", parentAccount);
    }

    public String getRealmName() {
        return get("realmName");
    }

    public void setRealmName(String realmName) {
        set("realmName", realmName);
    }

    public void setExternalAccountId(String externalAccountId) {
        set("externalAccountId", externalAccountId);
    }

    public String getExternalAccountId() {
        return get("externalAccountId");
    }

    public void setMiddleNameInitial(String middleNameInitial) {
        set("middleNameInitial", middleNameInitial);
    }

    public String getMiddleNameInitial() {
        return get("middleNameInitial");
    }
    public String getCaptchaCode(){
        return get("captchaCode");
    }
    public void setCaptchaCode(String captchaCode){
        set("captchaCode",captchaCode);
    }
    public String getCaptchaId(){
        return get("captchaId");
    }
    public void setCaptchaId(String captchaId){
        set("captchaId",captchaId);
    }

    public String getSessionState(){
        return get("sessionState");
    }
    public void setSessionState(String sessionState){
        set("sessionState",sessionState);
    }


}

