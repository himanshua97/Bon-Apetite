
<%@page import="JSON.JSONObject"%>
<%@page import="JSON.JSONArray"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="DB.Connect.*"%>
<%
//to display the list of all users expect students
         

String resid=request.getParameter("resid");
          JSON.JSONArray jSONArray=new JSONArray();
            try {

                // String userid = session.getAttribute("userid").toString();
                DB.Connect.openConnection();
                DB.Connect.rs = DB.Connect.stat.executeQuery("SELECT * FROM tblcomment where resid='"+resid+"'");
                while (DB.Connect.rs.next()) {
                   String comment = DB.Connect.rs.getString("comment");
                 String output=DB.NavieBayesClassifier.analayseData(comment);
                 
               JSON.JSONObject json=new JSONObject();
               json.put("comment", comment);
               json.put("output", output);
jSONArray.put(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // response.getWriter().print("\nexpense="+expense+"\namount="+amount+"\ndoe="+doe+"\ndescription="+description);
            response.getWriter().print(jSONArray);

%>