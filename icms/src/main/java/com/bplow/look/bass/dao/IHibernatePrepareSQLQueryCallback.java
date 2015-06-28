package com.bplow.look.bass.dao;

import org.hibernate.SQLQuery;

public interface IHibernatePrepareSQLQueryCallback {
	public abstract void doInPrepareSQLQuery(SQLQuery sqlquery);
}
