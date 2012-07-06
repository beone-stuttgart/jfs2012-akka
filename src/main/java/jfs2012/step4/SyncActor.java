package jfs2012.step4;

import jfs2012.SyncTools;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SyncActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {
		log.info("{} is handling {}", getSelf(), message);
		if (message instanceof SyncTask) {
			SyncTask syncTask = (SyncTask)message;
			switch (syncTask.type) {
				case GIT:
					SyncTools.syncGit(syncTask.dir);
					break;
				default:
					SyncTools.syncDir(syncTask.dir);
			}
			// We're done, tell the coordinator
			getSender().tell(new TaskComplete(syncTask));
		} else {
			unhandled(message);
		}
	}
}
