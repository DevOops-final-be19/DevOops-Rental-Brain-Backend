package  com.devoops.rentalbrain.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/* 설명. 에러 코드 정의 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* Common */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C003", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C004", "권한이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "Entity not found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C006", "지원하지 않는 HTTP 메서드입니다."),

    /* Employee */
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "직원을 찾을 수 없습니다."),

    /* After Service */
    AS_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "AS001", "AS/정기점검 요청 값이 올바르지 않습니다."),
    AS_DUPLICATE_SCHEDULE(HttpStatus.CONFLICT, "AS002", "이미 동일 자산·동일 예정일의 점검 일정이 존재합니다."),
    AS_NOT_FOUND(HttpStatus.NOT_FOUND, "AS003", "AS/정기점검 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}