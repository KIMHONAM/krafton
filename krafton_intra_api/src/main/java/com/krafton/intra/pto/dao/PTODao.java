package com.krafton.intra.pto.dao;

import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dto.PTOResponse;

public interface PTODao {
    public Object select(int id);
    public Object selectPtoType();
}
