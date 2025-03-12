package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.datamodel.User;

public class LoginController implements LoginListener {
    protected final IDatabase mDatabase;
    protected final ISession mSession;
    protected EntityManager mEntityManager;

    public LoginController(IDatabase mDatabase, ISession mSession, EntityManager mEntityManager) {
        this.mDatabase = mDatabase;
        this.mSession = mSession;
        this.mEntityManager = mEntityManager;
    }

    @Override
    public void doLogin(String tag, String password) {
        for (User user : this.mDatabase.getUsers()) {
            String userTag = user.getUserTag();
            String userPassword = user.getUserPassword();
            if(userTag.equals(tag) && userPassword.equals(password)) {
                this.mSession.connect(user);
                break;
            }
        }
    }
}
