package com.founder.concurrence.disruptor.generate2;


import com.founder.concurrence.disruptor.generate1.Trade;
import com.lmax.disruptor.EventHandler;

public class Handler2 implements EventHandler<Trade> {  
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
    	System.out.println("handler2: set price");
    	event.setPrice(17.0);
    	Thread.sleep(1000);
    }  
      
}  