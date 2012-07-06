package jfs2012.step2;

import java.io.File;
import java.io.IOException;

import scala.Option;

import jfs2012.SyncTools;

import akka.actor.UntypedActor;


public class TraversalActor extends UntypedActor {
	@Override
	public void preRestart(Throwable reason, Option<Object> message) {
		if (!message.isEmpty()) {
			getSender().tell(new TaskFailed(message.get()));
		}
		System.out.println("preRestart " + getSelf() + " " + message);
		super.preRestart(reason, message);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ScanTask) {
			ScanTask scanTask = (ScanTask)message;
			File dir = scanTask.dir;
			if (!dir.canRead()) {
				throw new IOException("Unreadable directory: " + dir);
			}
			// Synchronize
			File gitDir = new File(dir + File.separator + ".git");
			if (gitDir.isDirectory()) {
				SyncTools.syncGit(dir);
			} else {
				SyncTools.syncDir(dir);
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
