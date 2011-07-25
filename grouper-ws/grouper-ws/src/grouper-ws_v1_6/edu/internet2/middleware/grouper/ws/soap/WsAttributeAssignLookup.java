/**
 * 
 */
package edu.internet2.middleware.grouper.ws.soap;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * Class to lookup an attribute assignment via web service
 * 
 * developers make sure each setter calls this.clearAttributeAssignment();
 * </pre>
 * @author mchyzer
 */
public class WsAttributeAssignLookup {

  /**
   * see if this attributeAssign lookup has data
   * @return true if it has data
   */
  public boolean hasData() {
    return !StringUtils.isBlank(this.uuid);
  }
  
  /**
   * uuid of the attributeAssign to find
   */
  private String uuid;

  /**
   * uuid of the attributeAssign to find
   * @return the uuid
   */
  public String getUuid() {
    return this.uuid;
  }

  /**
   * uuid of the attributeAssign to find
   * @param uuid1 the uuid to set
   */
  public void setUuid(String uuid1) {
    this.uuid = uuid1;
  }

  /**
   * 
   */
  public WsAttributeAssignLookup() {
    //blank
  }

}