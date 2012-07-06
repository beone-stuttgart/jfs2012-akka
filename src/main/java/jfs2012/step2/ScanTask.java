package jfs2012.step2;

import java.io.File;


public class ScanTask {
	public final File dir;

	public ScanTask(File dir) {
		this.dir = dir;
	}
	
	@Override
	public String toString() {
		return "ScanTask("+dir+")";
	}
}
