/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Chicago

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

package edu.internet2.middleware.grouper.membership;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.textui.TestRunner;

import edu.internet2.middleware.grouper.Field;
import edu.internet2.middleware.grouper.FieldFinder;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.group.GroupSet;
import edu.internet2.middleware.grouper.helper.GrouperTest;
import edu.internet2.middleware.grouper.helper.T;
import edu.internet2.middleware.grouper.hibernate.HibernateSession;
import edu.internet2.middleware.grouper.misc.AddMissingGroupSets;
import edu.internet2.middleware.grouper.misc.CompositeType;
import edu.internet2.middleware.grouper.misc.GrouperDAOFactory;
import edu.internet2.middleware.grouper.privs.AccessPrivilege;
import edu.internet2.middleware.grouper.privs.NamingPrivilege;

/**
 * @author shilen
 */
public class AddMissingGroupSetsTest extends GrouperTest {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new AddMissingGroupSetsTest("AddMissingGroupSetsTest"));
  }
  
  /**
   * @param name
   */
  public AddMissingGroupSetsTest(String name) {
    super(name);
  }

  /**
   * @throws Exception
   */
  public void testAddMissingGroupSets() throws Exception {

    GrouperSession session = GrouperSession.startRootSession();
    Member rootMember = MemberFinder.findBySubject(session, SubjectFinder.findRootSubject(), true);
    
    Stem root = StemFinder.findRootStem(session);
    Stem top = root.addChildStem("top", "top");
    top.addChildStem("bottom", "bottom");
    
    GroupType testType = GroupType.createType(session, "testType", true);
    testType.addAttribute(session, "testAttribute", AccessPrivilege.READ, AccessPrivilege.UPDATE, false, true);
    Field fieldTestList = testType.addList(session, "testList", AccessPrivilege.READ, AccessPrivilege.UPDATE);
    
    Group composite1Owner = top.addChildGroup("composite1Owner", "composite1Owner");
    Group composite2Owner = top.addChildGroup("composite2Owner", "composite2Owner");
    Group composite1Left = top.addChildGroup("composite1Left", "composite1Left");
    Group composite2Left = top.addChildGroup("composite2Left", "composite2Left");
    Group composite1Right = top.addChildGroup("composite1Right", "composite1Right");
    Group composite2Right = top.addChildGroup("composite2Right", "composite2Right");
    Group composite1Subj = top.addChildGroup("composite1Subj", "composite1Subj");
    
    Group updateFieldTest = top.addChildGroup("updateFieldTest", "updateFieldTest");
    Group customFieldTest = top.addChildGroup("customFieldTest", "customFieldTest");
    customFieldTest.addType(testType);
    
    Group one = top.addChildGroup("one", "one");
    Group two = top.addChildGroup("two", "two");
    Group three = top.addChildGroup("three", "three");
    Group four = top.addChildGroup("four", "four");
    Group five = top.addChildGroup("five", "five");
    Group six = top.addChildGroup("six", "six");
    
    composite1Owner.addCompositeMember(CompositeType.UNION, composite1Left, composite1Right);
    composite2Owner.addCompositeMember(CompositeType.COMPLEMENT, composite2Left, composite2Right);
    composite1Left.addMember(composite1Subj.toSubject());
    
    one.addMember(two.toSubject());
    two.addMember(three.toSubject());
    three.addMember(four.toSubject());
    four.addMember(five.toSubject());
    five.addMember(six.toSubject());
    
    customFieldTest.addMember(four.toSubject(), fieldTestList);
    updateFieldTest.grantPriv(four.toSubject(), AccessPrivilege.UPDATE);
    top.grantPriv(four.toSubject(), NamingPrivilege.CREATE);

    // get and verify number of groupSets before proceeding..
    Set<GroupSet> originalGroupSets = GrouperDAOFactory.getFactory().getGroupSet().findAllByCreator(rootMember);
    T.amount("Total groupSets", 137, originalGroupSets.size());

    // delete all groupSets -- set parent_id to null first to avoid foreign key constraint violations
    HibernateSession.byHqlStatic().createQuery("update GroupSet set parentId = null").executeUpdate();
    HibernateSession.byHqlStatic().createQuery("delete from GroupSet").executeUpdate();
    
    // add groupSets and verify them
    new AddMissingGroupSets().showResults(false).addAllMissingGroupSets();
    Set<GroupSet> currentGroupSets = GrouperDAOFactory.getFactory().getGroupSet().findAllByCreator(rootMember);
    T.amount("Total groupSets", 137, currentGroupSets.size());
    Set<GroupSet> currentGroupSetsAfterMod = convertCurrentGroupSets(originalGroupSets, currentGroupSets);
    Set<GroupSet> checkGroupSets = new HashSet<GroupSet>(originalGroupSets);
    checkGroupSets.removeAll(currentGroupSetsAfterMod);
    T.amount("Check groupSet difference between original and current", 0, checkGroupSets.size());
    
    // now let's remove some group sets
    GrouperDAOFactory.getFactory().getGroupSet().deleteByOwnerGroupAndField(composite1Owner.getUuid(), Group.getDefaultList().getUuid());
    GrouperDAOFactory.getFactory().getGroupSet().deleteByOwnerGroupAndField(customFieldTest.getUuid(), FieldFinder.find("testList", true).getUuid());
    GrouperDAOFactory.getFactory().getGroupSet().deleteByOwnerStem(top.getUuid());
    
    // now add them back and verify again
    new AddMissingGroupSets().showResults(false).addAllMissingGroupSets();
    currentGroupSets = GrouperDAOFactory.getFactory().getGroupSet().findAllByCreator(rootMember);
    T.amount("Total groupSets", 137, currentGroupSets.size());
    currentGroupSetsAfterMod = convertCurrentGroupSets(originalGroupSets, currentGroupSets);
    checkGroupSets = new HashSet<GroupSet>(originalGroupSets);
    checkGroupSets.removeAll(currentGroupSetsAfterMod);
    T.amount("Check groupSet difference between original and current", 0, checkGroupSets.size());
  }
  
  private Set<GroupSet> convertCurrentGroupSets(Set<GroupSet> originalGroupSets, Set<GroupSet> currentGroupSets) {
    // This is a bit of a hack, but here's what I'm trying to do...
    // In order to verify that the currentGroupSets equal the originalGroupSets, we need to update the parentIds.
    // Based on the groupSets that were added, I'm using the following as a key for lookups.  In reality, this isn't a unique constraint.
    //      owner_group_id_null, owner_stem_id_null, member_group_id_null, member_stem_id_null, field_id
    
    Set<GroupSet> currentGroupSetsAfterMod = new HashSet<GroupSet>();
    
    Map<String, String> lookupOrigIdByKey = new HashMap<String, String>();
    Map<String, String> lookupOrigIdByCurrentId = new HashMap<String, String>();
    
    Iterator<GroupSet> originalGroupSetsIter = originalGroupSets.iterator();
    while (originalGroupSetsIter.hasNext()) {
      GroupSet gs = originalGroupSetsIter.next();
      String key = gs.getOwnerGroupIdNull() + ":" + gs.getOwnerStemIdNull() + ":" + gs.getMemberGroupIdNull() + ":" + 
          gs.getMemberStemIdNull() + ":" + gs.getFieldId();
      lookupOrigIdByKey.put(key, gs.getId());
    }
    
    Iterator<GroupSet> currentGroupSetsIter = currentGroupSets.iterator();
    while (currentGroupSetsIter.hasNext()) {
      GroupSet gs = currentGroupSetsIter.next();
      String key = gs.getOwnerGroupIdNull() + ":" + gs.getOwnerStemIdNull() + ":" + gs.getMemberGroupIdNull() + ":" + 
          gs.getMemberStemIdNull() + ":" + gs.getFieldId();
      lookupOrigIdByCurrentId.put(gs.getId(), lookupOrigIdByKey.get(key));
    }
    
    currentGroupSetsIter = currentGroupSets.iterator();
    while (currentGroupSetsIter.hasNext()) {
      GroupSet gs = currentGroupSetsIter.next();
      gs.setParentId(lookupOrigIdByCurrentId.get(gs.getParentId()));
      gs.setId(lookupOrigIdByCurrentId.get(gs.getId()));
      currentGroupSetsAfterMod.add(gs);
    }
    
    return currentGroupSetsAfterMod;
  }
}
