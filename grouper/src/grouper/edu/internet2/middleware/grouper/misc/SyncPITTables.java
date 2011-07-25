/*
 * Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package edu.internet2.middleware.grouper.misc;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import edu.internet2.middleware.grouper.Field;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.app.loader.GrouperLoaderConfig;
import edu.internet2.middleware.grouper.attr.AttributeDef;
import edu.internet2.middleware.grouper.attr.AttributeDefName;
import edu.internet2.middleware.grouper.attr.AttributeDefNameSet;
import edu.internet2.middleware.grouper.attr.assign.AttributeAssign;
import edu.internet2.middleware.grouper.attr.assign.AttributeAssignAction;
import edu.internet2.middleware.grouper.attr.assign.AttributeAssignActionSet;
import edu.internet2.middleware.grouper.attr.value.AttributeAssignValue;
import edu.internet2.middleware.grouper.group.GroupSet;
import edu.internet2.middleware.grouper.permissions.role.RoleSet;
import edu.internet2.middleware.grouper.pit.PITAttributeAssign;
import edu.internet2.middleware.grouper.pit.PITAttributeAssignAction;
import edu.internet2.middleware.grouper.pit.PITAttributeAssignActionSet;
import edu.internet2.middleware.grouper.pit.PITAttributeAssignValue;
import edu.internet2.middleware.grouper.pit.PITAttributeDef;
import edu.internet2.middleware.grouper.pit.PITAttributeDefName;
import edu.internet2.middleware.grouper.pit.PITAttributeDefNameSet;
import edu.internet2.middleware.grouper.pit.PITField;
import edu.internet2.middleware.grouper.pit.PITGroup;
import edu.internet2.middleware.grouper.pit.PITGroupSet;
import edu.internet2.middleware.grouper.pit.PITMember;
import edu.internet2.middleware.grouper.pit.PITMembership;
import edu.internet2.middleware.grouper.pit.PITRoleSet;
import edu.internet2.middleware.grouper.pit.PITStem;
import edu.internet2.middleware.grouper.util.GrouperUtil;

/**
 * @author shilen
 */
public class SyncPITTables {

  /** Whether or not to print out results of what's being done */
  private boolean showResults = true;
  
  /** Whether or not to actually save updates */
  private boolean saveUpdates = true;
  
  /** Whether or not to log details */
  private boolean logDetails = true;
  
  /** Whether or not to send flattened notifications */
  private boolean sendNotifications = true;
  
  /** Whether or not to create a report for GrouperReport */
  private boolean createReport = false;
  
  /** logger */
  private static final Log LOG = GrouperUtil.getLog(SyncPITTables.class);
  
  /** detailed output for grouper report */
  private StringBuilder report = new StringBuilder();
  
  /** whether or not to send flattened notifications for memberships */
  private boolean includeFlattenedMemberships = GrouperLoaderConfig.getPropertyBoolean("changeLog.includeFlattenedMemberships", true);
  
  /** whether or not to send flattened notifications for privileges */
  private boolean includeFlattenedPrivileges = GrouperLoaderConfig.getPropertyBoolean("changeLog.includeFlattenedPrivileges", true);
  
  /** whether or not to send flattened notifications for permissions */
  private boolean includeFlattenedPermissions = GrouperLoaderConfig.getPropertyBoolean("changeLog.includeFlattenedPermissions", true);
  
  /**
   * Whether or not to print out results of what's being done.  Defaults to true.
   * @param showResults
   * @return SyncPITTables
   */
  public SyncPITTables showResults(boolean showResults) {
    this.showResults = showResults;
    return this;
  }
  
  /**
   * Whether or not to actually save updates.  Defaults to true.
   * @param saveUpdates
   * @return SyncPITTables
   */
  public SyncPITTables saveUpdates(boolean saveUpdates) {
    this.saveUpdates = saveUpdates;
    return this;
  }
  
  /**
   * Whether or not to log details.  Defaults to true.
   * @param logDetails
   * @return AddMissingGroupSets
   */
  public SyncPITTables logDetails(boolean logDetails) {
    this.logDetails = logDetails;
    return this;
  }
  
  /**
   * Whether or not to create a report.  Defaults to false.
   * @param createReport
   * @return AddMissingGroupSets
   */
  public SyncPITTables createReport(boolean createReport) {
    this.createReport = createReport;
    return this;
  }
  
  /**
   * Whether or not to send flattened notifications for memberships, privileges, and permissions.  
   * If true, notifications will be based on configuration.  If false, notifications will not be sent
   * regardless of configuration.  Defaults to true.
   * @param sendNotifications
   * @return SyncPITTables
   */
  public SyncPITTables sendFlattenedNotifications(boolean sendNotifications) {
    this.sendNotifications = sendNotifications;
    return this;
  }

  /**
   * Sync all point in time tables
   * @return the number of updates made
   */
  public long syncAllPITTables() {

    GrouperSession session = null;
    long count = 0;

    try {
      session = GrouperSession.startRootSession();
      clearReport();
      
      count += processMissingActivePITFields();
      count += processMissingActivePITMembers();
      count += processMissingActivePITStems();
      count += processMissingActivePITGroups();
      count += processMissingActivePITRoleSets();
      count += processMissingActivePITAttributeDefs();
      count += processMissingActivePITAttributeDefNames();
      count += processMissingActivePITAttributeDefNameSets();
      count += processMissingActivePITAttributeAssignActions();
      count += processMissingActivePITAttributeAssignActionSets();
      count += processMissingActivePITGroupSets();
      count += processMissingActivePITMemberships();
      count += processMissingActivePITAttributeAssigns();
      count += processMissingActivePITAttributeAssignValues();
      
      count += processMissingInactivePITAttributeAssignValues();
      count += processMissingInactivePITAttributeAssigns();
      count += processMissingInactivePITMemberships();
      count += processMissingInactivePITGroupSets();
      count += processMissingInactivePITAttributeAssignActionSets();
      count += processMissingInactivePITAttributeAssignActions();
      count += processMissingInactivePITAttributeDefNameSets();
      count += processMissingInactivePITAttributeDefNames();
      count += processMissingInactivePITAttributeDefs();
      count += processMissingInactivePITRoleSets();
      count += processMissingInactivePITGroups();
      count += processMissingInactivePITStems();
      count += processMissingInactivePITMembers();
      count += processMissingInactivePITFields();
    } finally {
      GrouperSession.stopQuietly(session);
    }
    
    return count;
  }
 
  /**
   * @return detailed output of the sync
   */
  public String getDetailedOutput() {
    return report.toString();
  }
  
  /**
   * clear report
   */
  public void clearReport() {
    report = new StringBuilder();
  }

  /**
   * Add missing point in time memberships.
   * @return the number of missing point in time memberships
   */
  public long processMissingActivePITMemberships() {
    showStatus("\n\nSearching for missing active point in time memberships");
    
    long totalProcessed = 0;

    Set<Membership> mships = GrouperDAOFactory.getFactory().getPITMembership().findMissingActivePITMemberships();
    showStatus("Found " + mships.size() + " missing active point in time memberships");

    for (Membership mship : mships) {
      
      logDetail("Found missing point in time membership with ownerId: " + mship.getOwnerId() + 
          ", memberId: " + mship.getMemberUuid() + ", fieldId: " + mship.getFieldId());
            
      if (saveUpdates) {
        PITMembership pitMembership = new PITMembership();
        pitMembership.setId(mship.getImmediateMembershipId());
        pitMembership.setOwnerGroupId(mship.getOwnerGroupId());
        pitMembership.setOwnerStemId(mship.getOwnerStemId());
        pitMembership.setOwnerAttrDefId(mship.getOwnerAttrDefId());
        pitMembership.setMemberId(mship.getMemberUuid());
        pitMembership.setFieldId(mship.getFieldId());
        pitMembership.setActiveDb("T");
        pitMembership.setStartTimeDb(System.currentTimeMillis() * 1000);

        if (!GrouperUtil.isEmpty(mship.getContextId())) {
          pitMembership.setContextId(mship.getContextId());
        }
        
        if (sendNotifications) {
          pitMembership.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
          pitMembership.setFlatMembershipNotificationsOnSaveOrUpdate(includeFlattenedMemberships);
          pitMembership.setFlatPrivilegeNotificationsOnSaveOrUpdate(includeFlattenedPrivileges);
        }

        pitMembership.save();
      }
      
      totalProcessed++;
    }
    
    if (mships.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time attribute assign.
   * @return the number of missing point in time attribute assigns
   */
  public long processMissingActivePITAttributeAssigns() {
    showStatus("\n\nSearching for missing active point in time attribute assigns");
    
    long totalProcessed = 0;

    Set<AttributeAssign> assigns = GrouperDAOFactory.getFactory().getPITAttributeAssign().findMissingActivePITAttributeAssigns();
    showStatus("Found " + assigns.size() + " missing active point in time attribute assigns");

    // sort to avoid foreign key issues
    LinkedHashSet<AttributeAssign> assignsSorted = new LinkedHashSet<AttributeAssign>();
    for (AttributeAssign assign : assigns) {
      if (assign.getOwnerAttributeAssignId() == null) {
        assignsSorted.add(assign);
      }
    }
    
    for (AttributeAssign assign : assigns) {
      if (assign.getOwnerAttributeAssignId() != null) {
        assignsSorted.add(assign);
      }
    }
    
    for (AttributeAssign assign : assignsSorted) {
      
      logDetail("Found missing point in time attribute assign with id: " + assign.getId());
            
      if (saveUpdates) {
        PITAttributeAssign pitAttributeAssign = new PITAttributeAssign();
        pitAttributeAssign.setId(assign.getId());
        pitAttributeAssign.setAttributeDefNameId(assign.getAttributeDefNameId());
        pitAttributeAssign.setAttributeAssignActionId(assign.getAttributeAssignActionId());
        pitAttributeAssign.setAttributeAssignTypeDb(assign.getAttributeAssignTypeDb());
        pitAttributeAssign.setDisallowedDb(assign.getDisallowedDb());
        pitAttributeAssign.setActiveDb("T");
        pitAttributeAssign.setStartTimeDb(System.currentTimeMillis() * 1000);
        
        pitAttributeAssign.setOwnerGroupId(assign.getOwnerGroupId());
        pitAttributeAssign.setOwnerStemId(assign.getOwnerStemId());
        pitAttributeAssign.setOwnerMemberId(assign.getOwnerMemberId());
        pitAttributeAssign.setOwnerAttributeDefId(assign.getOwnerAttributeDefId());
        pitAttributeAssign.setOwnerMembershipId(assign.getOwnerMembershipId());
        pitAttributeAssign.setOwnerAttributeAssignId(assign.getOwnerAttributeAssignId());
        
        if (!GrouperUtil.isEmpty(assign.getContextId())) {
          pitAttributeAssign.setContextId(assign.getContextId());
        }
                
        if (sendNotifications) {
          pitAttributeAssign.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }

        pitAttributeAssign.save();
      }
      
      totalProcessed++;
    }
    
    if (assigns.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time attribute assign values.
   * @return the number of missing point in time attribute assign values
   */
  public long processMissingActivePITAttributeAssignValues() {
    showStatus("\n\nSearching for missing active point in time attribute assign values");
    
    long totalProcessed = 0;

    Set<AttributeAssignValue> values = GrouperDAOFactory.getFactory().getPITAttributeAssignValue().findMissingActivePITAttributeAssignValues();
    showStatus("Found " + values.size() + " missing active point in time attribute assign values");

    for (AttributeAssignValue value : values) {
      
      logDetail("Found missing point in time attribute assign value with id: " + value.getId() + ", value: " + value.getValueFriendly());
            
      if (saveUpdates) {
        PITAttributeAssignValue pitAttributeAssignValue = new PITAttributeAssignValue();
        pitAttributeAssignValue.setId(value.getId());
        pitAttributeAssignValue.setAttributeAssignId(value.getAttributeAssignId());
        pitAttributeAssignValue.setActiveDb("T");
        pitAttributeAssignValue.setStartTimeDb(System.currentTimeMillis() * 1000);
        
        pitAttributeAssignValue.setValueString(value.getValueString());
        pitAttributeAssignValue.setValueInteger(value.getValueInteger());
        pitAttributeAssignValue.setValueMemberId(value.getValueMemberId());
        pitAttributeAssignValue.setValueFloating(value.getValueFloating());
        
        if (!GrouperUtil.isEmpty(value.getContextId())) {
          pitAttributeAssignValue.setContextId(value.getContextId());
        }

        pitAttributeAssignValue.save();
      }
      
      totalProcessed++;
    }
    
    if (values.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time attribute defs.
   * @return the number of missing point in time attribute defs
   */
  public long processMissingActivePITAttributeDefs() {
    showStatus("\n\nSearching for missing active point in time attribute defs");
    
    long totalProcessed = 0;

    Set<AttributeDef> attrs = GrouperDAOFactory.getFactory().getPITAttributeDef().findMissingActivePITAttributeDefs();
    showStatus("Found " + attrs.size() + " missing active point in time attribute defs");

    for (AttributeDef attr : attrs) {
      
      logDetail("Found missing point in time attribute def with id: " + attr.getId() + ", name: " + attr.getName());
            
      if (saveUpdates) {
        // note that we may just need to update the name and/or stemId
        PITAttributeDef pitAttributeDef = GrouperDAOFactory.getFactory().getPITAttributeDef().findById(attr.getId());
        if (pitAttributeDef == null) {
          pitAttributeDef = new PITAttributeDef();
          pitAttributeDef.setId(attr.getUuid());
          pitAttributeDef.setAttributeDefTypeDb(attr.getAttributeDefTypeDb());
          pitAttributeDef.setActiveDb("T");
          pitAttributeDef.setStartTimeDb(System.currentTimeMillis() * 1000);
        }

        pitAttributeDef.setNameDb(attr.getName());
        pitAttributeDef.setStemId(attr.getStemId());
        
        if (!GrouperUtil.isEmpty(attr.getContextId())) {
          pitAttributeDef.setContextId(attr.getContextId());
        } else {
          pitAttributeDef.setContextId(null);
        }
        
        pitAttributeDef.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (attrs.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time attribute def names.
   * @return the number of missing point in time attribute def names
   */
  public long processMissingActivePITAttributeDefNames() {
    showStatus("\n\nSearching for missing active point in time attribute def names");
    
    long totalProcessed = 0;

    Set<AttributeDefName> attrs = GrouperDAOFactory.getFactory().getPITAttributeDefName().findMissingActivePITAttributeDefNames();
    showStatus("Found " + attrs.size() + " missing active point in time attribute def names");

    for (AttributeDefName attr : attrs) {
      
      logDetail("Found missing point in time attribute def name with id: " + attr.getId() + ", name: " + attr.getName());
            
      if (saveUpdates) {
        // note that we may just need to update the name
        PITAttributeDefName pitAttributeDefName = GrouperDAOFactory.getFactory().getPITAttributeDefName().findById(attr.getId());
        if (pitAttributeDefName == null) {
          pitAttributeDefName = new PITAttributeDefName();
          pitAttributeDefName.setId(attr.getId());
          pitAttributeDefName.setAttributeDefId(attr.getAttributeDefId());
          pitAttributeDefName.setStemId(attr.getStemId());
          pitAttributeDefName.setActiveDb("T");
          pitAttributeDefName.setStartTimeDb(System.currentTimeMillis() * 1000);
        }

        pitAttributeDefName.setNameDb(attr.getNameDb());

        if (!GrouperUtil.isEmpty(attr.getContextId())) {
          pitAttributeDefName.setContextId(attr.getContextId());
        } else {
          pitAttributeDefName.setContextId(null);
        }
        
        pitAttributeDefName.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (attrs.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time attribute def name sets.
   * @return the number of missing point in time attribute def name sets
   */
  public long processMissingActivePITAttributeDefNameSets() {
    showStatus("\n\nSearching for missing active point in time attribute def name sets");
    
    long totalProcessed = 0;

    Set<AttributeDefNameSet> attrSets = GrouperDAOFactory.getFactory().getPITAttributeDefNameSet().findMissingActivePITAttributeDefNameSets();
    showStatus("Found " + attrSets.size() + " missing active point in time attribute def name sets");

    for (AttributeDefNameSet attrSet : attrSets) {
      
      logDetail("Found missing point in time attribute def name set with id: " + attrSet.getId());
            
      if (saveUpdates) {
        PITAttributeDefNameSet pitAttributeDefNameSet = new PITAttributeDefNameSet();
        pitAttributeDefNameSet.setId(attrSet.getId());
        pitAttributeDefNameSet.setDepth(attrSet.getDepth());
        pitAttributeDefNameSet.setIfHasAttributeDefNameId(attrSet.getIfHasAttributeDefNameId());
        pitAttributeDefNameSet.setThenHasAttributeDefNameId(attrSet.getThenHasAttributeDefNameId());
        pitAttributeDefNameSet.setParentAttrDefNameSetId(attrSet.getParentAttrDefNameSetId());
        pitAttributeDefNameSet.setActiveDb("T");
        pitAttributeDefNameSet.setStartTimeDb(System.currentTimeMillis() * 1000);

        if (!GrouperUtil.isEmpty(attrSet.getContextId())) {
          pitAttributeDefNameSet.setContextId(attrSet.getContextId());
        }
        
        if (sendNotifications) {
          pitAttributeDefNameSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }
        
        pitAttributeDefNameSet.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (attrSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time groups.
   * @return the number of missing point in time groups
   */
  public long processMissingActivePITGroups() {
    showStatus("\n\nSearching for missing active point in time groups");
    
    long totalProcessed = 0;

    Set<Group> groups = GrouperDAOFactory.getFactory().getPITGroup().findMissingActivePITGroups();
    showStatus("Found " + groups.size() + " missing active point in time groups");

    for (Group group : groups) {
      
      logDetail("Found missing point in time group with id: " + group.getId() + ", name: " + group.getName());
            
      if (saveUpdates) {
        // note that we may just need to update the name and/or stemId
        PITGroup pitGroup = GrouperDAOFactory.getFactory().getPITGroup().findById(group.getId());
        if (pitGroup == null) {
          pitGroup = new PITGroup();
          pitGroup.setId(group.getUuid());
          pitGroup.setActiveDb("T");
          pitGroup.setStartTimeDb(System.currentTimeMillis() * 1000);
        }
        
        pitGroup.setNameDb(group.getName());  
        pitGroup.setStemId(group.getParentUuid());

        if (!GrouperUtil.isEmpty(group.getContextId())) {
          pitGroup.setContextId(group.getContextId());
        } else {
          pitGroup.setContextId(null);
        }
        
        pitGroup.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (groups.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time group sets.
   * @return the number of missing point in time group sets
   */
  public long processMissingActivePITGroupSets() {
    showStatus("\n\nSearching for missing active point in time group sets");
    
    long totalProcessed = 0;

    Set<GroupSet> groupSets = GrouperDAOFactory.getFactory().getPITGroupSet().findMissingActivePITGroupSets();
    showStatus("Found " + groupSets.size() + " missing active point in time group sets");

    for (GroupSet groupSet : groupSets) {
      
      logDetail("Found missing point in time group set with id: " + groupSet.getId());
            
      if (saveUpdates) {
        PITGroupSet pitGroupSet = new PITGroupSet();
        pitGroupSet.setId(groupSet.getId());
        pitGroupSet.setDepth(groupSet.getDepth());
        pitGroupSet.setParentId(groupSet.getParentId());
        pitGroupSet.setFieldId(groupSet.getFieldId());
        pitGroupSet.setMemberFieldId(groupSet.getMemberFieldId());
        pitGroupSet.setMemberGroupId(groupSet.getMemberGroupId());
        pitGroupSet.setMemberStemId(groupSet.getMemberStemId());
        pitGroupSet.setMemberAttrDefId(groupSet.getMemberAttrDefId());
        pitGroupSet.setOwnerGroupId(groupSet.getOwnerGroupId());
        pitGroupSet.setOwnerAttrDefId(groupSet.getOwnerAttrDefId());
        pitGroupSet.setOwnerStemId(groupSet.getOwnerStemId());
        pitGroupSet.setActiveDb("T");
        pitGroupSet.setStartTimeDb(System.currentTimeMillis() * 1000);

        if (!GrouperUtil.isEmpty(groupSet.getContextId())) {
          pitGroupSet.setContextId(groupSet.getContextId());
        }
        
        if (sendNotifications) {
          pitGroupSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
          pitGroupSet.setFlatMembershipNotificationsOnSaveOrUpdate(includeFlattenedMemberships);
          pitGroupSet.setFlatPrivilegeNotificationsOnSaveOrUpdate(includeFlattenedPrivileges);
        }
        
        pitGroupSet.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (groupSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time role sets.
   * @return the number of missing point in time role sets
   */
  public long processMissingActivePITRoleSets() {
    showStatus("\n\nSearching for missing active point in time role sets");
    
    long totalProcessed = 0;

    Set<RoleSet> roleSets = GrouperDAOFactory.getFactory().getPITRoleSet().findMissingActivePITRoleSets();
    showStatus("Found " + roleSets.size() + " missing active point in time role sets");

    for (RoleSet roleSet : roleSets) {
      
      logDetail("Found missing point in time role set with id: " + roleSet.getId());
            
      if (saveUpdates) {
        PITRoleSet pitRoleSet = new PITRoleSet();
        pitRoleSet.setId(roleSet.getId());
        pitRoleSet.setDepth(roleSet.getDepth());
        pitRoleSet.setIfHasRoleId(roleSet.getIfHasRoleId());
        pitRoleSet.setThenHasRoleId(roleSet.getThenHasRoleId());
        pitRoleSet.setParentRoleSetId(roleSet.getParentRoleSetId());
        pitRoleSet.setActiveDb("T");
        pitRoleSet.setStartTimeDb(System.currentTimeMillis() * 1000);

        if (!GrouperUtil.isEmpty(roleSet.getContextId())) {
          pitRoleSet.setContextId(roleSet.getContextId());
        }
        
        if (sendNotifications) {
          pitRoleSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }
        
        pitRoleSet.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (roleSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time fields.
   * @return the number of missing point in time fields
   */
  public long processMissingActivePITFields() {
    showStatus("\n\nSearching for missing active point in time fields");
    
    long totalProcessed = 0;

    Set<Field> fields = GrouperDAOFactory.getFactory().getPITField().findMissingActivePITFields();
    showStatus("Found " + fields.size() + " missing active point in time fields");

    for (Field field : fields) {
      
      logDetail("Found missing point in time field with id: " + field.getUuid() + ", name: " + field.getName());
            
      if (saveUpdates) {
        // note that we may just need to update the name and/or type
        PITField pitField = GrouperDAOFactory.getFactory().getPITField().findById(field.getUuid());
        if (pitField == null) {
          pitField = new PITField();
          pitField.setId(field.getUuid());
          pitField.setActiveDb("T");
          pitField.setStartTimeDb(System.currentTimeMillis() * 1000);
        }
        
        pitField.setNameDb(field.getName());
        pitField.setTypeDb(field.getTypeString());
        
        if (!GrouperUtil.isEmpty(field.getContextId())) {
          pitField.setContextId(field.getContextId());
        } else {
          pitField.setContextId(null);
        }
        
        pitField.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (fields.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time members.
   * @return the number of missing point in time members
   */
  public long processMissingActivePITMembers() {
    showStatus("\n\nSearching for missing active point in time members");
    
    long totalProcessed = 0;

    Set<Member> members = GrouperDAOFactory.getFactory().getPITMember().findMissingActivePITMembers();
    showStatus("Found " + members.size() + " missing active point in time members");

    for (Member member : members) {
      
      logDetail("Found missing point in time member with id: " + member.getUuid() + ", subject id: " + member.getSubjectId());
            
      if (saveUpdates) {
        // note that we may just need to update the subjectId, subjectSourceId, and/or subjectTypeId
        PITMember pitMember = GrouperDAOFactory.getFactory().getPITMember().findById(member.getUuid());
        if (pitMember == null) {
          pitMember = new PITMember();
          pitMember.setId(member.getUuid());
          pitMember.setActiveDb("T");
          pitMember.setStartTimeDb(System.currentTimeMillis() * 1000);
        }
        
        pitMember.setSubjectId(member.getSubjectIdDb());
        pitMember.setSubjectSourceId(member.getSubjectSourceIdDb());
        pitMember.setSubjectTypeId(member.getSubjectTypeId());
        
        if (!GrouperUtil.isEmpty(member.getContextId())) {
          pitMember.setContextId(member.getContextId());
        } else {
          pitMember.setContextId(null);
        }
        
        pitMember.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (members.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time stems.
   * @return the number of missing point in time stems
   */
  public long processMissingActivePITStems() {
    showStatus("\n\nSearching for missing active point in time stems");
    
    long totalProcessed = 0;

    Set<Stem> stems = GrouperDAOFactory.getFactory().getPITStem().findMissingActivePITStems();
    
    // the root stem may be returned because its parent stem id is null.  if it is returned and exists in point in time, remove it from the set...
    Stem rootStem = StemFinder.findRootStem(GrouperSession.staticGrouperSession());
    if (stems.contains(rootStem) && GrouperDAOFactory.getFactory().getPITStem().findById(rootStem.getUuid()) != null) {
      stems.remove(rootStem);
    }
    
    showStatus("Found " + stems.size() + " missing active point in time stems");

    LinkedHashSet<Stem> stemsSorted = new LinkedHashSet<Stem>();
    if (stems.contains(rootStem)) {
      stemsSorted.add(rootStem);
    }
    
    stemsSorted.addAll(stems);
    
    for (Stem stem : stemsSorted) {
      
      logDetail("Found missing point in time stem with id: " + stem.getUuid() + ", name: " + stem.getName());
            
      if (saveUpdates) {
        // note that we may just need to update the name and/or parentStemId
        PITStem pitStem = GrouperDAOFactory.getFactory().getPITStem().findById(stem.getUuid());
        if (pitStem == null) {
          pitStem = new PITStem();
          pitStem.setId(stem.getUuid());
          pitStem.setActiveDb("T");
          pitStem.setStartTimeDb(System.currentTimeMillis() * 1000);
        }

        pitStem.setNameDb(stem.getNameDb());
        pitStem.setParentStemId(stem.getParentUuid());
        
        if (!GrouperUtil.isEmpty(stem.getContextId())) {
          pitStem.setContextId(stem.getContextId());
        } else {
          pitStem.setContextId(null);
        }
        
        pitStem.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (stems.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time actions.
   * @return the number of missing point in time actions
   */
  public long processMissingActivePITAttributeAssignActions() {
    showStatus("\n\nSearching for missing active point in time actions");
    
    long totalProcessed = 0;

    Set<AttributeAssignAction> actions = GrouperDAOFactory.getFactory().getPITAttributeAssignAction().findMissingActivePITAttributeAssignActions();
    showStatus("Found " + actions.size() + " missing active point in time actions");

    for (AttributeAssignAction action : actions) {
      
      logDetail("Found missing point in time action with id: " + action.getId() + ", name: " + action.getName());
            
      if (saveUpdates) {
        // note that we may just need to update the name
        PITAttributeAssignAction pitAttributeAssignAction = GrouperDAOFactory.getFactory().getPITAttributeAssignAction().findById(action.getId());
        if (pitAttributeAssignAction == null) {
          pitAttributeAssignAction = new PITAttributeAssignAction();
          pitAttributeAssignAction.setId(action.getId());
          pitAttributeAssignAction.setAttributeDefId(action.getAttributeDefId());
          pitAttributeAssignAction.setActiveDb("T");
          pitAttributeAssignAction.setStartTimeDb(System.currentTimeMillis() * 1000); 
        }

        pitAttributeAssignAction.setNameDb(action.getNameDb());

        if (!GrouperUtil.isEmpty(action.getContextId())) {
          pitAttributeAssignAction.setContextId(action.getContextId());
        } else {
          pitAttributeAssignAction.setContextId(null);
        }
        
        pitAttributeAssignAction.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (actions.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * Add missing point in time action sets.
   * @return the number of missing point in time action sets
   */
  public long processMissingActivePITAttributeAssignActionSets() {
    showStatus("\n\nSearching for missing active point in time action sets");
    
    long totalProcessed = 0;

    Set<AttributeAssignActionSet> actionSets = GrouperDAOFactory.getFactory().getPITAttributeAssignActionSet().findMissingActivePITAttributeAssignActionSets();
    showStatus("Found " + actionSets.size() + " missing active point in time action sets");

    for (AttributeAssignActionSet actionSet : actionSets) {
      
      logDetail("Found missing point in time action set with id: " + actionSet.getId());
            
      if (saveUpdates) {
        PITAttributeAssignActionSet pitAttributeAssignActionSet = new PITAttributeAssignActionSet();
        pitAttributeAssignActionSet.setId(actionSet.getId());
        pitAttributeAssignActionSet.setDepth(actionSet.getDepth());
        pitAttributeAssignActionSet.setIfHasAttrAssignActionId(actionSet.getIfHasAttrAssignActionId());
        pitAttributeAssignActionSet.setThenHasAttrAssignActionId(actionSet.getThenHasAttrAssignActionId());
        pitAttributeAssignActionSet.setParentAttrAssignActionSetId(actionSet.getParentAttrAssignActionSetId());
        pitAttributeAssignActionSet.setActiveDb("T");
        pitAttributeAssignActionSet.setStartTimeDb(System.currentTimeMillis() * 1000);
        
        if (!GrouperUtil.isEmpty(actionSet.getContextId())) {
          pitAttributeAssignActionSet.setContextId(actionSet.getContextId());
        }
                
        if (sendNotifications) {
          pitAttributeAssignActionSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }

        pitAttributeAssignActionSet.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (actionSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }

  /**
   * End point in time memberships that are currently active but should be inactive.
   * @return the number of point in time memberships to end
   */
  public long processMissingInactivePITMemberships() {
    showStatus("\n\nSearching for point in time memberships that should be inactive");
 
    long totalProcessed = 0;

    Set<PITMembership> mships = GrouperDAOFactory.getFactory().getPITMembership().findMissingInactivePITMemberships();
    showStatus("Found " + mships.size() + " active point in time memberships that should be inactive");

    for (PITMembership mship : mships) {
      
      logDetail("Found active point in time membership that should be inactive with id: " + mship.getId() + ", ownerId: " + mship.getOwnerId() + 
          ", memberId: " + mship.getMemberId() + ", fieldId: " + mship.getFieldId());
      
      if (saveUpdates) {
        mship.setEndTimeDb(System.currentTimeMillis() * 1000);
        mship.setActiveDb("F");
        mship.setContextId(null);

        if (sendNotifications) {
          mship.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
          mship.setFlatMembershipNotificationsOnSaveOrUpdate(includeFlattenedMemberships);
          mship.setFlatPrivilegeNotificationsOnSaveOrUpdate(includeFlattenedPrivileges);
        }
        
        mship.update();
      }
      
      totalProcessed++;
    }
    
    if (mships.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time attribute assigns that are currently active but should be inactive.
   * @return the number of point in time attribute assigns to end
   */
  public long processMissingInactivePITAttributeAssigns() {
    showStatus("\n\nSearching for point in time attribute assigns that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeAssign> assigns = GrouperDAOFactory.getFactory().getPITAttributeAssign().findMissingInactivePITAttributeAssigns();
    showStatus("Found " + assigns.size() + " active point in time attribute assigns that should be inactive");

    for (PITAttributeAssign assign : assigns) {
      
      logDetail("Found active point in time attribute assign that should be inactive with id: " + assign.getId());
      
      if (saveUpdates) {
        assign.setEndTimeDb(System.currentTimeMillis() * 1000);
        assign.setActiveDb("F");
        assign.setContextId(null);

        if (sendNotifications) {
          assign.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }
        
        assign.update();
      }
      
      totalProcessed++;
    }
    
    if (assigns.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time attribute assign values that are currently active but should be inactive.
   * @return the number of point in time attribute assign values to end
   */
  public long processMissingInactivePITAttributeAssignValues() {
    showStatus("\n\nSearching for point in time attribute assign values that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeAssignValue> values = GrouperDAOFactory.getFactory().getPITAttributeAssignValue().findMissingInactivePITAttributeAssignValues();
    showStatus("Found " + values.size() + " active point in time attribute assign values that should be inactive");

    for (PITAttributeAssignValue value : values) {
      
      logDetail("Found active point in time attribute assign value that should be inactive with id: " + value.getId());
      
      if (saveUpdates) {
        value.setEndTimeDb(System.currentTimeMillis() * 1000);
        value.setActiveDb("F");
        value.setContextId(null);

        value.update();
      }
      
      totalProcessed++;
    }
    
    if (values.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time attribute defs that are currently active but should be inactive.
   * @return the number of point in time attribute defs to end
   */
  public long processMissingInactivePITAttributeDefs() {
    showStatus("\n\nSearching for point in time attribute defs that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeDef> attrs = GrouperDAOFactory.getFactory().getPITAttributeDef().findMissingInactivePITAttributeDefs();
    showStatus("Found " + attrs.size() + " active point in time attribute defs that should be inactive");

    for (PITAttributeDef attr : attrs) {
      
      logDetail("Found active point in time attribute def that should be inactive with id: " + attr.getId() + ", name: " + attr.getName());
      
      if (saveUpdates) {
        attr.setEndTimeDb(System.currentTimeMillis() * 1000);
        attr.setActiveDb("F");
        attr.setContextId(null);
        
        attr.saveOrUpdate();        
      }
      
      totalProcessed++;
    }
    
    if (attrs.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time attribute def names that are currently active but should be inactive.
   * @return the number of point in time attribute def names to end
   */
  public long processMissingInactivePITAttributeDefNames() {
    showStatus("\n\nSearching for point in time attribute def names that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeDefName> attrs = GrouperDAOFactory.getFactory().getPITAttributeDefName().findMissingInactivePITAttributeDefNames();
    showStatus("Found " + attrs.size() + " active point in time attribute def names that should be inactive");

    for (PITAttributeDefName attr : attrs) {
      
      logDetail("Found active point in time attribute def name that should be inactive with id: " + attr.getId() + ", name: " + attr.getName());
      
      if (saveUpdates) {
        attr.setEndTimeDb(System.currentTimeMillis() * 1000);
        attr.setActiveDb("F");
        attr.setContextId(null);
        
        attr.saveOrUpdate();        
      }
      
      totalProcessed++;
    }
    
    if (attrs.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time attribute def name sets that are currently active but should be inactive.
   * @return the number of point in time attribute def name sets to end
   */
  public long processMissingInactivePITAttributeDefNameSets() {
    showStatus("\n\nSearching for point in time attribute def name sets that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeDefNameSet> attrSets = GrouperDAOFactory.getFactory().getPITAttributeDefNameSet().findMissingInactivePITAttributeDefNameSets();
    showStatus("Found " + attrSets.size() + " active point in time attribute def name sets that should be inactive");

    for (PITAttributeDefNameSet attrSet : attrSets) {
      
      logDetail("Found active point in time attribute def name set that should be inactive with id: " + attrSet.getId());
      
      if (saveUpdates) {
        attrSet.setEndTimeDb(System.currentTimeMillis() * 1000);
        attrSet.setActiveDb("F");
        attrSet.setContextId(null);
        
        if (sendNotifications) {
          attrSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }
        
        attrSet.saveOrUpdate();        
      }
      
      totalProcessed++;
    }
    
    if (attrSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time groups that are currently active but should be inactive.
   * @return the number of point in time groups to end
   */
  public long processMissingInactivePITGroups() {
    showStatus("\n\nSearching for point in time groups that should be inactive");
 
    long totalProcessed = 0;

    Set<PITGroup> groups = GrouperDAOFactory.getFactory().getPITGroup().findMissingInactivePITGroups();
    showStatus("Found " + groups.size() + " active point in time groups that should be inactive");

    for (PITGroup group : groups) {
      
      logDetail("Found active point in time group that should be inactive with id: " + group.getId() + ", name: " + group.getName());
      
      if (saveUpdates) {
        group.setEndTimeDb(System.currentTimeMillis() * 1000);
        group.setActiveDb("F");
        group.setContextId(null);
        
        group.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (groups.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time group sets that are currently active but should be inactive.
   * @return the number of point in time group sets to end
   */
  public long processMissingInactivePITGroupSets() {
    showStatus("\n\nSearching for point in time group sets that should be inactive");
 
    long totalProcessed = 0;

    Set<PITGroupSet> groupSets = GrouperDAOFactory.getFactory().getPITGroupSet().findMissingInactivePITGroupSets();
    showStatus("Found " + groupSets.size() + " active point in time group sets that should be inactive");

    for (PITGroupSet groupSet : groupSets) {
      
      logDetail("Found active point in time group set that should be inactive with id: " + groupSet.getId());
      
      if (saveUpdates) {
        groupSet.setEndTimeDb(System.currentTimeMillis() * 1000);
        groupSet.setActiveDb("F");
        groupSet.setContextId(null);
        
        
        if (sendNotifications) {
          groupSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
          groupSet.setFlatMembershipNotificationsOnSaveOrUpdate(includeFlattenedMemberships);
          groupSet.setFlatPrivilegeNotificationsOnSaveOrUpdate(includeFlattenedPrivileges);
        }
        
        groupSet.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (groupSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time role sets that are currently active but should be inactive.
   * @return the number of point in time role sets to end
   */
  public long processMissingInactivePITRoleSets() {
    showStatus("\n\nSearching for point in time role sets that should be inactive");
 
    long totalProcessed = 0;

    Set<PITRoleSet> roleSets = GrouperDAOFactory.getFactory().getPITRoleSet().findMissingInactivePITRoleSets();
    showStatus("Found " + roleSets.size() + " active point in time role sets that should be inactive");

    for (PITRoleSet roleSet : roleSets) {
      
      logDetail("Found active point in time role set that should be inactive with id: " + roleSet.getId());
      
      if (saveUpdates) {
        roleSet.setEndTimeDb(System.currentTimeMillis() * 1000);
        roleSet.setActiveDb("F");
        roleSet.setContextId(null);
        
        if (sendNotifications) {
          roleSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }
        
        roleSet.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (roleSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time fields that are currently active but should be inactive.
   * @return the number of point in time fields to end
   */
  public long processMissingInactivePITFields() {
    showStatus("\n\nSearching for point in time fields that should be inactive");
 
    long totalProcessed = 0;

    Set<PITField> fields = GrouperDAOFactory.getFactory().getPITField().findMissingInactivePITFields();
    showStatus("Found " + fields.size() + " active point in time fields that should be inactive");

    for (PITField field : fields) {
      
      logDetail("Found active point in time field that should be inactive with id: " + field.getId() + ", name: " + field.getName());
      
      if (saveUpdates) {
        field.setEndTimeDb(System.currentTimeMillis() * 1000);
        field.setActiveDb("F");
        field.setContextId(null);
        
        field.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (fields.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time members that are currently active but should be inactive.
   * @return the number of point in time members to end
   */
  public long processMissingInactivePITMembers() {
    showStatus("\n\nSearching for point in time members that should be inactive");
 
    long totalProcessed = 0;

    Set<PITMember> members = GrouperDAOFactory.getFactory().getPITMember().findMissingInactivePITMembers();
    showStatus("Found " + members.size() + " active point in time members that should be inactive");

    for (PITMember member : members) {
      
      logDetail("Found active point in time member that should be inactive with id: " + member.getId() + ", subject id: " + member.getSubjectId());
      
      if (saveUpdates) {
        member.setEndTimeDb(System.currentTimeMillis() * 1000);
        member.setActiveDb("F");
        member.setContextId(null);
        
        member.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (members.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time stems that are currently active but should be inactive.
   * @return the number of point in time stems to end
   */
  public long processMissingInactivePITStems() {
    showStatus("\n\nSearching for point in time stems that should be inactive");
 
    long totalProcessed = 0;

    Set<PITStem> stems = GrouperDAOFactory.getFactory().getPITStem().findMissingInactivePITStems();
    showStatus("Found " + stems.size() + " active point in time stems that should be inactive");

    for (PITStem stem : stems) {
      
      logDetail("Found active point in time stem that should be inactive with id: " + stem.getId() + ", name: " + stem.getName());
      
      if (saveUpdates) {
        stem.setEndTimeDb(System.currentTimeMillis() * 1000);
        stem.setActiveDb("F");
        stem.setContextId(null);
        
        stem.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (stems.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time actions that are currently active but should be inactive.
   * @return the number of point in time actions to end
   */
  public long processMissingInactivePITAttributeAssignActions() {
    showStatus("\n\nSearching for point in time actions that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeAssignAction> actions = GrouperDAOFactory.getFactory().getPITAttributeAssignAction().findMissingInactivePITAttributeAssignActions();
    showStatus("Found " + actions.size() + " active point in time actions that should be inactive");

    for (PITAttributeAssignAction action : actions) {
      
      logDetail("Found active point in time action that should be inactive with id: " + action.getId() + ", name: " + action.getName());
      
      if (saveUpdates) {
        action.setEndTimeDb(System.currentTimeMillis() * 1000);
        action.setActiveDb("F");
        action.setContextId(null);
        
        action.saveOrUpdate();     
      }
      
      totalProcessed++;
    }
    
    if (actions.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }
  
  /**
   * End point in time action sets that are currently active but should be inactive.
   * @return the number of point in time action sets to end
   */
  public long processMissingInactivePITAttributeAssignActionSets() {
    showStatus("\n\nSearching for point in time action sets that should be inactive");
 
    long totalProcessed = 0;

    Set<PITAttributeAssignActionSet> actionSets = GrouperDAOFactory.getFactory().getPITAttributeAssignActionSet().findMissingInactivePITAttributeAssignActionSets();
    showStatus("Found " + actionSets.size() + " active point in time action sets that should be inactive");

    for (PITAttributeAssignActionSet actionSet : actionSets) {
      
      logDetail("Found active point in time action set that should be inactive with id: " + actionSet.getId());
      
      if (saveUpdates) {
        actionSet.setEndTimeDb(System.currentTimeMillis() * 1000);
        actionSet.setActiveDb("F");
        actionSet.setContextId(null);

        if (sendNotifications) {
          actionSet.setFlatPermissionNotificationsOnSaveOrUpdate(includeFlattenedPermissions);
        }
        
        actionSet.saveOrUpdate();
      }
      
      totalProcessed++;
    }
    
    if (actionSets.size() > 0 && saveUpdates) {
      showStatus("Done making " + totalProcessed + " updates");
    }
    
    return totalProcessed;
  }

  private void showStatus(String message) {
    if (showResults) {
      System.out.println(message);
    }
  }
  
  private void logDetail(String detail) {
    if (logDetails) {
      LOG.info(detail);
    }
    
    if (createReport && report != null) {
      report.append(detail + "\n");
    }
  }
}