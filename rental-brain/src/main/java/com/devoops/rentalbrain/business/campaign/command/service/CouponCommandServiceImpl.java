package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.entity.Coupon;
import com.devoops.rentalbrain.business.campaign.command.repository.CouponRepository;
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


@Service
@Slf4j
public class CouponCommandServiceImpl implements CouponCommandService {
    private final ModelMapper modelMapper;
    private final CouponRepository couponRepository;
    private final SegmentCommandRepository segmentCommandRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public CouponCommandServiceImpl(ModelMapper modelMapper, CouponRepository couponRepository, SegmentCommandRepository segmentCommandRepository, CodeGenerator codeGenerator) {
        this.modelMapper = modelMapper;
        this.couponRepository = couponRepository;
        this.segmentCommandRepository = segmentCommandRepository;
        this.codeGenerator = codeGenerator;
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

        couponRepository.save(coupon);

        return "coupon insert success";
    }

    @Override
    @Transactional
    public String updateCoupon(Long couponId, ModifyCouponDTO couponDTO) {
        Coupon coupon = couponRepository.findById(couponId).get();
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
    public String deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
        return "Coupon deleted successfully";
    }

}
