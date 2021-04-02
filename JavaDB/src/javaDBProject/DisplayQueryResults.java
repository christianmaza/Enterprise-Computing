
// Display the results of queries against the bikes table in the bikedb database.
package javaDBProject;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;


public class DisplayQueryResults extends JFrame 
{
   // default query retrieves all data from bikes table
   static final String DEFAULT_QUERY = "SELECT * FROM bikes";
   
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;
   private JLabel dbInformationLabel;
   private JLabel enterCommandLabel;
   private JLabel resultLabel;
   private JLabel driverSelectorLabel;
   private JLabel dbSelectorLabel;
   private JLabel dbUserNameLabel;
   private JLabel dbPasswordLabel;
   private JLabel connectedLabel;
   private JComboBox driverSelector;
   private JComboBox dbSelector;
   private JTextField dbUserName;
   private JTextField dbPassword;
   private JButton executeButton;
   private JButton connectButton;
   private JButton clearButton;
   private JButton clearResultButton;
   private JPanel panelDBSelection;
   private JPanel connecterQueryContainer;
   private JPanel resultContainer;
   private JPanel commandsBox;
   private Box boxResult;
   private JScrollPane resultPane;
   private FlowLayout resultFlow;
   private FlowLayout connecterQueryFlow;
   private FlowLayout commandFlow;
   private JTable resultTable;
   
   private Connection connection;
   private DatabaseMetaData dbMetaData;
   
   
   
   // create ResultSetTableModel and GUI
   public DisplayQueryResults() 
   {   
      super( "Displaying Query Results" );
      
      // create ResultSetTableModel and display database table
      try 
      {  
         //create an array of possible db drivers for the driverSelector JComboBox
         String[] comboStringDriver = {"com.mysql.cj.jdbc.Driver"};
         
         //create an array of possible db urls for the dbSelector JComboBox
         String[] comboStringDBURL = {"jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC",
                                      "jdbc:mysql://localhost:3306/bikedb?useTimezone=true&serverTimezone=UTC"};
         dbInformationLabel = new JLabel("Enter Database Information");
         dbInformationLabel.setForeground(Color.BLUE);
         driverSelectorLabel = new JLabel("JDBC Driver");
         dbSelectorLabel = new JLabel("Database URL");
         dbUserNameLabel = new JLabel("Username");
         dbPasswordLabel = new JLabel("Password");
        
         driverSelector = new JComboBox(comboStringDriver);
         driverSelector.setSelectedIndex(0);
         driverSelector.setPreferredSize(new Dimension(250, 30));
         driverSelector.setMaximumSize(driverSelector.getPreferredSize());
         
         dbSelector = new JComboBox(comboStringDBURL);
         dbSelector.setSelectedIndex(0);
         dbSelector.setPreferredSize(new Dimension(250, 30));
         dbSelector.setMaximumSize(dbSelector.getPreferredSize());
         
         dbUserName = new JTextField();
         dbPassword = new JPasswordField();
         
         connectedLabel = new JLabel("No Connection Now");
         connectedLabel.setBackground(Color.BLACK);
         connectedLabel.setForeground(Color.RED);
         connectedLabel.setOpaque(true);
         connectedLabel.setPreferredSize(new Dimension(250,30));
         connectedLabel.setMaximumSize(connectedLabel.getPreferredSize());
         
         connectButton = new JButton("Connect to Database");
         connectButton.setBackground(Color.GREEN);
         connectButton.setForeground(Color.BLACK);
         connectButton.setBorderPainted(false);
         connectButton.setOpaque(true);
         
         clearButton = new JButton("Clear SQL Command");
         clearButton.setBackground(Color.WHITE);
         clearButton.setForeground(Color.RED);
         clearButton.setBorderPainted(false);
         clearButton.setOpaque(true);
         
         // set up JButton for executing queries
         executeButton = new JButton("Execute SQL Command");
         executeButton.setBackground(Color.BLUE);
         executeButton.setForeground(Color.YELLOW);
         executeButton.setBorderPainted(false);
         executeButton.setOpaque(true);
        

         enterCommandLabel = new JLabel("Enter An SQL Command");
         enterCommandLabel.setForeground(Color.BLUE);
         enterCommandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
         // set up JTextArea in which user types queries
		//	queryArea = new JTextArea( 3, 100);
         queryArea = new JTextArea("", 11, 30);
         queryArea.setWrapStyleWord( true );
         queryArea.setLineWrap( true );
         
         JScrollPane scrollPane = new JScrollPane( queryArea,
         ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
         scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
 
         panelDBSelection = new JPanel();
         panelDBSelection.setLayout(new GridLayout(5,2));
         panelDBSelection.add(dbInformationLabel);
         panelDBSelection.add(new JLabel());
         panelDBSelection.add(driverSelectorLabel);
         panelDBSelection.add(driverSelector);
         panelDBSelection.add(dbSelectorLabel);
         panelDBSelection.add(dbSelector);
         panelDBSelection.add(dbUserNameLabel);
         panelDBSelection.add(dbUserName);
         panelDBSelection.add(dbPasswordLabel);
         panelDBSelection.add(dbPassword);
         
         commandFlow = new FlowLayout(FlowLayout.LEFT, 20, 1);
         commandsBox = new JPanel();
         commandsBox.setLayout(commandFlow);
         commandsBox.add(connectedLabel);
         commandsBox.add(connectButton);
         commandsBox.add(clearButton);
         commandsBox.add(executeButton);
         
         // create Box to manage placement of queryArea and 
         // submitButton in GUI
         Box boxQuery = Box.createVerticalBox();
         boxQuery.add(enterCommandLabel);
         boxQuery.add(Box.createRigidArea(new Dimension(0,9)));
         boxQuery.add(scrollPane);
         
         connecterQueryFlow = new FlowLayout(FlowLayout.LEFT);
         connecterQueryContainer = new JPanel();
         connecterQueryContainer.setLayout(connecterQueryFlow);
         connecterQueryContainer.add(panelDBSelection);
         connecterQueryContainer.add(boxQuery);
         
         resultLabel = new JLabel("SQL Execution Result Window");
         resultLabel.setForeground(Color.BLUE);
         // create JTable delegate for tableModel 
         resultTable = new JTable();
         resultTable.setModel(new DefaultTableModel());
         resultTable.setEnabled(false);
         resultTable.setGridColor(Color.BLACK);
         
         clearResultButton = new JButton("Clear Result Window");
         clearResultButton.setAlignmentX(Component.LEFT_ALIGNMENT);
         clearResultButton.setBackground(Color.YELLOW);
         clearResultButton.setForeground(Color.BLACK);
         clearResultButton.setBorderPainted(false);
         clearResultButton.setOpaque(true);
         
         resultPane = new JScrollPane(resultTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         resultPane.setPreferredSize(new Dimension(800,300));
         resultPane.setAlignmentX(Component.LEFT_ALIGNMENT);
         
         boxResult = Box.createVerticalBox();
         boxResult.add(resultLabel);
         boxResult.add(Box.createRigidArea(new Dimension(0,9)));
         boxResult.add(resultPane);
         boxResult.add(Box.createRigidArea(new Dimension(0,9)));
         boxResult.add(clearResultButton);
         
         resultFlow = new FlowLayout(FlowLayout.LEFT);
         resultContainer = new JPanel();
         resultContainer.setLayout(resultFlow);
         resultContainer.add(boxResult);
         
         
         // place GUI components on content pane
         add(connecterQueryContainer, BorderLayout.NORTH);
         add(commandsBox, BorderLayout.CENTER);
         add(resultContainer, BorderLayout.SOUTH);
         
         
         connectButton.addActionListener( 
         
            new ActionListener()
            {
                
               @Override
               public void actionPerformed( ActionEvent event )
               {
                 Properties properties = new Properties();
                 FileInputStream filein = null;
                 MysqlDataSource dataSource = null;
                 String dbName = "";
                 String userName = "";
                 String password = "";
                 try{
                    filein = new FileInputStream("db.properties");
                    properties.load(filein);
                    
                    if(driverSelector.getSelectedItem().toString().equals(properties.getProperty("MYSQL_DB_DRIVER_CLASS"))){
                        dataSource = new MysqlDataSource();
                    }
                    else{
                        return;
                    }
                    
                    dbName = dbSelector.getSelectedItem().toString();
                    userName = dbUserName.getText();
                    password = dbPassword.getText();
                    
                    dataSource.setURL(dbName);
                    dataSource.setUser(userName);
                    dataSource.setPassword(password);
                    
                    connection = dataSource.getConnection();
                    dbMetaData = connection.getMetaData();
                    if(!connection.isClosed()){
                        connectedLabel.setText("Connected to " + dbMetaData.getURL());
                    }
                    else if(connection.isClosed()){
                        connectedLabel.setText("No Connection Now");
                    }
                    
                    resultTable.setModel(new DefaultTableModel());
                    
                    try{
                        tableModel = new ResultSetTableModel(connection);
                    }
                    catch ( SQLException sqlException ) 
                    {
                       JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
                          "Database error", JOptionPane.ERROR_MESSAGE );
                       
                       // ensure database connection is closed
                       tableModel.disconnectFromDatabase();

                       System.exit( 1 );   // terminate application
                    }
                    catch ( ClassNotFoundException classNotFound ) 
                    {
                       JOptionPane.showMessageDialog( null, 
                          "MySQL driver not found", "Driver not found",
                          JOptionPane.ERROR_MESSAGE );

                       System.exit( 1 ); // terminate application
                    }
                    
                 }
                 catch(IOException e){
                     e.printStackTrace();
                 }
                 catch ( SQLException sqlException ){
                   JOptionPane.showMessageDialog(null, "Incorrect username or password", "Connection error", JOptionPane.ERROR_MESSAGE);
                   sqlException.printStackTrace();
                   //System.exit( 1 );
                 }
                
               } // end actionPerformed
            }  // end ActionListener inner class       
         );
         
         // create event listener for executeButton
         executeButton.addActionListener( 
         
            new ActionListener() 
            {
               // pass query to table model
               @Override
               public void actionPerformed( ActionEvent event )
               {
                  // perform a new query and set the model for resultTable
                  try 
                  {  
                     Pattern pattern = Pattern.compile("select", Pattern.CASE_INSENSITIVE);
                     Matcher matcher = pattern.matcher(queryArea.getText());
                     if(matcher.find() == true){
                         tableModel.setQuery(queryArea.getText());
                     }
                     else{
                         resultTable.setModel(new DefaultTableModel());
                         tableModel.setUpdate(queryArea.getText());
                         return;
                     }
                     
                     if(!resultTable.getModel().toString().contains("ResultSetTableModel")){
                        resultTable.setModel(tableModel);
                     }
                     
                  } // end try
                  catch ( SQLException sqlException ) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        sqlException.getMessage(), "Database error", 
                        JOptionPane.ERROR_MESSAGE );
                     
                     // try to recover from invalid user query 
                     // by executing default query                 
                  } // end outer catch
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
         
         clearButton.addActionListener( 
            new ActionListener() 
            {
               @Override
               public void actionPerformed( ActionEvent event )
               {    
                  //clear the queryArea by setting queryArea text to an empty string
                  try 
                  {  
                   queryArea.setText(""); 
                  } // end try
                  catch (Exception e) 
                  {
                    e.printStackTrace();
                  } // end outer catch
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
         
         clearResultButton.addActionListener( 
            new ActionListener() 
            {
               @Override
               public void actionPerformed( ActionEvent event )
               {    
                  //clear the queryArea by setting queryArea text to an empty string
                  try 
                  {  
                   resultTable.setModel(new DefaultTableModel());
                  } // end try
                  catch (Exception e) 
                  {
                    e.printStackTrace();
                  } // end outer catch
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
         
         setSize( 900, 700 ); // set window size
         setVisible( true ); // display window  
      } // end try
      catch(Exception e){
          e.printStackTrace();
      }
      
      // dispose of window when user quits application (this overrides
      // the default of HIDE_ON_CLOSE)
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      
      // ensure database connection is closed when user quits application
      addWindowListener(new WindowAdapter() 
         {
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
               tableModel.disconnectFromDatabase();
               System.exit( 0 );
            } // end method windowClosed
         } // end WindowAdapter inner class
      ); // end call to addWindowListener
   } // end DisplayQueryResults constructor
   
   // execute application
   public static void main( String args[] ) 
   {
      new DisplayQueryResults();     
   } // end main
} // end class DisplayQueryResults
