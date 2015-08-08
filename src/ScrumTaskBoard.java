import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class ScrumTaskBoard implements java.io.Serializable{
	private Map<Story, List<Task>> taskBoard = new TreeMap<Story, List<Task>>();

	public ScrumTaskBoard() {

	}

	public ScrumTaskBoard(Map<Story, List<Task>> taskBoard) {
		this.taskBoard = taskBoard;
	}

	private boolean hasStory(int storyId) {
		if (taskBoard.containsKey(new Story(storyId))) {
			return true;
		}
		return false;
	}

	private boolean hasTask(int storyId, int taskId) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist for task " + taskId + "!");
		List<Task> storyTasks = taskBoard.get(new Story(storyId));
		for (int i = 0; i < storyTasks.size(); i++) {
			if (storyTasks.get(i).getTaskId() == taskId)
				return true;
		}
		return false;
	}

	private boolean hasTaskComplete(int storyId) {
		List<Task> storyTasks = taskBoard.get(new Story(storyId));
		for (int i = 0; i < storyTasks.size(); i++) {
			if (storyTasks.get(i).getColumn() != "Done")
				return false;
		}
		return true;
	}

	/**
	 * Create a new user story with the given ID and description.
	 * 
	 * @param storyId
	 * @param description
	 */
	public void createStory(int storyId, String description) {
		if (hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " already exists!");
		Story newStory = new Story(storyId, description);
		List<Task> emptyList = new ArrayList<Task>();
		taskBoard.put(newStory, emptyList);
	}

	/**
	 * List all user stories that have been created, include Id's.
	 */
	public void listStories() {
		Set<Story> stories = taskBoard.keySet();
		if (stories.size() == 0) {
			System.out.println("No stories found.");
			return;
		}
		Iterator<Story> iter = stories.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	/**
	 * Delete the user story with the given ID. When deleting a user story, all
	 * tasks associated with the story will be deleted.
	 * 
	 * @param storyId
	 */
	public void deleteStory(int storyId) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist!");
		Set<Story> stories = taskBoard.keySet();
		Iterator<Story> iter = stories.iterator();
		while (iter.hasNext()) {
			if (iter.next().getStoryId() == storyId) {
				iter.remove();
				break;
			}
		}
	}

	/**
	 * Mark the user story with the given ID as completed. The user story can
	 * only be marked completed once all the tasks have been completed.
	 * 
	 * @param storyId
	 */
	public void completeStory(int storyId) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist!");
		if (!hasTaskComplete(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " contains uncompleted Tasks!");
		Set<Story> stories = taskBoard.keySet();
		Iterator<Story> iter = stories.iterator();
		Story newStory = null;
		List<Task> storyTasks = null;
		while (iter.hasNext()) {
			newStory = iter.next();
			if (newStory.getStoryId() == storyId) {
				storyTasks = taskBoard.get(newStory);
				iter.remove();
				break;
			}
		}
		newStory.markComplete();
		taskBoard.put(newStory, storyTasks);
	}

	/**
	 * Create a new task with the given task ID and description, and associate
	 * it with the given storyId.
	 * 
	 * @param storyId
	 * @param taskId
	 * @param taskDescription
	 */
	public void createTask(int storyId, int taskId, String taskDescription) {
		if (hasTask(storyId, taskId))
			throw new IllegalArgumentException("TaskId " + taskId + " already exists for story " + storyId + "!");
		Task newTask = new Task(storyId, taskId, taskDescription);
		taskBoard.get(new Story(storyId)).add(newTask);
	}

	/**
	 * List all the tasks associated with the given storyId.
	 * 
	 * @param storyId
	 */
	public void listTasks(int storyId) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist!");
		List<Task> storyTasks = taskBoard.get(new Story(storyId));
		if (storyTasks.size() == 0) {
			System.out.println("No tasks in story " + storyId + "!");
			return;
		}
		for (int i = 0; i < storyTasks.size(); i++) {
			System.out.println(storyTasks.get(i));
		}
	}

	/**
	 * Deletes the task with the given ID associated with the given storyId.
	 * 
	 * @param storyId
	 * @param taskId
	 */
	public void deleteTask(int storyId, int taskId) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist!");
		List<Task> storyTasks = taskBoard.get(new Story(storyId));
		for (int i = 0; i < storyTasks.size(); i++) {
			if (storyTasks.get(i).getTaskId() == taskId) {
				storyTasks.remove(i);
				return;
			}
		}
		throw new IllegalArgumentException("TaskId " + taskId + " does not exist for story " + storyId + "!");
	}

	/**
	 * Move the task to the new column (To Do, In Process, etc). Each task has
	 * to go through the “To Do”, “In Process”, “To Verify”, and “Done” columns.
	 * Each task that is not in the “Done” column can be moved back to “To Do”
	 * or “In Process” column if necessary New Column will always be in lower
	 * case.
	 * 
	 * @param storyId
	 * @param taskId
	 * @param newColumn
	 */
	public void moveTask(int storyId, int taskId, String newColumn) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist!");
		List<Task> storyTasks = taskBoard.get(new Story(storyId));
		for (int i = 0; i < storyTasks.size(); i++) {
			if (storyTasks.get(i).getTaskId() == taskId) {
				if (newColumn == "to do") {
					if (storyTasks.get(i).getColumn() != "Done")
						storyTasks.get(i).setToDo();
					else
						throw new IllegalArgumentException("Could not update the task to To Do.");
				} else if (newColumn == "in process") {
					if (storyTasks.get(i).getColumn() != "Done")
						storyTasks.get(i).setInProcess();
					else
						throw new IllegalArgumentException("Could not update the task to In Process.");
				} else if (newColumn == "to verify") {
					if (storyTasks.get(i).getColumn() == "In Process")
						storyTasks.get(i).setToVerify();
					else
						throw new IllegalArgumentException("Could not update the task to To Verify.");
				} else if (newColumn == "done") {
					if (storyTasks.get(i).getColumn() == "To Verify")
						storyTasks.get(i).setDone();
					else
						throw new IllegalArgumentException("Could not update the task to Done.");
				} else
					throw new IllegalArgumentException("Invalid column name!");
				return;
			}
		}
		throw new IllegalArgumentException("TaskId " + taskId + " does not exist for story " + storyId + "!");
	}

	/**
	 * Update/Modify a task’s description.
	 * 
	 * @param storyId
	 * @param taskId
	 * @param newDescription
	 */
	public void updateTask(int storyId, int taskId, String newDescription) {
		if (!hasStory(storyId))
			throw new IllegalArgumentException("StoryId " + storyId + " does not exist!");
		List<Task> storyTasks = taskBoard.get(new Story(storyId));
		for (int i = 0; i < storyTasks.size(); i++) {
			if (storyTasks.get(i).getTaskId() == taskId) {
				storyTasks.get(i).setTaskDescription(newDescription);
				return;
			}
		}
		throw new IllegalArgumentException("TaskId " + taskId + " does not exist for story " + storyId + "!");
	}
}
