package com.krafton.intra.pto.repository.mapper;

import com.krafton.intra.core.dto.PagingDto;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface PTOMapper {

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

    @SelectProvider(type= PTOSqlProvider.class, method = "getCommonCode")
    List<PTOResponse.CommonCodeDto> getCommonCode(String type);

    @SelectProvider(type=PTOSqlProvider.class, method = "getHolidayMap")
    List<Map<String, Object>> getHolidayMap();

    @SelectProvider(type=PTOSqlProvider.class, method = "checkPTOExists")
    int checkPTOExists(Map<String,Object> paramMap);

    @InsertProvider(type=PTOSqlProvider.class, method = "insertPTOHistory")
    int insertPTOHistory(PTORequest.PaidTimeOffDto pto);

    @InsertProvider(type = PTOSqlProvider.class, method = "insertPTOItems")
    int insertPTOItems();

    @Insert("INSERT INTO employee_pto_summary (employee_id,occur_days,use_days,unused_days,create_user) VALUES (#{employeeId},#{occurDays},#{useDays},#{unusedDays},#{employeeId}) ON DUPLICATE KEY UPDATE occur_days = #{occurDays}, use_days = #{useDays}, unused_days = #{unusedDays}, update_date = now(), update_user = #{employeeId}")
    int mergePTOSummary();

    @SelectProvider(type = PTOSqlProvider.class, method = "selectCancellablePTOs")
    List<PTOResponse.CancellablePaidTimeOffDto> selectCancellablePTOs();

    @UpdateProvider(type = PTOSqlProvider.class, method = "cancelPTOHistories")
    int cancelPTOHistories(PTORequest.CancelPaidTimeOffDto cancelPto);

    @SelectProvider(type = PTOSqlProvider.class, method = "selectSumUsePtoForRollback")
    float selectSumUsePtoForRollback(PTORequest.CancelPaidTimeOffDto cancelPto);

    @UpdateProvider(type = PTOSqlProvider.class, method = "rollbackUseDays")
    int rollbackUseDays();

    @DeleteProvider(type = PTOSqlProvider.class, method = "deletePTOItems")
    int deletePTOItems(PTORequest.CancelPaidTimeOffDto cancelPto);

    @SelectProvider(type = PTOSqlProvider.class, method = "countPTOHistories")
    int countPTOHistories(PTORequest.PaidTimeOffHistoryDto historyDto);

    @SelectProvider(type = PTOSqlProvider.class, method = "selectPTOHistories")
    List<PTOResponse.PaidTimeOffHistoryDto> selectPTOHistories(PagingDto pagingHistoryDto);

    @SelectProvider(type = PTOSqlProvider.class, method = "getPTOSchedule")
    List<PTOResponse.PaidTimeOffCalenderDto> getPTOSchedule();
}
