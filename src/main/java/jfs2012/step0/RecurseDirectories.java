package jfs2012.step0;

import java.io.File;

import jfs2012.SyncTools;

public class RecurseDirectories {
	public void traverse(File dir) {
		if (!dir.canRead()) {
			System.out.println("Unreadable: " + dir);
			return;
		}
		// Synchronize
		File gitDir = new File(dir + File.separator + ".git");
		if (gitDir.isDirectory()) {
			SyncTools.syncGit(dir);
			return;
		} else {
			SyncTools.syncDir(dir);
		}
		// Continue recursion
		File[] files = dir.listFiles();
		if (files!=null) {
			for (File f: files) {
				if (f.isDirectory()) {
					traverse(f);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length<1) {
			System.out.println("Need one parameter (start directory)");
			return;
		}
		File start = new File(args[0]);
		if (!start.isDirectory()) {
			System.out.println(args[0] + " is not a directory");
			return;
		}
		RecurseDirectories r = new RecurseDirectories();
		long startTime = System.currentTimeMillis();	
		r.traverse(start);
		long endTime = System.currentTimeMillis();	
		System.out.println("Total time: " + (endTime-startTime) + " ms. Done.");
	}
}
