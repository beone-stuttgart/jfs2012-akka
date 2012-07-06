package jfs2012.step1;

import java.io.File;
import java.io.IOException;

import jfs2012.SyncTools;

import akka.actor.UntypedActor;


public class TraversalActor extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof File) {
			File dir = (File)message;
			if (!dir.canRead()) {
				throw new IOException("Unreadable directory: " + dir);
			}
			// Synchronize
			File gitDir = new File(dir + File.separator + ".git");
			if (gitDir.isDirectory()) {
				SyncTools.syncGit(dir);
				return;
			} else {
				SyncTools.syncDir(dir);
			}
			// Continue traversal
			File[] files = dir.listFiles();
			if (files!=null) {
				for (File f: files) {
					if (f.isDirectory()) {
						getSelf().tell(f);
					}
				}
			}			
		} else {
			unhandled(message);
		}
	}
}
