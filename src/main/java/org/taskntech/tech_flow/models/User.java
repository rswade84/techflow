package org.taskntech.tech_flow.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class User extends AbstractEntity {
    @Id
    @GeneratedValue
    private int ID;


    public User(String name, String email){
        super(name,email);
    }
}
