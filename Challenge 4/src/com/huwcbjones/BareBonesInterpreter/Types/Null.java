package com.huwcbjones.BareBonesInterpreter.Types;

/**
 * Created by Huw on 24/10/2015.
 */
public class Null implements Type {
    @Override
    public Object getType() {
        return null;
    }
}
