/*--
$Id: JDBCSubject.java,v 1.4 2006-11-21 18:51:29 ddonn Exp $
$Date: 2006-11-21 18:51:29 $
 
Copyright 2005 Internet2 and Stanford University.  All Rights Reserved.
See doc/license.txt in this distribution.
 */
package edu.internet2.middleware.subject.provider;

import java.util.Set;
import java.util.Map;
import java.lang.String;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectType;


/**
 * JDBC Subject implementation.
 */
public class JDBCSubject
        implements Subject {
    
    
    private static Log log = LogFactory.getLog(JDBCSubject.class);
    
    protected JDBCSourceAdapter adapter;
    
    protected String id;
    protected String name;
    protected String description;
    protected SubjectType type;
    protected Map attributes;
    
    /** Public default constructor. It allows subclassing of JDBCSubject! */
    public JDBCSubject()
    {
    	id = null;
    	name = null;
    	description = null;
    	type = null;
    	attributes = null;
    }

        /*
         * Constructor called by SourceManager.
         */
    public JDBCSubject(String id, String name, String description,
            SubjectType type, JDBCSourceAdapter adapter) {
        log.debug("Name = "  + name);
        this.id = id;
        this.name = name;
        this.type = type;
        this.description =description;
        this.adapter = adapter;
    }
    

    /**
     * Constructor that takes the subject's attributes. Needed because the
     * setAttributes() method is protected.
     * @param id The subject ID
     * @param name The subject name
     * @param description The subject description
     * @param type The subject type
     * @param adapter The SourceAdapter
     * @param attributes The subject attributes
     */
    public JDBCSubject(String id, String name, String description,
            SubjectType type, JDBCSourceAdapter adapter,
            Map attributes)
    {
    	this(id, name, description, type, adapter);
        setAttributes(attributes);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * {@inheritDoc}
     */
    public SubjectType getType() {
        return this.type;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * {@inheritDoc}
     */
    
    public String getDescription() {
        return this.description;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getAttributeValue(String name) {
        if (attributes == null) {
            log.error("No attributes.");
        }
        Set values = (Set)this.attributes.get(name);
        if (values != null) {
            return ((String[])values.toArray(new String[0]))[0];
        } else {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Set getAttributeValues(String name) {
        if (attributes == null) {
            log.error("No attributes.");
        }
        return (Set)this.attributes.get(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map getAttributes() {
        if (attributes == null) {
            //this.adapter.loadAttributes(this);
        }
        return attributes;
    }
    
    /**
     * {@inheritDoc}
     */
    public Source getSource() {
        return this.adapter;
    }
    
    protected void setAttributes(Map attributes) {
        this.attributes = attributes;
    }
}