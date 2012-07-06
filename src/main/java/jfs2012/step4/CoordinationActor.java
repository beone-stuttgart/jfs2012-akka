package jfs2012.step4;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.SmallestMailboxRouter;
import akka.util.Duration;


public class CoordinationActor extends UntypedActor {
	final ActorRef traversal;
	final ActorRef sync;
	int dirCount = 0;
	int dirsToProcess = 0;
	int syncCount = 0;
	int syncsToProcess = 0;
	long startTime = System.currentTimeMillis();
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public CoordinationActor(int parallelActors) {
		SupervisorStrategy strategy = new OneForOneStrategy(-1, Duration.Inf(), new Class<?>[] { Exception.class });
		traversal = getContext().actorOf(new Props(TraversalActor.class).
				withRouter(new SmallestMailboxRouter(parallelActors).
				withSupervisorStrategy(strategy)), "traverse");
		sync = getContext().actorOf(new Props(SyncActor.class).
				withRouter(new SmallestMailboxRouter(parallelActors).
				withSupervisorStrategy(strategy)).
				withDispatcher("workerdispatcher"), "sync");
		getContext().system().scheduler().schedule(Duration.Zero(), Duration.create(250, TimeUnit.MILLISECONDS), getSelf(), "progress");
	}

	protected void appendStatusBar(StringBuffer buf, final String title, final int count, final int remaining) {
		int bars = 0;
		buf.append(title);
		buf.append(": ");
		if (count>0) {
			bars = 10-(10*remaining)/count;
		}
		for (int i=1; i<=10; ++i) {
			if (i<=bars) {
				buf.append('|'); 
			} else {
				buf.append('-');
			}
		}
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ScanTask) {
			dirCount++;
			dirsToProcess++;
			traversal.tell(message, getSelf());
		} else if (message instanceof SyncTask) {
			syncCount++;
			syncsToProcess++;
			sync.tell(message, getSelf());
		} else if (message instanceof TaskComplete) {
			Object task = ((TaskComplete)message).task;
			if (task instanceof ScanTask) {
				dirsToProcess--;
			} else if (task instanceof SyncTask) {
				syncsToProcess--;
			}
		} else if (message instanceof TaskFailed) {			
			Object task = ((TaskFailed)message).task;
			if (task instanceof ScanTask) {
				dirsToProcess--;
			} else if (task instanceof SyncTask) {
				syncsToProcess--;
			}
		} else if ("progress".equals(message)) {
			StringBuffer buf = new StringBuffer();
			appendStatusBar(buf, "Scan", dirCount, dirsToProcess);
			appendStatusBar(buf, "   Sync", syncCount, syncsToProcess);
			System.out.println(buf.toString());
		} else {
			unhandled(message);
		}
		log.info("After {}: dirCount={} dirsToProcess={} syncsToProcess={}", message, dirCount, dirsToProcess, syncsToProcess);
		if (dirsToProcess==0 && syncsToProcess==0) {
			long endTime = System.currentTimeMillis();
			getContext().system().shutdown();
			System.out.println("Total time: " + (endTime-startTime) + " ms. Shutting down...");
		}
	}
}
