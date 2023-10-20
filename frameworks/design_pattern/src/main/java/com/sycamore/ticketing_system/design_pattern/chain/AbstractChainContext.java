package com.sycamore.ticketing_system.design_pattern.chain;

import com.sycamore.ticketing_system.base.container.ApplicationContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: AbstractChainContext
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 15:07
 */
public class AbstractChainContext <T> implements CommandLineRunner {
    private final Map<String, List<AbstractChainHandler>> abstractChainHandlerContainer = new HashMap<>();

    /**
     * 责任链组件执行
     *
     * @param mark         责任链组件标识
     * @param requestParam 请求参数
     */
    public void handler(String mark, T requestParam) {
        List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(mark);
        if (CollectionUtils.isEmpty(abstractChainHandlers)) {
            throw new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.", mark));
        }
        abstractChainHandlers.forEach(each -> each.handler(requestParam));
    }
    @Override
    public void run(String... args) throws Exception {
        // 调用 SpirngIOC 工厂获取 AbstractChainHandler 接口类型的 Bean
        Map<String, AbstractChainHandler> chainFilterMap = ApplicationContextHolder
                .getBeans(AbstractChainHandler.class);
        chainFilterMap.forEach((beanName, bean) -> {
            // 获取 mark（责任链业务标识）的处理器集合
            List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(bean.mark());
            // 如果不存在则创建一个集合
            if (CollectionUtils.isEmpty(abstractChainHandlers)) {
                abstractChainHandlers = new ArrayList();
            }
            // 添加到处理器集合中
            abstractChainHandlers.add(bean);
            // 对处理器集合执行顺序进行排序
            List<AbstractChainHandler> actualAbstractChainHandlers = abstractChainHandlers.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder))
                    .collect(Collectors.toList());
            // 存入容器等待被运行时调用
            abstractChainHandlerContainer.put(bean.mark(), actualAbstractChainHandlers);
        });
    }
}
