
package sdk.model;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractModel.
 */
public class AbstractModel extends HashMap<String, Object> {

	LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new abstract model.
	 */
	public AbstractModel() {

	}

	/**
	 * Instantiates a new abstract model.
	 *
	 * @param hasResult
	 *            the has result
	 * @param errorMessage
	 *            the error message
	 */
	public AbstractModel(boolean hasResult, String errorMessage) {
		setHasResult(hasResult);
		setErrorMessage(errorMessage);
	}

	/**
	 * Gets the checks for result.
	 *
	 * @return the checks for result
	 */
	public boolean hasResult() {
		return getBoolean("hasResult");
	}

	/**
	 * Sets the checks for result.
	 *
	 * @param hasResult
	 *            the new checks for result
	 */
	public void setHasResult(boolean hasResult) {
		set("hasResult", hasResult);
	}

	/**
	 * Sets the.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the object
	 */
	public Object set(String key, Object value) {
		return put(key, value);
	}

	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return get("errorMessage");
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage
	 *            the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		set("errorMessage", errorMessage);
	}

	/**
	 * Instantiates a new abstract model.
	 *
	 * @param properties
	 *            the properties
	 */
	public AbstractModel(Map<String, Object> properties) {
		putAll(properties);
	}

	/**
	 * Gets the.
	 *
	 * @param <X>
	 *            the generic type
	 * @param property
	 *            the property
	 * @return the x
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sencha.gxt.legacy.client.data.BaseModelData#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <X> X get(String property) {
		X value = (X) super.get(property);
		if (value != null && value.equals("null")) {
			return null;
		}
		return value;
	}

	/**
	 * Sets the properties.
	 *
	 * @param properties
	 *            the properties
	 */
	public void setProperties(Map<String, Object> properties) {
		putAll(properties);
	}

	/**
	 * Put properties.
	 *
	 * @param properties
	 *            the properties
	 */
	public void putProperties(Map<String, Object> properties) {
		for (String key : properties.keySet()) {
			set(key, properties.get(key));
		}
	}

	/**
	 * Gets the double.
	 *
	 * @param id
	 *            the id
	 * @return the double
	 */
	public Double getDouble(String id) {
		if (get(id) != null) {
			return new Double(get(id).toString().replace("[^\\d.]", "").replace(",", ""));
		}
		return null;
	}

	/**
	 * Gets the integer.
	 *
	 * @param id
	 *            the id
	 * @return the integer
	 */
	public Integer getInteger(String id) {
		if (get(id) != null) {
			return new Integer(get(id).toString());
		}
		return null;
	}

	/**
	 * Gets the long.
	 *
	 * @param id
	 *            the id
	 * @return the long
	 */
	public Long getLong(String id) {
		if (get(id) != null) {
			return new Long(get(id).toString());
		}
		return null;
	}

	/**
	 * Gets the short.
	 *
	 * @param id
	 *            the id
	 * @return the short
	 */
	public Short getShort(String id) {
		if (get(id) != null) {
			return new Short(get(id).toString());
		}
		return null;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		try {
			return get("updatedDate");
		} catch (Exception e) {
			return new Date(getUpdatedDateAsLong());
		}
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate
	 *            the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		set("updatedDate", updatedDate);
		if (updatedDate != null) {
			setUpdatedDateAsLong(updatedDate.getTime());
		}
	}

	/**
	 * Gets the date.
	 *
	 * @param id
	 *            the id
	 * @return the date
	 */
	public Date getDate(String id) {
		try {
			return get(id);
		} catch (Exception e) {
			try {
				return getDateFromString(get(id).toString());
			} catch (Exception e1) {
				return new Date(getUpdatedDateAsLong());
			}
		}
	}

	/**
	 * Sets the date.
	 *
	 * @param id
	 *            the id
	 * @param date
	 *            the date
	 */
	public void setDate(String id, Object date) {
		if (date != null && date instanceof Long) {
			set(id, new Date((Long) date));
		}
		set(id, date);
	}

	/**
	 * Gets the updated date as long.
	 *
	 * @return the updated date as long
	 */
	public Long getUpdatedDateAsLong() {
		if (getLong("updatedDateLong") != null) {
			return getLong("updatedDateLong");
		}
		return new Date().getTime();
	}

	/**
	 * Sets the updated date as long.
	 *
	 * @param createdDate
	 *            the new updated date as long
	 */
	public void setUpdatedDateAsLong(Long createdDate) {
		set("updatedDateLong", createdDate);
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		try {
			return get("createdDate");
		} catch (Exception e) {
			return new Date(getCreatedDateAsLong());

		}
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate
	 *            the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		set("createdDate", createdDate);
		if (createdDate != null) {
			setCreatedDateAsLong(createdDate.getTime());
		}
	}

	/**
	 * Gets the created date as long.
	 *
	 * @return the created date as long
	 */
	public Long getCreatedDateAsLong() {
		if (getLong("createdDateLong") != null) {
			return getLong("createdDateLong");
		}
		return new Date().getTime();
	}

	/**
	 * Sets the created date as long.
	 *
	 * @param createdDate
	 *            the new created date as long
	 */
	public void setCreatedDateAsLong(Long createdDate) {
		set("createdDateLong", createdDate);
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public String getCreatedBy() {
		return get("createdBy");
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(String createdBy) {
		set("createdBy", createdBy);
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	public String getUpdatedBy() {
		return get("updatedBy");
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy
	 *            the new updated by
	 */
	public void setUpdatedBy(String updatedBy) {
		set("updatedBy", updatedBy);
	}

	/**
	 * Gets the service category code.
	 *
	 * @return the service category code
	 */
	public String getServiceCategoryCode() {
		return get("serviceCategoryCode");
	}

	/**
	 * Gets the service provider name.
	 *
	 * @return the service provider name
	 */
	public String getServiceProviderName() {
		return get("providerName");
	}

	public String getName() {
		return get("name");
	}

	/**
	 * Gets the boolean.
	 *
	 * @param id
	 *            the id
	 * @return the boolean
	 */
	public Boolean getBoolean(String id) {
		try {
			if (get(id) != null) {
				return new Boolean(get(id).toString());
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return this;
	}

	/**
	 * Gets the property names.
	 *
	 * @return the property names
	 */
	public Collection<String> getPropertyNames() {
		return super.keySet();
	}

	/**
	 * Gets the date from string.
	 *
	 * @param dateString
	 *            the date string
	 * @return the date from string
	 */
	private Date getDateFromString(String dateString) {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat formatter5 = new SimpleDateFormat("MMMddyyyy");
		DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		DateFormat formatter4 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		DateFormat formatter6 = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy");
		if (StringUtils.isEmpty(dateString)) {
			return null;
		}
		try {
			return (Date) formatter.parse(dateString);
		} catch (ParseException e) {
			try {
				return (Date) formatter2.parse(dateString);
			} catch (ParseException e1) {
				try {
					return (Date) formatter3.parse(dateString);
				} catch (ParseException e2) {
					try {
						return (Date) formatter4.parse(dateString);
					} catch (ParseException e3) {
						try {
							return (Date) formatter5.parse(dateString);
						} catch (ParseException e4) {
							try {
								return (Date) formatter6.parse(dateString.replaceAll("\\+", "\\+0"));
							} catch (ParseException e5) {
								return null;
							}
						}
					}
				}
			}
		}
	}
}
