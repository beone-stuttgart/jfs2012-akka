package jfs2012;

import java.io.File;


public class SyncTools {
	public static void syncGit(File dir) {
		System.out.println("Git sync: " + dir);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) { }
	}
	
	public static void syncDir(File dir) {
		System.out.println("Dir sync: " + dir);
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) { }
	}
}
