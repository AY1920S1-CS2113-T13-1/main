package models.member;

import models.task.Task;

public class Member implements IMember {
    private String name;
    private String phone;
    private String email;
    private int indexNumber;
    private MemberTaskList memberTaskList;

    /**
     * Class representing a member in a project team.
     * @param name The name of the member.
     * @param phone The phone number of the member.
     * @param email The email address of the member.
     * @param indexNumber The index number assigned to the member, unique to the project.
     */
    public Member(String name, String phone, String email, int indexNumber) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.indexNumber = indexNumber;
        this.memberTaskList = new MemberTaskList();
    }

    @Override
    public String getDetails() {
        return this.indexNumber + ". " + this.name + " (Phone: " + this.phone + " | Email: "
            + this.email + ")";
    }

    @Override
    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    @Override
    public int getIndexNumber() {
        return this.indexNumber;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void updateDetails(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public void assignTask(Task task) {
        this.memberTaskList.addAssignedTask(task);
    }

    public void unassignTask(Task task) {
        this.memberTaskList.removeTask(task);
    }
}
