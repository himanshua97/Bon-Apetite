<%@page import="JSON.JSONObject"%>
<%

            String userid = request.getParameter("userid");
            String comment = request.getParameter("comment");
            String resid = request.getParameter("resid");
           
          
            DB.Connect.openConnection();
           int i= DB.Connect.saveComment(userid, comment,resid);
           
            DB.Connect.closeConnection();
            
            JSON.JSONObject jObj=new JSONObject();
            
            if(i>0){
                jObj.put("msg", "Comment sent Succesfully");
                jObj.put("success",true);
          
            }else  {
               
                 jObj.put("msg", "Failed to save comment");
            jObj.put("success",false);
             
              
            
            }
            System.out.print(jObj);
                 response.getWriter().print(jObj);
%>