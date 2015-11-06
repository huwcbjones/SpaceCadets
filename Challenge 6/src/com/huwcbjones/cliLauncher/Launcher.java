package com.huwcbjones.cliLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Launches an App from CLI
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Launcher {
    private String _name;
    private String _version;
    private ArrayList _launchArgs;
    private HashMap<String, Command> _commands = new HashMap<>();

    public Launcher(String[] launchArgs) {
        this._launchArgs = new ArrayList(Arrays.asList(launchArgs));
    }

    /**
     * Sets Program name
     * @param name Name
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * Sets Program version
     * @param version Version
     */
    public void setVersion(String version) {
        this._version = version;
    }

    public void registerCommand(String handle, Command command) {
        this._commands.put(handle, command);
    }
}
