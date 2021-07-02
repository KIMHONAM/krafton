package com.krafton.intra.pto.repository.mapper;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

//ProviderMethodResolver - MyBatis 3.5.1 or later
public class PTOSqlProvider implements ProviderMethodResolver {

    public String getUserPtoInfoById(final int id) {
        return new SQL(){{
            SELECT("a.id, a.department_code, a.hire_date, a.name, a.employee_number");
            SELECT("b.department_name, c.role_name as position");
            FROM("employee a");
            INNER_JOIN("department b on a.department_code = b.department_code");
            INNER_JOIN("employee_role c on a.position_id = c.id");
            WHERE("a.id =  #{id}");
        }}.toString();
    }

}