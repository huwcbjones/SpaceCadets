package com.huwcbjones.chat.core;

/**
 * Message Target
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Target {
    private int _target;

    public Target(int target){
        this._target = target;
    }

    /**
     * Returns target for message
     * @return Target
     */
    public int getTarget(){
        return this._target;
    }
}
