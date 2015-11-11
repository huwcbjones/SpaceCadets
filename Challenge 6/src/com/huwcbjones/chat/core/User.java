package com.huwcbjones.chat.core;

/**
 * User class
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
class User {

    private String _username;
    private String _name;

    public User(String username, String name) {
        this._username = username;
        this._name = name;
    }

    public String getUsername() {
        return this._username;
    }

    public String getName() {
        return this._name;
    }

}
