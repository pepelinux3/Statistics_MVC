/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Dao;

import DataBase.ConnectDB;
import Model.Vo.BranchVo;
import Model.Vo.GroupVo;
import Model.Vo.SubgroupVo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Martin3
 */
public class CombosDao {
	
	public DefaultComboBoxModel qryBranch() {                  
        ConnectDB cnx_query = new ConnectDB();        
 
        String sql = "SELECT suc_clave, suc_emp_clave, suc_nombre, suc_nombreCorto "+
		             "  FROM sucursales WHERE suc_status = 1";
		
        DefaultComboBoxModel model = new DefaultComboBoxModel();
                   
        try{
            Statement st = cnx_query.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
                       
            while(rs.next()){
                model.addElement(new BranchVo(rs.getInt(1),  rs.getInt(2), rs.getString(3), rs.getString(4)));
            }
			
			rs.close();				
            st.close();
            cnx_query.getConnection().close(); 
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"ERROR AL CARGAR LOS DATOS DE LA BD \n ERROR : " + ex.getMessage());
        }         
        return model;
    }
	
	public DefaultComboBoxModel qryGroup(int idCompany) {                  
        ConnectDB cnx_query = new ConnectDB();        
 
        String sql = "SELECT gru_clave, gru_emp_clave, gru_nombre "+
		             "  FROM grupos WHERE gru_status = 1 "+
			         "   and gru_emp_clave = "+idCompany+" ORDER BY gru_nombre";
		
        DefaultComboBoxModel model = new DefaultComboBoxModel();
                   
        try{
            Statement st = cnx_query.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
                       
            while(rs.next()){
                model.addElement(new GroupVo(rs.getInt(1),  rs.getInt(2), rs.getString(3)));
            }
			
			rs.close();				
            st.close();
            cnx_query.getConnection().close(); 
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"ERROR AL CARGAR LOS DATOS DE LA BD \n ERROR : " + ex.getMessage());
        }         
        return model;
    }
	
	public DefaultComboBoxModel qrySubgroup(int idGroup) {                  
        ConnectDB cnx_query = new ConnectDB();        

        String sql = "SELECT subg_clave, subg_gru_clave, subg_nombre, subg_descripcion "+
		             "  FROM subgrupos WHERE subg_status = 1 and subg_gru_clave = "+idGroup+
			         "  ORDER BY subg_nombre";
		
        DefaultComboBoxModel model = new DefaultComboBoxModel();
                   
        try{
            Statement st = cnx_query.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
                       
            while(rs.next()){
                model.addElement(new SubgroupVo(rs.getInt(1),  rs.getInt(2), rs.getString(3), rs.getString(4)));
            }
			
			rs.close();				
            st.close();
            cnx_query.getConnection().close(); 
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"ERROR AL CARGAR LOS DATOS DE LA BD \n ERROR : " + ex.getMessage());
        }         
        return model;
    }
	
	
}
