<%@ include file="../common/commonTaglib.jsp"%>
<!-- simpleMembershipUpdate/simpleMembershipUpdateMain.html: main page on simpleMembershipUpdate screen -->
<div id="simpleMain">

<grouperGui:title key="simpleMembershipUpdate.updateTitle" />

<div class="section">

<grouperGui:subtitle key="simpleMembershipUpdate.groupSubtitle" />

<div class="sectionBody" style="min-width: 500px">
<table border="0" cellpadding="0" cellspacing="0">
  <tr valign="top">
    <td>
<grouperGui:groupBreadcrumb
  groupName="${simpleMembershipUpdateContainer.guiGroup.group.displayName}"
/></td><td> &nbsp; &nbsp; &nbsp; <a class="smallLink" href="#operation=SimpleMembershipUpdate.index"
  ><grouperGui:message key="simpleMembershipUpdate.changeLocation" /></a>
</td>
</tr>
</table>

<table class="formTable" cellspacing="2" style="margin-bottom: 0;">
  <tbody>
    <tr class="formTableRow">
      <td class="formTableLeft"><grouperGui:message key="groups.summary.display-extension" /></td>

      <td class="formTableRight">${fn:escapeXml(simpleMembershipUpdateContainer.guiGroup.group.displayExtension)}</td>
    </tr>
    <tr class="formTableRow">
      <td class="formTableLeft"><grouperGui:message key="groups.summary.display-name" /></td>
      <td class="formTableRight">${fn:escapeXml(simpleMembershipUpdateContainer.guiGroup.group.displayName)}</td>
    </tr>

    <tr class="formTableRow">

      <td class="formTableLeft"><grouperGui:message key="groups.summary.description" /></td>
      <td class="formTableRight">${fn:escapeXml(simpleMembershipUpdateContainer.guiGroup.group.description)}</td>
    </tr>
  </tbody>
</table>

<table class="formTable shows_simpleMembershipUpdateGroupDetails" cellspacing="2" 
    style="margin: 0; ${grouperGui:hideShowStyle('simpleMembershipUpdateGroupDetails', true)}">
  <tbody>
    <tr class="formTableRow ">
      <td class="formTableLeft"><grouperGui:message key="groups.summary.extension" /></td>

      <td class="formTableRight">${fn:escapeXml(simpleMembershipUpdateContainer.guiGroup.group.extension)}</td>

    </tr>
    <tr class="formTableRow ">
      <td class="formTableLeft"><grouperGui:message key="groups.summary.name" /></td>
      <td class="formTableRight">${fn:escapeXml(simpleMembershipUpdateContainer.guiGroup.group.name)}</td>
    </tr>
    <tr class="formTableRow ">
      <td class="formTableLeft"><grouperGui:message key="groups.summary.id" /></td>

      <td class="formTableRight">${fn:escapeXml(simpleMembershipUpdateContainer.guiGroup.group.uuid)}</td>
    </tr>

  </tbody>
</table>
<div style="margin-bottom: 8px;">
<%-- Group details button moved to advanced menu <a class="smallLink buttons_simpleMembershipUpdateGroupDetails"
  onclick="return guiHideShow(event, 'simpleMembershipUpdateGroupDetails');" href="#"
>${grouperGui:hideShowButtonText('simpleMembershipUpdateGroupDetails') }</a> --%>

<a id="advancedLink" href="#" class="smallLink"
><grouperGui:message key="simpleMembershipUpdate.advancedButton"/></a>

<%--
<span id="advancedMenu" ></span>
<script type="text/javascript">
guiInitDhtmlxMenu("advancedMenu", "SimpleMembershipUpdate.advancedMenu", 
    "SimpleMembershipUpdate.advancedMenuStructure", true, "#advancedLink");
</script>
--%>
<grouperGui:menu menuId="advancedMenu"
operation="SimpleMembershipUpdate.advancedMenu" 
structureOperation="SimpleMembershipUpdate.advancedMenuStructure" 
contextZoneJqueryHandle="#advancedLink" contextMenu="true" />

</div>

</div>
</div>

<div class="section" style="min-width: 900px">

  <grouperGui:subtitle key="simpleMembershipUpdate.addMemberSubtitle" />

  <div class="sectionBody">
    <form id="simpleMembershipUpdateAddMemberForm" name="simpleMembershipUpdateAddMemberForm" action="whatever">
    <div class="combohint"><grouperGui:message key="simpleMembershipUpdate.addMemberCombohint"/></div>
    <table width="900" cellpadding="0" cellspacing="0">
      <tr valign="top">
        <td style="padding: 0px" width="710">
          <grouperGui:combobox filterOperation="SimpleMembershipUpdate.filterUsers" id="simpleMembershipUpdateAddMember" 
            width="700"/>
          
        </td>
        <td>
          <input class="blueButton" type="submit" 
          onclick="ajax('../app/SimpleMembershipUpdate.addMember', {formIds: 'simpleMembershipUpdateAddMemberForm'}); return false;" 
          value="${grouperGui:message('simpleMembershipUpdate.addMemberButton', true, false) }" style="margin-top: 2px" />
        </td>
      </tr>
    </table>
    </form>
    <br />
  </div>
</div>

<div id="simpleMembershipResultsList" style="min-width: 500px"></div>

</div>
