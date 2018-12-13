

<%@page import="org.json.simple.JSONObject"%>
<%@page import="DB.SimpleEmail"%>

<%
            // String password = request.getParameter("txtpassword");
            String stremail = request.getParameter("email");

           

            JSONObject jObj = new JSONObject();



            
            try {
                DB.Connect.openConnection();

                DB.Connect.rs = DB.Connect.stat.executeQuery("select password from tbluser where  emailid='" + stremail + "'");
                if (DB.Connect.rs.next()) {
                   
                   DB.SimpleEmail email=new SimpleEmail();
                   email.sendMessage("Online Exam App Forget Password","Your Password is: "+DB.Connect.rs.getString("password"), stremail);
jObj.put("msg", "Mail sent successfully!!");
                } else {
                    jObj.put("msg", "Mail is not registered!!");

                }

                DB.Connect.closeConnection();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            response.getWriter().print(jObj);

%>