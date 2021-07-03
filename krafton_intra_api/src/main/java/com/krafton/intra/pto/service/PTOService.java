package com.krafton.intra.pto.service;

import com.krafton.intra.core.dto.PagingRequest;
import com.krafton.intra.core.dto.PagingResponse;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;

import java.util.List;

public interface PTOService {

    Object getEmployeePTOInfo(int id);
    void applicatePTO(PTORequest.PaidTimeOffDto pto);
    PTOResponse.PaidTimeOffCalenderDto getPTOSchedule(int id);
    List<PTOResponse.CancellablePaidTimeOffDto> getCancellablePTOs(int id);
    Object getPtoType();
    void cancelPTO(PTORequest.CancelPaidTimeOffDto cancelPto);
    PagingResponse getPTOHistory(PagingRequest<PTORequest.PaidTimeOffHistoryDto> pagingRequest);

}
