package jfs2012.step4;

import java.io.File;

public class SyncTask {
	enum SyncType { GIT, DEFAULT }

	public final File dir;
	public final SyncType type;
	
	public SyncTask(File dir, SyncType type) {
		this.dir = dir;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "SyncTask("+dir+","+type+")";
	}
}
