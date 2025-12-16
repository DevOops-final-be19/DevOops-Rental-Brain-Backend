package com.devoops.rentalbrain.approval.query.mapper;

import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApprovalQueryMapper {
    ApprovalStatusDTO getApprovalStatus(@Param("empId") Long empId);
    List<PendingApprovalDTO> selectPendingApprovalsByEmpIdWithPaging(
            @Param("empId") Long empId,
            @Param("criteria") Criteria criteria
    );

    long countPendingApprovalsByEmpId(
            @Param("empId") Long empId,
            @Param("criteria") Criteria criteria
    );
}
