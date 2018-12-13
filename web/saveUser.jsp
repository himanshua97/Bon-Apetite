<%@page import="JSON.JSONObject"%>
<%

            String userid = request.getParameter("userid");
            String name = request.getParameter("name");
            String dob = request.getParameter("dob");
            String email = request.getParameter("email");
         
            String password = request.getParameter("password");
            String aadhar_no = request.getParameter("aadhar_no");
            String image = request.getParameter("image");
            String phone = request.getParameter("phone");
          
            DB.Connect.openConnection();
           int i= DB.Connect.saveUsers(userid,name,dob,email,password,aadhar_no,image,phone);
           
            DB.Connect.closeConnection();
            
            JSON.JSONObject jObj=new JSONObject();
            
            if(i>0){
                jObj.put("msg", "User Registered Succesfully");
                jObj.put("success",true);
          
            }else  {
                i=DB.Connect.updateUser(userid, name, dob, email, password, aadhar_no, image, phone);
                if(i>0){
                  jObj.put("msg", "User Details updated successfully");
            jObj.put("success",true);
                }else{
                 jObj.put("msg", "Failed to save details");
            jObj.put("success",false);
                }
              
            
            }
            System.out.print(jObj);
                 response.getWriter().print(jObj);
%>