package jfs2012.step3;


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
