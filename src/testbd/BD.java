/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testbd;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 *
 * @author wil90
 */
public class BD {
    java.sql.Connection conn;
    int idShow = 0;
    ResultSet Data;
    

    public BD(){
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
    
    public String GetData(boolean next) {
        //ListItems
        
        PreparedStatement pst=null;
        ResultSet rs = null;
        String Requete = "SELECT * FROM new_table";
        String row = "";
        try
        {
            pst = conn.prepareStatement(Requete, 1005, 1008);
            //1005 == ResultSet.TYPE_SCROLL_SENSITIVE
            //1008 == ResultSet.CONCUR_UPDATABLE
            pst.clearParameters();
            rs = pst.executeQuery();

            // Get values out of the ResultSet
            if(rs.next() != false)
            {
                if(idShow != 0){
                while(idShow != Integer.parseInt(rs.getString("id"))){
                        rs.next();
                    }
                }

                if(next){
                    if(!rs.isLast() && idShow != 0 ){
                        rs.next();
                    }
                }
                else{
                    if(!rs.isFirst()){
                        rs.previous();
                    }
                }

                idShow = Integer.parseInt(rs.getString("id"));
                row = "id : " + rs.getString("id") + " one : " 
                        + rs.getString("one") + " two : " + rs.getString("two") 
                        + " three : " + rs.getString("three") + " four : " 
                        + rs.getString("four") + " five : " + rs.getString("five");

                }
            
            // Close ResultSet and PreparedStatement
            rs.close();
            pst.close();
        //ListItems.setListData();
        }
        catch(Exception e){
            System.out.println("Erreur select "+e);
            System.exit(-1);
        }
        
        return row;
    }
        
    public void Create(int Id, String One, int Two, 
            Date Three, int Four, double Five) {
        String Requete = "INSERT INTO new_table(id,one,two,three,four,five) " + 
                "values (?,?,?,?,?,?)";
        try{
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(Requete);
            preparedStmt.setInt(1, Id);
            preparedStmt.setString(2, One);
            preparedStmt.setInt(3, Two);
            preparedStmt.setDate(4, Three);
            preparedStmt.setInt(5, Four);  
            preparedStmt.setDouble(6, Five); 
            
            // execute the preparedstatement
            preparedStmt.execute();
        }
        catch(Exception e){
            System.out.println("Erreur select "+e);
            System.exit(-1);
        }     
    }
    
    public void Edit() {

    }
    
    public void Delete() {
        String sql = "DELETE FROM new_table WHERE id = ?";
 
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, idShow);
            // execute the delete statement
            pstmt.executeUpdate();
            idShow = 0;
 
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
