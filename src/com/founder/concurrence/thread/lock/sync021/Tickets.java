package com.founder.concurrence.thread.lock.sync021;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author yanglee
 * @Date 2019-04-24 11:22
 * @Description TODO 重入锁021-3  该demo模拟电影院的售票情况,tickets总票数。开启了10个窗口售票，售完为止
 * @Version 1.0
 **/
public class Tickets implements Runnable {

    private Lock lock = new ReentrantLock();

    private int tickets = 20000;

    @Override
    public void run() {
        while (true) {
            lock.lock(); // 获取锁
            try {
                if (tickets > 0) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    System.out.println(Thread.currentThread().getName() + " " + tickets--);
                } else {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); // 释放锁
            }
        }
    }

    public static void main(String[] args) {
        Tickets reentrantLockDemo = new Tickets();
//        Tickets reentrantLockDemo2 = new Tickets();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(reentrantLockDemo, "thread" + i);
            thread.start();
        }
//        Thread thread = new Thread(reentrantLockDemo, "thread" + 1);
//        thread.start();
//        Thread thread2 = new Thread(reentrantLockDemo2, "thread" + 2);
//        thread2.start();
    }
}
