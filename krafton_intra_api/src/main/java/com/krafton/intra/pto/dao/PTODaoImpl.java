package com.krafton.intra.pto.dao;

import com.krafton.intra.employee.dto.EmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PTODaoImpl implements PTODao{

    private final SqlSession sqlSession;

    @Override
    public EmployeeRequest.EmployeeDto select(int id) {
        return this.sqlSession.selectOne("getUserPtoInfoById", id);
    }
}
