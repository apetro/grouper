/**
 * Copyright 2014 Internet2
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
 */
/**
 * 
 */
package edu.internet2.middleware.grouper.attr;

import junit.textui.TestRunner;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.MembershipFinder;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemSave;
import edu.internet2.middleware.grouper.cfg.GrouperConfig;
import edu.internet2.middleware.grouper.exception.AttributeDefNotFoundException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.helper.GrouperTest;
import edu.internet2.middleware.grouper.helper.SessionHelper;
import edu.internet2.middleware.grouper.helper.StemHelper;
import edu.internet2.middleware.grouper.helper.SubjectTestHelper;
import edu.internet2.middleware.grouper.privs.AccessPrivilege;
import edu.internet2.middleware.grouper.privs.AttributeDefPrivilege;
import edu.internet2.middleware.subject.Subject;

/**
 * @author mchyzer
 *
 */
public class MembershipAttributeSecurityTest extends GrouperTest {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new MembershipAttributeSecurityTest("testGrouperSystem"));
    //TestRunner.run(MembershipAttributeSecurityTest.class);

  }

  /** grouper sesion */
  private GrouperSession grouperSession;
  /** edu stem */
  private Stem edu;
  /** test membership */
  private Membership membership;
  /** root stem */
  private Stem root;
  /** top stem */
  private Stem top;
  /** */
  private Stem etc;
  /** */
  private Group wheel;
  /** */
  private AttributeDef attributeDef1;
  /** */
  private AttributeDef attributeDef2;
  /** */
  private AttributeDefName attributeDefName1_1;
  /** */
  private AttributeDefName attributeDefName1_2;
  /** */
  private AttributeDefName attributeDefName2_1;
  /** */
  private AttributeDefName attributeDefName2_2;

  /**
   * 
   */
  public MembershipAttributeSecurityTest() {
    super();
  }

  /**
   * 
   * @param name
   */
  public MembershipAttributeSecurityTest(String name) {
    super(name);
  }

  /**
   * 
   * @see edu.internet2.middleware.grouper.helper.GrouperTest#setUp()
   */
  protected void setUp () {
    super.setUp();
    this.grouperSession     = SessionHelper.getRootSession();
    this.root  = StemHelper.findRootStem(grouperSession);
    this.edu   = StemHelper.addChildStem(root, "edu", "education");
    Group group = StemHelper.addChildGroup(this.edu, "group", "the group");
    group.addMember(SubjectTestHelper.SUBJ6);
    this.membership = MembershipFinder.findImmediateMembership(this.grouperSession, group, 
        SubjectTestHelper.SUBJ6, Group.getDefaultList(), true);
    this.top = this.root.addChildStem("top", "top display name");

    @SuppressWarnings("unused")
    Subject subject = SubjectTestHelper.SUBJ0;
    
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("attributeDefs.create.grant.all.attrAdmin", "false");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("attributeDefs.create.grant.all.attrOptin", "false");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("attributeDefs.create.grant.all.attrOptout", "false");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("attributeDefs.create.grant.all.attrRead", "false");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("attributeDefs.create.grant.all.attrUpdate", "false");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("attributeDefs.create.grant.all.attrView", "false");

    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("groups.create.grant.all.read", "false");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("groups.create.grant.all.view", "false");

    this.etc = new StemSave(this.grouperSession).assignStemNameToEdit("etc").assignName("etc").save();
    this.wheel = etc.addChildGroup("wheel","wheel");
    
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("groups.wheel.use", "true");
    GrouperConfig.retrieveConfig().propertiesOverrideMap().put("groups.wheel.group", wheel.getName());
    
    this.attributeDef1 = this.top.addChildAttributeDef("test", AttributeDefType.attr);
    this.attributeDef1.setAssignToImmMembership(true);
    this.attributeDef1.store();
    this.attributeDef2 = this.top.addChildAttributeDef("test2", AttributeDefType.attr);
    this.attributeDef2.setAssignToImmMembership(true);
    this.attributeDef2.store();
  
    this.attributeDefName1_1 = this.top.addChildAttributeDefName(attributeDef1, "testName1_1", "test name1_1");
    this.attributeDefName1_2 = this.top.addChildAttributeDefName(attributeDef1, "testName1_2", "test name1_2");
    this.attributeDefName2_1 = this.top.addChildAttributeDefName(attributeDef2, "testName2_1", "test name2_1");
    this.attributeDefName2_2 = this.top.addChildAttributeDefName(attributeDef2, "testName2_2", "test name2_2");
  
    //subj0 cant update/read group or update the attribute (def1)
    //subj1 can update/read group but nothing on attribute (def1)
    //subj2 cant update/read group, can update attribute (def1)
    //subj3 is wheel group
    //subj4 can update/read group and admin attribute (def1)
    //subj5 can read group and admin attribute (def1)
    //subj6 can update/read group and update attribute (not read) (def1)
    //subj7 can update/read group and update/read attribute (def1)
    
    group.grantPriv(SubjectTestHelper.SUBJ1, AccessPrivilege.UPDATE);
    group.grantPriv(SubjectTestHelper.SUBJ1, AccessPrivilege.READ);
    
    this.attributeDef1.getPrivilegeDelegate().grantPriv(SubjectTestHelper.SUBJ2, AttributeDefPrivilege.ATTR_UPDATE, true);
    
    this.wheel.addMember(SubjectTestHelper.SUBJ3);

    group.grantPriv(SubjectTestHelper.SUBJ4, AccessPrivilege.UPDATE);
    group.grantPriv(SubjectTestHelper.SUBJ4, AccessPrivilege.READ);
    this.attributeDef1.getPrivilegeDelegate().grantPriv(SubjectTestHelper.SUBJ4, AttributeDefPrivilege.ATTR_ADMIN, true);
    
    group.grantPriv(SubjectTestHelper.SUBJ5, AccessPrivilege.READ);
    this.attributeDef1.getPrivilegeDelegate().grantPriv(SubjectTestHelper.SUBJ5, AttributeDefPrivilege.ATTR_ADMIN, true);
    
    group.grantPriv(SubjectTestHelper.SUBJ6, AccessPrivilege.UPDATE);
    group.grantPriv(SubjectTestHelper.SUBJ6, AccessPrivilege.READ);
    this.attributeDef1.getPrivilegeDelegate().grantPriv(SubjectTestHelper.SUBJ6, AttributeDefPrivilege.ATTR_UPDATE, true);

    group.grantPriv(SubjectTestHelper.SUBJ7, AccessPrivilege.UPDATE);
    group.grantPriv(SubjectTestHelper.SUBJ7, AccessPrivilege.READ);
    this.attributeDef1.getPrivilegeDelegate().grantPriv(SubjectTestHelper.SUBJ7, AttributeDefPrivilege.ATTR_UPDATE, true);
    this.attributeDef1.getPrivilegeDelegate().grantPriv(SubjectTestHelper.SUBJ7, AttributeDefPrivilege.ATTR_READ, true);

  }

  /**
   * @throws Exception 
   */
  public void testGrouperSystem() throws Exception {

    //###################
    // try grouper system
    
    //assign these
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1).isChanged());
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2).isChanged());
    
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1));
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2));
    
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(2, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(2, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size());
    
    assertEquals(3, this.membership.getAttributeDelegate().retrieveAssignments().size());

    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2).isChanged());
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1).isChanged());
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2).isChanged());

    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2));
    
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size());
    
  }
  
  /**
   * subj0 cant admin group or update the attribute (def1)
   * @throws Exception 
   */
  public void testSecuritySubj0() throws Exception {
  
    //###################
    // assign an attribute

    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ0 );

    //subj0 cant even update/admin the attribute, or admin the group
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    //subj0 cant even update/admin the attribute, or admin the group
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }

    //############################################
    // Try to read an attribute
    
    
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    //##############################
    // remove attributes
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
  }

  /**
   * subj1 can admin group but nothing on attribute (def1)
   * @throws Exception 
   */
  public void testSecuritySubj1() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ1 );
  
    //subj0 cant even update/admin the attribute, or admin the group
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    //subj0 cant even update/admin the attribute, or admin the group
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
  
    //############################################
    // Try to read an attribute
    
    
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    //##############################
    // remove attributes
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
  }

  /**
   * subj2 cant admin group, can update attribute (def1)
   * @throws Exception 
   */
  public void testSecuritySubj2() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ2 );
  
    //subj0 cant even update/admin the attribute, or admin the group
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    //subj0 cant even update/admin the attribute, or admin the group
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
  
    //############################################
    // Try to read an attribute
    
    
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size();
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size();
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    //##############################
    // remove attributes
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
  }

  /**
   * subj3 is wheel group
   * @throws Exception 
   */
  public void testSecuritySubj3() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ3 );
  
    //assign these
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1).isChanged());
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2).isChanged());
    
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1));
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2));
    
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(2, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(2, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size());
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size());
    
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2).isChanged());
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1).isChanged());
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2).isChanged());

    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName2_2));
    
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2).size());
    
    
  }

  /**
   * subj4 can admin group and admin attribute (def1)
   * @throws Exception 
   */
  public void testSecuritySubj4() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ4 );
  
    //assign these
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }

    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2).isChanged());
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
  
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    
    
  }

  /**
   * subj5 can read group and admin attribute (def1)
   * @throws Exception 
   */
  public void testSecuritySubj5() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ5 );

    //assign these
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }

    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
  
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
  
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    
    
  }

  /**
   * subj6 can admin group and update attribute (not read) (def1)
   * @throws Exception 
   */
  public void testSecuritySubj6() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ6 );
  
    
    
    //assign these
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
  
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
  
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());

    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
  
  }

  /**
   * subj7 can admin group and update/read attribute (def1)
   * @throws Exception 
   */
  public void testSecuritySubj7() throws Exception {
  
    //###################
    // assign an attribute
  
    this.grouperSession.stop();
    this.grouperSession = GrouperSession.start( SubjectTestHelper.SUBJ7 );
  
    //assign these
    assertTrue(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().assignAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    assertFalse(this.membership.getAttributeDelegate().assignAttribute(attributeDefName1_1).isChanged());
    
    assertTrue(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    try {
      this.membership.getAttributeDelegate().retrieveAttributes(attributeDef2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDef2);
      fail("Not allowed");
    } catch (InsufficientPrivilegeException ipe) {
      //good
    }
  
    assertEquals(1, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    
    assertTrue(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_2).isChanged());
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_1);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    try {
      this.membership.getAttributeDelegate().removeAttribute(attributeDefName2_2);
      fail("Not allowed");
    } catch (AttributeDefNotFoundException adnfe) {
      //good
    }
    assertFalse(this.membership.getAttributeDelegate().removeAttribute(attributeDefName1_1).isChanged());
  
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_1));
    assertFalse(this.membership.getAttributeDelegate().hasAttribute(attributeDefName1_2));
    
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAttributes(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDef1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_1).size());
    assertEquals(0, this.membership.getAttributeDelegate().retrieveAssignments(attributeDefName1_2).size());
    
    
  }
  
}
