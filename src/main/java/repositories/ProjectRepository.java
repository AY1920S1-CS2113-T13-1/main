package repositories;

import models.data.Project;
import util.factories.ProjectFactory;
import java.util.ArrayList;
import models.data.IProject;

public class ProjectRepository implements IRepository<Project> {
    private ArrayList<Project> allProjects;
    private ProjectFactory projectFactory;

    public ProjectRepository() {
        this.allProjects = new ArrayList<>();
        this.projectFactory = new ProjectFactory();
    }

    @Override
    public ArrayList<Project> getAll() {
        return allProjects;
    }

    @Override
    public boolean addToRepo(String input) {
        IProject newProject = projectFactory.create(input);
        if (newProject.getDescription() == null || newProject.getMembers() == null) {
            return false;
        }
        Project newlyCreatedProject = (Project) newProject;
        allProjects.add(newlyCreatedProject);
        return true;
    }

    public Project getItem(int projectNumber) {
        return this.allProjects.get(projectNumber - 1);
    }

    @Override
    public void deleteItem() {
        // TODO yet to be implemented
    }
}
