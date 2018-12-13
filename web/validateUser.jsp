
<%@page import="JSON.JSONObject"%>
<%
    String password = request.getParameter("password");
    String username = request.getParameter("email");

    JSONObject jObj = new JSONObject();

    try {
        DB.Connect.openConnection();
        String sql = "select * from tbluser where (email='" + username + "' or phone='" + username + "') and password='" + password + "'";
        DB.Connect.rs = DB.Connect.stat.executeQuery(sql);
        System.out.print(sql);
        if (DB.Connect.rs.next()) {
            jObj.put("userid", DB.Connect.rs.getString("userid"));
            jObj.put("name", DB.Connect.rs.getString("name"));
            jObj.put("status", true);
            jObj.put("password", DB.Connect.rs.getString("password"));

        } else {
            jObj.put("status", false);
        }

        //DB.Connect.closeConnection();
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    response.getWriter().print(jObj);


%>