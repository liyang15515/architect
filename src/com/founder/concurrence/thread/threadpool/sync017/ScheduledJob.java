package com.founder.concurrence.thread.threadpool.sync017;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// Temp理解成一个定时任务Task类
class Temp extends Thread {
    public void run() {
        System.out.println("run");
    }
}
/**@Author yanglee
* @Description: TODO ScheduleAtFixedRate 是基于固定时间间隔进行任务调度，
                     ScheduleWithFixedDelay 取决于每次任务执行的时间长短，是基于不固定时间间隔进行任务调度
* @Param
* @Return
* @Date 2019-04-23 10:58
*/
public class ScheduledJob {
	
    public static void main(String args[]) throws Exception {
/**@Author yanglee
* @Description: TODO ScheduledExecutorService,是基于线程池设计的定时任务类,每个调度任务都会分配到线程池中的一个线程去执行,
                                        也就是说,任务是并发执行,互不影响。
                    需要注意,只有当调度任务来的时候,ScheduledExecutorService才会真正启动一个线程,其余时间ScheduledExecutorService都是出于轮询任务的状态。
 * @Param [args]
* @Return void
* @Date 2019-04-23 10:20
*/
    	Temp command = new Temp();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        /*@Author yanglee
        * @Description: TODO spring框架的Scheduled定时任务就是基于newScheduledThreadPool，quarz定时任务已经没有人在用了；
        * @Param [args]
        * @Return void
        * @Date 2019-04-24 20:53
        */
        /**@Author yanglee
        * @Description: TODO ScheduleWithFixedDelay 每次执行时间为上一次任务结束起向后推一个时间间隔，
                             即每次执行时间为：initialDelay, initialDelay+executeTime+delay, initialDelay+2*executeTime+2*delay
        * @Param [args]
        * @Return void
        * @Date 2019-04-23 10:54
        */
        ScheduledFuture<?> scheduleTask = scheduler.scheduleWithFixedDelay(command, 5, 1, TimeUnit.SECONDS);
        /**@Author yanglee
        * @Description: TODO ScheduleAtFixedRate 每次执行时间为上一次任务开始起向后推一个时间间隔，
                             即每次执行时间为 :initialDelay, initialDelay+period, initialDelay+2*period, …；
        * @Param [args]
        * @Return void
        * @Date 2019-04-23 10:56
        */
        ScheduledFuture<?> scheduleTask2 = scheduler.scheduleAtFixedRate(command, 5, 1, TimeUnit.SECONDS);
    }
}