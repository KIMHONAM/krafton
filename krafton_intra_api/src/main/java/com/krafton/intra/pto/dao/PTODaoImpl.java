package com.krafton.intra.pto.dao;

import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dto.PTOResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PTODaoImpl implements PTODao{

    private final SqlSession sqlSession;
    private final ImmutableConfiguration commonCode;

    // 휴가 구분 조회
    @Override
    public Object selectPtoType() {
        return this.sqlSession.selectList("getCommonCode", commonCode.getString("pto.type"));
    }

    @Override
    public Object select(int id) {
        return this.sqlSession.selectOne("getUserPtoInfoById", id);
    }
}
