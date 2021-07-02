package com.krafton.intra.pto.repository.mapper;

import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dto.PTOResponse;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PTOMapper {

    @Select("select * from employee where 1 = #{id}")
    EmployeeResponse.EmployeeDto findById(@Param("id") int id);

    @SelectProvider(type= PTOSqlProvider.class, method = "getUserPtoInfoById")
    @Results(value = {
            @Result(property = "employee.id", column = "id"),
            @Result(property = "employee.employeeNumber", column = "employee_number"),
            @Result(property = "employee.departmentName", column = "department_name"),
            @Result(property = "employee.departmentCode", column = "department_code"),
            @Result(property = "employee.name", column = "name"),
            @Result(property = "employee.position", column = "position"),
            @Result(property = "employee.hireDate", column = "hire_date"),
    })
    PTOResponse.EmployeePTODto getUserPtoInfoById(@Param("id") int id);
}
