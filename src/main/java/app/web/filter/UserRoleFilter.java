package app.web.filter;

import app.entity.UserRole;

public class UserRoleFilter extends RoleFilter {
    public UserRoleFilter(){
        super();
        super.setUserRole(UserRole.USER);
    }
}