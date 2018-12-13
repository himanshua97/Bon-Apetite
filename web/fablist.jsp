
<%@page import="JSON.JSONObject"%>
<%@page import="JSON.JSONArray"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="DB.Connect.*"%>
<%
//to display the list of all users expect students
         

String userid=request.getParameter("userid");
          JSON.JSONArray jSONArray=new JSONArray();
            try {

                // String userid = session.getAttribute("userid").toString();
                DB.Connect.openConnection();
                DB.Connect.rs = DB.Connect.stat.executeQuery("SELECT * FROM tblfab where userid='"+userid+"'");
                while (DB.Connect.rs.next()) {
                  
                 
               JSON.JSONObject json=new JSONObject();
               json.put("resid", DB.Connect.rs.getString("resid"));
               json.put("reference", DB.Connect.rs.getString("reference"));
               json.put("name", DB.Connect.rs.getString("name"));
jSONArray.put(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // response.getWriter().print("\nexpense="+expense+"\namount="+amount+"\ndoe="+doe+"\ndescription="+description);
            response.getWriter().print(jSONArray);

%>