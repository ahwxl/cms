package com.bplow.base.dbsource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 描述: 动态数据代理路由器
 * @author 韩冬
 *
 */
public class DbSourceMap extends AbstractRoutingDataSource {

	

	@Override
	protected Object determineCurrentLookupKey() {
		return DbSourceHolder.getDbSource();
	}

}
