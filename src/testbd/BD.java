/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testbd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wil90
 */
public class BD {
    java.sql.Connection conn;

    public void BD(){
        //Server
        String driver = "com.mysql.cj.jdbc.Driver";
        String servername = "localhost";
        String port = "3306";
        String shema = "test_shema";
        String parameter = "?serverTimezone=UTC";
        String url = "jdbc:mysql://" + servername + ":" + port + "/" + shema + parameter;
        String username = "root";
        String password = "t0t0g5wil"; 
        
        try{
            Class.forName(driver).newInstance();
            conn = java.sql.DriverManager.getConnection(
            url, username, password);
            } catch(Exception e){
            System.out.println("Connection ratée: "+e);
            System.exit(-1);
        }
    }
    
    public String GetData() {
        //ListItems
        
        PreparedStatement pst=null;
        ResultSet rs = null;
        String Requete = "SELECT * FROM new_table";
        try
        {
        pst = conn.prepareStatement(Requete, 1005, 1008);
        //1005 == ResultSet.TYPE_SCROLL_SENSITIVE
        //1008 == ResultSet.CONCUR_UPDATABLE
        pst.clearParameters();
        rs = pst.executeQuery();
        
        String header[] = new String[] { "id", "one", "two",
            "three", "four", "five" };
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        dtm.setColumnIdentifiers(header);
        ListItems.setModel(dtm);
        while(rs.next()){
            dtm.addRow(new Object[] { rs.getString(0), rs.getString(1), rs.getString(2),
                             rs.getString(3), rs.getString(4) });
        }
        //ListItems.setListData();
        }
        catch(Exception e){
            System.out.println("Connection ratée: "+e);
            System.exit(-1);
        }
    }
    
    public void Create() {
        
    }
    
    public void Edit() {

    }
    
    public void Delete() {

    }
}
