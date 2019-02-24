package app.web.filter;

import app.entity.UserRole;

public class AdminRoleFilter extends RoleFilter {
    public AdminRoleFilter(){
        super(UserRole.ADMIN);
    }
}