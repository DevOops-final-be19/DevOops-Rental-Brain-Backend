package com.devoops.rentalbrain.customer.overdue.command.controller;

import com.devoops.rentalbrain.customer.overdue.command.dto.ItemOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.dto.PayOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.service.OverdueCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers/overdues")
@Tag(name = "연체 관리 (Command)", description = "수납 연체 / 제품 연체 상태 변경 API")
public class OverdueCommandController {

    private final OverdueCommandService overdueCommandService;

    @Operation(
            summary = "수납 연체 상태 변경",
            description = """
                    수납 연체 상태를 변경합니다.
                    
                    - P → C : 연체 해결 (납부 완료)
                    - 해결 시 paidDate가 함께 처리됩니다.
                    
                    ※ 연체 이력은 삭제되지 않습니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수납 연체 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 상태값)"),
            @ApiResponse(responseCode = "404", description = "수납 연체 정보 없음")
    })
    @PutMapping("/pay/{id}")
    public void updatePayOverdue(
            @PathVariable Long id,
            @RequestBody PayOverdueCommandDTO dto
    ) {
        overdueCommandService.updatePayOverdue(id, dto);
    }

    @Operation(
            summary = "제품 연체 수정 / 해결",
            description = """
                제품 연체 정보를 수정합니다.
                
                - count 수정 가능
                - resolved=true → 회수 완료 처리 (P → C)
                
                ※ 수납 연체와는 무관합니다.
                """
    )
    @PutMapping("/item/{id}")
    public void updateItemOverdue(
            @PathVariable("id") Long overdueId,
            @RequestBody ItemOverdueCommandDTO dto
    ) {
        overdueCommandService.updateItemOverdue(overdueId, dto);
    }

    @Operation(
            summary = "수납 연체 삭제",
            description = """
                수납 연체 이력을 삭제합니다.
                
                - 존재하지 않는 id 요청 시 오류 발생
                - 물리 삭제 방식
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수납 연체 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "수납 연체 정보 없음")
    })
    @DeleteMapping("/pay/{id}")
    public void deletePayOverdue(@PathVariable Long id) {
        overdueCommandService.deletePayOverdue(id);
    }


    @Operation(
            summary = "제품 연체 삭제",
            description = """
                제품 연체 이력을 삭제합니다.
                
                - 존재하지 않는 id 요청 시 오류 발생
                - 물리 삭제 방식
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제품 연체 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "제품 연체 정보 없음")
    })
    @DeleteMapping("/item/{id}")
    public void deleteItemOverdue(@PathVariable Long id) {
        overdueCommandService.deleteItemOverdue(id);
    }

}

