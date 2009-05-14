/*--
$Id: Reason.java,v 1.9 2006-05-09 01:33:33 ddonn Exp $
$Date: 2006-05-09 01:33:33 $

Copyright 2006 Internet2, Stanford University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package edu.internet2.middleware.signet;

import java.util.Hashtable;
import java.util.NoSuchElementException;
import edu.internet2.middleware.signet.resource.ResLoaderUI;

/**
 * This is a typesafe enumeration that identifies the various reasons a
 * Signet operation may be disallowed.
 */
public class Reason implements ITypeSafeEnum
{
	/////////////////////////////////
	// static
	/////////////////////////////////

	/** implements Serializable */
	private static final long serialVersionUID = 1L;

	/** Keeps track of all instances by name, for efficient lookup. Use Hashtable
	 * to disallow null values and also support synchronized access.
	*/
	private static final Hashtable instancesByName;

	/**
	 * The instance that describes an attempt to modify an INACTIVE
	 * {@link Assignment} or {@link Proxy}.
	 */
	public static final Reason STATUS;

	/**
	 * The instance that describes an attempt to modify one's own
	 * {@link Assignment} or {@link Proxy}.
	 */
	public static final Reason SELF;

	/**
	 * The instance that describes an attempt to modify an {@link Assignment}
	 * beyond the reach of one's own {@link Limit}s.
	 */
	public static final Reason LIMIT;

	/**
	 * The instance that describes an attempt to modify an {@link Assignment}
	 * which involves a {@link Function} that one has no grantable
	 * <code>Assignment</code>s for.
	 */
	public static final Reason FUNCTION;

	/**
	 * The instance that describes an attempt to modify an {@link Assignment}
	 * which involves a scope that one has no grantable <code>Assignment</code>s
	 * over for the specified {@link Function}.
	 */
	public static final Reason SCOPE;

	/**
	 * The instance that describes an attempt to extend a {@link Proxy} which
	 * cannot be extended to a third party.
	 */
	public static final Reason CANNOT_EXTEND;

	/**
	 * The instance that describes an attempt to exercise a {@link Proxy}
	 * which cannot be used.
	 */
	public static final Reason CANNOT_USE;

	/**
	 * The instance that describes an attempt to exercise a {@link Proxy}
	 * which does not exist.
	 */
	public static final Reason NO_PROXY;

	static
	{
		instancesByName = new Hashtable(8, 1.0f);

		STATUS = new Reason("status", ResLoaderUI.getString("Reason.status.txt"));
		instancesByName.put(STATUS.name, STATUS);
		
		SELF = new Reason("self", ResLoaderUI.getString("Reason.self.txt"));
		instancesByName.put(SELF.name, SELF);

		LIMIT = new Reason("limit", ResLoaderUI.getString("Reason.limit.txt"));
		instancesByName.put(LIMIT.name, LIMIT);

		FUNCTION = new Reason("function", ResLoaderUI.getString("Reason.function.txt"));
		instancesByName.put(FUNCTION.name, FUNCTION);

		SCOPE = new Reason("scope", ResLoaderUI.getString("Reason.scope.txt"));
		instancesByName.put(SCOPE.name, SCOPE);

		CANNOT_EXTEND = new Reason("cannot extend", ResLoaderUI.getString("Reason.cannot_extend.txt"));
		instancesByName.put(CANNOT_EXTEND.name, CANNOT_EXTEND);

		CANNOT_USE = new Reason("cannot use", ResLoaderUI.getString("Reason.cannot_use.txt"));
		instancesByName.put(CANNOT_USE.name, CANNOT_USE);

		NO_PROXY = new Reason("no proxy", ResLoaderUI.getString("Reason.no_proxy.txt"));
		instancesByName.put(NO_PROXY.name, NO_PROXY);
	}

	/* (non-Javadoc)
	 * @see edu.internet2.middleware.signet.ITypeSafeEnum#getInstanceByName(java.lang.String)
	 */
	public static Object getInstanceByName(String name) throws NoSuchElementException
	{
		Reason result = (Reason)instancesByName.get(name);
		if (null == result)
			throw new NoSuchElementException(name);
		return (result);
	}


	////////////////////////////////
	// instance
	////////////////////////////////

	/**
	 * Stores the external name of this instance, by which it can be retrieved.
	 */
	private final String	name;

	/**
	 * Stores the human-readable description of this instance, by which
	 * it is identified in the user interface.
	 */
	private final transient String	description;

	/**
	 * Constructor is private to prevent instantiation except during class loading.
	 * @param name The external name of the DataType value.
	 * @param description The human-readable description of the DataType value;
	 * 	presented in the user interface.
	 */
	private Reason(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	////////////////////////////
	// implements ITypeSafeEnum
	////////////////////////////

	/**
	 * Return the external name associated with this instance.
	 * @return the name by which this instance is identified in code.
	 */
	public String getName() { return name; }

	/**
	 * Return the description associated with this instance.
	 * @return the human-readable description by which this instance is identified in the user interface.
	 */
	public String getHelpText() { return description; }


	//////////////////////////
	// Serializable support
	//////////////////////////

	/** Insure that deserialization preserves the signleton property. */
	private Object readResolve() { return (getInstanceByName(name)); }


	////////////////////////////
	// overrides Object
	////////////////////////////

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException
			("Instances of type-safe enumerations are singletons and cannot be cloned.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() { return (name); }

}