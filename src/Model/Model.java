/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Controller;
import Controller.ControllerFile;
import Model.Dao.ClosingDao;
import Model.Dao.CombosDao;
import Model.Vo.OrderVo;
import Model.Vo.StockBranchVo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

/**
 *
 * @author Martin3
 */
public class Model {	
	private ReadFile readFile;
	private ControllerFile ctrFile;
	private Controller ctrMain;
	
    public Model(){
        readFile = new ReadFile();
    }
	
	public void setControllerFile(ControllerFile ctrFile) {
        this.ctrFile = ctrFile;
    }
	
	public void setController(Controller ctrMain) {
        this.ctrMain = ctrMain;
    }
	
	public void setFile_txt_Stock(int idCompany, String path, String sDate){
        readFile.readClosing_stock(idCompany, path, sDate);
    }
	
	public void setFile_xls_items(int idCompany, String path, String sDate){
        readFile.readClosing_items(idCompany, path, sDate);
    }

	public void setFile_xls_NewItems(int idCompany, String path, String sDate){
		readFile.readClosing_Newitems(idCompany, path, sDate);
       // readFile.readClosing_items(idCompany, path, sDate);
    }
	
	public void setFile_xls_sales(int idCompany, String path, String sDate){
        readFile.readClosing_sales(idCompany, path, sDate);
    }	
	
	public void setFile_txt_sales(int idCompany, String path, String sDate){
        readFile.readTxt_Sales(idCompany, path, sDate);
    }
	
	public void addClosing_NewMonth(String sDate){
        //readFile.readClosing_items(idCompany, path, sDate);
		ClosingDao cld = new ClosingDao ();
		cld.sqlInsClosing2(sDate);
		
		
    }	
	
	public void setFile_txt_Order(int idCompany, int idBranch, String path){
        //readFile.readPurchase_order(idCompany, path);
		List<OrderVo> listOrder = new ArrayList<OrderVo>();
		listOrder = readFile.readPurchase_order(idCompany, path);
		
		System.out.println("Size: "+listOrder.size());
		
		for (OrderVo obj : listOrder) {
			System.out.println("No of Part: "+obj.getNoItem());
		}
		
		String arrOrderDetail [][] = new String [listOrder.size()][9];
		arrOrderDetail = getArrDetOrder(idBranch, listOrder);
		
		ctrFile.LoadTableModel(getModelTableOrder(arrOrderDetail));
	}
	
	public void setFile_txt_Repo(int idCompany, int idBranch, String path){
        //readFile.readPurchase_order(idCompany, path);
		List<OrderVo> listRepo = new ArrayList<OrderVo>();
		listRepo = readFile.readRepo_order(idCompany, path);
		
		System.out.println("Size: "+listRepo.size());
		
		for (OrderVo obj : listRepo) {
			System.out.println("No of Part: "+obj.getNoItem());
		}
		
		String arrRepoDetail [][] = new String [listRepo.size()][9];
		arrRepoDetail = getArrDetOrder(idBranch, listRepo);
		
		ctrFile.LoadTableModel(getModelTableOrder(arrRepoDetail));
	}
	
	public void setFile_txt_reqOrder(int idCompany, int idBranch, String path){
       // readFile.readClosing_items(idCompany, path, sDate);
		List<OrderVo> listRequ = new ArrayList<OrderVo>();
		listRequ = readFile.readRequest_order(idCompany, path);
		//listRequ = readFile.readRequest_orderCSV(idCompany, path);
		
		for (OrderVo obj : listRequ) {
			System.out.println("No of Part (Req): "+obj.getNoItem());
		}
		
		String arrReqDetail [][] = new String [listRequ.size()][10];
		arrReqDetail = getArrDetRequest(idBranch, listRequ);
		
		ctrFile.LoadTableModel(getModelTableRequest(arrReqDetail));
    }
	
	public void setFile_txt_reqRepo(int idCompany, int idBranch, String path){
		List<OrderVo> listRequ = new ArrayList<OrderVo>();
		listRequ = readFile.readRequest_Repo(idCompany, path);
		//listRequ = readFile.readRequest_RepoCSV(idCompany, path);
		System.out.println("ENTRA A setFile_txt_reqRepo    ****************************************************** ");
		
		for (OrderVo obj : listRequ) {
			System.out.println("No of Part (Req): "+obj.getNoItem());
		}
		
		System.out.println("SALE A setFile_txt_reqRepo    ****************************************************** ");
		
		String arrReqDetail [][] = new String [listRequ.size()][10];
		arrReqDetail = getArrDetRequest(idBranch, listRequ);
		
		ctrFile.LoadTableModel(getModelTableRequest(arrReqDetail));
    }
	
	public String [][] getArrDetOrder (int idBranch, List<OrderVo> listOrder){
		String detail [][] = new String [listOrder.size()][9];
		DecimalFormat formato = new DecimalFormat("#,##0.00");
		
		for(int i=0; i <listOrder.size(); i++){			
			
			detail[i][0] = i+1+"";
			detail[i][1] = listOrder.get(i).getNaSubg();
			detail[i][2] = listOrder.get(i).getIdItem()+"";
			detail[i][3] = idBranch+"";
			detail[i][4] = listOrder.get(i).getNoItem();
			detail[i][5] = listOrder.get(i).getDeItem();
			detail[i][6] = listOrder.get(i).getCant()+"";
			detail[i][7] = formato.format(listOrder.get(i).getCost());
			detail[i][8] = listOrder.get(i).getCant()+"";
			
			System.out.println(i+" asigna = "+listOrder.get(i).getNoItem());
		}
		
		return detail;
	}
	
	public String [][] getArrDetRequest (int idBranch, List<OrderVo> listRequest){
		String detail [][] = new String [listRequest.size()][10];
		DecimalFormat formato = new DecimalFormat("#,##0.00");
		
		for(int i=0; i <listRequest.size(); i++){			
			
			detail[i][0] = i+1+"";
			detail[i][1] = listRequest.get(i).getNaSubg();
			detail[i][2] = listRequest.get(i).getIdItem()+"";
			detail[i][3] = idBranch+"";
			detail[i][4] = listRequest.get(i).getNoItem();
			detail[i][5] = listRequest.get(i).getDeItem();
			detail[i][6] = listRequest.get(i).getCant()+"";
			detail[i][7] = listRequest.get(i).getRequ()+"";  // is exist
			detail[i][8] = listRequest.get(i).getCant()+"";
			detail[i][9] = formato.format(listRequest.get(i).getCost());
			
			System.out.println(i+" asigna req = "+listRequest.get(i).getNoItem());
		}
		
		return detail;
	}
	
	private DefaultTableModel getModelTableOrder(String [][] arrDetail){   
		String[] columns = {"No", "SubGrupo", "Id Art ","Id Suc","No. de Parte", "Descripcion", "Cantidad", "Costo", "Surtido"};
		
		DefaultTableModel ordModel = new DefaultTableModel(arrDetail, columns){
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column ==8){
					return true;
				} else{
					return false;
				}
			}
		};
		
		return ordModel;
	}

	private DefaultTableModel getModelTableRequest(String [][] arrDetail){   
		String[] columns = {"No", "SubGrupo", "Id Art ","Id Suc","No. de Parte", "Descripcion", "Cantidad", "Exist", "Surtido", "Costo"};
		
		DefaultTableModel ordModel = new DefaultTableModel(arrDetail, columns){
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column ==8){
					return true;
				} else{
					return false;
				}
			}
		};
		
		return ordModel;
	}
	
	private String [] getMonthStat(){
		String[] month = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
			              "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		
		//String[] month = {"January", "February", "March", "April", "May", "June", 
		//	              "July", "August", "September", "October", "November", "December"};
		
		int mes = LocalDate.now().getMonthValue()+1;
		int ani = LocalDate.now().getYear();
		
		String [] monthStat = new String [7];
		monthStat[0] = "Tipo";
		
		for(int i=0; i<6; i++){
			mes = mes-1;
			
			if (mes == 0){
				ani = ani-1;
				mes = 12;
			}
				
			System.out.println("No. mes = "+mes+"   month = "+ani+" - "+month[mes-1]);
			monthStat[6-i] = ani+" "+month[mes-1];
		}
		
		return monthStat;
	}
	
	public DefaultTableModel getModelTabStat_Empy(Object [][] arrDetail){   

		DefaultTableModel ordModel = new DefaultTableModel(arrDetail, getMonthStat()){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		return ordModel;
	}
	  //   getModelTabStat_Query
	public DefaultTableModel getModelTabStat_Query(int idItem, int idBranch){   

		ClosingDao cld = new ClosingDao ();
		//cld.sqlVar_zero();
		
		String [][] arrDetail = new String [3][7];
		arrDetail = cld.getStatiItem(idItem, idBranch);
		
		DefaultTableModel stdTModel = new DefaultTableModel(arrDetail, getMonthStat()){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		return stdTModel;
	}
	
	public DefaultComboBoxModel getBranch_DB() {
        CombosDao cbx_bra = new CombosDao();
		
        return cbx_bra.qryBranch();
    }
	
	public DefaultComboBoxModel getGroup_DB(int idCompany) {
        CombosDao cbx_gro = new CombosDao();
         
        return cbx_gro.qryGroup(idCompany);
    }
	
	public DefaultComboBoxModel getSubg_DB(int idGroup) {
        CombosDao cbx_sub = new CombosDao();
         
        return cbx_sub.qrySubgroup(idGroup);
    }

	public void queryTableModel(StockBranchVo stBra, boolean comple, boolean stock, int bra, int gru, int sub) {
		String sql;
        
        sql = createQuery(stBra, comple, stock, bra, gru, sub);
        ClosingDao dao = new ClosingDao();
        
        ctrMain.LoadTable(dao.createTableModel(sql));
	}

	private String createQuery(StockBranchVo stBra, boolean comple, boolean stock, int bra, int gru, int sub) {
		String query = "";
        
        query = " SELECT suc_emp_clave, suc_clave, subg_clave, art_clave, suc_nombreCorto, subg_nombre,  "+
			    "        art_claveArticulo, art_nombreLargo, cos_costoPromedio, ROUND(inv_existencia) as exist"+
                "   FROM articulos, inventario, costosucursal, subgrupos, sucursales " +
				"  WHERE art_subg_clave = subg_clave  and  cos_art_clave = art_clave" +
				"    and cos_suc_clave = suc_clave  and  inv_alm_clave = suc_clave" +
				"    and inv_art_clave = art_clave  and  art_status = 1 and suc_status = 1";
               
        try{
            if (stBra.getNoItem() != null  && comple == true)
                query = query + " and art_clavearticulo = '"+stBra.getNoItem()+"'";
			
			if (stBra.getNoItem() != null  && comple == false)
                query = query + " and art_clavearticulo LIKE '%"+stBra.getNoItem()+"%'";

            if (stBra.getNaItem() != null)
                query = query + " and art_nombreLargo LIKE '%"+stBra.getNaItem()+"%'";

			
			if (bra != 0)
                query = query + " and suc_clave = "+bra;
			
			if (gru != 0)
                query = query + " and subg_gru_clave = "+gru;

            if (sub != 0)
                query = query + " and subg_clave = "+sub;
			
            query = query + " ORDER BY suc_nombreCorto, subg_nombre, art_clavearticulo;";
			
			System.out.println(query);
        } 
        catch (Exception e) {
            //  JOptionPane.showMessageDialog(null,"Se ha presentado un Error"+ e.getMessage());
           System.out.println("hay error de parametros");
        }       
        return query;    
	}

}
