package jfs2012.step4;

import java.io.File;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class RecurseDirectories {
	void traverse(File start) {
		final ActorSystem system = ActorSystem.create("actors");
		final ActorRef coordinator = system.actorOf(new Props(new UntypedActorFactory() {			
			@Override
			public Actor create() {
				return new CoordinationActor(5);
			}
		}), "coordinate");
		coordinator.tell(new ScanTask(start));
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
		r.traverse(start);
	}
}
