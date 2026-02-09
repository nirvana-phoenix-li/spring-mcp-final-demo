package com.li.spring_mcp_final.tool;

import ch.qos.logback.core.util.StringUtil;
import io.micrometer.common.util.StringUtils;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 风控域 MCP Tool：调用 risk-hub 接口查询风控汇总等信息。
 */
@Component
public class RiskHubTools {

    private static final String RISK_SUMMARY_QUERY_URL =
            "https://hc-sit.yonghuivip.com/app/api/risk-hub/risk/summary/query";

    private final RestTemplate restTemplate;

    @Value("${app.risk-hub.cookie:}")
    private String cookie;

    public RiskHubTools(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * [风控域] 根据手机号查询风控汇总信息
     */
    @McpTool(
            name = "risk_summary_query",
            description = "[风控域] 根据手机号查询风控汇总信息")
    public Mono<String> queryRiskSummary(
            @McpToolParam(
                    description = "手机号，例如 15271589254",
                    required = true)
            String mobile) {

        String url = RISK_SUMMARY_QUERY_URL + "?mobile=" + mobile;

        return Mono.fromCallable(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Accept", "*/*");
                    headers.set("Accept-Language", "zh-CN,zh;q=0.9");
                    headers.set("Content-Type", "application/json");
                    headers.set("Referer", "https://hc-sit.yonghuivip.com/app/risk-web");
                    headers.set("User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36");
                    if (cookie != null && !StringUtils.isBlank(cookie)) {
                        headers.set("Cookie", cookie);
                    }

                    HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
                    ResponseEntity<Object> response = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            requestEntity,
                            Object.class);

                    return response.getBody() != null ? response.getBody().toString() : "{}";
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
