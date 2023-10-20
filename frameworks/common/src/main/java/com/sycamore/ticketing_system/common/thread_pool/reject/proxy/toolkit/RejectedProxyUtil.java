package com.sycamore.ticketing_system.common.thread_pool.reject.proxy.toolkit;

import com.sycamore.ticketing_system.common.thread_pool.reject.proxy.RejectedProxyInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: RejectedProxyUtil  拒绝策略代理工具类
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 17:06
 */
public class RejectedProxyUtil {
    /**
     * 创建拒绝策略代理类
     *
     * @param rejectedExecutionHandler 真正的线程池拒绝策略执行器
     * @param rejectedNum              拒绝策略执行统计器
     * @return 代理拒绝策略
     */
    public static RejectedExecutionHandler createProxy(RejectedExecutionHandler rejectedExecutionHandler, AtomicLong rejectedNum) {
        // 动态代理模式: 增强线程池拒绝策略，比如：拒绝任务报警或加入延迟队列重复放入等逻辑
        return (RejectedExecutionHandler) Proxy
                .newProxyInstance(
                        rejectedExecutionHandler.getClass().getClassLoader(),
                        new Class[]{RejectedExecutionHandler.class},
                        new RejectedProxyInvocationHandler(rejectedExecutionHandler, rejectedNum));
    }
    
    
    /**
     * 测试线程池拒绝策略动态代理程序
     */
    public static void main(String[] args) {
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        AtomicLong rejectedNum = new AtomicLong();
        RejectedExecutionHandler proxyRejectedExecutionHandler = RejectedProxyUtil.createProxy(abortPolicy, rejectedNum);
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        2,
                        4,
                        1024,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(2),
                        proxyRejectedExecutionHandler
                );
        for (int i = 0; i < 5; i++) {
            System.out.println("提交任务：" + i);
            try {
                int finalI = i;
                threadPoolExecutor.execute(() -> {try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                            System.out.println(finalI +"" + "执行完成");
                }
                );
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        System.out.println("================ 线程池拒绝策略执行次数: " + rejectedNum.get());
    }
}
