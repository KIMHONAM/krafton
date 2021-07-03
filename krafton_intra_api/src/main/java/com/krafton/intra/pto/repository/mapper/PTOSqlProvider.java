package com.krafton.intra.pto.repository.mapper;

import com.krafton.intra.pto.dto.PTORequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

//ProviderMethodResolver - MyBatis 3.5.1 or later
public class PTOSqlProvider implements ProviderMethodResolver {

    public String getUserPtoInfoById(final int id) {
        return new SQL(){{
            SELECT("a.id, a.department_code, TO_CHAR(a.hire_date, 'yyyy-mm-dd') as hire_date, a.name, a.employee_number");
            SELECT("b.department_name, c.role_name as position");
            SELECT("d.occur_days, d.use_days, d.unused_days");
            FROM("employee a");
            INNER_JOIN("department b on a.department_code = b.department_code");
            INNER_JOIN("employee_role c on a.position_id = c.id");
            LEFT_OUTER_JOIN("employee_pto_summary d on a.id = d.employee_id");
            WHERE("a.id =  #{id}");
        }}.toString();
    }

    public String getCommonCode(final String type){
        return new SQL(){{
            SELECT("code, code_name");
            FROM("common_code");
            WHERE("code like #{type} ||'%'");
        }}.toString();
    }

    public String getHolidayMap(String fromDate, String toDate){
        return new SQL(){{
            SELECT("holiday, holiday_detail");
            FROM("holiday_info");
            WHERE("holiday between #{fromDate} and #{toDate}");
        }}.toString();
    }

    public String checkPTOExists(Map<String,Object> paramMap){
        SQL sql = new SQL()
                .SELECT("count(pto_date)")
                .FROM("employee_pto_items");
        if(paramMap.get("allDayPtoType") != null){
            sql.WHERE("employee_id = #{employeeId} and pto_date between #{fromDate} and #{toDate} and (pto_type = #{ptoType} or pto_type = #{allDayPtoType})");
        }else{
            sql.WHERE("employee_id = #{employeeId} and pto_date between #{fromDate} and #{toDate}");
        }
        return sql.toString();
    }

    public String insertPTOHistory(PTORequest.PaidTimeOffDto pto){  // 휴가 이력 저장
        return new SQL(){{
            INSERT_INTO("employee_pto_history")
                    .INTO_COLUMNS("id","employee_id", "start_date","end_date","reason","pto_type","pto_days","applicate_date", "applicant", "approver","approve_date","create_user","status")
                    .INTO_VALUES("#{id}","#{employeeId}", "#{startDate}", "#{endDate}", "#{reason}","#{ptoType}","#{realUseDays}","#{applicateDate}","#{applicant}","#{approver}","#{approveDate}","#{employeeId}","#{status}");
        }}.toString();
    }

    public String insertPTOItems(Map<String,Object> ptoMap){

        SQL sql = new SQL()
                .INSERT_INTO("employee_pto_items")
                .INTO_COLUMNS("employee_id", "pto_history_id", "pto_date","pto_type","create_user");
        List<String> dayList = (List<String>)ptoMap.get("realUseDayList");
        for(int i=0;i<dayList.size();i++){
            if(i>0) sql.ADD_ROW();
            sql.INTO_VALUES("#{pto.employeeId}", "#{pto.id}","'"+dayList.get(i)+"'","#{pto.ptoType}","#{pto.employeeId}");
        }
        return sql.toString();
    }

    public String updatePTOSummary(){
        return new SQL(){{
            UPDATE("employee_pto_summary")
                    .SET("occur_days","use_days","unused_days");
        }}.toString();
    }
}
