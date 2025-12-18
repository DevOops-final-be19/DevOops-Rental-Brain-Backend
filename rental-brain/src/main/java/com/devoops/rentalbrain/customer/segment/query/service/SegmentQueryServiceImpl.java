package com.devoops.rentalbrain.customer.segment.query.service;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryKPIDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import com.devoops.rentalbrain.customer.segment.query.mapper.SegmentQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SegmentQueryServiceImpl implements SegmentQueryService{

    private final SegmentQueryMapper segmentQueryMapper;

    @Autowired
    public SegmentQueryServiceImpl(SegmentQueryMapper segmentQueryMapper) {
        this.segmentQueryMapper = segmentQueryMapper;
    }

    // 세그먼트 목록 조회
    @Override
    public List<SegmentQueryListDTO> selectSegmentList(String segmentName) {
        List<SegmentQueryListDTO> list = segmentQueryMapper.selectSegmentList(segmentName);

        log.info("세그먼트 목록 조회 - 갯수: {}개", list.size());
        return list;
    }

    // 세그먼트 상세 조회
    @Override
    public SegmentQueryDetailDTO selectSegmentDetail(Long segmentId) {
        SegmentQueryDetailDTO detail = segmentQueryMapper.selectSegmentDetail(segmentId);

        log.info("세그먼트 상세 조회 - segmentName: {}, segmentId: {}, customers: {}개의 회사",
                 detail.getSegmentName(),
                 detail.getSegmentId(),
                (detail.getCustomers() == null ? 0 : detail.getCustomers().size())
        );
        return detail;
    }

    // 세그먼트 kpi 조회
    @Override
    public SegmentQueryKPIDTO selectSegmentKpi(String month) {
        // 이건 뭐시당가
        // month 파라미터 없으면 이번달로 처리
        // month = "YYYY-MM" = 4자리 년도-두자리 월

        // month 파라미터가 없으면 → 이번 달 KPI OR 있으면 → "YYYY-MM" 형식으로 변환
        java.time.YearMonth ym = (month == null || month.isBlank())
                ? java.time.YearMonth.now()
                : java.time.YearMonth.parse(month);


        // 이번 달 기간 계산 (1일 00:00:00 ~ 마지막일 23:59:00)
        java.time.LocalDateTime monthStart = ym.atDay(1).atStartOfDay();
        java.time.LocalDateTime monthEnd = ym.atEndOfMonth().atTime(23, 59, 59);

        // 전월(비교용) 기간 계산
        java.time.YearMonth prevYm = ym.minusMonths(1);
        java.time.LocalDateTime prevStart = prevYm.atDay(1).atStartOfDay();
        java.time.LocalDateTime prevEnd = prevYm.atEndOfMonth().atTime(23, 59, 59);

        int lowStar = 2; // <=2.5를 정수로 근사

        SegmentQueryKPIDTO kpi = segmentQueryMapper.selectSegmentKpi(
                monthStart, monthEnd, prevStart, prevEnd, lowStar, "이탈 위험 고객"
        );

        log.info("세그먼트 KPI 조회 - month: {}, dto: {}", ym, kpi);
        return kpi;
    }
}
