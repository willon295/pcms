package cn.willon.pcms.pcmsmidware.domain.condition;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * UpdateChangeCondition
 *
 * @author Willon
 * @since 2019-04-19
 */
@Data
public class UpdateChangeDto implements Serializable {


    /**
     * ownerId : 2
     * changeName : 变更测试7
     * changeId : 31
     * branchName : br7
     * expireDate : 2019-05-24
     * projects : [{"projectId":3,"projectName":"member-info","users":[3]},{"projectId":2,"projectName":"interest","users":[3,5,6]},{"projectId":2,"projectName":"interest","users":[3,5,6]}]
     */

    private int ownerId;
    private String changeName;
    private int changeId;
    private String branchName;
    private String expireDate;
    private List<ProjectsBean> projects;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public int getChangeId() {
        return changeId;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public List<ProjectsBean> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectsBean> projects) {
        this.projects = projects;
    }

    public static class ProjectsBean {
        /**
         * projectId : 3
         * projectName : member-info
         * users : [3]
         */

        private int projectId;
        private String projectName;
        private List<Integer> users;

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public List<Integer> getUsers() {
            return users;
        }

        public void setUsers(List<Integer> users) {
            this.users = users;
        }
    }
}
