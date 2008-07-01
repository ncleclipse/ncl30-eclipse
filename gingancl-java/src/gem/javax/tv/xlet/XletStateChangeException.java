/*
 * @(#)XletStateChangeException.java	1.13 05/11/23
 * 
 * Copyright © 2005 Sun Microsystems, Inc. All rights reserved. 
 * Use is subject to license terms.
 * 
 */

package javax.tv.xlet;

/**
 * Signals that a requested Xlet state change failed. This
 * exception is thrown in response to state change calls
 * in the <code>Xlet</code> interface.
 *
 * @see Xlet
 */

public class XletStateChangeException extends Exception {

    /**
     * Constructs an exception with no specified detail message.
     */

    public XletStateChangeException(){  super(); }

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param s the detail message
     */

    public XletStateChangeException(String s){ super(s); }

}
