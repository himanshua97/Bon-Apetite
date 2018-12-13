
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Statement"%>

<%@page import="org.json.simple.JSONObject"%>
<%
    //JSONObject jObj = new JSONObject();
    String userid = request.getParameter("userid");//from
    //String email = userid;//from
   
   
    String latitude = request.getParameter("latitude");
    String longitude = request.getParameter("longitude");
    DB.Connect.updateLocation(latitude, longitude, userid);
    JSONObject jObj=new  JSONObject();
    jObj.put("msg", "location updated!!");
    System.out.println("location updated"+ jObj);
   response.getWriter().print(jObj);
           
 

%>