package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.entity.Coupon;
import com.devoops.rentalbrain.business.campaign.command.entity.IssuedCoupon;
import com.devoops.rentalbrain.business.campaign.command.repository.CouponRepository;
import com.devoops.rentalbrain.business.campaign.command.repository.IssuedCouponRepository;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.segment.command.entity.SegmentCommandEntity;
import com.devoops.rentalbrain.customer.segment.command.repository.SegmentCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
public class CouponCommandServiceImpl implements CouponCommandService {
    private final ModelMapper modelMapper;
    private final CouponRepository couponRepository;
    private final SegmentCommandRepository segmentCommandRepository;
    private final CodeGenerator codeGenerator;
    private final ContractCommandRepository contractCommandRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    @Autowired
    public CouponCommandServiceImpl(ModelMapper modelMapper,
                                    CouponRepository couponRepository,
                                    SegmentCommandRepository segmentCommandRepository,
                                    CodeGenerator codeGenerator,
                                    ContractCommandRepository contractCommandRepository,
                                    IssuedCouponRepository issuedCouponRepository) {
        this.modelMapper = modelMapper;
        this.couponRepository = couponRepository;
        this.segmentCommandRepository = segmentCommandRepository;
        this.codeGenerator = codeGenerator;
        this.contractCommandRepository = contractCommandRepository;
        this.issuedCouponRepository = issuedCouponRepository;
    }


    @Override
    @Transactional
    public String insertCoupon(InsertCouponDTO couponDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        String segmentName = couponDTO.getSegmentName();
        SegmentCommandEntity segment = segmentCommandRepository.findAllBySegmentName(segmentName);

        Long segmentId = segment.getSegmentId();
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);

        String couponCode = codeGenerator.generate(CodeType.COUPON);
        coupon.setCouponCode(couponCode);
        coupon.setSegmentId(segmentId);
        coupon.setStatus("A");

        couponRepository.save(coupon);

        return "coupon insert success";
    }

    @Override
    @Transactional
    public String updateCoupon(String couCode, ModifyCouponDTO couponDTO) {
        Coupon coupon = couponRepository.findByCouponCode(couCode);
        if(couponDTO.getName() != null && !coupon.getName().equals(couponDTO.getName())) {
            coupon.setName(couponDTO.getName());
        }
        if(couponDTO.getRate() != null && !coupon.getRate().equals(couponDTO.getRate())) {
            coupon.setRate(couponDTO.getRate());
        }
        if(couponDTO.getContent() != null && !coupon.getContent().equals(couponDTO.getContent())) {
            coupon.setContent(couponDTO.getContent());
        }
        if(couponDTO.getType() != null && !coupon.getType().equals(couponDTO.getType())) {
            coupon.setType(couponDTO.getType());
        }
        if(couponDTO.getStartDate() != null && !coupon.getStartDate().equals(couponDTO.getStartDate())) {
            coupon.setStartDate(couponDTO.getStartDate());
        }
        if(couponDTO.getEndDate() != null && !coupon.getEndDate().equals(couponDTO.getEndDate())) {
            coupon.setEndDate(couponDTO.getEndDate());
        }
        if(couponDTO.getDatePeriod() != null && !coupon.getDatePeriod().equals(couponDTO.getDatePeriod())) {
            coupon.setDatePeriod(couponDTO.getDatePeriod());
        }
        if(couponDTO.getMinFee() != null && !coupon.getMinFee().equals(couponDTO.getMinFee())) {
            coupon.setMinFee(couponDTO.getMinFee());
        }
        if(couponDTO.getMaxNum() != null && !coupon.getMaxNum().equals(couponDTO.getMaxNum())) {
            coupon.setMaxNum(couponDTO.getMaxNum());
        }
        if(couponDTO.getMaxNum() != null && !coupon.getMaxNum().equals(couponDTO.getMaxNum())) {
            coupon.setMaxNum(couponDTO.getMaxNum());
        }
        if(couponDTO.getSegmentName() != null) {
            String segmentName = couponDTO.getSegmentName();
            SegmentCommandEntity segment = segmentCommandRepository.findAllBySegmentName(segmentName);

            Long segmentId = segment.getSegmentId();
            if(!coupon.getSegmentId().equals(segmentId)) {
                coupon.setSegmentId(segmentId);
            }
        }

        couponRepository.save(coupon);

        return "coupon updated";
    }

    @Override
    @Transactional
    public String deleteCoupon(String couCode) {
        couponRepository.deleteByCouponCode(couCode);
        return "Coupon deleted successfully";
    }

    @Override
    @Transactional
    public String createIssuedCoupon(Long couponId, Long contractId) {
        Coupon coupon = couponRepository.findById(couponId).get();
        ContractCommandEntity contract = contractCommandRepository.findById(contractId).get();

        LocalDateTime start = contract.getStartDate();
        LocalDateTime end;
        if (coupon.getEndDate() != null) {
            end = coupon.getEndDate();
        } else if (coupon.getDatePeriod() != null) {
            end = start.plusDays(coupon.getDatePeriod());
        } else {
            end = null;
        }

        IssuedCoupon issued = new IssuedCoupon();
        issued.setIssuedDate(start);
        issued.setIsUsed("N");
        issued.setEndDate(end);
        issued.setCouponId(couponId);
        issued.setCumId(contract.getCustomer().getId());
        issued.setConId(contract.getId());

        issuedCouponRepository.save(issued);
        return "issued coupon created successfully";
    }

    @Override
    @Transactional
    public void updateIssuedCoupon(Long contractId) {

        boolean hasUnusedCoupon =
                issuedCouponRepository.existsByConIdAndIsUsed(contractId, "N");

        if (!hasUnusedCoupon) {
            return;
        }

        IssuedCoupon issued = issuedCouponRepository.findByConId(contractId);
        issued.setIsUsed("Y");
        issued.setUsedDate(LocalDateTime.now());

        issuedCouponRepository.save(issued);
    }

}
