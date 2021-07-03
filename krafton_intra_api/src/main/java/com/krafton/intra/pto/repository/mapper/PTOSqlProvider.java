package com.krafton.intra.pto.repository.mapper;

import com.krafton.intra.core.dto.PagingDto;
import com.krafton.intra.core.dto.PagingRequest;
import com.krafton.intra.pto.dto.PTORequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

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

    public String selectCancellablePTOs(){
        return new SQL(){{
            SELECT("a.id as pto_history_id, a.employee_id, TO_CHAR(a.start_date,'YYYY-MM-DD') as start_date, TO_CHAR(a.end_date,'YYYY-MM-DD') as end_date, a.pto_days as days, b.code_name as pto_type_name");
            FROM("employee_pto_history a");
            LEFT_OUTER_JOIN("common_code b on a.pto_type = b.code");
            WHERE("a.start_date >= TO_DATE(TO_CHAR(NOW(),'yyyymmdd'),'YYYYMMDD') AND a.cancel_yn = 'N'");
        }}.toString();
    }

    public String cancelPTOHistories(PTORequest.CancelPaidTimeOffDto cancelPto) {
        StringBuffer buffer = new StringBuffer("UPDATE employee_pto_history");
        buffer.append(" SET cancel_yn = 'Y', status = #{status}, cancel_date = now(), retractor = #{employeeId}, cancel_reason = #{cancelReason}, update_date = now(), update_user = #{employeeId}");
        buffer.append("  WHERE employee_id = #{employeeId} and id IN (");
        for (String id : cancelPto.getPtoHistoryIds()){
            buffer.append("'").append(id).append("'").append( ",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");
        return buffer.toString();
    }

    public String selectSumUsePtoForRollback(PTORequest.CancelPaidTimeOffDto cancelPto){
        StringBuffer buffer = new StringBuffer("SELECT sum(pto_days) FROM employee_pto_history where employee_id = #{employeeId} and id IN (");
        for (String id : cancelPto.getPtoHistoryIds()){
            buffer.append("'").append(id).append("'").append( ",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");
        return buffer.toString();
    }

    public String rollbackUseDays(){
        return new SQL(){{
            UPDATE("employee_pto_summary");
            SET("use_days = (use_days - #{rollbackDays}), unused_days = (unused_days + #{rollbackDays}), update_date = now(), update_user = #{employeeId}");
            WHERE("employee_id = #{employeeId}");
        }}.toString();
    }

    public String deletePTOItems(PTORequest.CancelPaidTimeOffDto cancelPto){
        StringBuffer buffer = new StringBuffer("DELETE FROM employee_pto_items where pto_history_id IN (");
        for (String id : cancelPto.getPtoHistoryIds()){
            buffer.append("'").append(id).append("'").append( ",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");
        return buffer.toString();
    }

    public String countPTOHistories(PTORequest.PaidTimeOffHistoryDto historyDto){
            SQL sql = new SQL();
            sql.SELECT("COUNT(id)");
            sql.FROM("employee_pto_history");
            sql.WHERE("employee_id = #{employeeId} and start_date >= #{fromDate} and end_date <= #{toDate}");

            if(StringUtils.isNotEmpty(historyDto.getPtoType())){
                sql.WHERE("pto_type = #{ptoType}");
            }
            return sql.toString();
    }

    public String selectPTOHistories(PagingDto<PTORequest.PaidTimeOffHistoryDto> pagingDto){

        PTORequest.PaidTimeOffHistoryDto historyDto = pagingDto.getParamData();

        SQL sql = new SQL();
        sql.SELECT("(select code_name from common_code where code = a.status) as status, TO_CHAR(a.start_date,'YYYY-MM-DD') as start_date, TO_CHAR(a.end_date,'YYYY-MM-DD') as end_date, a.pto_days");
        sql.SELECT(" a.reason, (select code_name from common_code where code = a.pto_type) as pto_type");
        sql.SELECT(" TO_CHAR(a.cancel_date,'YYYY-MM-DD') as cancel_date, a.cancel_reason, a.cancel_yn");
        sql.FROM(" employee_pto_history a");
        sql.WHERE("a.employee_id = #{paramData.employeeId} and a.start_date >= #{paramData.fromDate} and a.end_date <= #{paramData.toDate}");

        if(StringUtils.isNotEmpty(historyDto.getPtoType())){
            sql.WHERE("a.pto_type = #{paramData.ptoType}");
        }
        sql.ORDER_BY("a.end_date DESC, a.id DESC");
        sql.LIMIT(pagingDto.getLimit()).OFFSET(pagingDto.getOffset());
        return sql.toString();
    }
}
