package org.example.avtodiller.utils;

import org.example.avtodiller.models.RoleModel;
import org.example.avtodiller.models.UserModel;

import java.util.List;

public class UsersAndRoles {
    public List<UserModel> users;
    public List<RoleModel> roles;

    public UsersAndRoles(List<UserModel> users, List<RoleModel> roles) {
        this.users = users;
        this.roles = roles;
    }
}
