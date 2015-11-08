package com.huwcbjones.chat.core.exceptions;

/**
 * Thrown when protocol is not adhered to
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class InvalidProtocolException extends Exception {

    public InvalidProtocolException(String message) {
        super(message);
    }
}
