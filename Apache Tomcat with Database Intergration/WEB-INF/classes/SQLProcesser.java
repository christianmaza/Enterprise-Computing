
//Dodobirds are cool

import java.sql.*;
import java.lang.*;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.UnavailableException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class SQLProcesser extends HttpServlet 
{  
   private Connection connection;
   private Statement statement;
   private final String OVER_HUNDRED = ",\\s*\\d\\d\\d+";

   // set up database connection and create SQL statement
   @Override
   public void init( ServletConfig config ) throws ServletException
   {
       super.init(config);
      // attempt database connection and create Statement
      try 
      {
           Class.forName( config.getInitParameter( "databaseDriver" ) );
           connection = DriverManager.getConnection( 
           config.getInitParameter( "databaseName" ),
           config.getInitParameter( "username" ),
           config.getInitParameter( "password" ) );
      
         // create Statement to query database
         statement = connection.createStatement();
      } // end try
      // for any exception throw an UnavailableException to 
      // indicate that the servlet is not currently available
      catch ( Exception exception ) 
      {
         exception.printStackTrace();
         throw new UnavailableException( exception.getMessage() );
      } // end catch
   }  // end method init 

   // process survey response
   @Override
   protected void doPost( HttpServletRequest request,
      HttpServletResponse response )
         throws ServletException, IOException
   {
      // start HTML document
       
      String sql = request.getParameter( "sqlInput" );
      sql = sql.replaceAll("( )+", " ");
      sql = sql.replaceAll("[\\t\\n\\r]+"," ");
      
      if(sql.contains(";")){
        sql = sql.substring(0, sql.indexOf(";"));
      }
      // attempt to extract the the sql command within the textarea
      Pattern patternSelect = Pattern.compile("select", Pattern.CASE_INSENSITIVE);
      Matcher matcherSelect = patternSelect.matcher(sql);
      
      Pattern patternInsert = Pattern.compile("insert into", Pattern.CASE_INSENSITIVE);
      Matcher matcherInsert = patternInsert.matcher(sql);
      
      Pattern patternUpdate = Pattern.compile("update", Pattern.CASE_INSENSITIVE);
      Matcher matcherUpdate = patternUpdate.matcher(sql);
      
      String results = "";
      try 
      {
         if(matcherSelect.find() == true){
            ResultSet totalRS = statement.executeQuery(sql);
            //execute the sql qeury and assign it to totalRS
            ResultSetMetaData totalRSMD = totalRS.getMetaData();
            //get the meta data for totalRS and assign it to totalRSMD
            //totalRS.next(); // position to first record

            results+=("<table>");
            results+=("<tr>");
            for(int i = 1; i < totalRSMD.getColumnCount() + 1; i++){
                results+=("<th>");
                results+=(totalRSMD.getColumnName(i));
                results+=("</th>");
            }
            //attempt to print out the column names from the result set (totalRS)
            results+=("</tr>");
            while (totalRS.next()) 
            {
              results+=("<tr>");
              for(int i = 1; i < totalRSMD.getColumnCount() + 1; i++){
                  results+=("<td>");
                  results+=(totalRS.getString(i));
                  results+=("</td>");
              }
              results+=("</tr>");
            } // end while
            results+=("</table>");
            totalRS.close();
            HttpSession session = request.getSession();
            session.setAttribute("results",  results);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project4.jsp");
            dispatcher.forward(request, response);
        }
         else if(matcherInsert.find() == true){
            int rowsAffected = statement.executeUpdate(sql);
            Pattern patternShipmentInsert = Pattern.compile("insert into shipments values", Pattern.CASE_INSENSITIVE);
            matcherInsert = patternShipmentInsert.matcher(sql);
            if(matcherInsert.find() == true){
                String supplierNum = sql.substring((sql.indexOf("(") + 1), (sql.indexOf(",")));
                Pattern overHundredPattern = Pattern.compile(OVER_HUNDRED);
                Matcher overHundredMatcher = overHundredPattern.matcher(sql);
                results+=("<div class=\"successUpdate\">");
                results+=("<p class=\"successUpdateBold\">The statement executed successfully.</p>");
                results+=("<p class=\"successUpdateBold\">" + rowsAffected + " row(s) affected</p>");
                if(overHundredMatcher.find() == true){
                    int supplierStatusUpdates = statement.executeUpdate("UPDATE suppliers SET status = status + 5 WHERE snum=" + supplierNum);
                    results+=("<p>Business Logic Detected! - Updating Supplier Status</p>");
                    results+=("<p>Business Logic updated " + supplierStatusUpdates + " status marks</p>");
                }
            }
            else{
                results+=("<div class=\"successUpdate\">");
                results+=("<p class=\"successUpdateBold\">The statement executed successfully.</p>");
                results+=("<p class=\"successUpdateBold\">" + rowsAffected + " row(s) affected</p>");
            }
            results+=("</div>");
            HttpSession session = request.getSession();
            session.setAttribute("results",  results);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project4.jsp");
            dispatcher.forward(request, response);
         }
         else if(matcherUpdate.find() == true){
            int rowsAffected = statement.executeUpdate(sql);
            results+=("<div class=\"successUpdate\">");
            results+=("<p class=\"successUpdateBold\">The statement executed successfully.</p>");
            results+=("<p class=\"successUpdateBold\">" + rowsAffected + " row(s) affected</p>");
            String whereClause = sql.toLowerCase().substring(sql.indexOf("where"), (sql.length()));
            int supplierStatusUpdates = statement.executeUpdate("UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT snum FROM shipments " + whereClause + " AND quantity > 100)");
            if(supplierStatusUpdates > 0){
                results+=("<p>Business Logic Detected! - Updating Supplier Status</p>");
                results+=("<p>Business Logic updated " + supplierStatusUpdates + " status marks</p>");
            }
            HttpSession session = request.getSession();
            session.setAttribute("results",  results);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project4.jsp");
            dispatcher.forward(request, response);
         }
      } // end try
      // if database exception occurs, return error page
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
         results+=("<div class=\"errorDB\">");
         results+=("<p class=\"errorDBBold\">Error executing the SQL statement:</p>");
         results+=("<p>" + sqlException.getMessage() + "</p>");
         results+=("</div>");
         
         HttpSession session = request.getSession();
         session.setAttribute("results",  results);
         RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project4.jsp");
         dispatcher.forward(request, response);
      } // end catch
   } // end method doPost
   
   

   // close SQL statements and database when servlet terminates
   @Override
   public void destroy()
   {
      // attempt to close statements and database connection
      try 
      {
         statement.close();
         connection.close();
      } // end try
      // handle database exceptions by returning error to client
      catch( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
   } // end method destroy
}
