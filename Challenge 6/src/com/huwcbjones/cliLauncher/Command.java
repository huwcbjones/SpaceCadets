package com.huwcbjones.cliLauncher;

import java.util.ArrayList;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public abstract class Command {

    protected int _numberOfParams = 0;
    protected ArrayList<String> _params;

    public Command(ArrayList<String> params){
        _params = params;
    }

    public abstract void execute();
    public abstract String getHelp();

    /**
     * Number of Parameters argument takes
     * @return Number of Params
     */
    public int getNumberOfParams(){
        return this._numberOfParams;
    }
}
