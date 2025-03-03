package sdk.dao.util;


import jakarta.persistence.criteria.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.util.StringUtils;
import sdk.POJO.Accounts;
import sdk.POJO.SspUser;
import sdk.model.*;
import sdk.util.SystemConstants;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DAOHelper {

    private static final Logger logger = LogManager.getLogger(DAOHelper.class);

    public static SearchQueryContent createSearchDynamicQuery(SearchQueryContent searchQueryContent, List<SearchDataModel> conditions, Map<String, Field> fieldMap, Class<?> clazz) {

        CriteriaBuilder criteriaBuilder = searchQueryContent.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = searchQueryContent.getCriteria();
        Root<?> root = searchQueryContent.getRoot();
        Predicate predicate = null;
        Join<?, ?> join = searchQueryContent.getJoin();
        Predicate currentPredicate = null;
        if (conditions != null) {
            List<Predicate> predicates = new ArrayList<>();
            String operator;
            String field;
            String secondField = null;
            Path path = null;
            Date date;
            String dateFormatValue;
            List<Object> searchValues;
            List<Object> values = new ArrayList<Object>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat sqlTimestampFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
            Object obj1, obj2;

            for (SearchDataModel condition : conditions) {

                if (condition.getSubSearchDataModelList() != null) {
                    if (currentPredicate == null) {
                        Class<?> subclazz = condition.getClazz();
                        if (subclazz == null)
                            return null;
                        Map<String, Field> subFieldMap = DAOHelper.createFieldMap(subclazz);
                        Subquery<?> subquery = createSubSearchDynamicQuery(searchQueryContent, condition, subFieldMap, subclazz);
                        if (subquery == null)
                            return null;
                        currentPredicate = criteriaBuilder.equal(root.get(condition.getSearchField()), subquery);
                    } else {
                        Class<?> subclazz = condition.getClazz();
                        if (subclazz == null)
                            return null;
                        Map<String, Field> subFieldMap = DAOHelper.createFieldMap(subclazz);
                        Subquery<?> subquery = createSubSearchDynamicQuery(searchQueryContent, condition, subFieldMap, subclazz);
                        if (subquery == null)
                            return null;
                        Predicate subPredicate = criteriaBuilder.equal(root.get(condition.getSearchField()), subquery);
                        currentPredicate = criteriaBuilder.and(currentPredicate, subPredicate);
                    }

                    continue;
                }

                condition.buildObject();
                field = condition.getSearchField();
                operator = condition.getOperator();


                if (condition.getSubSearchDataModelList() == null) {
                    if (operator == null || field == null)
                        return null;
                    if (fieldMap != null && fieldMap.get(field) == null) {
                        if (fieldMap.get("id." + field) == null)
                            return null;
                        field = "id." + field;
                        secondField = fieldMap.get(field).getName();
                    }
                    if (fieldMap != null && condition.getSearchValues() != null) {
                        if (condition.getSearchValues() instanceof List) {
                            searchValues = condition.getValues();
                            if (searchValues.isEmpty())
                                return null;
                            Class type = fieldMap.get(field).getType();
                            for (int index = 0; index < searchValues.size(); index++)
                                searchValues.set(index, getObjectFromType(type, searchValues.get(index)));
                        } else if (!fieldMap.get(field).getType().getSimpleName().equals("Double") || !SystemConstants.likeTypes.contains(operator)) {
                            condition.setValue(getObjectFromType(fieldMap.get(field).getType(), condition.getValue()));
                        }
                    }
                }
                if (secondField != null && !secondField.isEmpty()) {
                    String parentField = field.substring(field.indexOf(".") + 1, field.lastIndexOf("."));
                    path = root.get(parentField).get(secondField);

                } else
                    path = root.get(field);

                if (QueryConditionModel.Operators.EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()));
                        //predicates.add(criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, root.get(field)), criteriaBuilder.function("TO_DATE", String.class, criteriaBuilder.parameter(String.class, dateFormat.format((Date) condition.getValue())), criteriaBuilder.literal("MM/dd/yyyy"))));
                    else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.equal(path, condition.getValue());
                    }

                } else if (QueryConditionModel.Operators.NOT_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.notEqual(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()));
                        //predicates.add(criteriaBuilder.notEqual(criteriaBuilder.function("TO_CHAR", String.class, root.get(field)), criteriaBuilder.function("TO_DATE", String.class, criteriaBuilder.parameter(String.class, condition.getValue().toString()), criteriaBuilder.literal("MM/dd/yyyy"))));
                    else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.notEqual(path, condition.getValue());
                    }

                } else if (QueryConditionModel.Operators.LIKE_FIRST.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(path, value + "%");
                        values.add(value + "%");
                    } else if (condition.getValue() instanceof Date) {
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()) + "%");
                    } else {

                        predicate = criteriaBuilder.like(path, newLikeValue + "%", '\\');

                        //predicates.add(criteriaBuilder.like(root.get(field),  (newLikeValue != null ? newLikeValue : condition.getValue()) + "%" + (newLikeValue != null ? " ESCAPE '\\'" : "")));


                    }
                } else if (QueryConditionModel.Operators.LIKE_LAST.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(path, "%" + value);
                        values.add("%" + value);
                    } else if (condition.getValue() instanceof Date) {
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()));
                    } else {
                        predicate = criteriaBuilder.like(path, "%" + newLikeValue, '\\');
                    }
                } else if (QueryConditionModel.Operators.LIKE_BOTH.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(path, "%" + value + "%");
                        values.add("%" + value + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.like(path, "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.LIKE_BOTH_INSENSITIVE.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString().toLowerCase();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value).toLowerCase();
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%");
                        values.add("%" + value + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.like(path, "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.NOT_LIKE_BOTH_INSENSITIVE.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString().toLowerCase();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value).toLowerCase();
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.notLike(criteriaBuilder.lower(path), "%" + value + "%");
                        values.add("%" + value.toLowerCase() + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.notLike(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.notLike(path, "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.LESS_THAN.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThan(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThan(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.lessThan(path, condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.LESS_THAN_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThanOrEqualTo(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        date = addDaysToDate(date, 1);
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThanOrEqualTo(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.lessThanOrEqualTo(path, condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.GREATER_THAN.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") && condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThan(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        date = addDaysToDate(date, 1);
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThan(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.greaterThan(path, condition.getValue().toString());

                    }
                } else if (QueryConditionModel.Operators.GREATER_THAN_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(path, condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.IS_NULL.code().equals(operator)) {
                    predicate = criteriaBuilder.isNull(path);
                } else if (QueryConditionModel.Operators.IS_NOT_NULL.code().equals(operator)) {
                    predicate = criteriaBuilder.isNotNull(path);
                } else if (QueryConditionModel.Operators.IS_EMPTY.code().equals(operator)) {
//                    Path expression = root.get(field);
//                    predicate =criteriaBuilder.isEmpty(expression);
                    predicates.add(criteriaBuilder.isEmpty(path));
                    //predicate =criteriaBuilder.isEmpty(root.get(field));
                } else if (QueryConditionModel.Operators.IS_NOT_EMPTY.code().equals(operator)) {
                    predicate = criteriaBuilder.isNotEmpty(path);
                } else if (QueryConditionModel.Operators.BETWEEN.code().equals(operator) || QueryConditionModel.Operators.NOT_BETWEEN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.size() < 2 || searchValues.get(0) == null || searchValues.get(1) == null) {
                        logger.warn("Between criteria ignored because it does not have two variables to compare with");
                        return null;
                    }
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || searchValues.get(0) instanceof Timestamp) {
                        obj1 = removeTime((Date) searchValues.get(0));
                        values.add(sqlTimestampFormat.format(searchValues.get(0)).toString());
                    } else if (searchValues.get(0) instanceof Date) {
                        obj1 = removeTime((Date) searchValues.get(0));
                        values.add(sqlDateFormat.format(searchValues.get(0)).toString());
                    } else {
                        obj1 = searchValues.get(0);
                        values.add(searchValues.get(0));
                    }

                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || searchValues.get(1) instanceof Timestamp) {
                        obj2 = getEndOfTheDay((Date) searchValues.get(1));
                        values.add(sqlTimestampFormat.format(obj2).toString());
                    } else if (searchValues.get(1) instanceof Date) {
                        obj2 = getEndOfTheDay((Date) searchValues.get(1));
                        values.add(sqlDateFormat.format(obj2).toString());
                    } else {
                        obj2 = searchValues.get(1);
                        values.add(searchValues.get(1));
                    }
                    if (QueryConditionModel.Operators.BETWEEN.code().equals(operator))
                        predicate = criteriaBuilder.between(path, (Date) obj1, (Date) obj2);
                    else
                        predicate = criteriaBuilder.not(criteriaBuilder.between(path, (Date) obj1, (Date) obj2));
                } else if (QueryConditionModel.Operators.IN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.isEmpty())
                        return null;

                    Expression<String> inExpression = path;
                    predicate = inExpression.in(searchValues.toArray());


                    for (Object value : searchValues)
                        if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || value instanceof Timestamp) {
                            values.add(sqlTimestampFormat.format(value).toString());
                        } else if (value instanceof Date)
                            values.add(sqlDateFormat.format(value).toString());
                        else values.add(value);
                } else if (QueryConditionModel.Operators.NOT_IN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.isEmpty())
                        return null;

                    Expression<String> inExpression = path;
                    Predicate inPredicate = inExpression.in(searchValues.toArray());
                    predicate = criteriaBuilder.not(inPredicate);
                    for (Object value : searchValues)
                        if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || value instanceof Timestamp) {
                            values.add(sqlTimestampFormat.format(value).toString());
                        } else if (value instanceof Date)
                            values.add(sqlDateFormat.format(value).toString());
                        else values.add(value);
                } else {
                    return null;
                }

                if (predicate != null) {
                    if (currentPredicate == null) {
                        currentPredicate = predicate;
                    } else if (condition.getCondition() != null && condition.getCondition().equals(QueryConditionModel.Conditions.OR.code())) {
                        currentPredicate = criteriaBuilder.or(currentPredicate, predicate);

                    } else {
                        currentPredicate = criteriaBuilder.and(currentPredicate, predicate);
                    }
                }
            }
            predicates.add(currentPredicate);
            searchQueryContent.setValues(values);
            criteriaQuery = criteriaQuery.where(predicates.toArray(new Predicate[0]));
            searchQueryContent.setCriteria(criteriaQuery);
        }

        return searchQueryContent;
    }

    public static Map<String, Field> createFieldMap(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (!Modifier.toString(field.getModifiers()).contains("static") && field.getType().getCanonicalName().contains("POJO") /*&& field.getName().toLowerCase().endsWith("id")*/) {
                    Object newInstance = field.getType().newInstance();
                    Field[] fields = newInstance.getClass().getDeclaredFields();
                    for (Field field2 : fields) {
                        field2.setAccessible(true);
                        fieldMap.put("id." + field.getName() + "." + field2.getName(), field2);
                    }
                    continue;
                }
                fieldMap.put(field.getName(), field);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return fieldMap;
    }

    private static boolean valueContainsLikeSpecialChars(String value) {
        if (value == null || value.isEmpty())
            return false;
        for (int i = 0; i < value.length(); i++)
            if (value.charAt(i) == '_' || value.charAt(i) == '%')
                return true;
        return false;
    }

    private static String getLikeValue(String value) {
        if (value == null || value.isEmpty())
            return value;
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '_' || value.charAt(i) == '%' || value.charAt(i) == '\\')
                newValue += '\\';
            newValue += value.charAt(i);
        }
        return newValue;
    }

    /**
     * Removes the time.
     *
     * @param date the date
     * @return the date
     */
    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Adds the days to date.
     *
     * @param date the date
     * @param days the days
     * @return the date
     * @author aalrish
     */
    public static Date addDaysToDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        date = cal.getTime();
        return date;
    }

    /**
     * Gets the end of the day.
     *
     * @param date the date
     * @return the end of the day
     * @author aalrish
     */
    public static Date getEndOfTheDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static Object getObjectFromType(Class<?> type, Object object) {
        if (type.isInstance(object))
            return object;
        String typeName = type.getSimpleName();
        if (typeName.equals("String"))
            return object.toString();
        if (typeName.equals("Long"))
            return Long.parseLong(object.toString());
        if (typeName.equals("Double"))
            return Double.parseDouble(object.toString());
        if (typeName.equals("Date") || typeName.equals("Timestamp"))
            return getDateFromString(object.toString());
        if (typeName.equals("Integer"))
            return Integer.parseInt(object.toString());
        if (typeName.equals("BigDecimal"))
            return new BigDecimal(object.toString());
        if (typeName.equals("Byte"))
            return Byte.parseByte(object.toString());
        if (typeName.equals("Short"))
            return Short.parseShort(object.toString());
        if (typeName.equals("Float"))
            return Float.parseFloat(object.toString());
        return object;
    }

    /**
     * Gets the date from string.
     *
     * @param dateString the date string
     * @return the date from string
     */
    public static Date getDateFromString(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter3 = new SimpleDateFormat("MMMddyyyy");
        DateFormat formatter4 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat formatter5 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat formatter6 = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy");
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

    public static SearchQueryContent createSortDynamicQuery(SearchQueryContent searchQueryContent, List<SortDataModel> sortDataModelList, Map<String, Field> fieldMap, Class<?> clazz) {
        if (sortDataModelList != null) {
            String field;
            Path path;
            String secondField = null;
            CriteriaBuilder criteriaBuilder = searchQueryContent.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = searchQueryContent.getCriteria();
            Root<?> root = searchQueryContent.getRoot();
            Join<?, ?> join = searchQueryContent.getJoin();
            List<Order> orderList = new ArrayList();
            for (SortDataModel sortDataModel : sortDataModelList) {
                sortDataModel.buildObject();
                field = sortDataModel.getSortField();
                if (field == null)
                    return null;
                if (fieldMap != null && fieldMap.get(field) == null) {
                    if (fieldMap.get("id." + field) == null)
                        return null;
                    field = "id." + field;
                    secondField = fieldMap.get(field).getName();
                }

                if (secondField != null && !secondField.isEmpty()) {
                    String parentField = field.substring(field.indexOf(".") + 1, field.lastIndexOf("."));
                    path = root.get(parentField).get(secondField);
                } else
                    path = root.get(field);

                if (sortDataModel.getDirection() == null || QueryConditionModel.Operators.ORFER_BY_ASC.code().equals(sortDataModel.getDirection())) {
                    orderList.add(criteriaBuilder.asc(path));

                } else if (QueryConditionModel.Operators.ORFER_BY_DESC.code().equals(sortDataModel.getDirection())) {
                    orderList.add(criteriaBuilder.desc(path));
                } else return null;
            }
            criteriaQuery.orderBy(orderList);
            searchQueryContent.setCriteria(criteriaQuery);
        }
        return searchQueryContent;
    }


    @SuppressWarnings("all")
    public static AbstractModel createModelDataFromObject(Object data) {
        AbstractModel model = new AbstractModel();
        final Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!Modifier.toString(field.getModifiers()).contains("static")) {
                    field.setAccessible(true);
                    final String name = field.getName();
                    final Object value = field.get(data);
                    if (value == null) {
                        model.set(name, value);
                    } else if (value instanceof Date) {
                        if (field.getType().getName().equalsIgnoreCase("java.sql.Time"))
                            model.set(name, value);
                        else {
                            String format = "MM/dd/yyyy";
                            if (field.getType().getName().equalsIgnoreCase("java.sql.Timestamp"))
                                format += " HH:mm:ss";
                            SimpleDateFormat df = new SimpleDateFormat(format);
                            Date dt = (Date) value;
                            model.set(name, df.format(dt));
                        }
                    } else if (!(value instanceof Date) && !(value instanceof Clob) && !(value instanceof Blob) && !(value instanceof Collection) && !(value instanceof Map) && !value.getClass().getPackage().getName().contains(".pojo") && !value.getClass().getName().toLowerCase().endsWith("id")) {
                        model.set(name, value);
                    } else if (value instanceof Blob) {
                        try {
                            String val = IOUtils.toString(new InputStreamReader(((Blob) value).getBinaryStream()));
                            model.set(name, val);
                        } catch (Exception e) {
                            try {
                                ObjectInputStream inputStream = new ObjectInputStream(((Blob) value).getBinaryStream());
                                Object object = inputStream.readObject();
                                if (object instanceof Map) {
                                    model.set(name, new HashMap((Map) object));
                                }
                                if (object instanceof Collection) {
                                    model.set(name, new ArrayList((Collection) object));
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    } else if (value instanceof Clob) {
                        try {
                            Clob clob = (Clob) value;
                            String stringValue = clob.getSubString(1, (int) clob.length());
                            model.set(name, stringValue);
                        } catch (Exception e) {
                            try {
                                ObjectInputStream inputStream = new ObjectInputStream(((Clob) value).getAsciiStream());
                                Object object = inputStream.readObject();
                                if (object instanceof Map) {
                                    model.set(name, new HashMap((Map) object));
                                }
                                if (object instanceof Collection) {
                                    model.set(name, new ArrayList((Collection) object));
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    } else if (value.getClass().getPackage().getName().contains(".pojo") && value.getClass().getName().toLowerCase().endsWith("id")) {
                        AbstractModel modelData = createModelDataFromObject(value);
                        for (String key : modelData.getPropertyNames()) {
                            model.set(key, modelData.get(key));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
                throw new RuntimeException(e);
            }
        }
        return model;
    }


    public static Subquery<?> createSubSearchDynamicQuery(SearchQueryContent searchQueryContent, SearchDataModel conditions, Map<String, Field> fieldMap, Class<?> clazz) {
        CriteriaBuilder criteriaBuilder = searchQueryContent.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = searchQueryContent.getCriteria();
        Predicate predicate = null;
        Predicate currentPredicate = null;
        Subquery<?> query = criteriaQuery.subquery(clazz);
        Root<?> root = query.from(clazz);
        String mainField = conditions.getSubSearchField();
        List<SubSearchDataModel> SubSearchDataModelList = conditions.getSubSearchDataModelList();
        if (SubSearchDataModelList != null) {
            List<Predicate> predicates = new ArrayList<>();
            String operator;
            String field;
            Date date;
            String dateFormatValue;
            List<Object> searchValues;
            List<Object> values = new ArrayList<Object>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat sqlTimestampFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
            Object obj1, obj2;

            for (SubSearchDataModel condition : SubSearchDataModelList) {

                if (condition.getSearchDataModel() != null) {
                    if (currentPredicate == null) {
                        Class<?> subclazz = condition.getSearchDataModel().getClazz();
                        if (subclazz == null)
                            return null;
                        Map<String, Field> subFieldMap = DAOHelper.createFieldMap(subclazz);
                        Subquery<?> subquery = createSubSearchDynamicQuery(searchQueryContent, condition.getSearchDataModel(), subFieldMap, subclazz);
                        if (subquery == null)
                            return null;

                        currentPredicate = criteriaBuilder.equal(root.get(condition.getSearchField()), subquery);
                        //currentPredicate =criteriaBuilder.equal(root.get(condition.getSearchField()), subquery);
                    } else {
                        Class<?> subclazz = condition.getSearchDataModel().getClazz();
                        if (subclazz == null)
                            return null;
                        Map<String, Field> subFieldMap = DAOHelper.createFieldMap(subclazz);
                        Subquery<?> subquery = createSubSearchDynamicQuery(searchQueryContent, condition.getSearchDataModel(), subFieldMap, subclazz);
                        if (subquery == null)
                            return null;
                        Predicate subPredicate = criteriaBuilder.equal(root.get(condition.getSearchField()), subquery);
                        currentPredicate = criteriaBuilder.and(currentPredicate, subPredicate);
                    }

                    continue;
                }

                field = condition.getSearchField();
                operator = condition.getOperator();
                if (condition.getSearchDataModel() == null) {
                    if (operator == null || field == null)
                        return null;
                    if (fieldMap != null && fieldMap.get(field) == null) {
                        if (fieldMap.get("id." + field) == null)
                            return null;
                        field = "id." + field;
                    }
                    if (fieldMap != null && condition.getSearchValues() != null) {
                        if (condition.getSearchValues() instanceof List) {
                            searchValues = condition.getValues();
                            if (searchValues.isEmpty())
                                return null;
                            Class type = fieldMap.get(field).getType();
                            for (int index = 0; index < searchValues.size(); index++)
                                searchValues.set(index, getObjectFromType(type, searchValues.get(index)));
                        } else if (!fieldMap.get(field).getType().getSimpleName().equals("Double") || !SystemConstants.likeTypes.contains(operator)) {
                            condition.setValue(getObjectFromType(fieldMap.get(field).getType(), condition.getValue()));
                        }
                    }
                }

                if (QueryConditionModel.Operators.EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()));
                        //predicates.add(criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, root.get(field)), criteriaBuilder.function("TO_DATE", String.class, criteriaBuilder.parameter(String.class, dateFormat.format((Date) condition.getValue())), criteriaBuilder.literal("MM/dd/yyyy"))));
                    else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.equal(root.get(field), condition.getValue());
                    }

                } else if (QueryConditionModel.Operators.NOT_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.notEqual(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()));
                        //predicates.add(criteriaBuilder.notEqual(criteriaBuilder.function("TO_CHAR", String.class, root.get(field)), criteriaBuilder.function("TO_DATE", String.class, criteriaBuilder.parameter(String.class, condition.getValue().toString()), criteriaBuilder.literal("MM/dd/yyyy"))));
                    else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.notEqual(root.get(field), condition.getValue());
                    }

                } else if (QueryConditionModel.Operators.LIKE_FIRST.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(root.get(field), value + "%");
                        values.add(value + "%");
                    } else if (condition.getValue() instanceof Date) {
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()) + "%");
                    } else {

                        predicate = criteriaBuilder.like(root.get(field), newLikeValue + "%", '\\');

                        //predicates.add(criteriaBuilder.like(root.get(field),  (newLikeValue != null ? newLikeValue : condition.getValue()) + "%" + (newLikeValue != null ? " ESCAPE '\\'" : "")));


                    }
                } else if (QueryConditionModel.Operators.LIKE_LAST.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(root.get(field), "%" + value);
                        values.add("%" + value);
                    } else if (condition.getValue() instanceof Date) {
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()));
                    } else {
                        predicate = criteriaBuilder.like(root.get(field), "%" + newLikeValue, '\\');
                    }
                } else if (QueryConditionModel.Operators.LIKE_BOTH.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(root.get(field), "%" + value + "%");
                        values.add("%" + value + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.like(root.get(field), "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.LIKE_BOTH_INSENSITIVE.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString().toLowerCase();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value).toLowerCase();
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + value.toLowerCase() + "%");
                        values.add("%" + value + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.like(root.get(field), "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.NOT_LIKE_BOTH_INSENSITIVE.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString().toLowerCase();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value).toLowerCase();
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.notLike(criteriaBuilder.lower(root.get(field)), "%" + value + "%");
                        values.add("%" + value.toLowerCase() + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.notLike(criteriaBuilder.function("TO_CHAR", String.class, root.get(field), criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.notLike(root.get(field), "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.LESS_THAN.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThan(root.get(field), date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThan(root.get(field), date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.lessThan(root.get(field), condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.LESS_THAN_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThanOrEqualTo(root.get(field), date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        date = addDaysToDate(date, 1);
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThanOrEqualTo(root.get(field), date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.lessThanOrEqualTo(root.get(field), condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.GREATER_THAN.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") && condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThan(root.get(field), date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        date = addDaysToDate(date, 1);
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThan(root.get(field), date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.greaterThan(root.get(field), condition.getValue().toString());

                    }
                } else if (QueryConditionModel.Operators.GREATER_THAN_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(field), date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(field), date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(field), condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.IS_NULL.code().equals(operator)) {
                    predicate = criteriaBuilder.isNull(root.get(field));
                } else if (QueryConditionModel.Operators.IS_NOT_NULL.code().equals(operator)) {
                    predicate = criteriaBuilder.isNotNull(root.get(field));
                } else if (QueryConditionModel.Operators.IS_EMPTY.code().equals(operator)) {
//                    Path expression = root.get(field);
//                    predicate =criteriaBuilder.isEmpty(expression);
                    predicates.add(criteriaBuilder.isEmpty(root.get(field)));
                    //predicate =criteriaBuilder.isEmpty(root.get(field));
                } else if (QueryConditionModel.Operators.IS_NOT_EMPTY.code().equals(operator)) {
                    predicate = criteriaBuilder.isNotEmpty(root.get(field));
                } else if (QueryConditionModel.Operators.BETWEEN.code().equals(operator) || QueryConditionModel.Operators.NOT_BETWEEN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.size() < 2 || searchValues.get(0) == null || searchValues.get(1) == null) {
                        logger.warn("Between criteria ignored because it does not have two variables to compare with");
                        return null;
                    }
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || searchValues.get(0) instanceof Timestamp) {
                        obj1 = removeTime((Date) searchValues.get(0));
                        values.add(sqlTimestampFormat.format(searchValues.get(0)).toString());
                    } else if (searchValues.get(0) instanceof Date) {
                        obj1 = removeTime((Date) searchValues.get(0));
                        values.add(sqlDateFormat.format(searchValues.get(0)).toString());
                    } else {
                        obj1 = searchValues.get(0);
                        values.add(searchValues.get(0));
                    }

                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || searchValues.get(1) instanceof Timestamp) {
                        obj2 = getEndOfTheDay((Date) searchValues.get(1));
                        values.add(sqlTimestampFormat.format(obj2).toString());
                    } else if (searchValues.get(1) instanceof Date) {
                        obj2 = getEndOfTheDay((Date) searchValues.get(1));
                        values.add(sqlDateFormat.format(obj2).toString());
                    } else {
                        obj2 = searchValues.get(1);
                        values.add(searchValues.get(1));
                    }
                    if (QueryConditionModel.Operators.BETWEEN.code().equals(operator))
                        predicate = criteriaBuilder.between(root.get(field), (Date) obj1, (Date) obj2);
                    else
                        predicate = criteriaBuilder.not(criteriaBuilder.between(root.get(field), (Date) obj1, (Date) obj2));
                } else if (QueryConditionModel.Operators.IN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.isEmpty())
                        return null;

                    Expression<String> inExpression = root.get(field);
                    predicate = inExpression.in(searchValues.toArray());


                    for (Object value : searchValues)
                        if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || value instanceof Timestamp) {
                            values.add(sqlTimestampFormat.format(value).toString());
                        } else if (value instanceof Date)
                            values.add(sqlDateFormat.format(value).toString());
                        else values.add(value);
                } else if (QueryConditionModel.Operators.NOT_IN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.isEmpty())
                        return null;

                    Expression<String> inExpression = root.get(field);
                    Predicate inPredicate = inExpression.in(searchValues.toArray());
                    predicate = criteriaBuilder.not(inPredicate);
                    for (Object value : searchValues)
                        if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || value instanceof Timestamp) {
                            values.add(sqlTimestampFormat.format(value).toString());
                        } else if (value instanceof Date)
                            values.add(sqlDateFormat.format(value).toString());
                        else values.add(value);
                } else {
                    return null;
                }

                if (predicate != null) {
                    if (currentPredicate == null) {
                        currentPredicate = predicate;
                    } else if (condition.getCondition() != null && condition.getCondition().equals(QueryConditionModel.Conditions.OR.code())) {
                        currentPredicate = criteriaBuilder.or(currentPredicate, predicate);

                    } else {
                        currentPredicate = criteriaBuilder.and(currentPredicate, predicate);
                    }
                }
            }
            predicates.add(currentPredicate);
            query.select(root.get(mainField)).where(predicates.toArray(new Predicate[0]));

            //criteriaQuery= criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }


        return query;
    }


    public static Join<?, ?> createJoin(SearchQueryContent searchQueryContent, Map<String, Field> fieldMap, List<JoinModel> joins) {

        Join<?, ?> join = null;
        Join<?, ?> currentJoin = null;
        String joinMapped = null;
        String joinType;
        Root <?> root=searchQueryContent.getRoot();
        if (joins == null)
            return null;
        for (JoinModel joinModel : joins) {
            joinType = joinModel.getJoinType();
            joinMapped = joinModel.getJoinMapped();

            if (joinType == null || joinType.isEmpty())
                return null;

            if (joinMapped == null || joinMapped.isEmpty())
                return null;
            if (QueryConditionModel.Joins.inner.code().equals(joinType)) {
                if (join == null)
                    join = root.join(joinMapped, JoinType.INNER);
                else
                    join = join.join(joinMapped, JoinType.INNER);
            } else if (QueryConditionModel.Joins.left.code().equals(joinType)) {
                if (join == null)
                    join = root.join(joinMapped, JoinType.LEFT);
                else
                    join = join.join(joinMapped, JoinType.LEFT);

            } else if (QueryConditionModel.Joins.right.code().equals(joinType)) {
                if (join == null)
                    join = root.join(joinMapped, JoinType.RIGHT);
                else
                    join = join.join(joinMapped, JoinType.LEFT);
            } else
                return null;

            if (joinModel.getJoinDataModel() !=null && !joinModel.getJoinDataModel().isEmpty()) {
                List<Predicate> predicates=createPredicateDynamic(searchQueryContent,joinModel.getJoinDataModel(),fieldMap);
                if (predicates==null)
                    return null;
                join.on(predicates.toArray(new Predicate[0]));
            }

        }

        return join;
    }


    public static List<Predicate> createPredicateDynamic(SearchQueryContent searchQueryContent, List<JoinDataModel> conditions, Map<String, Field> fieldMap) {
        CriteriaBuilder criteriaBuilder = searchQueryContent.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = searchQueryContent.getCriteria();
        List<Predicate> predicates = new ArrayList<>();
        Root<?> root = searchQueryContent.getRoot();
        Predicate predicate = null;
        Predicate currentPredicate = null;
        if (conditions != null) {
            String operator=null;
            String field;
            String secondField = null;
            Path path = null;
            Date date;
            String dateFormatValue;
            List<Object> searchValues;
            List<Object> values = new ArrayList<Object>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat sqlTimestampFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
            Object obj1, obj2;

            for (JoinDataModel condition : conditions) {


                field = condition.getSearchField();
              if ( field == null)
                        return null;
                    if (fieldMap != null && fieldMap.get(field) == null) {
                        if (fieldMap.get("id." + field) == null)
                            return null;
                        field = "id." + field;
                        secondField = fieldMap.get(field).getName();
                    }

                if (condition.getSecondSearchField()== null) {
                    operator = condition.getOperator();
                    if (operator == null)
                       return null;
                    if (fieldMap != null && condition.getSearchValues() != null) {
                        if (condition.getSearchValues() instanceof List) {
                            searchValues = condition.getValues();
                            if (searchValues.isEmpty())
                                return null;
                            Class type = fieldMap.get(field).getType();
                            for (int index = 0; index < searchValues.size(); index++)
                                searchValues.set(index, getObjectFromType(type, searchValues.get(index)));
                        } else if (!fieldMap.get(field).getType().getSimpleName().equals("Double") || !SystemConstants.likeTypes.contains(operator)) {
                            condition.setValue(getObjectFromType(fieldMap.get(field).getType(), condition.getValue()));
                        }
                    }
                }
                if (secondField != null && !secondField.isEmpty()) {
                    String parentField = field.substring(field.indexOf(".") + 1, field.lastIndexOf("."));
                    path = root.get(parentField).get(secondField);
                } else
                    path = root.get(field);

               if (condition.getSecondSearchField()!= null) {
                   String secondSearchField= condition.getSecondSearchField();
                   if (fieldMap != null && fieldMap.get(secondSearchField) == null) {
                       if (fieldMap.get("id." + secondSearchField) == null)
                           return null;
                       secondSearchField = "id." + secondSearchField;
                       secondField = fieldMap.get(secondSearchField).getName();
                   }
                   String parentField = secondSearchField.substring(secondSearchField.indexOf(".") + 1, secondSearchField.lastIndexOf("."));
                   Path secondPath = root.get(parentField).get(secondField);
                   predicate = criteriaBuilder.equal(path, secondPath);
               }
               else  if (QueryConditionModel.Operators.EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()));
                        //predicates.add(criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, root.get(field)), criteriaBuilder.function("TO_DATE", String.class, criteriaBuilder.parameter(String.class, dateFormat.format((Date) condition.getValue())), criteriaBuilder.literal("MM/dd/yyyy"))));
                    else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.equal(path, condition.getValue());
                    }

                } else if (QueryConditionModel.Operators.NOT_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.notEqual(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()));
                        //predicates.add(criteriaBuilder.notEqual(criteriaBuilder.function("TO_CHAR", String.class, root.get(field)), criteriaBuilder.function("TO_DATE", String.class, criteriaBuilder.parameter(String.class, condition.getValue().toString()), criteriaBuilder.literal("MM/dd/yyyy"))));
                    else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.notEqual(path, condition.getValue());
                    }

                } else if (QueryConditionModel.Operators.LIKE_FIRST.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(path, value + "%");
                        values.add(value + "%");
                    } else if (condition.getValue() instanceof Date) {
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), dateFormat.format((Date) condition.getValue()) + "%");
                    } else {

                        predicate = criteriaBuilder.like(path, newLikeValue + "%", '\\');

                        //predicates.add(criteriaBuilder.like(root.get(field),  (newLikeValue != null ? newLikeValue : condition.getValue()) + "%" + (newLikeValue != null ? " ESCAPE '\\'" : "")));


                    }
                } else if (QueryConditionModel.Operators.LIKE_LAST.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(path, "%" + value);
                        values.add("%" + value);
                    } else if (condition.getValue() instanceof Date) {
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()));
                    } else {
                        predicate = criteriaBuilder.like(path, "%" + newLikeValue, '\\');
                    }
                } else if (QueryConditionModel.Operators.LIKE_BOTH.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value);
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(path, "%" + value + "%");
                        values.add("%" + value + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.like(path, "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.LIKE_BOTH_INSENSITIVE.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString().toLowerCase();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value).toLowerCase();
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%");
                        values.add("%" + value + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.like(path, "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.NOT_LIKE_BOTH_INSENSITIVE.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    String value = condition.getValue().toString().toLowerCase();
                    String newLikeValue = null;
                    if (valueContainsLikeSpecialChars(value))
                        newLikeValue = getLikeValue(value).toLowerCase();
                    if (fieldMap != null && fieldMap.get(field).getType().getSimpleName().equals("String") && newLikeValue == null) {
                        predicate = criteriaBuilder.notLike(criteriaBuilder.lower(path), "%" + value + "%");
                        values.add("%" + value.toLowerCase() + "%");
                    } else if (condition.getValue() instanceof Date)
                        predicate = criteriaBuilder.notLike(criteriaBuilder.function("TO_CHAR", String.class, path, criteriaBuilder.literal("MM/dd/yyyy")), "%" + dateFormat.format((Date) condition.getValue()) + "%");
                    else {
                        predicate = criteriaBuilder.notLike(path, "%" + newLikeValue + "%", '\\');

                    }
                } else if (QueryConditionModel.Operators.LESS_THAN.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThan(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThan(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.lessThan(path, condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.LESS_THAN_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThanOrEqualTo(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        date = addDaysToDate(date, 1);
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.lessThanOrEqualTo(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.lessThanOrEqualTo(path, condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.GREATER_THAN.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") && condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThan(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        date = addDaysToDate(date, 1);
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThan(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.greaterThan(path, condition.getValue().toString());

                    }
                } else if (QueryConditionModel.Operators.GREATER_THAN_EQUAL.code().equals(operator)) {
                    if (condition.getValue() == null)
                        return null;
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || condition.getValue() instanceof Timestamp) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlTimestampFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(path, date);
                    } else if (condition.getValue() instanceof Date) {
                        date = removeTime((Date) condition.getValue());
                        values.add(sqlDateFormat.format(date).toString());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(path, date);
                    } else {
                        values.add(condition.getValue());
                        predicate = criteriaBuilder.greaterThanOrEqualTo(path, condition.getValue().toString());
                    }
                } else if (QueryConditionModel.Operators.IS_NULL.code().equals(operator)) {
                    predicate = criteriaBuilder.isNull(path);
                } else if (QueryConditionModel.Operators.IS_NOT_NULL.code().equals(operator)) {
                    predicate = criteriaBuilder.isNotNull(path);
                } else if (QueryConditionModel.Operators.IS_EMPTY.code().equals(operator)) {
//                    Path expression = root.get(field);
//                    predicate =criteriaBuilder.isEmpty(expression);
                    predicates.add(criteriaBuilder.isEmpty(path));
                    //predicate =criteriaBuilder.isEmpty(root.get(field));
                } else if (QueryConditionModel.Operators.IS_NOT_EMPTY.code().equals(operator)) {
                    predicate = criteriaBuilder.isNotEmpty(path);
                } else if (QueryConditionModel.Operators.BETWEEN.code().equals(operator) || QueryConditionModel.Operators.NOT_BETWEEN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.size() < 2 || searchValues.get(0) == null || searchValues.get(1) == null) {
                        logger.warn("Between criteria ignored because it does not have two variables to compare with");
                        return null;
                    }
                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || searchValues.get(0) instanceof Timestamp) {
                        obj1 = removeTime((Date) searchValues.get(0));
                        values.add(sqlTimestampFormat.format(searchValues.get(0)).toString());
                    } else if (searchValues.get(0) instanceof Date) {
                        obj1 = removeTime((Date) searchValues.get(0));
                        values.add(sqlDateFormat.format(searchValues.get(0)).toString());
                    } else {
                        obj1 = searchValues.get(0);
                        values.add(searchValues.get(0));
                    }

                    if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || searchValues.get(1) instanceof Timestamp) {
                        obj2 = getEndOfTheDay((Date) searchValues.get(1));
                        values.add(sqlTimestampFormat.format(obj2).toString());
                    } else if (searchValues.get(1) instanceof Date) {
                        obj2 = getEndOfTheDay((Date) searchValues.get(1));
                        values.add(sqlDateFormat.format(obj2).toString());
                    } else {
                        obj2 = searchValues.get(1);
                        values.add(searchValues.get(1));
                    }
                    if (QueryConditionModel.Operators.BETWEEN.code().equals(operator))
                        predicate = criteriaBuilder.between(path, (Date) obj1, (Date) obj2);
                    else
                        predicate = criteriaBuilder.not(criteriaBuilder.between(path, (Date) obj1, (Date) obj2));
                } else if (QueryConditionModel.Operators.IN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.isEmpty())
                        return null;

                    Expression<String> inExpression = path;
                    predicate = inExpression.in(searchValues.toArray());


                    for (Object value : searchValues)
                        if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || value instanceof Timestamp) {
                            values.add(sqlTimestampFormat.format(value).toString());
                        } else if (value instanceof Date)
                            values.add(sqlDateFormat.format(value).toString());
                        else values.add(value);
                } else if (QueryConditionModel.Operators.NOT_IN.code().equals(operator)) {
                    searchValues = condition.getValues();
                    if (searchValues == null || searchValues.isEmpty())
                        return null;

                    Expression<String> inExpression = path;
                    Predicate inPredicate = inExpression.in(searchValues.toArray());
                    predicate = criteriaBuilder.not(inPredicate);
                    for (Object value : searchValues)
                        if (fieldMap.get(field).getType().getSimpleName().equals("Timestamp") || value instanceof Timestamp) {
                            values.add(sqlTimestampFormat.format(value).toString());
                        } else if (value instanceof Date)
                            values.add(sqlDateFormat.format(value).toString());
                        else values.add(value);
                } else {
                    return null;
                }

                if (predicate != null) {
                    if (currentPredicate == null) {
                        currentPredicate = predicate;
                    } else if (condition.getCondition() != null && condition.getCondition().equals(QueryConditionModel.Conditions.OR.code())) {
                        currentPredicate = criteriaBuilder.or(currentPredicate, predicate);

                    } else {
                        currentPredicate = criteriaBuilder.and(currentPredicate, predicate);
                    }
                }
            }
            predicates.add(currentPredicate);

        }

        return predicates;
    }

}
