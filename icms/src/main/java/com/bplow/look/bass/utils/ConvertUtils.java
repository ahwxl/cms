package com.bplow.look.bass.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

public abstract class ConvertUtils
{

    public ConvertUtils()
    {
    }

    public static byte toByte(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Byte)
            return ((Byte)object).byteValue();
        else
            return Byte.parseByte(object.toString());
    }

    public static Integer[] toInteger(Object objects[])
    {
        if(objects == null)
            return null;
        Integer intObjects[] = new Integer[objects.length];
        for(int i = 0; i < intObjects.length; i++)
            intObjects[i] = toInteger(objects[i]);

        return intObjects;
    }

    public static Integer toInteger(Object object)
    {
        if(object == null)
            return null;
        else
            return Integer.valueOf(toInt(object));
    }

    public static int[] toInt(Object objects[])
    {
        if(objects == null)
            return null;
        int intObjects[] = new int[objects.length];
        for(int i = 0; i < intObjects.length; i++)
            intObjects[i] = toInt(objects[i]);

        return intObjects;
    }

    public static int toInt(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Integer)
            return ((Integer)object).intValue();
        if(object instanceof Short)
            return ((Short)object).intValue();
        if(object instanceof Double)
            return ((Double)object).intValue();
        if(object instanceof Long)
            return ((Long)object).intValue();
        if(object instanceof Float)
            return ((Float)object).intValue();
        if(object instanceof Number)
            return ((Number)object).intValue();
        else
            return Integer.parseInt(object.toString());
    }

    public static int toInt(Object object, int defaultValue)
    {
        try
        {
            return toInt(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static short toShort(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Short)
            return ((Short)object).shortValue();
        if(object instanceof Integer)
            return ((Integer)object).shortValue();
        if(object instanceof Double)
            return ((Double)object).shortValue();
        if(object instanceof Long)
            return ((Long)object).shortValue();
        if(object instanceof Float)
            return ((Float)object).shortValue();
        if(object instanceof Number)
            return ((Number)object).shortValue();
        else
            return Short.parseShort(object.toString());
    }

    public static short toShort(Object object, short defaultValue)
    {
        try
        {
            return toShort(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static float toFloat(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Float)
            return ((Float)object).floatValue();
        if(object instanceof Short)
            return ((Short)object).floatValue();
        if(object instanceof Integer)
            return ((Integer)object).floatValue();
        if(object instanceof Long)
            return ((Long)object).floatValue();
        if(object instanceof Double)
            return ((Double)object).floatValue();
        if(object instanceof Number)
            return ((Number)object).floatValue();
        else
            return Float.parseFloat(object.toString());
    }

    public static float toFloat(Object object, float defaultValue)
    {
        try
        {
            return toFloat(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static double toDouble(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Double)
            return ((Double)object).doubleValue();
        if(object instanceof Short)
            return ((Short)object).doubleValue();
        if(object instanceof Integer)
            return ((Integer)object).doubleValue();
        if(object instanceof Long)
            return ((Long)object).doubleValue();
        if(object instanceof Float)
            return ((Float)object).doubleValue();
        if(object instanceof Number)
            return ((Number)object).doubleValue();
        else
            return Double.parseDouble(object.toString());
    }

    public static double toDouble(Object object, double defaultValue)
    {
        try
        {
            return toDouble(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static double[] toDouble(Object doubleArray[])
    {
        if(doubleArray == null)
            return null;
        double returnDoubleArray[] = new double[doubleArray.length];
        for(int i = 0; i < returnDoubleArray.length; i++)
            returnDoubleArray[i] = toDouble(doubleArray[i]);

        return returnDoubleArray;
    }

    public static double[] toDouble(Object doubleArray[], double defaultValue[])
    {
        try
        {
            return toDouble(doubleArray);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static Double toDoubleObject(Object object)
    {
        return Double.valueOf(toDouble(object));
    }

    public static Double toDoubleObject(Object object, Double defaultValue)
    {
        try
        {
            return toDoubleObject(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static Double[] toDoubleObject(Object doubleArray[])
    {
        if(doubleArray == null)
            return null;
        Double returnDoubleArray[] = new Double[doubleArray.length];
        for(int i = 0; i < returnDoubleArray.length; i++)
            returnDoubleArray[i] = toDoubleObject(doubleArray[i]);

        return returnDoubleArray;
    }

    public static Double[] toDoubleObject(Object doubleArray[], Double defaultValues[])
    {
        try
        {
            return toDoubleObject(doubleArray);
        }
        catch(Throwable e)
        {
            return defaultValues;
        }
    }

    public static java.util.Date toDate(Object object)
    {
        if(object == null)
            return null;
        if(object instanceof Date)
        {
            java.util.Date date = new java.util.Date();
            date.setTime(((Date)object).getTime());
            return date;
        }
        if(object instanceof Timestamp)
        {
            java.util.Date date = new java.util.Date();
            date.setTime(((Timestamp)object).getTime());
            return date;
        }
        if(object instanceof java.util.Date)
        {
            java.util.Date date = new java.util.Date();
            date.setTime(((java.util.Date)object).getTime());
            return date;
        }
        if(object instanceof Calendar)
            return ((Calendar)object).getTime();
        if(object instanceof Number)
            return new java.util.Date(toLong(object));
        try
        {
            return getDateFormat(object.toString()).parse(object.toString());
        }
        catch(ParseException pe)
        {
            log.error((new StringBuilder("threw in toDate(")).append(object).append(")").toString(), pe);
        }
        return null;
    }

    public static Date toSqlDate(Object object)
    {
        if(object == null)
            return null;
        if(object instanceof Date)
            return (Date)object;
        if(object instanceof Timestamp)
            return new Date(((Timestamp)object).getTime());
        if(object instanceof java.util.Date)
            return new Date(((java.util.Date)object).getTime());
        if(object instanceof Calendar)
            return new Date(((Calendar)object).getTimeInMillis());
        if(object instanceof Number)
            return new Date(toLong(object));
        try
        {
            return new Date(getDateFormat(object.toString()).parse(object.toString()).getTime());
        }
        catch(ParseException pe)
        {
            log.error((new StringBuilder("threw in toDate(")).append(object).append(")").toString(), pe);
        }
        return null;
    }

    public static Timestamp toTimestamp(Calendar cal)
    {
        if(cal == null)
            return null;
        else
            return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp toTimestamp(String dateStr)
    {
        if(dateStr == null)
            return null;
        else
            return new Timestamp(toDate(dateStr).getTime());
    }

    public static Timestamp toTimestamp(java.util.Date date)
    {
        if(date == null)
            return null;
        else
            return new Timestamp(date.getTime());
    }

    public static DateFormat getDefineDateFormat(String dateStr)
    {
        if("yyyy-MM-dd".equals(dateStr))
            return new SimpleDateFormat("yyyy-MM-dd");
        if("HH:mm:ss".equals(dateStr))
            return new SimpleDateFormat("HH:mm:ss");
        if("yyyy-MM-dd HH:mm:ss".equals(dateStr))
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        else
            return null;
    }

    public static DateFormat getDateFormat(String dateStr)
    {
        int pos1 = dateStr.indexOf("-");
        int pos2 = dateStr.indexOf(":");
        DateFormat dt = null;
        if(pos1 != -1 && pos2 != -1)
            dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        else
        if(pos1 != -1 && pos2 == -1)
            dt = new SimpleDateFormat("yyyy-MM-dd");
        else
        if(pos1 == -1 && pos2 != -1)
            dt = new SimpleDateFormat("HH:mm:ss");
        else
        if(dateStr.length() == "yyyyMM".length())
            dt = new SimpleDateFormat("yyyyMM");
        else
        if(dateStr.length() == "yyyyMMdd".length())
            dt = new SimpleDateFormat("yyyyMMdd");
        else
            dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt;
    }

    public static String getDateFormatStr(String dateStr)
    {
        int pos1 = dateStr.indexOf("-");
        int pos2 = dateStr.indexOf(":");
        String dt = null;
        if(pos1 != -1 && pos2 != -1)
            dt = "yyyy-MM-dd HH:mm:ss";
        else
        if(pos1 != -1 && pos2 == -1)
            dt = "yyyy-MM-dd";
        else
        if(pos1 == -1 && pos2 != -1)
            dt = "HH:mm:ss";
        else
        if(dateStr.length() == "yyyyMM".length())
            dt = "yyyyMM";
        else
            dt = "yyyyMMdd";
        return dt;
    }

    public static Calendar toCalendar(Object obj)
    {
        if(obj == null)
            return null;
        if(obj instanceof Calendar)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(((Calendar)obj).getTimeInMillis());
            return cal;
        }
        if(obj instanceof java.util.Date)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(((java.util.Date)obj).getTime());
            return cal;
        }
        if(obj instanceof Number)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(toLong(obj));
            return cal;
        }
        try
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateFormat(obj.toString()).parse(obj.toString()));
            return cal;
        }
        catch(ParseException pe)
        {
            log.error(null, pe);
        }
        return null;
    }

    public static Boolean toBoolean(Object object)
    {
        if(object == null)
        {
            Boolean obj = null;
            return obj;
        } else
        {
            return Boolean.valueOf(toBool(object));
        }
    }

    public static boolean toBool(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Boolean)
            return ((Boolean)object).booleanValue();
        else
            return Boolean.parseBoolean(object.toString());
    }

    public static boolean toBool(Object object, boolean defaultValue)
    {
        try
        {
            return toBool(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static boolean toBool(int object)
    {
        return object == 1;
    }

    public static boolean toCustomizeBool(Object object)
    {
        return toCustomizeBool(object, false);
    }

    public static boolean toCustomizeBool(Object object, boolean defaultValue)
    {
        if(object == null)
            return defaultValue;
        String s = object.toString().trim();
        if(s.length() == 0)
            return defaultValue;
        char c = s.charAt(0);
        if(c == 'n' || c == 'N' || c == 'f' || c == 'F')
            return false;
        if(c == 'y' || c == 'Y' || c == 't' || c == 'T')
            return true;
        try
        {
            return Integer.valueOf(s).intValue() != 0;
        }
        catch(Exception ex)
        {
            return defaultValue;
        }
    }

    public static long toLong(Object object)
    {
        Assert.notNull(object);
        if(object instanceof Long)
            return ((Long)object).longValue();
        if(object instanceof Double)
            return ((Double)object).longValue();
        if(object instanceof Short)
            return ((Short)object).longValue();
        if(object instanceof Integer)
            return ((Integer)object).longValue();
        if(object instanceof Float)
            return ((Float)object).longValue();
        if(object instanceof Number)
            return ((Number)object).longValue();
        else
            return Long.parseLong(object.toString());
    }

    public static long toLong(Object object, long defaultValue)
    {
        try
        {
            return toLong(object);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static String toString(java.util.Date date)
    {
        return toString(date, "yyyy-MM-dd");
    }

    public static String toString(java.util.Date date, String dateFormatString)
    {
        Assert.notNull(dateFormatString, "Date format string is required");
        return toString(date, dateFormatString, "");
    }

    public static String toString(java.util.Date date, String dateFormatString, String defaultValue)
    {
        if(date == null)
            return defaultValue;
        try
        {
            return (new SimpleDateFormat(dateFormatString)).format(date);
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static String toString(Calendar cal)
    {
        return toString(cal, "yyyy-MM-dd");
    }

    public static String toString(Calendar cal, String dateFormatString)
    {
        Assert.notNull(dateFormatString, "Date format string is required");
        return toString(cal, dateFormatString, "");
    }

    public static String toString(Calendar cal, String dateFormatString, String defaultValue)
    {
        if(cal == null)
            return defaultValue;
        try
        {
            return (new SimpleDateFormat(dateFormatString)).format(cal.getTime());
        }
        catch(Throwable e)
        {
            return defaultValue;
        }
    }

    public static String toString(int i)
    {
        return Integer.toString(i);
    }

    public static String toString(int i, String formatString)
    {
        Assert.notNull(formatString, "Format string is required");
        return (new DecimalFormat(formatString)).format(i);
    }

    public static String toString(short s)
    {
        return Short.toString(s);
    }

    public static String toString(long l)
    {
        return Long.toString(l);
    }

    public static String toString(long l, String formatString)
    {
        Assert.notNull(formatString, "Format string is required");
        return (new DecimalFormat(formatString)).format(l);
    }

    public static String toString(float f)
    {
        return Float.toString(f);
    }

    public static String toString(float f, String formatString)
    {
        Assert.notNull(formatString, "Format string is required");
        return (new DecimalFormat(formatString)).format(f);
    }

    public static String toString(double d)
    {
        return Double.toString(d);
    }

    public static String toString(double d, String formatString)
    {
        Assert.notNull(formatString, "Format string is required");
        return (new DecimalFormat(formatString)).format(d);
    }

    public static String toString(boolean b)
    {
        return Boolean.toString(b);
    }

    public static String toString(Object object)
    {
        return toString(object, null);
    }

    public static String toString(Object object, String formatString)
    {
        if(object == null)
            return "";
        if(object instanceof String)
            return (String)object;
        if(object instanceof Integer)
            if(formatString == null)
                return ((Integer)object).toString();
            else
                return toString(toInt(object), formatString);
        if(object instanceof Short)
            if(formatString == null)
                return ((Short)object).toString();
            else
                return toString(toShort(object), formatString);
        if(object instanceof Double)
            if(formatString == null)
                return ((Double)object).toString();
            else
                return toString(toDouble(object), formatString);
        if(object instanceof Long)
            if(formatString == null)
                return ((Long)object).toString();
            else
                return toString(toLong(object), formatString);
        if(object instanceof Float)
            if(formatString == null)
                return ((Float)object).toString();
            else
                return toString(toFloat(object), formatString);
        if(object instanceof Number)
            if(formatString == null)
                return ((Number)object).toString();
            else
                return toString(toDouble(object), formatString);
        if(object instanceof Calendar)
            if(formatString == null)
                return toString((Calendar)object);
            else
                return toString((Calendar)object, formatString);
        if(object instanceof java.util.Date)
            if(formatString == null)
                return toString((java.util.Date)object);
            else
                return toString((java.util.Date)object, formatString);
        if(object instanceof Collection)
            return (new StringBuilder("[")).append(toString((Collection)object, ", ")).append("]").toString();
        if(object instanceof Map)
            return (new StringBuilder("[")).append(toString((Map)object, "=", ", ")).append("]").toString();
        if(object.getClass().isArray())
            return (new StringBuilder("[")).append(toString((Object[])object, ", ")).append("]").toString();
        else
            return String.valueOf(object);
    }

    public static String toString(Collection collection, String token)
    {
        if(collection == null || collection.isEmpty())
            return null;
        Assert.notNull(token, "token is required; it must not be null");
        StringBuffer sb = new StringBuffer();
        Object object;
        for(Iterator it = collection.iterator(); it.hasNext(); sb.append(object != null ? object.toString() : null).append(token))
            object = it.next();

        if(sb.length() - token.length() >= 0)
            sb.delete(sb.length() - token.length(), sb.length());
        return sb.toString();
    }

    public static String toString(Map map, String keyValueToken, String token)
    {
        if(map == null || map.isEmpty())
            return null;
        Assert.notNull(token, "token is required; it must not be null");
        Assert.notNull(keyValueToken, "keyValueToken is required; it must not be null");
        StringBuffer sb = new StringBuffer();
        String key;
        String value;
        for(Iterator it = map.entrySet().iterator(); it.hasNext(); sb.append(key).append(keyValueToken).append(value).append(token))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
            key = entry.getKey() != null ? entry.getKey().toString() : null;
            value = entry.getValue() != null ? entry.getValue().toString() : null;
        }

        if(sb.length() - token.length() >= 0)
            sb.delete(sb.length() - token.length(), sb.length());
        return sb.toString();
    }

    public static String toString(Object array[], String split)
    {
        if(array == null)
            return null;
        Assert.notNull(split, "split is required; it must not be null");
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < array.length; i++)
            sb.append(array[i] != null ? array[i].toString() : null).append(split);

        if(sb.length() - split.length() >= 0)
            sb.delete(sb.length() - split.length(), sb.length());
        return sb.toString();
    }

    public static Object toJavaObject(int sqlType, Object value)
    {
        switch(sqlType)
        {
        case 4: // '\004'
            return toInteger(value);

        case 5: // '\005'
            return Integer.valueOf(toInt(value));

        case -5: 
            return Integer.valueOf(toInt(value));

        case -6: 
            return Integer.valueOf(toInt(value));

        case 8: // '\b'
            return Double.valueOf(toDouble(value));

        case 3: // '\003'
            return Integer.valueOf(toInt(value));

        case 2: // '\002'
            return Integer.valueOf(toInt(value));

        case 6: // '\006'
            return Float.valueOf(toFloat(value));

        case 91: // '['
            return toDate(value);

        case 92: // '\\'
            return toCalendar(value);

        case 93: // ']'
            return toCalendar(value);

        case 1: // '\001'
            return value != null ? value.toString() : null;

        case 12: // '\f'
            return value != null ? value.toString() : null;
        }
        log.warn((new StringBuilder("sqlType [")).append(sqlType).append("] not find java type mapping, setting to String...").toString());
        return value != null ? value.toString() : null;
    }

    public static Properties toProperties(String props, String splitRegex, String regex)
    {
        Properties properties = new Properties();
        if(props == null)
            return properties;
        String nameAndValues[] = StringUtils.split(props, splitRegex);
        String as[];
        int j = (as = nameAndValues).length;
        for(int i = 0; i < j; i++)
        {
            String nameAndValue = as[i];
            int index = nameAndValue.indexOf(regex);
            if(index != -1)
                properties.put(nameAndValue.substring(0, index), nameAndValue.substring(index + regex.length(), nameAndValue.length()));
        }

        return properties;
    }

//    public static Properties toProperties(Resource resources[])
//    {
//        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
//        pfb.setLocations(resources);
//        try
//        {
//            pfb.afterPropertiesSet();
//            return (Properties)pfb.getObject();
//        }
//        catch(IOException e)
//        {
//            //throws new Exception("");
//        	//throws new THRO;
//        }
//    }

    public static Map toMap(Properties properties)
    {
        Map map = new LinkedHashMap();
        if(properties != null)
        {
            java.util.Map.Entry entry;
            for(Iterator iterator = properties.entrySet().iterator(); iterator.hasNext(); map.put(entry.getKey(), entry.getValue()))
                entry = (java.util.Map.Entry)iterator.next();

        }
        return map;
    }

    public static Resource[] toResource(String props, String regex)
    {
        if(props == null)
            return new Resource[0];
        String resourceStringArray[] = StringUtils.split(props, regex);
        Resource resourceArray[] = new Resource[0];
        String as[];
        int j = (as = resourceStringArray).length;
        for(int i = 0; i < j; i++)
        {
            String resourceString = as[i];
            Resource resource = null;
            if(resourceString.startsWith("/WEB-INF/"))
            {
                String webroot = "";//BIPropertiesBean.getProperty("webAppRootKey");
                String path = (new StringBuilder(String.valueOf(webroot))).append(File.separator).append(resourceString).toString();
                File location = new File(path);
                if(location.exists())
                    resource = new FileSystemResource(location);
            }
            if(resource == null)
                try
                {
                    File file = ResourceUtils.getFile(resourceString);
                    resource = new FileSystemResource(file);
                }
                catch(FileNotFoundException fe)
                {
                   // throw new RollbackException("Resource not found.", fe);
                }
            if(resource != null)
                resourceArray = (Resource[])ArrayUtils.add(resourceArray, resource);
        }

        return resourceArray;
    }

//    public static String toXmlData(Object object)
//    {
//        StringWriter writer = new StringWriter();
//        try
//        {
//            Marshaller.marshal(object, writer);
//            return writer.toString();
//        }
//        catch(MarshalException e)
//        {
//            throw new ServiceAccessException("Object convert to xml failed.", e);
//        }
//        catch(ValidationException e)
//        {
//            throw new ServiceAccessException("Object convert to xml failed.", e);
//        }
//    }

//    public static Object toObject(Class clazz, String xmlData)
//    {
//        StringReader reader = new StringReader(xmlData);
//        try
//        {
//            return Unmarshaller.unmarshal(clazz, reader);
//        }
//        catch(MarshalException e)
//        {
//            //throw new ServiceAccessException("Xml convert to object failed.", e);
//        }
//        catch(ValidationException e)
//        {
//            //throw new ServiceAccessException("Xml convert to object failed.", e);
//        }
//    }

    public static Color toAWTColor(String htmlColor)
    {
        String c = htmlColor.startsWith("#") ? StringUtils.substringAfter(htmlColor, "#") : htmlColor;
        return new Color(Integer.parseInt(c, 16));
    }

    public static Color toAWTColor(String htmlColor, Color defaultColor)
    {
        try
        {
            return toAWTColor(htmlColor);
        }
        catch(Exception e)
        {
            return defaultColor;
        }
    }

    private static final Logger log = Logger.getLogger(ConvertUtils.class);
    private static final String WEB_INF = "/WEB-INF/";
    public static final String DATE_FMT = "yyyy-MM-dd";
    public static final String TIME_FMT = "HH:mm:ss";
    public static final String DT_FMT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_FMT = "yyyy-MM-dd";

}
