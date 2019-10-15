package models.task;

import java.util.ArrayList;
import java.util.Comparator;

public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Class representing a list with all task sort in the project.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Adds a new task to the list of this project.
     * @param task A new task to be added to the project.
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Deletes the task from the list using the index number.
     * @param taskIndexNumber The index number of the task to be deleted.
     */
    public void removeTask(int taskIndexNumber) {
        this.taskList.remove(taskIndexNumber - 1);
    }

    /**
     * Returns an ArrayList with String descriptions of task details.
     * @return An ArrayList with String descriptions of task details.
     */
    public ArrayList<String> getAllTaskDetails() {
        ArrayList<String> taskDetails = new ArrayList<>();
        ArrayList<Task> sortedTaskList = taskList;
        sortedTaskList.sort(sortTasksByPriority);
        for (Task task : sortedTaskList) {
            taskDetails.add(task.getDetails());
        }
        return taskDetails;
    }

    /**
     * Returns the list of all tasks.
     * @return An ArrayList with all tasks.
     */
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    /*
    * This method compares the two tasks and sort list in descending order.
     */
    private Comparator<Task> sortTasksByPriority = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            return task2.getTaskPriority() - task1.getTaskPriority();
        }
    };

    public Task getTask(int taskIndex) {
        return this.taskList.get(taskIndex - 1);
    }

    public int getSize() {
        return this.taskList.size();
    }
}
