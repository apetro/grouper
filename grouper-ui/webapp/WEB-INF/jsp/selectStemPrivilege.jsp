<%-- @annotation@
		Tile which displays a form which allows a user to select a stem privilege and
		see a list of Subjects with that privilege for the active stem
--%><%--
  @author Gary Brown.
  @version $Id: selectStemPrivilege.jsp,v 1.5 2008-04-15 13:43:21 isgwb Exp $
--%>
<%@include file="/WEB-INF/jsp/include.jsp"%>
<grouper:recordTile key="Not dynamic" tile="${requestScope['javax.servlet.include.servlet_path']}">
<html:form action="populateStemPriviligees" method="post">
<fieldset>
	<html:hidden property="stemId"/>
	<input type="hidden" name="stems" value="true"/>
	<input type="submit" class="blueButton" value="<grouper:message bundle="${nav}" key="priv.show-subjects-with"/>"/>
	<label class="noCSSOnly" for="privilege"><grouper:message bundle="${nav}" key="priv.show-subjects-with"/></label> 
	<html:select property="privilege" styleId="privilege">
		<html:optionsCollection name="allStemPrivs" />
	</html:select> <grouper:message bundle="${nav}" key="priv.privilege"/>
</fieldset>
</html:form>
</grouper:recordTile>