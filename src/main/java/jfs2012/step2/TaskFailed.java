package jfs2012.step2;


public class TaskFailed {
	public final Object task;

	public TaskFailed(Object task) {
		this.task = task;
	}
	
	@Override
	public String toString() {
		return "TaskFailed("+task.toString()+")";
	}
}
