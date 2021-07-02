package com.krafton.intra.pto.service;

import com.krafton.intra.employee.dto.EmployeeRequest;
import com.krafton.intra.pto.dao.PTODao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PTOServiceImpl implements PTOService{

    @Autowired
    PTODao ptoDao;

    @Override
    public EmployeeRequest.EmployeeDto getEmployeePTOInfo(int id) {
        return ptoDao.select(id);
    }
}
