package models.member;

import models.task.Task;
import models.task.TaskList;

public class MemberTaskList {
    /**
     * Represents a list of tasks which are assigned to a member in a team.
     */
    private TaskList memberTaskList;

    public void addAssignedTask(Task task) {
        this.memberTaskList.addTask(task);
    }

    public void removeTask(Task task) { this.memberTaskList.removeTask(task);
    }
}
