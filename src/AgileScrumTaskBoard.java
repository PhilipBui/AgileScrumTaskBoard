import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class AgileScrumTaskBoard {
	static Scanner inputReader = new Scanner(System.in);
	static final String CREATE_STORY = "create story ";
	static final String LIST_STORIES = "list stories";
	static final String DELETE_STORY = "delete story ";
	static final String COMPLETE_STORY = "complete story ";
	static final String CREATE_TASK = "create task ";
	static final String LIST_TASKS = "list tasks ";
	static final String DELETE_TASK = "delete task ";
	static final String MOVE_TASK = "move task ";
	static final String UPDATE_TASK = "update task ";
	static final String HELP = "help";
	static final String EXIT = "exit";
	// Location of the saved ScrumTaskBoard
	static final String TAG = "scrumTaskBoard.ser";
	static ScrumTaskBoard scrumTaskBoard;

	/**
	 * Parses a String into an Integer more quickly than Java's inbuilt.
	 * 
	 * @param id
	 * @return Integer value of String
	 */
	public static int fastParseInt(String id) {
		if (id.length() == 0) {
			throw new IllegalArgumentException("Id cannot be empty!");
		}
		int result = 0;
		for (int i = 0; i < id.length(); i++) {
			if (Character.isDigit(id.charAt(i)))
				result = 10 * result + (id.charAt(i) - 48);
			else
				throw new NumberFormatException(id + " is not a valid integer!");
		}
		return result;
	}

	public static void main(String args[]) {
		scrumTaskBoard = new ScrumTaskBoard();
		String line = "";
		// If there's a saved ScrumTaskBoard, load it.
		try {
			FileInputStream fileIn = new FileInputStream(TAG);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			scrumTaskBoard = (ScrumTaskBoard) objectIn.readObject();
			objectIn.close();
			fileIn.close();

		} catch (IOException i) {
			// Do nothing since we expect or don't expect a previous
			// ScrumTaskBoard.
		} catch (Exception e) {
			// Mostly ClassNotFound error, another program wrote another class
			// onto it.
			e.printStackTrace();
		}
		FileOutputStream fileOut;
		ObjectOutputStream objectOut;
		while (true) {
			System.out.println("Enter something");
			line = inputReader.nextLine();
			line = line.replaceAll(" +", " ");
			// No switch statement for String if using JDK < 7
			try {
				if (line.startsWith(CREATE_STORY)) {
					line = line.replaceFirst(CREATE_STORY, "");
					String[] parameterArray = line.split(" ");
					int storyId = fastParseInt(parameterArray[0]);
					line = line.replaceFirst(parameterArray[0], "");
					String description = line;
					scrumTaskBoard.createStory(storyId, description);
					System.out.println("Created story " + storyId + " successfully.");
				} else if (line.startsWith(LIST_STORIES)) {
					scrumTaskBoard.listStories();
				} else if (line.startsWith(DELETE_STORY)) {
					line = line.replaceFirst(DELETE_STORY, "");
					int storyId = fastParseInt(line);
					scrumTaskBoard.deleteStory(storyId);
					System.out.println("Deleted story " + storyId + " successfully.");
				} else if (line.startsWith(COMPLETE_STORY)) {
					line = line.replaceFirst(COMPLETE_STORY, "");
					int storyId = fastParseInt(line);
					scrumTaskBoard.completeStory(storyId);
					System.out.println("Completed story " + storyId + " successfully.");
				} else if (line.startsWith(CREATE_TASK)) {
					line = line.replaceFirst(CREATE_TASK, "");
					String[] parameterArray = line.split(" ");
					int storyId = fastParseInt(parameterArray[0]);
					int taskId = fastParseInt(parameterArray[1]);
					line = line.replaceFirst(parameterArray[0], "");
					line = line.replaceFirst(parameterArray[1], "");
					String taskDescription = line.substring(1);
					scrumTaskBoard.createTask(storyId, taskId, taskDescription);
					System.out.println("Created task " + taskId + " for story " + storyId + " successfully.");
				} else if (line.startsWith(LIST_TASKS)) {
					line = line.replaceFirst(LIST_TASKS, "");
					int storyId = fastParseInt(line);
					scrumTaskBoard.listTasks(storyId);
				} else if (line.startsWith(DELETE_TASK)) {
					line = line.replaceFirst(DELETE_TASK, "");
					String[] parameterArray = line.split(" ");
					int storyId = fastParseInt(parameterArray[0]);
					int taskId = fastParseInt(parameterArray[1]);
					scrumTaskBoard.deleteTask(storyId, taskId);
				} else if (line.startsWith(MOVE_TASK)) {
					line = line.replaceFirst(MOVE_TASK, "");
					String[] parameterArray = line.split(" ");
					int storyId = fastParseInt(parameterArray[0]);
					int taskId = fastParseInt(parameterArray[1]);
					line = line.replaceFirst(parameterArray[0], "");
					line = line.replaceFirst(parameterArray[1], "");
					String newColumn = line.substring(1);
					scrumTaskBoard.moveTask(storyId, taskId, newColumn);
				} else if (line.startsWith(UPDATE_TASK)) {
					line = line.replaceFirst(UPDATE_TASK, "");
					String[] parameterArray = line.split(" ");
					int storyId = fastParseInt(parameterArray[0]);
					int taskId = fastParseInt(parameterArray[1]);
					line = line.replaceFirst(parameterArray[0], "");
					line = line.replaceFirst(parameterArray[1], "");
					String newDescription = line.substring(1);
					scrumTaskBoard.updateTask(storyId, taskId, newDescription);
				} else if (line.startsWith(HELP)) {
					System.out.println(
							"create story <id> <description> - Create a new user story with the given ID and description");
					System.out.println("list stories - List all user stories that have been created, including Id’s");
					System.out.println("delete story <id> - Delete the user story with the given ID");
					System.out.println("complete story <id> - Mark the user story with the given ID as completed");
					System.out.println(
							"create task <storyId> <id> <description> - Create a new task with the given task ID and description, and associate it with the given storyId");
					System.out.println("list tasks <storyId> - List all the tasks associated with the given storyId");
					System.out.println(
							"delete task <storyId> <id> - Deletes the task with the given ID associated with the given storyId");
					System.out.println(
							"move task <storyId> <id> <new column> - Move the task to the new column (To Do, In Process, etc)");
					System.out.println(
							"update task <storyId> <id> <new description> - Update/Modify a task’s description");
				} else if (line.startsWith(EXIT)) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					System.out.println("Invalid command. Type help for list of commands.");
				}
				System.out.println("");
				fileOut = new FileOutputStream(TAG);
				objectOut = new ObjectOutputStream(fileOut);
				objectOut.writeObject(scrumTaskBoard);
				objectOut.close();
				fileOut.close();
			} catch (IOException i) {
				i.printStackTrace();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		}
	}
}
