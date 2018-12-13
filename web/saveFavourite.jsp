<%@page import="JSON.JSONObject"%>
<%

            String userid = request.getParameter("userid");         
            String resid = request.getParameter("resid");
            String name = request.getParameter("name");
            String reference = request.getParameter("reference");
           
          
            DB.Connect.openConnection();
           int i= DB.Connect.saveFab(userid,resid,name,reference);
           
            DB.Connect.closeConnection();
            
            JSON.JSONObject jObj=new JSONObject();
            
            if(i>0){
                jObj.put("msg", "Place added to favourite list");
                jObj.put("success",true);
          
            }else  {
               
                 jObj.put("msg", "Failed to save favourite");
            jObj.put("success",false);
             
              
            
            }
            System.out.print(jObj);
                 response.getWriter().print(jObj);
%>