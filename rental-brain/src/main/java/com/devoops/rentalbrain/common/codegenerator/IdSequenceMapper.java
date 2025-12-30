package com.devoops.rentalbrain.common.codegenerator;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IdSequenceMapper {

    int nextSequence(@Param("domain") String domain,
                     @Param("year") int year);

    int selectLastInsertId();
}
