@SuppressWarnings("serial")
public class Story implements Comparable<Story> , java.io.Serializable{
	private int storyId;
	private String description;
	private boolean completed;
	public Story (int storyId) {
		this.storyId = storyId;
	}
	public Story (int storyId, String description) {
		this.storyId = storyId;
		if (description.matches(" *"))
			this.description = "No Description";
		else
			this.description = description;
		completed = false;
	}
	public int getStoryId() {
		return storyId;
	}
	public String getDescription() {
		return description;
	}
	public boolean getCompleted() {
		return completed;
	}
	public void markComplete() {
		completed = true;
	}
	public void markUncomplete() {
		completed = false;
	}
	@Override
	public String toString() {
		return storyId + ": " + description + ". Completed: " + completed;
	}
	@Override
	public int compareTo(Story s2) {
		if (getStoryId() > s2.getStoryId())
			return 1;
		else if (s2.getStoryId() > getStoryId())
			return -1;
		else
			return 0;
	}
}
