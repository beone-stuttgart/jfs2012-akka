package jfs2012.step3;


public class TaskComplete {
	public final Object task;
	
	public TaskComplete(Object task) {
		this.task = task;
	}
	
	@Override
	public String toString() {
		return "TaskComplete("+task.toString()+")";
	}
}
