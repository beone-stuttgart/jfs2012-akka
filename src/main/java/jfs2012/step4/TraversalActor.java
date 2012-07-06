package jfs2012.step4;

import java.io.File;
import java.io.IOException;

import scala.Option;

import jfs2012.SyncTools;
import jfs2012.step4.SyncTask.SyncType;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class TraversalActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	@Override
	public void preStart() {
		log.info("TraversalActor {} created", getSelf());
		super.preStart();
	}
	
	@Override
	public void preRestart(Throwable reason, Option<Object> message) {
		if (!message.isEmpty()) {
			getSender().tell(new TaskFailed(message.get()));
		}
		super.preRestart(reason, message);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		log.info("{} is handling {}", getSelf(), message);
		if (message instanceof ScanTask) {
			ScanTask scanTask = (ScanTask)message;
			File dir = scanTask.dir;
			if (!dir.canRead()) {
				throw new IOException("Unreadable directory: " + dir);
			}
			// Synchronize
			File gitDir = new File(dir + File.separator + ".git");
			if (gitDir.isDirectory()) {
				getSender().tell(new SyncTask(dir, SyncType.GIT));
			} else {
				getSender().tell(new SyncTask(dir, SyncType.DEFAULT));
				// Continue traversal
				File[] files = dir.listFiles();
				if (files!=null) {
					for (File f: files) {
						if (f.isDirectory()) {
							getSender().tell(new ScanTask(f));
						}
					}
				}
			}
			// We're done, tell the coordinator
			getSender().tell(new TaskComplete(scanTask));
		} else {
			unhandled(message);
		}
	}
}
