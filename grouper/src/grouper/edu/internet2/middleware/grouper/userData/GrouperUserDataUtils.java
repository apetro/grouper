/*******************************************************************************
 * Copyright 2012 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * @author mchyzer
 * $Id$
 */
package edu.internet2.middleware.grouper.userData;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupSave;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.attr.AttributeDefName;
import edu.internet2.middleware.grouper.cache.GrouperCache;
import edu.internet2.middleware.grouper.misc.GrouperCheckConfig;
import edu.internet2.middleware.grouper.misc.GrouperDAOFactory;
import edu.internet2.middleware.grouper.privs.AccessPrivilege;
import edu.internet2.middleware.grouper.privs.PrivilegeHelper;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.subject.Subject;


/**
 * utility methods and constants for ldap loader
 */
public class GrouperUserDataUtils {

  /**
   * cache the group that the memberships are in
   */
  private static GrouperCache<String, Group> userDataGroupCache = null;
  
  /**
   * get the cache for groups and init if needed
   * @return the cache
   */
  private static GrouperCache<String, Group> userDataGroupCache() {
    if (userDataGroupCache == null) {
      synchronized (GrouperUserDataUtils.class) {
        if (userDataGroupCache == null) {
          userDataGroupCache = new GrouperCache(GrouperUserDataUtils.class.getName() + ".userDataGroupCache");
        }        
      }
    }
    return userDataGroupCache;
  }
  
  /**
   * cache the memberships for a group and subject.  MultiKey is the group name, subject source, and subject
   */
  private static GrouperCache<String, Group> userDataMembershipCache = null;
  
  /**
   * get the cache for groups and init if needed
   * @return the cache
   */
  private static GrouperCache<String, Group> userDataMembershipCache() {
    if (userDataGroupCache == null) {
      synchronized (GrouperUserDataUtils.class) {
        if (userDataGroupCache == null) {
          userDataGroupCache = new GrouperCache(GrouperUserDataUtils.class.getName() + ".userDataGroupCache");
        }        
      }
    }
    return userDataGroupCache;
  }
  
  /**
   * get the group that user data uses, will cache this for 10 minutes by default
   * @param groupName
   * @return the group
   */
  private static Group userDataGroup(String groupName) {
 
    GrouperSession grouperSession = GrouperSession.staticGrouperSession(true);
    if (!PrivilegeHelper.isRoot(grouperSession)) {
      throw new RuntimeException("Grouper session must be root in user data! " + GrouperUtil.subjectToString(grouperSession.getSubject()));
    }
    
    Group group = userDataGroupCache().get(groupName);
    
    if (group == null) {
      
      synchronized (GrouperUserDataUtils.class) {
        group = userDataGroupCache().get(groupName);
        
        if (group == null) {
          
          group = new GroupSave(grouperSession).assignName(groupName).assignDescription(
              "Internal group for grouper which has user data stored in membership attributes for " + GrouperUtil.extensionFromName(groupName) ).save();

          //this is an internal group, other people do not need to read or view it
          group.revokePriv(SubjectFinder.findAllSubject(), AccessPrivilege.READ, false);
          group.revokePriv(SubjectFinder.findAllSubject(), AccessPrivilege.VIEW, false);
          
          userDataGroupCache().put(groupName, group);
          
        }
      }
      
    }
    
    return group;
    
  }
  
  
  
  /**
   * @param subjectToAddTo
   * @param userDataGroupName
   * @param groupUuid
   */
  public static void addFavoriteGroup(String userDataGroupName, Subject subjectToAddTo, String groupUuid) {
    
    
    
    
  }
  
  /** user data def extension */
  public static final String USER_DATA_DEF = "grouperUserDataDef";

  /** user data value def extension */
  public static final String USER_DATA_VALUE_DEF = "grouperUserDataValueDef";
  
  
  /** stem name of user data attributes */
  private static String grouperUserDataStemName = null;
  
  /**
   * stem name for user data attributes
   * @return stem name
   */
  public static String grouperUserDataStemName() {
    if (grouperUserDataStemName == null) {
      grouperUserDataStemName = GrouperCheckConfig.attributeRootStemName() + ":userData";
    }
    return grouperUserDataStemName;
  }
  
  /** extension of the attribute def name for the marker attribute for grouper user data */
  public static final String ATTR_DEF_EXTENSION_MARKER = "grouperUserData";

  /** attribute def name of marker */
  private static String grouperUserDataName;
  
  /**
   * attribute def name of marker attribute
   * @return name
   */
  public static String grouperUserDataName() {
    if (grouperUserDataName == null) {
      grouperUserDataName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_MARKER;
    }
    return grouperUserDataName;
  }
  
  /**
   * return attribute def name for attribute type marker
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataAttributeDefName() {
    return grouperUserDataAttributeDefName(true);
    
  }
  /**
   * return attribute def name for attribute type marker
   * @param exceptionIfNotFound 
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataAttributeDefName(boolean exceptionIfNotFound) {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataName(), exceptionIfNotFound);
  }

  /** extension of the attribute def name for favorite groups */
  public static final String ATTR_DEF_EXTENSION_FAVORITE_GROUPS = "grouperUserDataFavoriteGroups";

  /** attribute def name of favorite groups */
  private static String grouperUserDataFavoriteGroupsName;
  
  /**
   * attribute def name of favorite groups
   * @return name
   */
  public static String grouperUserDataFavoriteGroupsName() {
    if (grouperUserDataFavoriteGroupsName == null) {
      grouperUserDataFavoriteGroupsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_FAVORITE_GROUPS;
    }
    return grouperUserDataFavoriteGroupsName;
  }
  
  /**
   * return attribute def name for attribute favorite groups
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataFavoriteGroupsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataFavoriteGroupsName(), true);
  }


  /** extension of the attribute def name for recent groups */
  public static final String ATTR_DEF_EXTENSION_RECENT_GROUPS = "grouperUserDataRecentGroups";

  /** attribute def name of recent groups */
  private static String grouperUserDataRecentGroupsName;
  
  /**
   * attribute def name of recent groups
   * @return name
   */
  public static String grouperUserDataRecentGroupsName() {
    if (grouperUserDataRecentGroupsName == null) {
      grouperUserDataRecentGroupsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_RECENT_GROUPS;
    }
    return grouperUserDataRecentGroupsName;
  }
  
  /**
   * return attribute def name for attribute recent groups
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataRecentGroupsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataRecentGroupsName(), true);
  }

  
  /** extension of the attribute def name for recent stems */
  public static final String ATTR_DEF_EXTENSION_RECENT_STEMS = "grouperUserDataRecentStems";

  /** attribute def name of recent stems */
  private static String grouperUserDataRecentStemsName;
  
  /**
   * attribute def name of recent stems
   * @return name
   */
  public static String grouperUserDataRecentStemsName() {
    if (grouperUserDataRecentStemsName == null) {
      grouperUserDataRecentStemsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_RECENT_STEMS;
    }
    return grouperUserDataRecentStemsName;
  }
  
  /**
   * return attribute def name for attribute recent stems
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataRecentStemsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataRecentStemsName(), true);
  }


  /** extension of the attribute def name for favorite stems */
  public static final String ATTR_DEF_EXTENSION_FAVORITE_STEMS = "grouperUserDataFavoriteStems";

  /** attribute def name of favorite stems */
  private static String grouperUserDataFavoriteStemsName;
  
  /**
   * attribute def name of favorite stems
   * @return name
   */
  public static String grouperUserDataFavoriteStemsName() {
    if (grouperUserDataFavoriteStemsName == null) {
      grouperUserDataFavoriteStemsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_FAVORITE_STEMS;
    }
    return grouperUserDataFavoriteStemsName;
  }
  
  /**
   * return attribute def name for attribute favorite stems
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataFavoriteStemsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataFavoriteStemsName(), true);
  }

  
  
  
  
  /** extension of the attribute def name for recent attributeDefs */
  public static final String ATTR_DEF_EXTENSION_RECENT_ATTIRBUTE_DEFS = "grouperUserDataRecentAttributeDefs";

  /** attribute def name of recent attributeDefs */
  private static String grouperUserDataRecentAttributeDefsName;
  
  /**
   * attribute def name of recent attributeDefs
   * @return name
   */
  public static String grouperUserDataRecentAttributeDefsName() {
    if (grouperUserDataRecentAttributeDefsName == null) {
      grouperUserDataRecentAttributeDefsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_RECENT_ATTIRBUTE_DEFS;
    }
    return grouperUserDataRecentAttributeDefsName;
  }
  
  /**
   * return attribute def name for attribute recent attributeDefs
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataRecentAttributeDefsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataRecentAttributeDefsName(), true);
  }


  /** extension of the attribute def name for favorite attributeDefs */
  public static final String ATTR_DEF_EXTENSION_FAVORITE_ATTIRBUTE_DEFS = "grouperUserDataFavoriteAttributeDefs";

  /** attribute def name of favorite attributeDefs */
  private static String grouperUserDataFavoriteAttributeDefsName;
  
  /**
   * attribute def name of favorite attributeDefs
   * @return name
   */
  public static String grouperUserDataFavoriteAttributeDefsName() {
    if (grouperUserDataFavoriteAttributeDefsName == null) {
      grouperUserDataFavoriteAttributeDefsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_FAVORITE_ATTIRBUTE_DEFS;
    }
    return grouperUserDataFavoriteAttributeDefsName;
  }
  
  /**
   * return attribute def name for attribute favorite attributeDefs
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataFavoriteAttributeDefsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataFavoriteAttributeDefsName(), true);
  }

  
  /** extension of the attribute def name for recent attributeDefNames */
  public static final String ATTR_DEF_EXTENSION_RECENT_ATTRIBUTE_DEF_NAMES = "grouperUserDataRecentAttributeDefNames";

  /** attribute def name of recent attributeDefNames */
  private static String grouperUserDataRecentAttributeDefNamesName;
  
  /**
   * attribute def name of recent attributeDefNames
   * @return name
   */
  public static String grouperUserDataRecentAttributeDefNamesName() {
    if (grouperUserDataRecentAttributeDefNamesName == null) {
      grouperUserDataRecentAttributeDefNamesName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_RECENT_ATTRIBUTE_DEF_NAMES;
    }
    return grouperUserDataRecentAttributeDefNamesName;
  }
  
  /**
   * return attribute def name for attribute recent attributeDefNames
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataRecentAttributeDefNamesAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataRecentAttributeDefNamesName(), true);
  }


  /** extension of the attribute def name for favorite attributeDefNames */
  public static final String ATTR_DEF_EXTENSION_FAVORITE_ATTRIBUTE_DEF_NAMES = "grouperUserDataFavoriteAttributeDefNames";

  /** attribute def name of favorite attributeDefNames */
  private static String grouperUserDataFavoriteAttributeDefNamesName;
  
  /**
   * attribute def name of favorite attributeDefNames
   * @return name
   */
  public static String grouperUserDataFavoriteAttributeDefNamesName() {
    if (grouperUserDataFavoriteAttributeDefNamesName == null) {
      grouperUserDataFavoriteAttributeDefNamesName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_FAVORITE_ATTRIBUTE_DEF_NAMES;
    }
    return grouperUserDataFavoriteStemsName;
  }
  
  /**
   * return attribute def name for attribute favorite attributeDefNames
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataFavoriteAttributeDefNamesAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataFavoriteStemsName(), true);
  }


  
  
  /** extension of the attribute def name for recent subjects */
  public static final String ATTR_DEF_EXTENSION_RECENT_SUBJECTS = "grouperUserDataRecentSubjects";

  /** attribute def name of recent subjects */
  private static String grouperUserDataRecentSubjectsName;
  
  /**
   * attribute def name of recent subjects
   * @return name
   */
  public static String grouperUserDataRecentSubjectsName() {
    if (grouperUserDataRecentSubjectsName == null) {
      grouperUserDataRecentSubjectsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_RECENT_SUBJECTS;
    }
    return grouperUserDataRecentSubjectsName;
  }
  
  /**
   * return attribute def name for attribute recent subjects
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataRecentSubjectsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataRecentSubjectsName(), true);
  }


  /** extension of the attribute def name for favorite subjects */
  public static final String ATTR_DEF_EXTENSION_FAVORITE_SUBJECTS = "grouperUserDataFavoriteSubjects";

  /** attribute def name of favorite subjects */
  private static String grouperUserDataFavoriteSubjectsName;
  
  /**
   * attribute def name of favorite subjects
   * @return name
   */
  public static String grouperUserDataFavoriteSubjectsName() {
    if (grouperUserDataFavoriteSubjectsName == null) {
      grouperUserDataFavoriteSubjectsName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_FAVORITE_SUBJECTS;
    }
    return grouperUserDataFavoriteSubjectsName;
  }
  
  /**
   * return attribute def name for attribute favorite subjects
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataFavoriteSubjectsAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataFavoriteSubjectsName(), true);
  }



  /** extension of the attribute def name for preferences */
  public static final String ATTR_DEF_EXTENSION_PREFERENCES = "grouperUserDataPreferences";

  /** attribute def name of preferences */
  private static String grouperUserDataPreferencesName;
  
  /**
   * attribute def name of preferences
   * @return name
   */
  public static String grouperUserDataPreferencesName() {
    if (grouperUserDataPreferencesName == null) {
      grouperUserDataPreferencesName = grouperUserDataStemName() + ":" + ATTR_DEF_EXTENSION_PREFERENCES;
    }
    return grouperUserDataPreferencesName;
  }
  
  /**
   * return attribute def name for attribute preferences
   * @return attribute def name
   */
  public static AttributeDefName grouperUserDataPreferencesAttributeDefName() {
    return GrouperDAOFactory.getFactory().getAttributeDefName().findByNameSecure(grouperUserDataPreferencesName(), true);
  }


  
  
}