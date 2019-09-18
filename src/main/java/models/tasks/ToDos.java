package models.tasks;

import java.io.Serializable;

public class ToDos implements ITask, Serializable {
    /**
     * Class representing the ToDos data model.
     */
    private String description;
    private boolean isDone;
    private String initials;
    private String taskDuration;

    /**
     * Constructor of ToDos data model.
     *
     * @param description : Description of new task
     */
    public ToDos(String description) {
        this.description = description;
        this.isDone = false;
        this.initials = "T";
    }

    /**
     * Constructor of ToDos data model.
     *
     * @param description : Description of new task
     * @param taskDuration : Duration of the task in hour string.
     */
    public ToDos(String description, String taskDuration) {
        this.description = description;
        this.isDone = false;
        this.initials = "T";
        this.taskDuration = taskDuration;
    }

    @Override
    public String getStatusIcon() {
        return (isDone ? "✓" : "✗"); //return tick or X symbols
    }

    @Override
    public void markAsDone() {
        this.isDone = true;
    }

    @Override
    public String getDescription() {
        if (this.description.isBlank()) {
            return this.description;
        } else {
            return this.description + " (needs " + taskDuration + " hours)";
        }
    }

    @Override
    public String getInitials() {
        return this.initials;
    }

    @Override
    public String getDateTime() {
        return taskDuration;
    }
}
