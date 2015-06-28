package com.bplow.look.bass.dao.usertype;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bplow.look.bass.ServiceAccessException;
import com.bplow.look.bass.dao.IHibernatePrepareSQLQueryCallback;
import com.bplow.look.bass.utils.ConvertUtils;
@Deprecated
public class SQLEntity
    implements Serializable
{

    public SQLEntity()
    {
        sql = new StringBuffer();
        params = new ArrayList();
        paramTypeMap = new LinkedHashMap();
    }

    public SQLEntity(String sqlp, Collection paramsp)
    {
        this(sqlp, paramsp, ((Collection) (null)));
    }

    public SQLEntity(String sqlp, Collection paramsp, Collection paramTypes)
    {
        sql = new StringBuffer();
        params = new ArrayList();
        paramTypeMap = new LinkedHashMap();
        if(sqlp != null)
            append(sqlp);
        if(paramsp != null)
            if(paramTypes != null)
                addParams(paramsp, paramTypes);
            else
                addParams(paramsp);
    }

    public SQLEntity(String sqlp, Object paramsp[])
    {
        this(sqlp, paramsp, ((int []) (null)));
    }

    public SQLEntity(String sqlp, Object paramsp[], int paramTypes[])
    {
        sql = new StringBuffer();
        params = new ArrayList();
        paramTypeMap = new LinkedHashMap();
        if(sqlp != null)
            append(sqlp);
        if(paramsp != null)
            if(paramTypes != null)
                addParams(paramsp, paramTypes);
            else
                addParams(paramsp);
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public SQLEntity append(String sqlp)
    {
        sql.append(sqlp);
        return this;
    }

    public SQLEntity append(String sqlp, Object param)
    {
        return append(sqlp).addParam(param);
    }

    public SQLEntity append(String sqlp, Object param, int paramType)
    {
        return append(sqlp).addParam(param, paramType);
    }

    public SQLEntity append(String sqlp, Object paramsp[])
    {
        return append(sqlp).addParams(paramsp);
    }

    public SQLEntity append(String sqlp, Object paramsp[], int paramTypes[])
    {
        return append(sqlp).addParams(paramsp, paramTypes);
    }

    public boolean hasWhere()
    {
        return sql.toString().toLowerCase().indexOf(" where ") != -1;
    }

    public SQLEntity appendWhereAnd(String sqlp)
    {
        if(!hasWhere())
            sql.append((new StringBuilder(" where ")).append(sqlp).toString());
        else
            sql.append((new StringBuilder(" and ")).append(sqlp).toString());
        return this;
    }

    public SQLEntity appendWhereOr(String sqlp)
    {
        if(!hasWhere())
            sql.append((new StringBuilder(" where ")).append(sqlp).toString());
        else
            sql.append((new StringBuilder(" or ")).append(sqlp).toString());
        return this;
    }

    private boolean paramIsExist(Object param)
    {
        if(param == null)
            return false;
        if(param instanceof String)
        {
            if(StringUtils.isEmpty((String)param))
                return false;
        } else
        if((param instanceof Number) && ConvertUtils.toDouble(param) == 0.0D)
            return false;
        return true;
    }

    public SQLEntity appendIfExist(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return append(sqlp, param);
    }

    public SQLEntity appendIfExistLeftMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return append(sqlp, (new StringBuilder()).append(param).append("%").toString());
    }

    public SQLEntity appendIfExistRightMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return append(sqlp, (new StringBuilder("%")).append(param).toString());
    }

    public SQLEntity appendIfExistBothMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return append(sqlp, (new StringBuilder("%")).append(param).append("%").toString());
    }

    private boolean paramHaveMatching(Object param)
    {
        if(param instanceof String)
        {
            String str = (String)param;
            if(str.startsWith("%") && str.length() == 1)
                return true;
            if(str.endsWith("%") && str.length() == 1)
                return true;
            if(str.startsWith("%") && str.endsWith("%") && str.length() == 2)
                return true;
        }
        return false;
    }

    public SQLEntity appendIfExistIgnoreMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        if(paramHaveMatching(param))
            return this;
        else
            return append(sqlp, param);
    }

    public SQLEntity appendWhereAnd(String sqlp, Object param)
    {
        return appendWhereAnd(sqlp).addParam(param);
    }

    public SQLEntity appendWhereAndIfExist(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereAnd(sqlp, param);
    }

    public SQLEntity appendWhereAndIfExistLeftMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereAnd(sqlp, (new StringBuilder()).append(param).append("%").toString());
    }

    public SQLEntity appendWhereAndIfExistRightMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereAnd(sqlp, (new StringBuilder("%")).append(param).toString());
    }

    public SQLEntity appendWhereAndIfExistBothMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereAnd(sqlp, (new StringBuilder("%")).append(param).append("%").toString());
    }

    public SQLEntity appendWhereAndIfExistIgnoreMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        if(paramHaveMatching(param))
            return this;
        else
            return appendWhereAnd(sqlp, param);
    }

    public SQLEntity appendWhereOr(String sqlp, Object param)
    {
        return appendWhereOr(sqlp).addParam(param);
    }

    public SQLEntity appendWhereOrIfExist(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereOr(sqlp, param);
    }

    public SQLEntity appendWhereOrIfExistLeftMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereOr(sqlp, (new StringBuilder()).append(param).append("%").toString());
    }

    public SQLEntity appendWhereOrIfExistRightMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereOr(sqlp, (new StringBuilder("%")).append(param).toString());
    }

    public SQLEntity appendWhereOrIfExistBothMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        else
            return appendWhereOr(sqlp, (new StringBuilder("%")).append(param).append("%").toString());
    }

    public SQLEntity appendWhereOrIfExistIgnoreMatching(String sqlp, Object param)
    {
        if(!paramIsExist(param))
            return this;
        if(paramHaveMatching(param))
            return this;
        else
            return appendWhereOr(sqlp, param);
    }

    public SQLEntity appendWhereAnd(String sqlp, Object paramsp[])
    {
        return appendWhereAnd(sqlp).addParams(paramsp);
    }

    public SQLEntity appendWhereOr(String sqlp, Object paramsp[])
    {
        return appendWhereOr(sqlp).addParams(paramsp);
    }

    public SQLEntity append(String sqlp, Collection paramsp)
    {
        return append(sqlp).addParams(paramsp);
    }

    public SQLEntity appendWhereAnd(String sqlp, Collection paramsp)
    {
        return appendWhereAnd(sqlp).addParams(paramsp);
    }

    public SQLEntity appendWhereOr(String sqlp, Collection paramsp)
    {
        return appendWhereOr(sqlp).addParams(paramsp);
    }

    public SQLEntity addParam(Object param)
    {
        params.add(param);
        return this;
    }

    public SQLEntity addParam(Object param, int paramType)
    {
        params.add(param);
        paramTypeMap.put(Integer.valueOf(params.indexOf(param)), Integer.valueOf(paramType));
        return this;
    }

    public SQLEntity addParams(Collection paramsp)
    {
        params.addAll(paramsp);
        return this;
    }

    public SQLEntity addParams(Collection paramsp, Collection paramTypes)
    {
        if(paramsp.size() != paramTypes.size())
            throw new ServiceAccessException("Params count not equals types count.");
        Iterator tIt = paramTypes.iterator();
        for(Iterator it = paramsp.iterator(); it.hasNext(); addParam(it.next(), ((Integer)tIt.next()).intValue()));
        return this;
    }

    public SQLEntity addParams(Object paramsp[])
    {
        for(int i = 0; i < paramsp.length; i++)
            params.add(paramsp[i]);

        return this;
    }

    public SQLEntity addParams(Object paramsp[], int paramTypes[])
    {
        if(paramsp.length != paramTypes.length)
            throw new ServiceAccessException("Params count not equals types count.");
        for(int i = 0; i < paramsp.length; i++)
            addParam(paramsp[i], paramTypes[i]);

        return this;
    }

    public SQLEntity addInParams(Collection paramsp)
    {
        return addParams(paramsp).addLikeSymbol(paramsp.size());
    }

    public SQLEntity addInParams(Collection paramsp, Collection paramTypes)
    {
        return addParams(paramsp, paramTypes).addLikeSymbol(paramsp.size());
    }

    public SQLEntity addInParams(Object paramsp[])
    {
        return addParams(paramsp).addLikeSymbol(paramsp.length);
    }

    public SQLEntity addInParams(Object paramsp[], int paramTypes[])
    {
        return addParams(paramsp, paramTypes).addLikeSymbol(paramsp.length);
    }

    private SQLEntity addLikeSymbol(int count)
    {
        for(int i = 0; i < count; i++)
        {
            sql.append("?");
            if(i < count - 1)
                sql.append(", ");
        }

        return this;
    }

    public SQLEntity addParamType(int type)
    {
        return addParamType(paramTypeMap.size(), type);
    }

    public SQLEntity addParamType(int index, int type)
    {
        paramTypeMap.put(Integer.valueOf(index), Integer.valueOf(type));
        return this;
    }

    public String parseSql()
    {
        return sql.toString();
    }

    public String parseSql(String dbSymbol, String dateFormatString)
    {
        StringBuffer parseSql = new StringBuffer();
        int paramCount = 0;
        for(int i = 0; i < sql.length(); i++)
        {
            char c = sql.charAt(i);
            if(c == '?')
            {
                Object param = params.get(paramCount++);
                if(param != null)
                {
                    parseSql.append(dbSymbol);
                    String paramString = null;
                    if(param instanceof Date)
                    {
                        if(dateFormatString != null)
                            paramString = ConvertUtils.toString((Date)param, dateFormatString);
                        else
                            paramString = ConvertUtils.toString((Date)param);
                    } else
                    if(param instanceof Calendar)
                    {
                        if(dateFormatString != null)
                            paramString = ConvertUtils.toString((Calendar)param, dateFormatString);
                        else
                            paramString = ConvertUtils.toString((Calendar)param);
                    } else
                    {
                        paramString = param.toString();
                    }
                    parseSql.append(paramString);
                    parseSql.append(dbSymbol);
                } else
                {
                    parseSql.append("null");
                }
            } else
            {
                parseSql.append(c);
            }
        }

        return parseSql.toString();
    }

    public Object[] parseParams()
    {
        if(params.isEmpty())
            return null;
        Object ps[] = new Object[params.size()];
        for(int i = 0; i < ps.length; i++)
            ps[i] = params.get(i);

        return ps;
    }

    public int[] parseParamsTypes()
    {
        if(params.isEmpty())
            return null;
        int argTypes[] = new int[params.size()];
        for(int i = 0; i < argTypes.length; i++)
        {
            Integer argType = (Integer)paramTypeMap.get(Integer.valueOf(i));
            if(argType == null)
                argTypes[i] = -2147483648;
            else
                argTypes[i] = argType.intValue();
        }

        return argTypes;
    }

    public String paramsToString()
    {
        StringBuffer paramsString = new StringBuffer();
        if(params.isEmpty())
            paramsString.append("null");
        for(int i = 0; i < params.size(); i++)
        {
            paramsString.append("'").append(params.get(i)).append("'");
            if(i < params.size() - 1)
                paramsString.append(", ");
        }

        return paramsString.toString();
    }

    public void clear()
    {
        clearSql();
        clearParams();
        clearParamTypes();
        prepareSQLQueryCallback = null;
    }

    public void clearSql()
    {
        sql.delete(0, sql.length());
    }

    public void clearParams()
    {
        params.clear();
    }

    public void clearParamTypes()
    {
        paramTypeMap.clear();
    }

    public int paramsLength()
    {
        return params.size();
    }

    public List getParams()
    {
        return params;
    }

    public void setParams(List params)
    {
        this.params = params;
    }

    public StringBuffer getSql()
    {
        return sql;
    }

    public void setSql(StringBuffer sql)
    {
        this.sql = sql;
    }

    public int sqlLength()
    {
        return sql.length();
    }

    public SQLEntity sqlDeleteEnd(int count)
    {
        sqlDelete(sql.length() - count, sql.length());
        return this;
    }

    public SQLEntity sqlDeleteStart(int count)
    {
        sqlDelete(0, count);
        return this;
    }

    public SQLEntity sqlDelete(int start, int end)
    {
        sql.delete(start, end);
        return this;
    }

    public boolean sqlEndsWith(String suffix)
    {
        return sql.toString().endsWith(suffix);
    }

    public boolean sqlStartsWith(String suffix)
    {
        return sql.toString().startsWith(suffix);
    }

    public SQLEntity cloneMe()
    {
        SQLEntity sqlEntity = new SQLEntity();
        sqlEntity.type = type;
        sqlEntity.sql = new StringBuffer(sql.toString());
        sqlEntity.params = new ArrayList(params);
        sqlEntity.paramTypeMap = new LinkedHashMap(paramTypeMap);
        sqlEntity.prepareSQLQueryCallback = prepareSQLQueryCallback;
        return sqlEntity;
    }

    public String toString()
    {
        return parseSql("'", "yyyy-MM-dd HH:mm:ss");
    }

    public IHibernatePrepareSQLQueryCallback getPrepareSQLQueryCallback()
    {
        return prepareSQLQueryCallback;
    }

    public void setPrepareSQLQueryCallback(IHibernatePrepareSQLQueryCallback prepareSQLQueryCallback)
    {
        this.prepareSQLQueryCallback = prepareSQLQueryCallback;
    }

    //private static final Internationalization i18n = null;
    public static final long serialVersionUID = 20071022L;
    private static final String LIKE_SYMBOL = "%";
    private static final String WHERE_KEYWORD = " where ";
    private static final String AND_KEYWORD = " and ";
    private static final String OR_KEYWORD = " or ";
    private String type;
    private StringBuffer sql;
    private List params;
    private Map paramTypeMap;
    private transient IHibernatePrepareSQLQueryCallback prepareSQLQueryCallback;

}
