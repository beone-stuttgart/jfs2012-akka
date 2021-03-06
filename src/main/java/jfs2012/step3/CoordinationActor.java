package jfs2012.step3;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.SmallestMailboxRouter;


public class CoordinationActor extends UntypedActor {
	final ActorRef traversal;
	int dirCount = 0;
	int dirsToProcess = 0;
	long startTime = System.currentTimeMillis();	
	
	public CoordinationActor(int parallelActors) {
		traversal = getContext().actorOf(new Props(TraversalActor.class).withRouter(new SmallestMailboxRouter(parallelActors)), "traverse");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ScanTask) {
			dirCount++;
			dirsToProcess++;
			traversal.tell(message, getSelf());
		} else if (message instanceof TaskComplete) {			
			dirsToProcess--;
		} else if (message instanceof TaskFailed) {			
			dirsToProcess--;
		} else {
			unhandled(message);
		}
		if (dirsToProcess==0) {
			long endTime = System.currentTimeMillis();	
			System.out.println("Total time: " + (endTime-startTime) + " ms. Shutting down...");
			getContext().system().shutdown();
		}
	}
}
