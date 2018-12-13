<%@page import="JSON.JSONObject"%>
<%
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String userid = request.getParameter("userid");
            String father_name = request.getParameter("father_name");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            
           
            String mobile = request.getParameter("mobile");
           
           int i= DB.Connect.updateUser(fname, lname, userid, father_name, email, address, mobile);
         
            
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