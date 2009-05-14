
/**
 * GrouperServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package edu.internet2.middleware.grouper.webservicesClient;

    /**
     *  GrouperServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class GrouperServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public GrouperServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public GrouperServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for groupDeleteLite method
            * override this method for handling normal response from groupDeleteLite operation
            */
           public void receiveResultgroupDeleteLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GroupDeleteLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from groupDeleteLite operation
           */
            public void receiveErrorgroupDeleteLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for groupSave method
            * override this method for handling normal response from groupSave operation
            */
           public void receiveResultgroupSave(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GroupSaveResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from groupSave operation
           */
            public void receiveErrorgroupSave(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addMemberLite method
            * override this method for handling normal response from addMemberLite operation
            */
           public void receiveResultaddMemberLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.AddMemberLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addMemberLite operation
           */
            public void receiveErroraddMemberLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for hasMember method
            * override this method for handling normal response from hasMember operation
            */
           public void receiveResulthasMember(
                    edu.internet2.middleware.grouper.ws.soap.xsd.HasMemberResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from hasMember operation
           */
            public void receiveErrorhasMember(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stemDelete method
            * override this method for handling normal response from stemDelete operation
            */
           public void receiveResultstemDelete(
                    edu.internet2.middleware.grouper.ws.soap.xsd.StemDeleteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stemDelete operation
           */
            public void receiveErrorstemDelete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stemSaveLite method
            * override this method for handling normal response from stemSaveLite operation
            */
           public void receiveResultstemSaveLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.StemSaveLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stemSaveLite operation
           */
            public void receiveErrorstemSaveLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for groupSaveLite method
            * override this method for handling normal response from groupSaveLite operation
            */
           public void receiveResultgroupSaveLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GroupSaveLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from groupSaveLite operation
           */
            public void receiveErrorgroupSaveLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addMember method
            * override this method for handling normal response from addMember operation
            */
           public void receiveResultaddMember(
                    edu.internet2.middleware.grouper.ws.soap.xsd.AddMemberResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addMember operation
           */
            public void receiveErroraddMember(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findGroups method
            * override this method for handling normal response from findGroups operation
            */
           public void receiveResultfindGroups(
                    edu.internet2.middleware.grouper.ws.soap.xsd.FindGroupsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findGroups operation
           */
            public void receiveErrorfindGroups(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteMember method
            * override this method for handling normal response from deleteMember operation
            */
           public void receiveResultdeleteMember(
                    edu.internet2.middleware.grouper.ws.soap.xsd.DeleteMemberResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteMember operation
           */
            public void receiveErrordeleteMember(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for hasMemberLite method
            * override this method for handling normal response from hasMemberLite operation
            */
           public void receiveResulthasMemberLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.HasMemberLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from hasMemberLite operation
           */
            public void receiveErrorhasMemberLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stemDeleteLite method
            * override this method for handling normal response from stemDeleteLite operation
            */
           public void receiveResultstemDeleteLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.StemDeleteLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stemDeleteLite operation
           */
            public void receiveErrorstemDeleteLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGrouperPrivilegesLite method
            * override this method for handling normal response from getGrouperPrivilegesLite operation
            */
           public void receiveResultgetGrouperPrivilegesLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GetGrouperPrivilegesLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGrouperPrivilegesLite operation
           */
            public void receiveErrorgetGrouperPrivilegesLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMembersLite method
            * override this method for handling normal response from getMembersLite operation
            */
           public void receiveResultgetMembersLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GetMembersLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMembersLite operation
           */
            public void receiveErrorgetMembersLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGroupsLite method
            * override this method for handling normal response from getGroupsLite operation
            */
           public void receiveResultgetGroupsLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GetGroupsLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGroupsLite operation
           */
            public void receiveErrorgetGroupsLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for assignGrouperPrivilegesLite method
            * override this method for handling normal response from assignGrouperPrivilegesLite operation
            */
           public void receiveResultassignGrouperPrivilegesLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.AssignGrouperPrivilegesLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from assignGrouperPrivilegesLite operation
           */
            public void receiveErrorassignGrouperPrivilegesLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findGroupsLite method
            * override this method for handling normal response from findGroupsLite operation
            */
           public void receiveResultfindGroupsLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.FindGroupsLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findGroupsLite operation
           */
            public void receiveErrorfindGroupsLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for groupDelete method
            * override this method for handling normal response from groupDelete operation
            */
           public void receiveResultgroupDelete(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GroupDeleteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from groupDelete operation
           */
            public void receiveErrorgroupDelete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGroups method
            * override this method for handling normal response from getGroups operation
            */
           public void receiveResultgetGroups(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GetGroupsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGroups operation
           */
            public void receiveErrorgetGroups(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findStems method
            * override this method for handling normal response from findStems operation
            */
           public void receiveResultfindStems(
                    edu.internet2.middleware.grouper.ws.soap.xsd.FindStemsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findStems operation
           */
            public void receiveErrorfindStems(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for memberChangeSubjectLite method
            * override this method for handling normal response from memberChangeSubjectLite operation
            */
           public void receiveResultmemberChangeSubjectLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.MemberChangeSubjectLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from memberChangeSubjectLite operation
           */
            public void receiveErrormemberChangeSubjectLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMembers method
            * override this method for handling normal response from getMembers operation
            */
           public void receiveResultgetMembers(
                    edu.internet2.middleware.grouper.ws.soap.xsd.GetMembersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMembers operation
           */
            public void receiveErrorgetMembers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteMemberLite method
            * override this method for handling normal response from deleteMemberLite operation
            */
           public void receiveResultdeleteMemberLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.DeleteMemberLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteMemberLite operation
           */
            public void receiveErrordeleteMemberLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stemSave method
            * override this method for handling normal response from stemSave operation
            */
           public void receiveResultstemSave(
                    edu.internet2.middleware.grouper.ws.soap.xsd.StemSaveResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stemSave operation
           */
            public void receiveErrorstemSave(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findStemsLite method
            * override this method for handling normal response from findStemsLite operation
            */
           public void receiveResultfindStemsLite(
                    edu.internet2.middleware.grouper.ws.soap.xsd.FindStemsLiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findStemsLite operation
           */
            public void receiveErrorfindStemsLite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for memberChangeSubject method
            * override this method for handling normal response from memberChangeSubject operation
            */
           public void receiveResultmemberChangeSubject(
                    edu.internet2.middleware.grouper.ws.soap.xsd.MemberChangeSubjectResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from memberChangeSubject operation
           */
            public void receiveErrormemberChangeSubject(java.lang.Exception e) {
            }
                


    }
    