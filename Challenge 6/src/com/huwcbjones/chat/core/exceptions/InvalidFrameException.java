package com.huwcbjones.chat.core.exceptions;

/**
 * Thrown when an invalid frame is received
 *
 * @author Huw Jones
 * @since 07/11/2015
 */
public class InvalidFrameException extends Exception {
    public InvalidFrameException(){
        super("An invalid frame was received.");
    }
}
