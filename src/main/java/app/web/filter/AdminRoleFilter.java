package app.web.filter;

import app.entity.UserRole;

public class AdminRoleFilter extends RoleFilter {
    public AdminRoleFilter(){
        super();
        super.setUserRole(UserRole.ADMIN);
    }
}