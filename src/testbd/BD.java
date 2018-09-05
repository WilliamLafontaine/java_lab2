/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testbd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wil90
 */
public class BD {
    private java.sql.Connection conn;
    private int idShow = 0;
    private ResultSet Data;
    

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
    
    public void Close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String GetData(int option) {
        //ListItems
        
        PreparedStatement pst=null;
        ResultSet rs = null;
        String Requete = "SELECT * FROM persons";
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

                if(option == 0){
                    if(!rs.isLast() && idShow != 0 ){
                        rs.next();
                    }
                }
                else if( option == 1){
                    if(!rs.isFirst()){
                        rs.previous();
                    }
                }

                idShow = Integer.parseInt(rs.getString("id"));
                row = "id : " + rs.getString("id") + " Nom : " 
                        + rs.getString("Nom") + " Age : " + rs.getString("Age") 
                        + " Naissance : " + rs.getString("Naissance") + " Travails : " 
                        + rs.getString("Travails") + " Argent : " + rs.getString("Argent");

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
        
    public void Create(Person person, boolean Stm) {
            try{
                if(Stm){
                    String Requete = "INSERT INTO persons(id,Nom,Age,Naissance,Travails,Argent) " + 
                        "values (?,?,?,?,?,?)";
                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(Requete);
                    preparedStmt.setInt(1, person.id);
                    preparedStmt.setString(2, person.Nom);
                    preparedStmt.setInt(3, person.Age);
                    preparedStmt.setDate(4, person.Naissance);
                    preparedStmt.setInt(5, person.Travails);  
                    preparedStmt.setDouble(6, person.Argent); 

                    // execute the preparedstatement
                    preparedStmt.execute();
                    preparedStmt.close();
                }
                else{
                    //La date ne marche pas
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(String.format("INSERT INTO persons(id,"
                            + "Nom,Age,Naissance,Travails,Argent) " + 
                    "values (%s,%s,%s,%s,%s,%s)", person.id, person.Nom, person.Age, 
                    new SimpleDateFormat("dd/MM/yyyy").format(person.Naissance),
                    person.Travails, person.Argent));
                    statement.close();
                }
                idShow = person.id;
            }
            catch(Exception e){
                System.out.println("Erreur Create "+e);
                System.exit(-1);
            }
    }
    
    public void Edit(Person person, boolean Stm) {
        
            try{
                if(Stm){
                    String Requete = "UPDATE persons SET id = ?, Nom = ?, Age = ?,"
                    + " Naissance = ?, Travails = ?, Argent = ? WHERE id = ? ";
                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(Requete);
                    preparedStmt.setInt(1, person.id);
                    preparedStmt.setString(2, person.Nom);
                    preparedStmt.setInt(3, person.Age);
                    preparedStmt.setDate(4, person.Naissance);
                    preparedStmt.setInt(5, person.Travails);  
                    preparedStmt.setDouble(6, person.Argent);
                    preparedStmt.setInt(7, person.id);

                    // execute the preparedstatement
                    preparedStmt.execute();
                    preparedStmt.close();
                }
                else{
                    //La date ne marche pas
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(String.format("UPDATE persons SET "
                        + "id = %s, Nom = %s, Age = %s,"
                        + " Naissance = %s, Travails = %s, Argent = %s WHERE id = %s ",
                        person.id, person.Nom, person.Age, 
                        new SimpleDateFormat("dd/MM/yyyy").format(person.Naissance), 
                        person.Travails, person.Argent, person.id));
                    statement.close();
                }
            }
            catch(Exception e){
                System.out.println("Erreur Edit "+e);
                System.exit(-1);
            }
        
        
    }
    
    public void Delete(boolean Stm) {
       
 
            try{
                 if(Stm){
                    String sql = "DELETE FROM persons WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // set the corresponding param
                    pstmt.setInt(1, idShow);
                    // execute the delete statement
                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else{
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(String.format("DELETE FROM persons "
                            + "WHERE id = %s",idShow));
                    statement.close();
                }
                idShow = 0;
            }
            catch (Exception e) {
                System.out.println("erreur Delete " + e.getMessage());
            }
        
    }
}
