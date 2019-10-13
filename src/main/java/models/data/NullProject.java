package models.data;

import models.member.Member;
import models.member.ProjectMemberList;
import models.task.Task;
import models.task.TaskList;

public class NullProject implements IProject {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ProjectMemberList getMembers() {
        return null;
    }

    @Override
    public TaskList getTasks() {
        return null;
    }

    @Override
    public int getNumOfMembers() {
        return 0;
    }

    @Override
    public int getNumOfTasks() {
        return 0;
    }

    @Override
    public void addMember(Member newMember) {
        /*
        Empty method
         */
    }

    @Override
    public void editMember(int memberIndexNumber, String updatedMemberDetails) {
        /*
        Empty method
         */
    }

    @Override
    public void removeMember(int memberIndexNumber) {
        /*
        Empty method
         */
    }

    @Override
    public void addTask(Task newTask) {
        /*
        Empty method
         */
    }

    @Override
    public boolean memberIndexExists(int indexNumber) {
        return false;
    }

    @Override
    public Task getTask(int taskIndex) {
        return null;
    }
}
