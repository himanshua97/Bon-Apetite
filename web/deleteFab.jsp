<%@page import="JSON.JSONObject"%>
<%

            String userid = request.getParameter("userid");         
        
            String reference = request.getParameter("reference");
           
          
            DB.Connect.openConnection();
           int i= DB.Connect.deleteFab(userid,reference);
           
            DB.Connect.closeConnection();
            
            JSON.JSONObject jObj=new JSONObject();
            
            if(i>0){
                jObj.put("msg", "Item deleted successfully");
                jObj.put("success",true);
          
            }else  {
               
                 jObj.put("msg", "Failed to delete item");
            jObj.put("success",false);
             
              
            
            }
            System.out.print(jObj);
                 response.getWriter().print(jObj);
%>