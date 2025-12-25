package com.devoops.rentalbrain.common.codegenerator;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IdSequenceMapper {

    // 있으면 last_number를 +1 하고 LAST_INSERT_ID에 저장. 업데이트 된 row 수 리턴(0이면 없음)
    int incrementLastNumber(@Param("domain") String domain, @Param("year") int year);

    // 없으면 최초 생성(last_number=1). 성공 1
    int insertFirst(@Param("domain") String domain, @Param("year") int year);

    // 같은 커넥션에서 LAST_INSERT_ID() 조회
    int selectLastInsertId();
}