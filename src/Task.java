@SuppressWarnings("serial")
public class Task implements java.io.Serializable{
	private int storyId;
	private int taskId;
	private String taskDescription;
	private String column;
	public Task() {
		
	}
	public Task(int storyId, int taskId, String taskDescription) {
		this.storyId = storyId;
		this.taskId = taskId;
		if (taskDescription.matches(" *"))
			this.taskDescription = "No Description";
		else
			this.taskDescription = taskDescription;
		setToDo();
	}
	public int getStoryId() {
		return storyId;
	}
	public int getTaskId() {
		return taskId;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getColumn() {
		return column;
	}
	public void setToDo() {
		column = "To Do";
	}
	public void setInProcess() {
		column = "In Process";
	}
	public void setToVerify() {
		column = "To Verify";
	}
	public void setDone() {
		column = "Done";
	}
	@Override
	public String toString() {
		return taskId + ": " + taskDescription + ". Status: " + column;
	}
	
}
