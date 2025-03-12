package com.ubo.tp.message.core.user;

import com.ubo.tp.message.datamodel.User;

import java.util.List;

public interface IUserObserver {

    void notifyUserListChanged(List<User> newUsersList);
}
