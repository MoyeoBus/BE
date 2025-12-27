package com.moyeobus.websocket.api.docs

import com.moyeobus.websocket.api.dto.BusLocationDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Tag(
    name = "실시간 버스 위치(SSE)",
    description = """
    🛰 실시간 버스 위치 스트리밍 API 그룹

    - 이 API는 Server-Sent Events(SSE)기반으로 동작합니다.
    - 요청 후 응답이 종료되지 않으며, 서버가 이벤트를 실시간으로 푸시합니다.
    - ⚠️ Swagger UI에서는 테스트할 수 없습니다. 동작 여부 확인은 개발자 도구의 Network를 참고하세요.
    """
)
interface SseControllerDocs {

    @Operation(
        summary = "노선별 실시간 버스 위치 구독 (SSE)",
        description = """
        특정 노선(`routeId`)에 대한 버스 위치를 실시간 스트림(SSE) 으로 구독합니다.

        ### 동작 방식
        - HTTP 연결을 즉시 종료하지 않고 유지합니다.
        - 서버에서 버스 위치 이벤트가 발생할 때마다 클라이언트로 푸시됩니다.
        - 브라우저에서는 `EventSource`를 사용해야 합니다.

        ### ⚠️ 주의
        - Swagger UI에서는 응답이 종료되지 않아 **로딩 상태로 유지됩니다.**
        - 실제 테스트는 브라우저 또는 `curl -N`을 사용하세요.
        """
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "SSE 스트림 연결 성공",
                content = [
                    Content(
                        mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                        examples = [
                            ExampleObject(
                                name = "SSE 이벤트 예시",
                                value = """
                                event: broadcast
                                data: {
                                  "busId": 12,
                                  "lat": 37.5665,
                                  "lng": 126.9780
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun subscribe(
        @Parameter(
            description = "버스 노선 ID",
            example = "1"
        )
        routeId: Long
    ): SseEmitter


    @Operation(
        summary = "버스 위치 업데이트 수신",
        description = """
        버스 단말 또는 외부 시스템에서 현재 버스 위치를 서버로 전송합니다.

        - 수신된 위치 정보는 해당 노선을 구독 중인 모든 SSE 클라이언트에게 즉시 브로드캐스트됩니다.
        - 이 API는 실시간 데이터 입력용이며, 일반 사용자 호출을 권장하지 않습니다.
        """
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "버스 위치 수신 및 브로드캐스트 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "요청 바디 예시",
                                value = """
                                {
                                  "busId": 12,
                                  "lat": 37.5665,
                                  "lng": 126.9780
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun updateLocation(
        dto: BusLocationDto
    )
}