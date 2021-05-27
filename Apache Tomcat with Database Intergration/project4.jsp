<!DOCTYPE html>
<!-- Survey.html -->
<%String results=(String) session.getAttribute("results"); if (results==null) results=" " ; %>
  <html lang="en">

  <head>
    <title>CNT 4714 Color Preference Survey</title>
    <style>
      body {
        background-color: blue;
        color: white;
        text-align: center;
      }

      table,
      th,
      td {
        border: 1px solid black;
        border-collapse: collapse;
      }

      table {
        margin: auto;
      }

      tr {
        background-color: white;
      }

      tr:nth-child(even) {
        background-color: lightgrey;
      }

      td {
        color: black;
      }

      td,
      th {
        padding: 10px;
        font-size: 13px;
        font-family: Arial, Helvetica, sans-serif;
      }

      th {
        background-color: red;
      }

      input[type="submit"],
      input[type="reset"] {
        background-color: black;
        color: yellow;
        font-size: 0.9em;
      }

      textarea {
        background-color: black;
        color: lightgreen;
        font-size: 0.9em;
      }

      h1 {
        font-size: 3em;
      }

      h2,
      h3 {
        color: white;
      }

      .successUpdate {
        background-color: chartreuse;
        border: 2px solid black;
        font-family: Arial, Helvetica, sans-serif;
        width: 50%;
        margin: auto;
        margin-bottom: 3%;
      }

      .successUpdateBold {
        font-weight: bold;
        color: black;
      }

      .errorDB {
        background-color: red;
        border: 2px solid black;
        font-family: Arial, Helvetica, sans-serif;
        width: 50%;
        margin: auto;
        margin-bottom: 3%;
      }

      .y {
        color: yellow;
      }

      .errorDBBold {
        font-weight: bold;
      }
    </style>
  </head>

  <body>
    <h1>Welcome to the Spring 2021 Project 4 Enterprise Database System</h1>
    <h1>A Servlet/JSP-based Multi-tiered Enterprise Application Utilizing A Tomcat Container</h1>
    <h3 class="y">Developed by: Christian Mazariegos</h3>
    <hr>
    <p>You are connected to the Project 4 Enterprise System database as an administrator.</p>
    <p>Please enter valid SQL query or update command.</p>
    <form action="/Project4/dbserverlet" method="post">
      <p>
        <textarea id="sqlInput" name="sqlInput" rows="15" cols="80">SELECT * FROM suppliers</textarea>
      </p>
      <p>
        <input type="submit" value="Execute Command" />
        <input type="reset" value="Reset">
      </p>
    </form>
    <br>
    <br>
    <br>
    <p>All execution results will appear below.</p>
    <hr>
    <h3>Database Results:</h3>
    <div>
      <%=results%>
    </div>
  </body>

  </html>