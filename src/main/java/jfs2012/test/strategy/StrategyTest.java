package jfs2012.test.strategy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.util.Duration;


public class StrategyTest {
	public void doIt() throws Exception {
		final ActorSystem system = ActorSystem.create("actors");
		final ActorRef traversal = system.actorOf(new Props(TestActor.class), "test");
		Thread.sleep(500);
		System.out.println("Sende Testnachricht");
		traversal.tell("foo");
		Thread.sleep(500);
		system.shutdown();
	}
	
	public static void main(String[] args) throws Exception {
		StrategyTest t = new StrategyTest();
		t.doIt();
	}
}
