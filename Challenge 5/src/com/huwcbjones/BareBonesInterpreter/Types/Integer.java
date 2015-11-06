package com.huwcbjones.BareBonesInterpreter.Types;

import com.huwcbjones.BareBonesInterpreter.Lib.Types;

/**
 * Created by Huw on 23/10/2015.
 */
public class Integer implements Type {

    @Override
    public java.lang.Object getType() {
        return Types.INTEGER;
    }

    @Override
    public String toString() {
        return "Type({" + Types.INTEGER + "})";
    }
}
