<%
    String[] array = (String[])request.getAttribute("testVariable2"); 
%>
<p><%= request.getAttribute("testVariable") %></p>
<p><%= array[0] %></p>