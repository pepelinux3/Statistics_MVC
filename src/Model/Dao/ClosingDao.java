/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Dao;

import DataBase.ConnectDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Martin3
 */
public class ClosingDao {
    
	public int qryIdSubg(String nomSubg, int idEmp){  // SET @num = 0;
		int idSubg= 0;
		 
		try {		
			ConnectDB connection = new ConnectDB();
            
            String query;
            query =  " SELECT subg_clave FROM subgrupos, grupos ";   
            query += "  WHERE gru_clave = subg_gru_clave ";
			query += "    and subg_nombre = '"+nomSubg.replace("_", " ")+"' ";
			query += "    and gru_emp_clave = "+idEmp+";";
            
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
            if(rs.next()){
                idSubg = rs.getInt(1);
            }else{
                idSubg = 0;
            } 
			
			rs.close();
			ps.close();
            connection.getConnection().close();
		
		} catch (SQLException ex){
            System.out.println("Error en la consulta en Subgrupo");
        }
		return idSubg;
	}
	
	public String [][] getStatiItem(int idItem, int idBranch){
		String [][] arrStatis = new String [3][7];
		int cont_row = 0;

        try {
            ConnectDB connection = new ConnectDB();

            String query;
            query =  " SELECT 'Ventas' AS 'tipo',  ";   
			
            query += "  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') = ";
			query += "  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -4 + "+0+" MONTH), '%Y%m'), ";
			query += "  if(cie_suc_clave = 1, cie_transferencias + cie_ventas, cie_ventas), 0)) as 'M1', "; 
            
			query += "  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') = ";
			query += "  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -3 + "+0+" MONTH), '%Y%m'), ";
			query += "  if(cie_suc_clave = 1, cie_transferencias + cie_ventas, cie_ventas), 0)) as 'M2', "; 
			
			query += "  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') = ";
			query += "  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -2 + "+0+" MONTH), '%Y%m'), ";
			query += "  if(cie_suc_clave = 1, cie_transferencias + cie_ventas, cie_ventas), 0)) as 'M3', "; 
			
			query += "  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') = ";
			query += "  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -1 + "+0+" MONTH), '%Y%m'), ";
			query += "  if(cie_suc_clave = 1, cie_transferencias + cie_ventas, cie_ventas), 0)) as 'M4', "; 
			
			query += "  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') = ";
			query += "  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -0 + "+0+" MONTH), '%Y%m'), ";
			query += "  if(cie_suc_clave = 1, cie_transferencias + cie_ventas, cie_ventas), 0)) as 'M5',  0 as M6"; 
			
			query += "  FROM cierres   ";
			query += "  WHERE cie_art_clave = "+idItem+" and cie_suc_clave = "+idBranch;
			query += "        and DATE_FORMAT(cie_fecha, '%Y%m') >= DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -5 + 0  MONTH), '%Y%m')";
			query += "        and DATE_FORMAT(cie_fecha, '%Y%m') <= DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL  0 + 0  MONTH), '%Y%m')";
  			
			query += " UNION ";

			query +=" SELECT 'Existencia', ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =   ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -4 + "+0+" MONTH), '%Y%m'), cie_existencia, 0)) as 'M1',  ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =   ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -3 + "+0+" MONTH), '%Y%m'), cie_existencia, 0)) as 'M2',  ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =   ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -2 + "+0+" MONTH), '%Y%m'), cie_existencia, 0)) as 'M3',  ";
    
            query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =   ";   
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -1 + "+0+" MONTH), '%Y%m'), cie_existencia, 0)) as 'M4',  ";	
    
            query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =   ";  
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL  0 + "+0+" MONTH), '%Y%m'), cie_existencia, 0)) as 'M5', 0 as M6 ";
			
			query +="  FROM cierres   ";  
			query +="  WHERE cie_art_clave = "+idItem+" and cie_suc_clave = "+idBranch;
            query +="	 and DATE_FORMAT(cie_fecha, '%Y%m') >= DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -5 + "+0+" MONTH), '%Y%m')  ";  
			query +="	 and DATE_FORMAT(cie_fecha, '%Y%m') <= DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL  0 + "+0+" MONTH), '%Y%m')  ";   
                                    
            query +=" UNION ";

			query +=" SELECT 'Compras',   ";
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =    ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -4 + "+0+" MONTH), '%Y%m'), cie_compras, 0)) as 'M1',   ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =    ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -3 + "+0+" MONTH), '%Y%m'), cie_compras, 0)) as 'M2',   ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =    ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -2 + "+0+" MONTH), '%Y%m'), cie_compras, 0)) as 'M3',   ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =    ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -1 + "+0+" MONTH), '%Y%m'), cie_compras, 0)) as 'M4',   ";
			
			query +="  sum(if(DATE_FORMAT(cie_fecha, '%Y%m') =    ";
			query +="  DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL  0 + "+0+" MONTH), '%Y%m'), cie_compras, 0)) as 'M5', 0 as M6  ";
			
			query +="  FROM cierres     ";
			query +=" WHERE cie_art_clave = "+idItem+" and cie_suc_clave = "+idBranch;
			query +="   and DATE_FORMAT(cie_fecha, '%Y%m') >= DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL -5 + "+0+" MONTH), '%Y%m')  ";
			query +="   and DATE_FORMAT(cie_fecha, '%Y%m') <= DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL  0 + "+0+" MONTH), '%Y%m')  ";
					
			PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
			while(rs.next()) {
				arrStatis[cont_row][0] = rs.getString(1);
				arrStatis[cont_row][1] = rs.getString(2);
				arrStatis[cont_row][2] = rs.getString(3);
				arrStatis[cont_row][3] = rs.getString(4);
				arrStatis[cont_row][4] = rs.getString(5);
				arrStatis[cont_row][5] = rs.getString(6);
				arrStatis[cont_row][6] = rs.getString(7);
				
				cont_row++;
				System.out.println("MES 5 y 6 = "+rs.getString(5)+", "+rs.getString(6));
			}
						
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta DE ESTADISTICAS");
        }
        return arrStatis;
	}
	
	public int qryIdLine(String noLine, int idEmp){
		int idLine = 0;
		
        try {
            ConnectDB connection = new ConnectDB();
 
            String query;
            query =  " SELECT lin_clave FROM lineas ";   
            query += "  WHERE LEFT(lin_nombre, 2) = '"+noLine.substring(0, 2)+"' and lin_emp_clave = "+idEmp+";";          
            			
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
            if(rs.next()){
                idLine = rs.getInt(1);
            }else{
                idLine = 0;
            } 
			
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en linea");
        }
        return idLine;
    }
	
	public int qryIdItem(String noItem, int idEmp){
		int idItem = 0;
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String query;
            query =  " SELECT art_clave FROM articulos WHERE art_status = 1";   
            query += "    and art_claveArticulo = '"+noItem+"' and art_emp_clave = "+idEmp+";";          
			
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
            if(rs.next()){
                idItem = rs.getInt(1);
            }else{
                idItem = 0;
            } 
			
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en articulo");
        }
        return idItem;
    }
	
	public boolean qrySubgRepo(int idItem, int idEmp){
		boolean bRepo = false;
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String query;
            query =  " SELECT subg_clave FROM articulos, subgrupos ";   
            query += "  WHERE art_subg_clave = subg_clave and subg_status = 1 and subg_reposicion = 1";          
			query += "    and art_status = 1 and art_clave = '"+idItem+"' and art_emp_clave = "+idEmp+";";   
			
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
            if(rs.next()){
                bRepo = true;
            }else{
                bRepo = false;
            } 
			
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en Subgrupo Repo");
        }
        return bRepo;
    }
	
	public String qryDesItem(int idItem, int idEmp){
		String NameItem = "";
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String query;
            query =  " SELECT art_nombreLargo FROM articulos ";   
            query += "  WHERE art_clave = '"+idItem+"' and art_emp_clave = "+idEmp+";";          
            
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
            if(rs.next()){
                NameItem = rs.getString(1);
            }
			
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en articulo");
        }
        return NameItem;
    }
	
	public String qrySubItem(int idItem, int idEmp){
		String nameSubg = "";
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String query;
            query =  " SELECT subg_nombre FROM subgrupos, articulos ";   
            query += "  WHERE art_subg_clave = subg_clave ";          
            query += "    and art_clave = "+idItem+" and art_emp_clave = "+idEmp+";";
			
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
            if(rs.next()){
                nameSubg = rs.getString(1);
            }
			
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en articulo");
        }
        return nameSubg;
    }
	
	public ArrayList<Integer> qryAllBrach (int idComp){
		ArrayList<Integer> arrIdBranch = new ArrayList<Integer>();
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String query;
            query =  " SELECT suc_clave FROM sucursales ";   
            query += "  WHERE suc_status = 1 and suc_emp_clave = "+idComp;
			query +=  "  ORDER BY suc_clave;"; 
            
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();                              
                
			while(rs.next()) {
				arrIdBranch.add(rs.getInt(1));				
			}
						
			rs.close();
			ps.close();
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta");
        }
        return arrIdBranch;
	}
	
	public void sqlInsClosing (String insert){
        try{
            ConnectDB connection = new ConnectDB();           
   
			PreparedStatement ps = connection.getConnection().prepareStatement(insert);
            ps.execute(insert);
            		      
			ps.close();
            connection.getConnection().close();
			
        } catch (SQLException ex){
            System.err.println("Error sql en insert "+ex);
        }
    }
	
	public int getIdMinClosing (String date){
		int idStart = 0;
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String qryIdClosing;
            qryIdClosing =  " SELECT MIN(cie_clave) FROM cierres ";   
            qryIdClosing += "  WHERE cie_fecha = '"+date+"';";          
            
            PreparedStatement ps1 = connection.getConnection().prepareStatement(qryIdClosing);
            ResultSet rs1 = ps1.executeQuery();                              
                
            if(rs1.next()){
                idStart = rs1.getInt(1);
            }else{
                idStart = 0;
            } 
			
			rs1.close();
			ps1.close();
		
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en articulo");
        }
        return idStart;
	}
	
	public int getIdMaxClosing (String date){
		int idStart = 0;
		
        try {
            ConnectDB connection = new ConnectDB();
            
            String qryIdClosing;
            qryIdClosing =  " SELECT MAX(cie_clave) FROM cierres ";   
            qryIdClosing += "  WHERE cie_fecha = '"+date+"';";          
            
            PreparedStatement ps1 = connection.getConnection().prepareStatement(qryIdClosing);
            ResultSet rs1 = ps1.executeQuery();                              
                
            if(rs1.next()){
                idStart = rs1.getInt(1);
            }else{
                idStart = 0;
            } 
			
			rs1.close();
			ps1.close();
		
            connection.getConnection().close();
                    
        } catch (SQLException ex){
            System.out.println("Error en la consulta en articulo");
        }
        return idStart;
	}
	
	public void sqlInsClosing2(String sDate){
		try{
            ConnectDB connection = new ConnectDB();
						
            String insClosing  = " INSERT INTO cierres ";
                   insClosing += "   (Cie_fecha, Cie_art_clave, Cie_suc_clave, Cie_existencia, Cie_compras, ";
				   insClosing += "    Cie_ventas, Cie_transferencias, Cie_negados, Cie_pendientes, Cie_costo) ";
				                           
				   insClosing += "    (SELECT '"+sDate+"' as 'fecha', cos_art_clave, cos_suc_clave, 0, 0, 0, 0, 0, 0, cos_costoPromedio";
				   insClosing += "     FROM costosucursal, sucursales WHERE cos_suc_clave = suc_clave and suc_emp_clave in (1, 2));";
             
            PreparedStatement ps2 = connection.getConnection().prepareStatement(insClosing);
            ps2.execute(insClosing);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en insert2 closing.. "+ex);
        }
    }
	
	public void updClosingStock(int idStart, int idEnd){
		try{
            ConnectDB connection = new ConnectDB();
				
            String updClosing  = " UPDATE cierres, tempinventario SET cie_existencia = teinv_existencia";
                   updClosing += "  WHERE cie_clave >= "+idStart+" and cie_clave <= "+idEnd+" ";
				   updClosing += "    and cie_suc_clave = teinv_suc_clave";
                   updClosing += "    and cie_art_clave = teinv_art_clave";
				   
            PreparedStatement ps2 = connection.getConnection().prepareStatement(updClosing);
            ps2.execute(updClosing);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en UPDATE closing.. "+ex);
        }
    }
	
	public void updClosingStockZero(int idStart, int idEnd){
		try{
            ConnectDB connection = new ConnectDB();
				
            String updClosing  = " UPDATE cierres, tempinventario SET cie_existencia = 0";
                   updClosing += "  WHERE cie_clave >= "+idStart+" and cie_clave <= "+idEnd+" ";
				   updClosing += "    and cie_suc_clave = teinv_suc_clave";
                   updClosing += "    and cie_art_clave = teinv_art_clave";
				   
            PreparedStatement ps2 = connection.getConnection().prepareStatement(updClosing);
            ps2.execute(updClosing);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en UPDATE closing.. "+ex);
        }
    }
	
	public void updClosingSales(String sDate){
		try{
            ConnectDB connection = new ConnectDB();
				
            String updClosing  = " UPDATE cierres, tempventas SET cie_ventas = tevta_cantidad";
                   updClosing += "  WHERE cie_fecha = '"+sDate+"'";
				   updClosing += "    and cie_suc_clave = tevta_suc_clave";
                   updClosing += "    and cie_art_clave = tevta_art_clave";
             
            PreparedStatement ps2 = connection.getConnection().prepareStatement(updClosing);
            ps2.execute(updClosing);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en UPDATE closing.. "+ex);
        }
    }
	
	public void sqlTruncate(String sTable){
		try{
            ConnectDB connection = new ConnectDB();
				
            String sTrun  = " TRUNCATE "+sTable+";";
   
            PreparedStatement ps2 = connection.getConnection().prepareStatement(sTrun);
            ps2.execute(sTrun);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en TRUNCATE closing.. "+ex);
        }
    }
	
	public void sqlInsItem(String noItem, String deItem, String saItem, int idSubg, int idLin, int idCompa){
		try{
            ConnectDB connection = new ConnectDB();
				
			String strAddItem = "INSERT INTO articulos (art_clavearticulo, art_nombreCorto, art_nombreLargo, art_modelo, "+
								"		art_codigoBarras, art_estado, art_serializable, art_inventariable, art_importado, art_sustituto, art_kit, "+
				                "		art_adicional2, art_status, art_mca_clave, art_emp_clave, "+
				                "		art_uni_clave, art_mon_clave, art_lin_clave, art_cargo1, art_cargo2, art_costoPromedio, art_ultimoCosto, "+
				                "		art_subg_clave, art_costo, art_iva, art_categoria1, art_categoria2)	VALUES "+
								
								"		('"+noItem+"', '', '"+deItem+"', '', '', 'ACTIVO', 0, 1, 0, 0, 0, "+
								"		 '"+saItem+"', 1, "+idCompa+", "+idCompa+", "+idCompa+", "+idCompa+", "+
								"		 "+idLin+", 0, 0, 0, 0, "+idSubg+", 0, 16, 'O', 'C')";
			
            PreparedStatement ps2 = connection.getConnection().prepareStatement(strAddItem);
            ps2.execute(strAddItem);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en INSERT Item.. "+ex);
        }
    }
	
	public void sqlInsCostBranch(int idItem, int idCompany){
		try{
            ConnectDB connection = new ConnectDB();
				
			String strAddCost = "INSERT INTO costosucursal (cos_costoPromedio, cos_ultimoCosto, cos_art_clave, cos_suc_clave) "+
				
								"		SELECT 0, 0, art_clave, suc_clave FROM articulos, sucursales "+
				                "		 WHERE art_status = 1 and art_clave =  "+idItem+" "+
				                "          and suc_emp_clave = "+idCompany+" "+   
				                "		 ORDER BY art_clave, suc_clave;  ";
				               		
            PreparedStatement ps2 = connection.getConnection().prepareStatement(strAddCost);
            ps2.execute(strAddCost);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en INSERT cost.. "+ex);
        }
    }
	
	public void sqlInsStockBranch(int idItem, int idCompany){
		try{
            ConnectDB connection = new ConnectDB();
				
			String strAddStock = "INSERT INTO inventario (inv_existencia, inv_nivelMaximo, inv_nivelMinimo, inv_alm_clave, inv_art_clave, inv_pedidos)  "+
				
								"		SELECT 0, 0, 0, suc_clave, art_clave, 0  "+
				                "		  FROM articulos, sucursales"+
				                "        WHERE art_status = 1 and art_clave =  "+idItem+" "+
								"          and suc_emp_clave = "+idCompany+" "+   
				                "		 ORDER BY art_clave, suc_clave;  ";
				               		
            PreparedStatement ps2 = connection.getConnection().prepareStatement(strAddStock);
            ps2.execute(strAddStock);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en INSERT stock.. "+ex);
        }
    }
	
	public void sqlInsClosingBranch(int idItem, int idCompany, String sDate){
		try{
            ConnectDB connection = new ConnectDB();
				
			String strAddStock = "INSERT INTO cierres (Cie_fecha, Cie_art_clave, Cie_suc_clave, Cie_existencia, Cie_compras, Cie_ventas, cie_transferencias, Cie_negados, Cie_pendientes, Cie_Costo)  "+
				
								"		SELECT '"+sDate+"', art_clave, suc_clave, 0, 0, 0, 0, 0, 0, 0  "+
				                "		  FROM articulos, sucursales"+
				                "        WHERE art_status = 1 and art_clave =  "+idItem+" "+
								"          and suc_emp_clave = "+idCompany+" "+   
				                "		 ORDER BY art_clave, suc_clave;  ";
				               		
            PreparedStatement ps2 = connection.getConnection().prepareStatement(strAddStock);
            ps2.execute(strAddStock);
             
			ps2.close();
			connection.getConnection().close();
			 
        } catch (SQLException ex){
            System.err.println("Error sql en INSERT stock.. "+ex);
        }
    }
	
	public DefaultTableModel createTableModel(String sql){
		ConnectDB cnx_query = new ConnectDB();
        
        String[] colItem = {"Sucursal", "SubGrupo", "Id Art ", "Id Suc", "No. de Parte", "Descripcion", "Exist", "Costo", "Precio"};
        String[] DATA = new String[9];
        
        DefaultTableModel TABLE = new DefaultTableModel(null, colItem){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
        
		DecimalFormat formato = new DecimalFormat("#,##0.00");
		
        try{
            Statement Instruction = cnx_query.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet Result = Instruction.executeQuery(sql);
 
            while(Result.next()) {
				// formato.format(listOrder.get(i).getCost());
                DATA[0] = Result.getString("suc_nombreCorto");
                DATA[1] = Result.getString("subg_nombre");
                DATA[2] = Result.getString("art_clave");
				DATA[3] = Result.getString("suc_clave");
                DATA[4] = Result.getString("art_claveArticulo");
				DATA[5] = Result.getString("art_nombreLargo");
                DATA[6] = Result.getString("exist")+"        ";
               // DATA[7] = Result.getString("cos_costoPromedio");
			    DATA[7] = formato.format(Result.getDouble("cos_costoPromedio"));
                DATA[8] = "0.00"; 
                 
                TABLE.addRow(DATA);
            }            
            
            Instruction.close();
            cnx_query.getConnection().close();
        }    
        catch (SQLException SQLE){
            JOptionPane.showMessageDialog(null,"ERROR AL CARGAR LOS DATOS DE LA BD \n ERROR : " + SQLE.getMessage());
        }
        return TABLE;
	}
}
