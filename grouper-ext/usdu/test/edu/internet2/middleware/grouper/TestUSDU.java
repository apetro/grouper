package edu.internet2.middleware.grouper;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.internal.dao.RegistrySubjectDAO;
import edu.internet2.middleware.grouper.internal.dto.RegistrySubjectDTO;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

public class TestUSDU extends GrouperTest {

  private static final Log LOG = LogFactory.getLog(TestUSDU.class);

  protected void setUp() {

    LOG.debug("setUp");
    RegistryReset.reset();
  }

  protected void tearDown() {

    LOG.debug("tearDown");
  }

  private void deleteSubject(Subject subject) throws InterruptedException {

    RegistrySubjectDAO dao = GrouperDAOFactory.getFactory().getRegistrySubject();
    RegistrySubjectDTO dto = new RegistrySubjectDTO().setId(subject.getId()).setName(subject.getName()).setType(
        subject.getType().toString());
    dao.delete(dto);

    // must be longer than timeToIdleSeconds and timeToLiveSeconds in
    // src/test/conf/grouper.ehcache.xml
    Thread.sleep(1100);

    try {
      SubjectFinder.findById(subject.getId());
      fail("should not find subject " + subject.getId());
    } catch (SubjectNotFoundException e) {
      // OK
    } catch (SubjectNotUniqueException e) {
      fail("subject should be unique " + subject.getId());
    }
  }

  public void testMemberships() {

    try {
      LOG.info("testMemberships");

      R r = R.populateRegistry(1, 6, 1);

      Group gA = r.getGroup("a", "a");
      Group gB = r.getGroup("a", "b");
      Group gC = r.getGroup("a", "c");
      Group gD = r.getGroup("a", "d");
      Group gE = r.getGroup("a", "e");
      Group gF = r.getGroup("a", "f");

      Subject subjA = SubjectFinder.findById("a");

      gA.addMember(subjA);
      gB.addMember(gA.toSubject());
      gC.addCompositeMember(CompositeType.UNION, gA, gD);
      gE.addCompositeMember(CompositeType.INTERSECTION, gA, gB);
      gF.addCompositeMember(CompositeType.COMPLEMENT, gA, gD);

      assertTrue(gA.hasMember(subjA));
      assertTrue(gB.hasMember(subjA));
      assertTrue(gC.hasMember(subjA));
      assertTrue(gE.hasMember(subjA));
      assertTrue(gF.hasMember(subjA));

      deleteSubject(subjA);

      Set<Member> unresolvables = USDU.getUnresolvableMembers(r.getSession(), null);

      assertTrue(unresolvables.size() == 1);
      assertTrue(unresolvables.iterator().next().getSubjectId().equals(subjA.getId()));

      USDU.resolveMembers(unresolvables, true);

      assertFalse(gA.hasMember(subjA));
      assertFalse(gB.hasMember(subjA));
      assertFalse(gC.hasMember(subjA));
      assertFalse(gE.hasMember(subjA));
      assertFalse(gF.hasMember(subjA));

      assertTrue(USDU.getUnresolvableMembers(r.getSession(), null).size() == 1);

    } catch (Exception e) {
      T.e(e);
    }
  }

  public void testCustomList() {

    try {
      LOG.info("testCustomList");

      R r = R.populateRegistry(1, 1, 1);
      Group gA = r.getGroup("a", "a");
      Subject subjA = SubjectFinder.findById("a");

      GroupType customGroupType = GroupType.createType(r.getSession(), "customGroupType");
      Field customListField = customGroupType.addList(r.getSession(), "customList", AccessPrivilege.VIEW,
          AccessPrivilege.UPDATE);
      gA.addType(customGroupType);
      gA.addMember(subjA, customListField);
      assertTrue(gA.hasMember(subjA, customListField));

      deleteSubject(subjA);

      Set<Member> unresolvables = USDU.getUnresolvableMembers(r.getSession(), null);
      assertTrue(unresolvables.size() == 1);
      USDU.resolveMembers(unresolvables, true);
      assertFalse(gA.hasMember(subjA, customListField));

    } catch (Exception e) {
      T.e(e);
    }
  }

  public void testAccessPrivilege() {

    try {
      LOG.info("testAccessPrivilege");

      R r = R.populateRegistry(1, 1, 1);
      Group gA = r.getGroup("a", "a");
      Subject subjA = SubjectFinder.findById("a");

      gA.grantPriv(subjA, AccessPrivilege.ADMIN);
      gA.grantPriv(subjA, AccessPrivilege.OPTIN);
      gA.grantPriv(subjA, AccessPrivilege.OPTOUT);
      gA.grantPriv(subjA, AccessPrivilege.READ);
      gA.grantPriv(subjA, AccessPrivilege.UPDATE);
      gA.grantPriv(subjA, AccessPrivilege.VIEW);

      deleteSubject(subjA);

      Set<Member> unresolvables = USDU.getUnresolvableMembers(r.getSession(), null);
      assertTrue(unresolvables.size() == 1);
      USDU.resolveMembers(unresolvables, true);
      Member unresolvable = unresolvables.iterator().next();
      assertTrue(USDU.getAllImmediateMemberships(unresolvable, USDU.getMemberFields()).isEmpty());

    } catch (Exception e) {
      T.e(e);
    }
  }

  public void testNamingPrivilege() {

    try {
      LOG.info("testNamingPrivilege");

      R r = R.populateRegistry(1, 0, 1);
      Subject subjA = SubjectFinder.findById("a");
      Stem stem = StemFinder.findByName(r.getSession(), "i2");

      stem.grantPriv(subjA, NamingPrivilege.CREATE);
      stem.grantPriv(subjA, NamingPrivilege.STEM);

      deleteSubject(subjA);

      Set<Member> unresolvables = USDU.getUnresolvableMembers(r.getSession(), null);
      assertTrue(unresolvables.size() == 1);
      USDU.resolveMembers(unresolvables, true);
      Member unresolvable = unresolvables.iterator().next();
      assertTrue(USDU.getAllImmediateMemberships(unresolvable, USDU.getMemberFields()).isEmpty());

    } catch (Exception e) {
      T.e(e);
    }
  }
}
