/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.expression;

import org.h2.constant.ErrorCode;
import org.h2.engine.Session;
import org.h2.message.DbException;
import org.h2.table.ColumnResolver;
import org.h2.table.TableFilter;
import org.h2.util.StringUtils;
import org.h2.value.Value;

/**
 * A wildcard expression as in SELECT * FROM TEST.
 * This object is only used temporarily during the parsing phase, and later
 * replaced by column expressions.
 */
//Wildcard表达式即*号表达式在org.h2.command.dml.Select.expandColumnList()中会被替换成字段列表
//所以此类很多方法是不能调用的，非法的
public class Wildcard extends Expression {
	//对于下面三条sql:
	//sql = "select * from WildcardTest";
	//sql = "select WildcardTest.* from WildcardTest";
	//sql = "select public.WildcardTest.* from WildcardTest";
	//字段schema、table分别是
	//null, null
	//null, WildcardTest
	//public, WildcardTest
    private final String schema;
    private final String table;

    public Wildcard(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    @Override
    public boolean isWildcard() {
        return true;
    }

    @Override
    public Value getValue(Session session) {
        throw DbException.throwInternalError();
    }

    @Override
    public int getType() {
        throw DbException.throwInternalError();
    }

    @Override
    public void mapColumns(ColumnResolver resolver, int level) {
        throw DbException.get(ErrorCode.SYNTAX_ERROR_1, table);
    }

    @Override
    public Expression optimize(Session session) {
        throw DbException.get(ErrorCode.SYNTAX_ERROR_1, table);
    }

    @Override
    public void setEvaluatable(TableFilter tableFilter, boolean b) {
        DbException.throwInternalError();
    }

    @Override
    public int getScale() {
        throw DbException.throwInternalError();
    }

    @Override
    public long getPrecision() {
        throw DbException.throwInternalError();
    }

    @Override
    public int getDisplaySize() {
        throw DbException.throwInternalError();
    }

    @Override
    public String getTableAlias() {
        return table;
    }

    @Override
    public String getSchemaName() {
        return schema;
    }
    
    //对于select WildcardTest.* from WildcardTest
    //此时table=WildcardTest
    @Override
    public String getSQL() {
        if (table == null) {
            return "*";
        }
        return StringUtils.quoteIdentifier(table) + ".*"; //此时返回: "WildcardTest".*
    }

    @Override
    public void updateAggregate(Session session) {
        DbException.throwInternalError();
    }

    @Override
    public boolean isEverything(ExpressionVisitor visitor) {
        throw DbException.throwInternalError();
    }

    @Override
    public int getCost() {
        throw DbException.throwInternalError();
    }

}
