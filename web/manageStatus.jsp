<%
            String status = request.getParameter("status");

            String username = request.getParameter("username");

            DB.Connect.openConnection();
           int i= DB.Connect.updateField("tbluser", "status", status, "username", username);
            DB.Connect.closeConnection();
            if(i>0){
            response.getWriter().print("Status Updated Successfully!!");
            }else  response.getWriter().print("Please check username, System Failed To Update Status");
%>