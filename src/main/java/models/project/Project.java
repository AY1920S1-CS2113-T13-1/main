package models.project;

import models.member.IMember;
import models.member.Member;
import models.member.MemberList;
import models.member.NullMember;
import models.reminder.Reminder;
import models.reminder.ReminderList;
import models.task.ITask;
import models.task.NullTask;
import models.task.Task;
import models.task.TaskList;
import models.task.TaskState;

import java.util.ArrayList;
import java.util.HashMap;

public class Project implements IProject {
    private String name;
    private MemberList memberList;
    private TaskList taskList;
    private ReminderList reminderList;
    private HashMap<String, ArrayList<String>> taskAndListOfMembersAssigned; //taskID_listOfMemberIDs
    private HashMap<String, ArrayList<String>> memberAndIndividualListOfTasks; //memberID_listOfTaskIDs

    /**
     * Class representing a task in a project.
     * @param name The description of the project.
     */
    public Project(String name) {
        this.name = name;
        this.memberList = new MemberList();
        this.taskList = new TaskList();
        this.reminderList = new ReminderList();
        this.taskAndListOfMembersAssigned = new HashMap<>();
        this.memberAndIndividualListOfTasks = new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MemberList getMemberList() {
        return this.memberList;
    }

    @Override
    public TaskList getTaskList() {
        return this.taskList;
    }

    /**
     * Checks if a task already exists in the task list of a project.
     * @param task The task to be checked.
     * @return true if the task already exists, false otherwise.
     */
    @Override
    public boolean taskExists(ITask task) {
        return this.taskList.contains((Task) task);
    }

    @Override
    public String getTaskIndexName(Integer index) {
        return getTask(index).getTaskName();
    }

    @Override
    public int getNumOfMembers() {
        return this.memberList.getNumOfMembers();
    }

    @Override
    public int getNumOfTasks() {
        return this.taskList.getTaskList().size();
    }

    //@@author iamabhishek98
    @Override
    public void addMember(Member newMember) {
        this.memberList.addMember(newMember);
        this.memberAndIndividualListOfTasks.put(newMember.getMemberID(), new ArrayList<>());
    }

    //@@author iamabhishek98
    @Override
    public String editMember(int memberIndexNumber, String updatedMemberDetails) {
        return this.memberList.editMember(memberIndexNumber, updatedMemberDetails);
    }

    //@@author iamabhishek98
    @Override
    public void removeMember(Member memberToBeRemoved) {
        for (String taskID: this.taskAndListOfMembersAssigned.keySet()) {
            this.taskAndListOfMembersAssigned.get(taskID).remove(memberToBeRemoved.getMemberID());
        }
        this.memberAndIndividualListOfTasks.remove(memberToBeRemoved.getMemberID());
        this.memberList.removeMember(memberToBeRemoved);
    }

    //@@author sinteary
    @Override
    public Member getMember(int indexNumber) {
        return (Member) this.memberList.getMember(indexNumber);
    }

    /**
     * Checks if a member exists in the member list of a project.
     * @param member the member to be checked
     * @return true if the member already exists in the project, false otherwise.
     */
    @Override
    public boolean memberExists(IMember member) {
        return this.memberList.contains(member);
    }
    //@@author

    @Override
    public void addTask(Task newTask) {
        this.taskList.addTask(newTask);
        this.taskAndListOfMembersAssigned.put(newTask.getTaskID(), new ArrayList<>());
    }

    //@@author iamabhishek98
    @Override
    public void removeTask(int taskIndexNumber) {
        Task taskToRemove = this.getTask(taskIndexNumber);
        for (String memberID : this.memberAndIndividualListOfTasks.keySet()) {
            this.memberAndIndividualListOfTasks.get(memberID).remove(taskToRemove.getTaskID());
        }
        this.taskAndListOfMembersAssigned.remove(taskToRemove.getTaskID());
        this.taskList.removeTask(taskIndexNumber);
    }

    @Override
    public Task getTask(int taskIndex) {
        return this.taskList.getTask(taskIndex);
    }


    //@@author iamabhishek98
    @Override
    public String[] editTask(int taskIndexNumber, String updatedTaskDetails) {
        return this.taskList.editTask(taskIndexNumber, updatedTaskDetails);
    }

    @Override
    public String[] editTaskRequirements(int taskIndexNumber, String updatedTaskRequirements) {
        return this.taskList.editTaskRequirements(taskIndexNumber, updatedTaskRequirements);
    }

    //@@author iamabhishek98
    /**
     * Returns the member names with the credits of their assigned tasks.
     * @return The member names with the credits of their assigned tasks.
     */
    @Override
    public ArrayList<String> getCredits() {
        /*
        *
        *       THIS METHOD IS TO BE REFACTORED
        *
         */
        ArrayList<String> allMemberCredits = new ArrayList<>();
        ArrayList<Member> allMembers = this.getMemberList().getMemberList();
        HashMap<String, ArrayList<String>> memberIDAndTaskIDs = this.getMembersIndividualTaskList(); //memberID_taskIDs
        int count = 1;
        for (Member member : allMembers) {
            float totalCredits = 0;
            float doneCredits = 0;
            for (String taskID : memberIDAndTaskIDs.get(member.getMemberID())) {
                // credits are split equally between members
                ITask assignedTask = getTaskFromID(taskID);
                float taskCredit = (assignedTask.getTaskCredit()) / (float)(memberIDAndTaskIDs.size());
                totalCredits += taskCredit;
                // members only get credits if the task is "DONE"
                if (assignedTask.getTaskState() == TaskState.DONE) {
                    doneCredits += taskCredit;
                }
            }
            int scale = 20;
            int percentDone = (int)((doneCredits / totalCredits) * scale);
            String progress = "";
            for (int i = 0; i < percentDone; i++) {
                progress += "#";
            }
            for (int i = percentDone; i < scale; i++) {
                progress += ".";
            }
            allMemberCredits.add(count + ". " + member.getName() + ": " + String.format("%.1f", doneCredits) + " credits");
            allMemberCredits.add("   Progress: " + progress + " (" + percentDone * (100 / scale) + "%)");
            count++;
        }
        return allMemberCredits;
    }

    //@@author sinteary
    /**
     * This method assigns a task to a member by mapping the task's unique ID to the assigned member's
     * unique ID. This association is stored in the 2 HashMaps: taskAndListOfMembersAssigned and
     * memberAndIndividualListOfTasks.
     * @param task the task which you wish to assign to the member.
     * @param member the member you wish to assign the task to.
     */
    @Override
    public void createAssignment(Task task, Member member) {
        taskAndListOfMembersAssigned.get(task.getTaskID()).add(member.getMemberID());
        memberAndIndividualListOfTasks.get(member.getMemberID()).add(task.getTaskID());
    }

    /**
     * Removes the assignment between a task and member by removing the mapping between the task's unique
     * ID and the member's unique ID. The association is removed from the 2 HashMaps:
     * taskAndListOfMembersAssigned and memberAndIndividualListOfTasks.
     * @param member the member to unassign the task from.
     * @param task the task to be unassigned.
     */
    @Override
    public void removeAssignment(Member member, Task task) {
        taskAndListOfMembersAssigned.get(task.getTaskID()).remove(member.getMemberID());
        memberAndIndividualListOfTasks.get(member.getMemberID()).remove(task.getTaskID());
    }

    /**
     * Checks if assignment exists between a member and task by ensuring that the association exists
     * in the 2 HashMaps: taskAndListOfMembersAssigned and memberAndIndividualListOfTasks.
     * @param task The task in question.
     * @param member The member in question.
     * @return true task has already been assigned to a member.
     */
    @Override
    public boolean containsAssignment(Task task, Member member) {
        return memberAndIndividualListOfTasks.get(member.getMemberID()).contains(task.getTaskID())
            && taskAndListOfMembersAssigned.get(task.getTaskID()).contains(member.getMemberID());
    }

    /**
     * Returns a hashmap with information about each member's task assignment.
     * @return hashmap with member as key and accompanying task list.
     */
    @Override
    public HashMap<String, ArrayList<String>> getMembersIndividualTaskList() {
        return this.memberAndIndividualListOfTasks;
    }

    /**
     * Returns a hashmap with information about each task's assignment to members.
     * @return hashmap with task as key and accompanying list of assigned members.
     */
    @Override
    public HashMap<String, ArrayList<String>> getTasksAndAssignedMembers() {
        return this.taskAndListOfMembersAssigned;
    }

    /**
     * Returns a member object based on the unique member ID.
     * @param memberID The member ID associated with a member.
     * @return The member object with the matching member ID.
     */
    @Override
    public IMember getMemberFromID(String memberID) {
        for (Member member : this.memberList.getMemberList()) {
            if (memberID.equals(member.getMemberID())) {
                return member;
            }
        }
        return new NullMember("Unable to find this member.");
    }

    /**
     * Returns a task object based on the unique task ID.
     * @param taskID The task ID associated with a task.
     * @return The task object with the matching task ID.
     */
    @Override
    public ITask getTaskFromID(String taskID) {
        for (Task task : this.taskList.getTaskList()) {
            if (taskID.equals(task.getTaskID())) {
                return task;
            }
        }
        return new NullTask();
    }
    //@@author

    @Override
    public void addReminderToList(Reminder reminder) {
        this.reminderList.addReminderList(reminder);
    }

    @Override
    public ArrayList<Reminder> getReminderList() {
        return reminderList.getReminderList();
    }


    /**
     * Set the status of the Reminder.
     * @param isDone the status of the reminder.
     * @param index the 1 based index of the reminder.
     */
    @Override
    public void markReminder(Boolean isDone, int index) {
        reminderList.getReminderList().get(index - 1).setIsDone(isDone);
    }

    /**
     * Get a reminder from the reminderList.
     * @param index the index of the Reminder.
     * @return Reminder object.
     */
    @Override
    public Reminder getReminder(int index) {
        return reminderList.getReminderList().get(index - 1);
    }

    @Override
    public void removeReminder(int index) {
        reminderList.getReminderList().remove(index - 1);
    }

    @Override
    public int getReminderListSize() {
        return reminderList.getReminderList().size();
    }

    @Override
    public HashMap<String,ArrayList<Reminder>> getCategoryReminderList() {

        HashMap<String,ArrayList<Reminder>> reminderCategoryList = new HashMap<>();
        ArrayList<Reminder> remindersLists = reminderList.getReminderList();
        for (Reminder reminder : remindersLists) {
            if (!(reminderCategoryList.containsKey(reminder.getCategory()))) {
                ArrayList<Reminder> reminderL = new ArrayList<>();
                reminderL.add(reminder);
                reminderCategoryList.put(reminder.getCategory(),reminderL);
            } else {
                reminderCategoryList.get(reminder.getCategory()).add(reminder);
            }
        }

        return reminderCategoryList;
    }
}
