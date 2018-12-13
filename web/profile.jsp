
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="DB.Connect.*"%>
<%
            String userid = "", father_name, fname = "", lname = null, email, rdate, mobile, address, usertype;

            JSONObject jObj = new JSONObject();

            try {

                String username1 = request.getParameter("username");
                System.out.println("username=" + username1);
                DB.Connect.openConnection();
                String sql="SELECT * FROM tbluser where userid='" + username1 + "' ";
                DB.Connect.rs = DB.Connect.stat.executeQuery(sql);
System.out.print(sql);;
                while (DB.Connect.rs.next()) {
                    userid = DB.Connect.rs.getString("userid");
                    fname = DB.Connect.rs.getString("fname");
                    lname = DB.Connect.rs.getString("lname");
                    father_name = DB.Connect.rs.getString("father_name");
                  
                    mobile = DB.Connect.rs.getString("mobile");
                    email = DB.Connect.rs.getString("emailid");
                    address = DB.Connect.rs.getString("address");
                    rdate = DB.Connect.rs.getString("rdate").substring(0, 10);
                    usertype = DB.Connect.rs.getString("usertype");
                   
                   // amount = DB.Connect.rs.getString("balance");

                    jObj.put("userid", userid);

                    jObj.put("name", fname + " " + lname);
                    jObj.put("fname", fname );
                    jObj.put("lname", lname );
                   
                    jObj.put("mobile", mobile);
                    jObj.put("email", email);
                    jObj.put("address", address);
                    jObj.put("rdate", rdate);
                    jObj.put("father_name", father_name);
                   
                    jObj.put("usertype", usertype);
                    jObj.put("latitude", DB.Connect.rs.getString("latitude"));
                    jObj.put("longitude", DB.Connect.rs.getString("longitude"));
                    jObj.put("status", DB.Connect.rs.getBoolean("status"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // response.getWriter().print("\nexpense="+expense+"\namount="+amount+"\ndoe="+doe+"\ndescription="+description);
System.out.println(jObj);
             response.getWriter().print(jObj);

%>