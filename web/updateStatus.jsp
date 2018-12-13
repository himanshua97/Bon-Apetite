<%@page import="JSON.JSONObject"%>
<%
           
            String userid = request.getParameter("userid");
            String status = request.getParameter("status");
          
           
           int i= DB.Connect.updateField("tbluser", "status", status, "userid", userid);
         
            
            JSON.JSONObject jObj=new JSONObject();
            
            if(i>0){
                jObj.put("msg", "User updated Succesfully");
                jObj.put("check", "success");
          
            }else  {jObj.put("msg", "Failed To update details");
            jObj.put("check", "fail");
            
            }
            System.out.print(jObj);
                 response.getWriter().print(jObj);
%>