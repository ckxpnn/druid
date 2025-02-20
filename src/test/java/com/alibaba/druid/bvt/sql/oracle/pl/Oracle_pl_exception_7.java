/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.sql.oracle.pl;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class Oracle_pl_exception_7 extends OracleTest {
    public void test_0() throws Exception {
        String sql = "BEGIN\n" +
                " \n" +
                "  DECLARE\n" +
                "    credit_limit CONSTANT NUMBER(3) := 5000;\n" +
                "  BEGIN\n" +
                "    NULL;\n" +
                "  END;\n" +
                " \n" +
                "EXCEPTION\n" +
                "  WHEN VALUE_ERROR THEN\n" +
                "    DBMS_OUTPUT.PUT_LINE('Exception raised in declaration.');\n" +
                "END;"; //

        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.ORACLE);
        assertEquals(1, statementList.size());

        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.ORACLE);
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
        }

//        System.out.println("Tables : " + visitor.getTables());
//        System.out.println("fields : " + visitor.getColumns());
//        System.out.println("coditions : " + visitor.getConditions());
//        System.out.println("relationships : " + visitor.getRelationships());
//        System.out.println("orderBy : " + visitor.getOrderByColumns());

        assertEquals(0, visitor.getTables().size());

//        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
//        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("emp_name")));

//        Assert.assertEquals(7, visitor.getColumns().size());
//        Assert.assertEquals(3, visitor.getConditions().size());
//        Assert.assertEquals(1, visitor.getRelationships().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));

        {
            String output = SQLUtils.toSQLString(statementList, JdbcConstants.ORACLE);
            System.out.println(output);
            assertEquals("BEGIN\n" +
                            "\tDECLARE\n" +
                            "\t\tcredit_limit CONSTANT NUMBER(3) := 5000;\n" +
                            "\tBEGIN\n" +
                            "\t\tNULL;\n" +
                            "\tEND;\n" +
                            "EXCEPTION\n" +
                            "\tWHEN VALUE_ERROR THEN DBMS_OUTPUT.PUT_LINE('Exception raised in declaration.');\n" +
                            "END;", //
                    output);
        }
        {
            String output = SQLUtils.toSQLString(statementList, JdbcConstants.ORACLE, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
            assertEquals("begin\n" +
                            "\tdeclare\n" +
                            "\t\tcredit_limit constant NUMBER(3) := 5000;\n" +
                            "\tbegin\n" +
                            "\t\tnull;\n" +
                            "\tend;\n" +
                            "exception\n" +
                            "\twhen VALUE_ERROR then DBMS_OUTPUT.PUT_LINE('Exception raised in declaration.');\n" +
                            "end;", //
                    output);
        }
    }
}
