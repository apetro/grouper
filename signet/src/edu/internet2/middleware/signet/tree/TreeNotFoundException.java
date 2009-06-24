/*--
$Id: TreeNotFoundException.java,v 1.2 2004-12-24 04:15:46 acohen Exp $
$Date: 2004-12-24 04:15:46 $

Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
Licensed under the Signet License, Version 1,
see doc/license.txt in this distribution.
*/
package edu.internet2.middleware.signet.tree;

/**
Used to indicate that a requested Tree is not found in the data source.
*/
public class TreeNotFoundException extends Exception
{
/**
 * 
 */
public TreeNotFoundException()
{
  super();
  // TODO Auto-generated constructor stub
}

/**
 * @param cause
 */
public TreeNotFoundException(Throwable cause)
{
  super(cause);
  // TODO Auto-generated constructor stub
}

public TreeNotFoundException(String msg)
{
	super(msg);
}

public TreeNotFoundException(String msg, Throwable cause)
{
	super(msg, cause);
}
}