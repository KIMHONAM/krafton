package com.krafton.intra.pto.repository.mapper;

import com.krafton.intra.employee.dto.EmployeeRequest;
import org.apache.ibatis.annotations.*;
import org.json.JSONObject;

@Mapper
public interface PTOMapper {

    @Select("select * from employee where 1 = #{id}")
    EmployeeRequest.EmployeeDto findById(@Param("id") int id);

    @SelectProvider(type= PTOSqlProvider.class, method = "getUserPtoInfoById")
    EmployeeRequest.EmployeeDto getUserPtoInfoById(@Param("id") int id);
}
