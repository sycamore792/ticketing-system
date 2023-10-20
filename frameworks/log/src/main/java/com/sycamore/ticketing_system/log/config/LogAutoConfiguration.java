package com.sycamore.ticketing_system.log.config;

import com.sycamore.ticketing_system.log.annotation.ILog;
import com.sycamore.ticketing_system.log.core.ILogPrintAspect;
import org.springframework.context.annotation.Bean;


public class LogAutoConfiguration {

    /**
     * {@link ILog} 日志打印 AOP 切面
     */
    @Bean
    public ILogPrintAspect iLogPrintAspect() {
        return new ILogPrintAspect();
    }
}
