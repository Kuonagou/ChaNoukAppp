package com.ubo.tp.message.core.user;

import com.ubo.tp.message.datamodel.User;

import java.util.List;

public interface IUserSearchObserver {
    void notifyNewUserSearch(List<User> searchUsersList);
}
