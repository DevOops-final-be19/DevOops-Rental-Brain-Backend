package com.devoops.rentalbrain.contract.command.service;

import com.devoops.rentalbrain.approval.command.Repository.*;
import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.business.campaign.command.service.*;
import com.devoops.rentalbrain.business.contract.command.dto.*;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.*;
import com.devoops.rentalbrain.business.contract.command.service.ContractCommandServiceImpl;
import com.devoops.rentalbrain.common.codegenerator.*;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentTransitionCommandService;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractCommandServiceImplTest {
    @InjectMocks
    private ContractCommandServiceImpl contractCommandService;

    @Mock private ContractCommandRepository contractCommandRepository;
    @Mock private ApprovalCommandRepository approvalCommandRepository;
    @Mock private ApprovalMappingCommandRepository approvalMappingCommandRepository;
    @Mock private ContractItemCommandRepository contractItemCommandRepository;
    @Mock private PaymentDetailCommandRepository paymentDetailCommandRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private CodeGenerator codeGenerator;
    @Mock private SegmentTransitionCommandService segmentTransitionCommandService;
    @Mock private PromotionCommandService promotionCommandService;
    @Mock private CouponCommandService couponCommandService;
    @Mock private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                contractCommandService,
                "entityManager",
                entityManager
        );
        // 트랜잭션 동기화 강제 활성화
        TransactionSynchronizationManager.initSynchronization();
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.clearSynchronization();
        SecurityContextHolder.clearContext();
    }


    private void mockLoginUser(Long empId, int positionId) {
        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(empId);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(() -> "ROLE_USER") // ⭐ 권한을 주면 authenticated = true
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        Employee emp = new Employee();
        emp.setId(empId);
        emp.setPositionId((long) positionId);

        when(entityManager.find(Employee.class, empId))
                .thenReturn(emp);
    }

    @Test
    @DisplayName("계약 생성 성공 - CEO 직급 (결재선 없음)")
    void createContract_success_ceo() {

    /* =========================
       1. 로그인 사용자 (CEO)
       ========================= */
        mockLoginUser(1L, 1); // empId=1, positionId=1(대표)

        Employee emp = new Employee();
        emp.setId(1L);
        emp.setPositionId(1L);

        when(entityManager.find(Employee.class, 1L))
                .thenReturn(emp);

        when(entityManager.getReference(eq(Employee.class), any()))
                .thenReturn(emp);

        when(entityManager.getReference(eq(CustomerlistCommandEntity.class), any()))
                .thenReturn(mock(CustomerlistCommandEntity.class));

    /* =========================
       2. 계약 DTO
       ========================= */
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractName("테스트 계약");
        dto.setStartDate(LocalDateTime.now());
        dto.setContractPeriod(12);
        dto.setMonthlyPayment(100_000);
        dto.setTotalAmount(1_200_000L);
        dto.setPayMethod("A");
        dto.setSpecialContent("단위테스트");
        dto.setCumId(10L);
        dto.setItems(List.of()); // 아이템 없음 (재고 로직 스킵)

    /* =========================
       3. 코드 생성 Mock
       ========================= */
        when(codeGenerator.generate(CodeType.CONTRACT))
                .thenReturn("CON-2026-001");

        when(codeGenerator.generate(CodeType.APPROVAL))
                .thenReturn("APP-2026-001");

    /* =========================
       4. 계약 save Mock
       ========================= */
        ContractCommandEntity savedContract = new ContractCommandEntity();
        savedContract.setId(99L);
        savedContract.setStartDate(dto.getStartDate());
        savedContract.setContractPeriod(dto.getContractPeriod());

        when(contractCommandRepository.save(any()))
                .thenReturn(savedContract);

    /* =========================
       5. 승인 save Mock (⭐ 핵심)
       ========================= */
        ApprovalCommandEntity savedApproval = new ApprovalCommandEntity();
        savedApproval.setId(1L);
        savedApproval.setContract(savedContract); // ⭐ 절대 빠지면 안 됨

        when(approvalCommandRepository.save(any()))
                .thenReturn(savedApproval);

    /* =========================
       6. 실행 + 검증
       ========================= */
        assertDoesNotThrow(() ->
                contractCommandService.createContract(dto)
        );

    /* =========================
       7. 핵심 행위 검증
       ========================= */
        verify(contractCommandRepository, times(1)).save(any());
        verify(approvalCommandRepository, times(1)).save(any());
        verify(approvalMappingCommandRepository, atLeastOnce()).save(any());

        // CEO는 즉시 승인 → 결제 스케줄 생성
        verify(paymentDetailCommandRepository, times(1)).saveAll(any());

        // 쿠폰/프로모션은 없어도 호출 안전
        verify(couponCommandService, never()).createIssuedCoupon(any(), any());
        verify(promotionCommandService, never()).createPromotionLog(any(), any());
    }

    @Test
    @DisplayName("계약 생성 성공 - 팀장 (CEO 승인 필요)")
    void createContract_success_leader() {

        // 팀장 직급 (예: positionId = 3)
        mockLoginUser(2L, 3);

        Employee leader = new Employee();
        leader.setId(2L);
        leader.setPositionId(3L);

        Employee ceo = new Employee();
        ceo.setId(1L);
        ceo.setPositionId(1L);

        when(entityManager.find(Employee.class, 2L)).thenReturn(leader);
        when(entityManager.getReference(eq(Employee.class), eq(1L))).thenReturn(ceo);
        when(entityManager.getReference(eq(Employee.class), eq(2L))).thenReturn(leader);
        when(entityManager.getReference(eq(CustomerlistCommandEntity.class), any()))
                .thenReturn(mock(CustomerlistCommandEntity.class));

        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractName("팀장 계약");
        dto.setStartDate(LocalDateTime.now());
        dto.setContractPeriod(6);
        dto.setMonthlyPayment(200_000);
        dto.setTotalAmount(1_200_000L);
        dto.setPayMethod("A");
        dto.setCumId(10L);
        dto.setCeoId(1L);
        dto.setItems(List.of());

        when(codeGenerator.generate(any())).thenReturn("CODE");

        ContractCommandEntity savedContract = new ContractCommandEntity();
        savedContract.setId(100L);
        savedContract.setStartDate(dto.getStartDate());
        savedContract.setContractPeriod(dto.getContractPeriod());

        when(contractCommandRepository.save(any())).thenReturn(savedContract);

        ApprovalCommandEntity approval = new ApprovalCommandEntity();
        approval.setId(10L);
        approval.setContract(savedContract);

        when(approvalCommandRepository.save(any())).thenReturn(approval);

        assertDoesNotThrow(() ->
                contractCommandService.createContract(dto)
        );

        // 팀장은 즉시 승인 X → 결제 스케줄 생성 X
        verify(paymentDetailCommandRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("계약 생성 성공 - 팀원 (팀장 + CEO 결재)")
    void createContract_success_member() {

        mockLoginUser(3L, 6); // 팀원

        Employee member = new Employee();
        member.setId(3L);
        member.setPositionId(6L);

        Employee leader = new Employee();
        leader.setId(2L);
        leader.setPositionId(3L);

        Employee ceo = new Employee();
        ceo.setId(1L);
        ceo.setPositionId(1L);

        when(entityManager.find(Employee.class, 3L)).thenReturn(member);
        when(entityManager.getReference(eq(Employee.class), any()))
                .thenReturn(member);

        when(entityManager.getReference(eq(CustomerlistCommandEntity.class), any()))
                .thenReturn(mock(CustomerlistCommandEntity.class));

        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractName("팀원 계약");
        dto.setStartDate(LocalDateTime.now());
        dto.setContractPeriod(3);
        dto.setMonthlyPayment(300_000);
        dto.setTotalAmount(900_000L);
        dto.setPayMethod("A");
        dto.setCumId(10L);
        dto.setLeaderId(2L);
        dto.setCeoId(1L);
        dto.setItems(List.of());

        when(codeGenerator.generate(any())).thenReturn("CODE");

        ContractCommandEntity savedContract = new ContractCommandEntity();
        savedContract.setId(200L);
        savedContract.setStartDate(dto.getStartDate());
        savedContract.setContractPeriod(dto.getContractPeriod());

        when(contractCommandRepository.save(any())).thenReturn(savedContract);

        ApprovalCommandEntity approval = new ApprovalCommandEntity();
        approval.setId(20L);
        approval.setContract(savedContract);

        when(approvalCommandRepository.save(any())).thenReturn(approval);

        assertDoesNotThrow(() ->
                contractCommandService.createContract(dto)
        );

        verify(approvalMappingCommandRepository, times(1))
                .saveAll(anyList());
    }

    @Test
    @DisplayName("계약 생성 실패 - 팀원이 결재선 없이 계약 생성 시도")
    void createContract_fail_member_without_approval_line() {

        mockLoginUser(3L, 6); // 팀원

        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractName("권한 오류 계약");
        dto.setStartDate(LocalDateTime.now());
        dto.setContractPeriod(6);
        dto.setMonthlyPayment(100_000);
        dto.setTotalAmount(600_000L);
        dto.setPayMethod("A");
        dto.setCumId(10L);
        dto.setItems(List.of());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> contractCommandService.createContract(dto)
        );

        // 메시지 말고 ErrorCode로 검증
        assertEquals(
                ErrorCode.FORBIDDEN,
                ex.getErrorCode()
        );
    }

    @Test
    @DisplayName("계약 생성 실패 - 상품 재고 부족")
    void createContract_fail_item_stock_not_enough() {

        mockLoginUser(1L, 1); // CEO

        when(entityManager.getReference(eq(CustomerlistCommandEntity.class), any()))
                .thenReturn(mock(CustomerlistCommandEntity.class));

        ContractItemDTO item = new ContractItemDTO();
        item.setItemName("노트북");
        item.setQuantity(5);

        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractName("재고 부족 계약");
        dto.setStartDate(LocalDateTime.now());
        dto.setContractPeriod(6);
        dto.setCumId(10L);
        dto.setItems(List.of(item));

        // Contract save
        ContractCommandEntity savedContract = new ContractCommandEntity();
        savedContract.setId(1L);
        savedContract.setStartDate(dto.getStartDate());
        savedContract.setContractPeriod(dto.getContractPeriod());

        when(contractCommandRepository.save(any()))
                .thenReturn(savedContract);

        // Approval save
        ApprovalCommandEntity savedApproval = new ApprovalCommandEntity();
        savedApproval.setId(10L);
        savedApproval.setContract(savedContract);

        when(approvalCommandRepository.save(any()))
                .thenReturn(savedApproval);

        // 실제 가능한 재고는 2개뿐
        when(itemRepository.findRentableItemIdsForContract(any(), anyInt()))
                .thenReturn(List.of(1L, 2L));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> contractCommandService.createContract(dto)
        );

        assertEquals(
                ErrorCode.CONTRACT_ITEM_STOCK_NOT_ENOUGH,
                ex.getErrorCode()
        );
    }


    @Test
    @DisplayName("계약 해지 성공")
    void terminateContract_success() {

        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setId(1L);
        contract.setStatus("P");

        when(contractCommandRepository.findById(1L))
                .thenReturn(java.util.Optional.of(contract));

        when(itemRepository.updateItemsToOverdueExceptRepairAndStatus(1L))
                .thenReturn(2);

        assertDoesNotThrow(() ->
                contractCommandService.terminateContract(1L)
        );

        assertEquals("T", contract.getStatus());
    }

    @Test
    @DisplayName("계약 해지 실패 - 이미 종료된 계약")
    void terminateContract_fail_already_closed() {

        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setStatus("C");

        when(contractCommandRepository.findById(any()))
                .thenReturn(java.util.Optional.of(contract));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> contractCommandService.terminateContract(1L)
        );

        assertEquals(
                ErrorCode.INVALID_CONTRACT_STATUS,
                ex.getErrorCode()
        );
    }

}
