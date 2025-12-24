package com.devoops.rentalbrain.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


//MyBatis Mapper 스캔 설정(단어 임베딩에 사용중)
@Configuration
@MapperScan(basePackages = {
        "com.devoops.rentalbrain.**.query.mapper",
        "com.devoops.rentalbrain.**.command.mapper",
        "com.devoops.rentalbrain.common.codegenerator" // IdSequenceMapper 여기 있으면 포함
})
public class MyBatisMapperScanConfig {
}