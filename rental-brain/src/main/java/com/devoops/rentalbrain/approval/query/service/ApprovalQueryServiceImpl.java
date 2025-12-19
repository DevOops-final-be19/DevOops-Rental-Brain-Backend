package com.devoops.rentalbrain.approval.query.service;

import com.devoops.rentalbrain.approval.query.dto.ApprovalCompletedDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalProgressDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.approval.query.mapper.ApprovalQueryMapper;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalQueryServiceImpl implements ApprovalQueryService {

    private final ApprovalQueryMapper approvalQueryMapper;

    @Autowired
    public ApprovalQueryServiceImpl(ApprovalQueryMapper approvalQueryMapper) {
        this.approvalQueryMapper = approvalQueryMapper;
    }

    @Override
    public ApprovalStatusDTO getApprovalStatus(Long empId) {
        return approvalQueryMapper.getApprovalStatus(empId);
    }

    @Override
    public PageResponseDTO<PendingApprovalDTO> getPendingApprovals(Long empId, Criteria criteria) {
        // 1. 목록 조회
        List<PendingApprovalDTO> list =
                approvalQueryMapper.selectPendingApprovalsByEmpIdWithPaging(empId, criteria);

        // 2. 전체 개수 조회
        long totalCount =
                approvalQueryMapper.countPendingApprovalsByEmpId(empId,criteria);

        // 3. 페이지 버튼 정보 생성
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        // 4. 응답 DTO 조립
        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    public PageResponseDTO<ApprovalProgressDTO> getApprovalProgress(Long empId, Criteria criteria) {
        // 1. 목록 조회
        List<ApprovalProgressDTO> list =
                approvalQueryMapper.selectInProgressApprovals(empId, criteria);

        // 2. 전체 개수 조회
        long totalCount =
                approvalQueryMapper.countInProgressApprovals(empId,criteria);

        // 3. 페이지 버튼 정보 생성
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        // 4. 응답 DTO 조립
        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    public PageResponseDTO<ApprovalCompletedDTO> getApprovalCompleted(Long empId, Criteria criteria) {
        // 1. 목록조회
        List<ApprovalCompletedDTO> list =
                approvalQueryMapper.selectCompletedApprovals(empId,criteria);

        // 2. 전체 개수 조회
        long totalCount =
                approvalQueryMapper.countCompletedApprovals(empId,criteria);

        // 3. 페이지 버튼 정보 생성
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        // 4. 응답 DTO 조립
        return new PageResponseDTO<>(list, totalCount, paging);
    }


}
