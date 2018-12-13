
<%@page import="org.json.simple.JSONObject"%>
<%
    JSONObject jObj=new JSONObject();
            String op = request.getParameter("txtoldpassword");
            System.out.print("oldp="+op);
            String np = request.getParameter("txtnewpassword");
           
            String id = request.getParameter("userid");
            String dbp = DB.Connect.getUserPassword(id);
            String message="";
            try {
                if (dbp.equalsIgnoreCase(op)) {
                 
                        if (!(np.isEmpty()  || op.isEmpty())) {
                            DB.Connect.changePass(id, np);
                                System.out.print("Passwordchanged");
                                message="Password changed successfully";
                        } else {
                             message="Fields cannot be empty";

                        }
                   
                } else {
                  message="Incorrect password";
                }
            } catch (Exception e) {

                e.printStackTrace();
                 message="An error occured";
            }
         jObj.put("msg", message);
            response.getWriter().print(jObj);
%>