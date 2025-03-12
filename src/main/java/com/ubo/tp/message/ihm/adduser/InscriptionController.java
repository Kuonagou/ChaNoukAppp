package com.ubo.tp.message.ihm.adduser;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.datamodel.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InscriptionController {

    protected final IDatabase mDatabase;

    protected final EntityManager mEntityManager;

    public InscriptionController(IDatabase mDatabase, EntityManager mEntityManager) {
        this.mDatabase = mDatabase;
        this.mEntityManager = mEntityManager;
    }

    public void addUser(String nom, String password, String tag, String avatar) {
        Set<User> users = this.mDatabase.getUsers();
        boolean isNewUserValid = !nom.isEmpty() && !tag.isEmpty();
        for (User user : users) {
            if(user.getUserTag().equals(tag)) {
                isNewUserValid = false;
                break;
            }
        }

        if(isNewUserValid) {
            User newUser = new User(UUID.randomUUID(), tag, password, nom, new HashSet<>(), avatar);
            this.mEntityManager.writeUserFile(newUser);
        }
    }
}
