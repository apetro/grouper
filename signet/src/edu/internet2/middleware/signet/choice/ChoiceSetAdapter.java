/*--
$Id: ChoiceSetAdapter.java,v 1.3 2005-03-07 18:55:44 acohen Exp $
$Date: 2005-03-07 18:55:44 $

Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
Licensed under the Signet License, Version 1,
see doc/license.txt in this distribution.
*/

package edu.internet2.middleware.signet.choice;

import edu.internet2.middleware.signet.AdapterUnavailableException;

/**
 * This interface should be implemented by anyone who wants to use
 * some ChoiceSet implementation other than the default,
 * database-persistent one provided with Signet.
 */
public interface ChoiceSetAdapter
{
  /**
   * Gets a single ChoiceSet by ID.
   * 
   * @param id
   * @return the specified ChoiceSet
   * @throws ChoiceSetNotFound
   */
	public ChoiceSet getChoiceSet(String id)
		throws ChoiceSetNotFound;
	
	/**
	 * Must be called to initialize a newly instantiated
	 * ChoiceSetAdapter.
	 * 
	 * @throws AdapterUnavailableException
	 */
	public void init()
		throws AdapterUnavailableException;
}