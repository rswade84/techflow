package org.taskntech.tech_flow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User extends AbstractEntity {

    @Id
    @GeneratedValue
    private int userId;


    public User(String name, String email){
        super(name,email);
    }

    public int getUserId() {
        return userId;
    }
}
