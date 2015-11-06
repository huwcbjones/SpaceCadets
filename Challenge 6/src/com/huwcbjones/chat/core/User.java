package com.huwcbjones.chat.core;

/**
 * Class that represents the user
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class User {

    private String _name;

    public User(String name){
        this._name = name;
    }

    public String getName(){
        return this._name;
    }
}
