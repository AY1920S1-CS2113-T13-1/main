package models.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import models.member.Member;
import models.member.TaskMemberList;

public class Task {
    private String taskName;
    private int taskPriority;
    private Date dueDate;
    private int taskCredit;
    private TaskState taskState;
    private TaskMemberList taskMemberList;
    private ArrayList<String> taskRequirements;
    private int taskIndexNumber;

    /**
     * Class representing a task in a project.
     * @param taskName The name of the task.
     * @param taskPriority The priority value of the task.
     * @param dueDate The date that the task is due.
     * @param taskCredit The amount of credit a person would receive for completing the task.
     *                   A more difficult task would receive more credit.
     * @param taskState taskState refers to whether the task is in OPEN, TO-DO, DOING, DONE.
     */
    public Task(String taskName, int taskPriority, Date dueDate, int taskCredit, TaskState taskState,
                ArrayList<String> taskRequirements) {
        this.taskName = taskName;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.taskCredit = taskCredit;
        this.taskState = taskState;
        this.taskMemberList = new TaskMemberList();
        this.taskRequirements = taskRequirements;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public String getDueDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return formatter.format(this.dueDate);
    }

    /**
     * Gets the details of the task in a String format in the correct layout.
     * @return String containing all the details of the task.
     */
    public String getDetails() {
        if (this.dueDate != null) {
            return this.taskName + " | Priority: "
                    + this.taskPriority + " | Due: " + this.getDueDateString() + " | Credit: "
                    + this.taskCredit + " | State: " + this.taskState;
        } else {
            return this.taskName + " | Priority: "
                    + this.taskPriority + " | Due: -- | Credit: " + this.taskCredit + " | State: "
                    + this.taskState;
        }
    }

    public TaskState getTaskState() {
        return this.taskState;
    }

    public int getTaskCredit() {
        return this.taskCredit;
    }

    public void assignMember(Member member) {
        this.taskMemberList.addMember(member);
    }

    public HashSet<Integer> getAssignedIndexes() {
        return this.taskMemberList.getAssignedMembersIndexNumbers();
    }

    public void removeMember(Integer memberIndex) {
        this.taskMemberList.removeMember(memberIndex);
    }

    public ArrayList<String> getTaskRequirements() {
        return this.taskRequirements;
    }

    public int getIndexNumber() {
        return this.taskIndexNumber;
    }

    public void setIndexNumber(int newIndexNumber) {
        this.taskIndexNumber = newIndexNumber;
    }
}
