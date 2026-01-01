package com.devoops.rentalbrain.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan(basePackages = {
        "com.devoops.rentalbrain.**.query.mapper",
        "com.devoops.rentalbrain.**.**.mapper",
        "com.devoops.rentalbrain.**.command.mapper",
        "com.devoops.rentalbrain.common.codegenerator"
})
public class MyBatisMapperScanConfig {
}