<%@page import="org.json.simple.JSONArray"%>

<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="DB.Connect.*"%>
<%
            String userid = "", fname = "", lname = null ;


          JSONArray jArray = new JSONArray();

            try {

                String key = request.getParameter("key").toString();
               String sql="SELECT * FROM tbluser where username='"+key+"' or fname like'%"+key+"%' ";
               System.out.print(sql);
                DB.Connect.openConnection();
                DB.Connect.rs = DB.Connect.stat.executeQuery(sql);
                while (DB.Connect.rs.next()) {
                    userid = DB.Connect.rs.getString("userid");
                    fname = DB.Connect.rs.getString("fname");
                    lname = DB.Connect.rs.getString("lname");
                  
                    JSONObject jObj=new JSONObject();
                    jObj.put("fuserid", userid);
                  
                    jObj.put("name",  fname + " " + lname);
              jArray.add(jObj);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
          System.out.print(jArray.toString());
            response.getWriter().print(jArray.toString());

%>