package com.huwcbjones;

import org.jsoup.Jsoup;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;

public class SCChallengeEmail {

    private String _userID = "";
    private String _uri = "";
    private Document _doc;

    public static void main(String[] args) {
        SCChallengeEmail challenge = new SCChallengeEmail();
        challenge.getID();
    }

    public void getID() {
        // Keep prompting for a good User ID
        while (!_getUserID()) {
        }
        String uri = _getURI();
        if (uri.length() == 0) {
            System.out.println("Failed to create URI");
            return;
        }
        this._uri = uri;

        if (!_fetchDoc()) {
            return;
        }

        String name = _getName();
        if (   name.length() == 0
            || name.equals("ECS People")) {
            System.out.println("Profile for \"" + this._userID + "\" not found, is it private?");
            return;
        }
        System.out.println("Name:  " + name);
        System.out.println("Email: " + this._userID + "@ecs.soton.ac.uk");
    }

    private boolean _getUserID() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Fatal error! Console not found");
            System.exit(-1);
            return false;
        }
        String userID = console.readLine("Enter user ID: ");

        // Check for blank string
        if (userID.length() == 0) {
            System.out.println("User ID must not be blank.");
            return false;
        }

        // Simple regex to check for alphanumeric characters
        if (userID.matches("^([A-z\\d])+$")) {

            // Change userID to lower for consistency
            this._userID = userID.toLowerCase();
            return true;
        } else {
            System.out.println("User ID can only contain letters and numbers.");
        }

        return false;
    }

    private String _getURI() {
        try {
            String encUserID = URLEncoder.encode(this._userID, StandardCharsets.UTF_8.toString());
            String uri = "http://www.ecs.soton.ac.uk/people/" + encUserID;
            return uri;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    private boolean _fetchDoc() {
        try {
            this._doc = Jsoup.connect(this._uri)
                    .userAgent("Space Cadets Crawler")
                    .get();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    private String _getName() {
        try {
            return this._doc.getElementById("uos-sia-page-title").text().trim();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }
}
