package jfs2012.test.strategy;

import akka.actor.UntypedActor;

public class DummyActor extends UntypedActor {
	@Override
	public void preStart() {
		super.preStart();
		System.out.println("DummyActor started");
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		unhandled(message);
	}		
}
