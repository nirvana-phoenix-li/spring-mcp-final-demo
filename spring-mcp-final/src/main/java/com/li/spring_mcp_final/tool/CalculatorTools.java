package com.li.spring_mcp_final.tool;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 计算器 MCP Tool：两数之和等基础运算。
 */
@Component
public class CalculatorTools {

    /**
     * 两数之和：计算两个数的和
     */
    @McpTool(
            name = "add_two_numbers",
            description = "计算两数之和，返回两个数字相加的结果")
    public Mono<Integer> addTwoNumbers(
            @McpToolParam(description = "第一个加数", required = true) int a,
            @McpToolParam(description = "第二个加数", required = true) int b) {
        return Mono.just(a + b);
    }
}
