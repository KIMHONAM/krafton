package com.krafton.intra.pto.service;

import com.krafton.intra.core.dto.PagingRequest;
import com.krafton.intra.core.dto.PagingResponse;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class PtoServiceTest {

    @Autowired
    private PTOService ptoService;

    @Test
    @Ignore
    public void testPTOPaging(){
        PagingRequest<PTORequest.PaidTimeOffHistoryDto> pagingRequest = new PagingRequest<>();
        PTORequest.PaidTimeOffHistoryDto dto = new PTORequest.PaidTimeOffHistoryDto();
        dto.setPtoType("PTOT000001");
        dto.setEmployeeId(1);
        dto.setFromDate("2021-07-03");
        dto.setFromDate("2021-07-04");
        pagingRequest.setPage(1);
        pagingRequest.setLimit(3);
        pagingRequest.setPayload(dto);

        PagingResponse res = ptoService.getPTOHistory(pagingRequest);
        assertThat(res.getList().size()).isEqualTo(1);
    }

    @Test
    public void testCalendar(){

        String deptCode = "DEPT100002";
        String start = "2021-07-02";
        String end = "2021-07-02";


        List<PTOResponse.PaidTimeOffCalenderDto> list =  ptoService.getPTOSchedule(deptCode, start, end);
        assertThat(list.size()).isNotZero();
    }
}
