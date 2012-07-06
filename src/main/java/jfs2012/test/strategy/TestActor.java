package jfs2012.test.strategy;

import scala.Option;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.util.Duration;

public class TestActor extends UntypedActor {
	final ActorRef dummyActor;
	final SupervisorStrategy strategy = new OneForOneStrategy(-1, Duration.Inf(), new Class<?>[] { Exception.class });

	public TestActor() {
		dummyActor = getContext().actorOf(new Props(DummyActor.class), "dummy");
	}
	
	@Override
	public void preStart() {
		super.preStart();
		System.out.println("TestActor started");
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		throw new RuntimeException("I don't do anything");
	}
	
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}
}
