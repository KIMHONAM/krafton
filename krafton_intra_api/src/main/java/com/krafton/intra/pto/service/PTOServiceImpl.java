package com.krafton.intra.pto.service;

import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dao.PTODao;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PTOServiceImpl implements PTOService{

    @Autowired
    PTODao ptoDao;

    @Override
    public int applicatePTO(PTORequest.PaidTimeOffDto pto) {

        // 이전 날짜 휴가 신청 제한 & 2년뒤 휴가 신청 제한

        // 해당 기간 내 휴가 겹치는 시간 있는지 체크 - employee_pto_items

        // 시작일 ~ 종료일 주말, 공휴일 제외한 날짜 계산
        // 신청 내역에 기록
        // 개별 employee_pto_items 테이블에 기록
        // 휴가 요약 테이블 갱신

        // 이후  컨트롤러에서 기본정보, 휴가 취소 가능 내역 재조회
        // 뷰에서 휴가 신청내역 재조회

        return 0;
    }

    @Override
    public PTOResponse.PaidTimeOffCalenderDto getPTOSchedule(int id) {
        return null;
    }

    @Override
    public int getCancellablePTOs(int id) {
        return 0;
    }

    @Override
    public Object getEmployeePTOInfo(int id) {
        return ptoDao.select(id);
    }

    @Override
    public Object getPtoType() {
        return ptoDao.selectPtoType();
    }
}
