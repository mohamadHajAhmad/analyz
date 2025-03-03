package sdk.dao;

public interface TanentDao extends CISDAO{


    /**
     * Gets the partner by user name.
     *
     * @param partnerCode
     *            the user name
     * @return the partner by user name
     */
    public String getPartner(String partnerCode);

}
