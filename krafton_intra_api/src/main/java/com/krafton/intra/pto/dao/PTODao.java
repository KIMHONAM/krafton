package com.krafton.intra.pto.dao;

import com.krafton.intra.employee.dto.EmployeeRequest;

public interface PTODao {
    public EmployeeRequest.EmployeeDto select(int id);
}
