package com.huwcbjones.helper;

/**
 * Created by Huw on 17/10/2015.
 */
public class ComboItem {

    private Integer _value;
    private String _label;

    public ComboItem(int value, String label){
        this._value = value;
        this._label = label;
    }

    public int getValue(){
        return this._value;
    }

    public String getLabel(){
        return this._label;
    }

    @Override
    public String toString(){
        return this._label;
    }


}
