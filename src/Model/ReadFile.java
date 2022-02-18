/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.ControllerFile;
import Model.Dao.ClosingDao;
import Model.Vo.OrderVo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author Martin3
 */
public class ReadFile {

	public static final char SEPARADOR = ',';
	public static final char COMILLAS = '"';
	
	public void readClosing_stock (int idCompany, String pathFile, String sDate){
		String line = "";
		String strInsRow = "";
		String strInsAll = "";
			
		int idBranch;
		int idItem;
		int noStock;
		
        try(BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
			ClosingDao clo = new ClosingDao();
			clo.sqlTruncate("tempinventario");
			
			String strInsHeader = "INSERT INTO tempinventario (teinv_fecha, teinv_art_clave, teinv_suc_clave, teinv_existencia) VALUES ";
			
            while ((line = br.readLine()) != null) {
				System.out.println(line.substring(0,9)+"-"+line.substring(10,30)+"-"+line.substring(69,78));
				
				idBranch = findIdBranch((line.substring(0,9).trim()));
				idItem = clo.qryIdItem(line.substring(10,30).trim(), idCompany);
				noStock = (int) Double.parseDouble(line.substring(69,78).trim());
					
				if(idItem != 0 && idBranch != 0 && !sDate.isEmpty()){
					strInsRow = "('"+sDate+"', "+idItem+", "+idBranch+", "+noStock+"), ";
					strInsAll = strInsAll + strInsRow;
				} else{
					System.out.println("FALTA UN DATO AL LEER TXT  x");
				}
            }
			
			String strInsFinal = strInsHeader + strInsAll;
			strInsFinal = strInsFinal.substring(0, strInsFinal.length()-2);

			clo.sqlInsClosing(strInsFinal);
			int idStart = clo.getIdMinClosing(sDate);
			int idEnd = clo.getIdMaxClosing(sDate);
			
			System.out.println("Cierre Inicial = "+idStart+"    .......................................................");
			
			if((idStart == 0) || (idEnd == 0)){
				JOptionPane.showMessageDialog(null, "Date incorrect!",
					"Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				clo.updClosingStockZero(idStart, idEnd);
				clo.updClosingStock(idStart, idEnd);
			}
				
			System.out.println("Terminado.... ");   
			JOptionPane.showMessageDialog(null, "Stock update successfully completed ");
        }
		catch(java.lang.StringIndexOutOfBoundsException ex){
            System.out.println("Ocurrio un error:(Indice fuera de rango): " + ex);
			JOptionPane.showMessageDialog(null, "Error to read file, "+ex, "Error File", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
        }
		
        catch (IOException e) {
            System.out.println("An error occurred.");
			JOptionPane.showMessageDialog(null, "Error to load file", "Error File", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }	
	}

	public List<OrderVo> readPurchase_order(int idCompany, String pathFile){
		String line = "";
		
		List<OrderVo> listOrder= new ArrayList<OrderVo>(); 
		
        try(BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
			ClosingDao clo = new ClosingDao();
			OrderVo orderVo;
			
            while ((line = br.readLine()) != null) {			
				int idItem = clo.qryIdItem(line.substring(0, 20).trim(), idCompany);
				
				if(idItem == 0 ){
					System.out.println("Articulo: "+line.substring(0,20).trim()+"  no existe... ");
					JOptionPane.showMessageDialog(null, "Articulo: '"+line.substring(0,20).trim()+"' no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
					//break;
				} else{
					int cantOrd = (int) Double.parseDouble(line.substring(117, 125).trim());
					
					String orItemDes = clo.qryDesItem(idItem, idCompany);
					String orSubgDes = clo.qrySubItem(idItem, idCompany);
					String orItemNum = line.substring(0, 20).trim();
					double orOrdeCos = Double.parseDouble(line.substring(135, 148).trim());					
					
					
					// Muestra en pantalla el valor 123.456,79 teniendo en cuenta que se usa la puntuación española
					
					System.out.println(orSubgDes+"   "+line.substring(0, 20).trim()+"  "+orItemDes+"   "+cantOrd+"  "+line.substring(135, 146));
					// OrderVo(int idItem, String naSubg, String noItem, String deItem, int cant, double cost)
					orderVo = new OrderVo(idItem, orSubgDes, orItemNum, orItemDes, cantOrd, 0, orOrdeCos);
					listOrder.add(orderVo);
				}				
            }
			
			System.out.println("Terminado.... ");   
			JOptionPane.showMessageDialog(null, "Load order successfully completed ");
        }
		catch(java.lang.StringIndexOutOfBoundsException ex){
            System.out.println("Ocurrio un error:(Indice fuera de rango): " + ex);
			JOptionPane.showMessageDialog(null, "Error to read file, "+ex, "Error File", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
        }
		
        catch (IOException e) {
            System.out.println("An error occurred.");
			JOptionPane.showMessageDialog(null, "Error to load file", "Error File", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
		
		return listOrder;
	}
	
	public List<OrderVo> readRepo_order(int idCompany, String pathFile){
		String line = "";
		boolean isRepo = false;
		
		List<OrderVo> listOrder= new ArrayList<OrderVo>(); 
		
        try(BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
			ClosingDao clo = new ClosingDao();
			OrderVo orderVo;
			
            while ((line = br.readLine()) != null) {			
				int idItem = clo.qryIdItem(line.substring(0, 20).trim(), idCompany);
				int cantOrd = (int) Math.round( Double.parseDouble(line.substring(117, 125).trim()));
				
				if(idItem != 0)
					isRepo = clo.qrySubgRepo(idItem, idCompany);	
				
				if((idItem != 0) && (isRepo) && (cantOrd != 0)){				
					String orItemDes = clo.qryDesItem(idItem, idCompany);
					String orSubgDes = clo.qrySubItem(idItem, idCompany);
					String orItemNum = line.substring(0, 20).trim();
					double orOrdeCos = Double.parseDouble(line.substring(135, 148).trim());								
					
					System.out.println(orSubgDes+"   "+line.substring(0, 20).trim()+"  "+orItemDes+"   "+cantOrd+"  "+line.substring(135, 146));
		
					orderVo = new OrderVo(idItem, orSubgDes, orItemNum, orItemDes, cantOrd, 0, orOrdeCos);
					listOrder.add(orderVo);
				} 
				
				if(idItem == 0 ){
					System.out.println("Articulo: "+line.substring(0,20).trim()+"  no existe... ");
					JOptionPane.showMessageDialog(null, "Articulo: '"+line.substring(0,20).trim()+"' no encontrado", "Error", JOptionPane.ERROR_MESSAGE);		
				}
            }
			
			System.out.println("Terminado.... ");   
			JOptionPane.showMessageDialog(null, "Load order successfully completed ");
        }
		catch(java.lang.StringIndexOutOfBoundsException ex){
            System.out.println("Ocurrio un error:(Indice fuera de rango): " + ex);
			JOptionPane.showMessageDialog(null, "Error to read file, "+ex, "Error File", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
        }
		
        catch (IOException e) {
            System.out.println("An error occurred.");
			JOptionPane.showMessageDialog(null, "Error to load file", "Error File", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
		
		return listOrder;
	}
	

	public int findIdBranch (String stBranch){
		int result = 0;
		
		switch (stBranch) {
            case "GENERAL": result = 1;   break;
            case "CBA":     result = 2;   break;
            case "COA":     result = 3;   break;
			case "TUX":     result = 4;   break;
			case "MER":     result = 5;   break;
			case "VIL":     result = 6;   break;	
			case "TEJ":     result = 11;  break;
			   
            default:        result = 0;   break;
        }
		return result;
	}
	
	public void readClosing_Newitems(int idCompany, String pathFile, String sDate){
		try {	
			File f = new File(pathFile);
			Workbook wb = Workbook.getWorkbook(f);
			Sheet s = wb.getSheet(0);
			
			int idItem = 0;
			int idSubg = 0;
			int idLine = 0;
			int row = s.getRows();
			
			String stNombre = "";
			String stItem = "";
			String stSat = "";

			ClosingDao clo = new ClosingDao();
			
			for(int i=0; i<row; i++){
				Cell cItem = s.getCell(1, i);      // No. Parte   
				Cell cNomb = s.getCell(3, i);      // Descripcion
				Cell cSubg = s.getCell(83, i);     // Subgrupo
				Cell cSat =  s.getCell(155, i);    // clave SAT
				
				idItem = clo.qryIdItem(cItem.getContents(), idCompany);
				
				stNombre = cNomb.getContents();
				stItem = cItem.getContents();
				stSat = cSat.getContents()+"";

				if(idItem == 0){					
					idSubg = clo.qryIdSubg(cSubg.getContents(), idCompany);
					idLine = clo.qryIdLine(cSubg.getContents(), idCompany);
					
					if(idSubg > 0  &&  idLine > 0){
						clo.sqlInsItem(stItem, stNombre, stSat, idSubg, idLine, idCompany);
						
						idItem = clo.qryIdItem(stItem, idCompany);
						clo.sqlInsCostBranch(idItem, idCompany);
						clo.sqlInsStockBranch(idItem, idCompany);
						clo.sqlInsClosingBranch(idItem, idCompany, sDate);
					} else{
						System.out.println("Subgrupo o Linea : '"+cSubg.getContents()+"'  no existe, Articulo: '"+cItem.getContents()+"'");
					}
					
				} else{
					System.out.println("Articulo ya existe: "+cItem.getContents());
				}	
			}
			
			System.out.println("Terminado.... ");
			JOptionPane.showMessageDialog(null, "Closing successfully completed ");
				
		} catch (IOException ex) {
			System.out.println("An error occurred (Excel).");
			JOptionPane.showMessageDialog(null, "Error to load file (Excel)", "Error File", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
			
		} catch (BiffException ex) {
			System.out.println("An error occurred (Excel)2.");
			JOptionPane.showMessageDialog(null, "Error to read file (Excel)", "Error File", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
		}
	}
	
	//  https://www.youtube.com/watch?v=yuOmQ5I65II
	public void readClosing_items(int idCompany, String pathFile, String sDate){
		try {	
			File f = new File(pathFile);
			Workbook wb = Workbook.getWorkbook(f);
			Sheet s = wb.getSheet(0);
			
			int idItem = 0;
			int row = s.getRows();
			//int col = s.getColumns();
			
			String strInsRow = "";
			String strInsAll = "";
			
			ClosingDao clo = new ClosingDao();
			
			ArrayList<Integer> arrAllBranch = new ArrayList<Integer>();	
			arrAllBranch = clo.qryAllBrach(idCompany);
			
			String strInsHeader = "INSERT INTO tempventas (tevta_fecha, tevta_art_clave, tevta_suc_clave, tevta_cantidad) VALUES ";
			System.out.println(strInsHeader);
			
			for(int i=0; i<row; i++){
				Cell c = s.getCell(1, i);
				
				idItem = clo.qryIdItem(c.getContents(), idCompany);
			//	ctrlFile.setItemClosing(idItem+" x");
				//System.out.println(strInsRow);
				if(idItem != 0){
					for (int j=0; j<arrAllBranch.size(); j++) {
					//System.out.println("id Suc = "+arrAllBranch.get(j));
						int idBranch = arrAllBranch.get(j);

						strInsRow = "('"+sDate+"', "+idItem+", "+idBranch+", 0, 0, 0, 0, 0, 0, 0), ";
						strInsAll = strInsAll + strInsRow;
					}
				} else {
					System.out.println("Articulo no encontrado: "+c.getContents());
				}	
			}
			
			String strInsFinal = strInsHeader + strInsAll;
			strInsFinal = strInsFinal.substring(0, strInsFinal.length()-2);

			clo.sqlInsClosing(strInsFinal);
			
			System.out.println("Terminado.... ");
			JOptionPane.showMessageDialog(null, "Closing successfully completed ");
				
		} catch (IOException ex) {
			System.out.println("An error occurred (Excel).");
			JOptionPane.showMessageDialog(null, "Error to load file (Excel)", "Error File", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
			
		} catch (BiffException ex) {
			System.out.println("An error occurred (Excel)2.");
			JOptionPane.showMessageDialog(null, "Error to read file (Excel)", "Error File", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
		}
	}
	
	public void readClosing_sales(int idCompany, String pathFile, String sDate){
		try {	
			File f = new File(pathFile);
			Workbook wb = Workbook.getWorkbook(f);
			Sheet s = wb.getSheet(0);
			
			int row = s.getRows();
			
			String strInsRow = "";
			String strInsAll = "";
			
			ClosingDao clo = new ClosingDao();
			clo.sqlTruncate("tempventas");
			
			String strInsHeader = "INSERT INTO tempventas (tevta_fecha, tevta_art_clave, tevta_suc_clave, tevta_cantidad) VALUES ";
			
			for(int i=1; i<row; i++){   //  for starts with i=0
				Cell xlBra = s.getCell(0, i);
				Cell xlIte = s.getCell(2, i);
				Cell xlCan = s.getCell(5, i);
				
				int idBran = findIdBranch(xlBra.getContents());
				int idItem = clo.qryIdItem(xlIte.getContents(), idCompany);
				int cant =   Integer.parseInt(xlCan.getContents()); 
				
				if(idItem != 0){
					strInsRow = "('"+sDate+"', "+idItem+", "+idBran+", "+cant+"), ";
					strInsAll = strInsAll + strInsRow;
					
				} else {
					System.out.println("Articulo no encontrado: "+xlIte.getContents());
				}	
			}
			
			String strInsFinal = strInsHeader+strInsAll;
			strInsFinal = strInsFinal.substring(0, strInsFinal.length()-2);

			clo.sqlInsClosing(strInsFinal);
			clo.updClosingSales(sDate);
			
			System.out.println("Terminado.... ");
			JOptionPane.showMessageDialog(null, "Closing sales successfully completed ");
				
		} catch (IOException ex) {
			System.out.println("An error occurred (Excel).");
			JOptionPane.showMessageDialog(null, "Error to load file (Excel)", "Error File", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
			
		} catch (BiffException ex) {
			System.out.println("An error occurred (Excel)2.");
			JOptionPane.showMessageDialog(null, "Error to read file (Excel)", "Error File", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
		}
	}

	public void readTxt_Sales(int idCompany, String pathFile, String sDate) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";  
		
		String strInsRow = "";
		String strInsAll = ""; 
		
		try {
			ClosingDao clo = new ClosingDao();
			clo.sqlTruncate("tempventas");
			
			br = new BufferedReader(new FileReader(pathFile));
			
			String strInsHeader = "INSERT INTO tempventas (tevta_fecha, tevta_art_clave, tevta_suc_clave, tevta_cantidad) VALUES ";
			
			while ((line = br.readLine()) != null) {                
				String[] datos = line.split(cvsSplitBy);
				
				int idBranch = findIdBranch(datos[0].trim());
				int idItem = clo.qryIdItem(datos[2].trim(), idCompany);
				System.out.println("antes de parsear...");
				int cant = (int) Double.parseDouble(datos[5].trim()); 
				
				strInsRow = "('"+sDate+"', "+idItem+", "+idBranch+", "+cant+"), ";
				strInsAll = strInsAll + strInsRow;
				
				System.out.println("Branch = "+datos[0].trim()+ ",  Id Item = " + datos[2].trim()+ ",  Cant = " + datos[5].trim());
			}
			
			String strInsFinal = strInsHeader+strInsAll;
			strInsFinal = strInsFinal.substring(0, strInsFinal.length()-2);

			clo.sqlInsClosing(strInsFinal);
			clo.updClosingSales(sDate);
			
			System.out.println("Terminado.... "); 
			JOptionPane.showMessageDialog(null, "Load Request successfully completed ");
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Error file (txt): "+e.getMessage(), "Error File", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error file (txt): "+e.getMessage(), "Error read text", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error file (txt): "+e.getMessage(), "Error read text", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<OrderVo> readRequest_order(int idCompany, String path) {
		int idItem = 0;
		
		List<OrderVo> listReques = new ArrayList<OrderVo>();
		ClosingDao clo = new ClosingDao();
		OrderVo orderVo;
		
		BufferedReader br = null;
		String line = "";
		String cvsSplit1 = ","+"\"";
		String cvsSplit2 = ",";
		
		try {
			br = new BufferedReader(new FileReader(path));
			
			while ((line = br.readLine()) != null) {                
				System.out.println("linea : "+line);
				String[] arrBegin = line.split(cvsSplit1);
				String[] arrEnd = arrBegin[4].split(cvsSplit2);
				String[] arrCos = arrBegin[7].split(cvsSplit2);			
				
				idItem = clo.qryIdItem(arrBegin[1].replace("\"", ""), idCompany);
				int cantOrd = (int) Math.round(Double.parseDouble(arrEnd[8].trim()));
				int orItemExi = (int) Math.round(Double.parseDouble(arrEnd[1].trim()));
				
				if(idItem == 0){
					JOptionPane.showMessageDialog(null, "Articulo: '"+arrBegin[1].replace("\"", "").trim()+"' no encontrado", "Error", JOptionPane.ERROR_MESSAGE);		
				} else {	
					if(cantOrd != 0){
						String orItemDes = clo.qryDesItem(idItem, idCompany);
						String orSubgDes = clo.qrySubItem(idItem, idCompany);
						String orItemNum = arrBegin[1].replace("\"", "").trim();
						double orOrdeCos = Double.parseDouble(arrCos[1].trim());									
						//double orOrdeCos = 0;
					
						orderVo = new OrderVo(idItem, orSubgDes, orItemNum, orItemDes, cantOrd, orItemExi, orOrdeCos);
						listReques.add(orderVo);	
					}
				}			
			}
			
			System.out.println("Terminado.... "); 
			JOptionPane.showMessageDialog(null, "Load Request successfully completed ");
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	
		return listReques;
	}
	
	public List<OrderVo> readRequest_Repo(int idCompany, String path) {
		boolean isRepo = false;
		int idItem = 0;
		
		List<OrderVo> listReques = new ArrayList<OrderVo>();
		ClosingDao clo = new ClosingDao();
		OrderVo orderVo;
		
		BufferedReader br = null;
		String line = "";
		String cvsSplit1 = ","+"\"";
		String cvsSplit2 = ",";
		
		try {
			br = new BufferedReader(new FileReader(path));
			
			while ((line = br.readLine()) != null) {  
				System.out.println("linea : "+line);
				String[] arrBegin = line.split(cvsSplit1);
				String[] arrEnd = arrBegin[4].split(cvsSplit2);
				String[] arrCos = arrBegin[7].split(cvsSplit2);
				
				//System.out.println("idItem: "+arrBegin[1]+"    -----"+arrBegin[1].replace("\"", "")+"---");
				//System.out.println("cantOrd: "+arrEnd[8]);
				//System.out.println("orItemExi: "+arrEnd[1]);
				//System.out.println("COSTO: "+arrCos[1]);
				
				idItem = clo.qryIdItem(arrBegin[1].replace("\"", ""), idCompany);
				int cantOrd = (int) Math.round(Double.parseDouble(arrEnd[8].trim()));
				int orItemExi = (int) Math.round(Double.parseDouble(arrEnd[1].trim()));
					
				if(idItem != 0)
					isRepo = clo.qrySubgRepo(idItem, idCompany);	
				
				if((idItem != 0) && (isRepo) && (cantOrd != 0)){
					String orItemDes = clo.qryDesItem(idItem, idCompany);
					String orSubgDes = clo.qrySubItem(idItem, idCompany);
					String orItemNum = arrBegin[1].replace("\"", "").trim();
					double orOrdeCos = Double.parseDouble(arrCos[1].trim());									
					//double orOrdeCos = 0;
					
					orderVo = new OrderVo(idItem, orSubgDes, orItemNum, orItemDes, cantOrd, orItemExi, orOrdeCos);
					listReques.add(orderVo);
				}
				
				if(idItem == 0 )	
					JOptionPane.showMessageDialog(null, "Articulo: '"+arrBegin[1].replace("\"", "").trim()+"' no encontrado", "Error", JOptionPane.ERROR_MESSAGE);		
				
				 System.out.println("No parte = "+arrBegin[1].replace("\"", "")+ ",  existencia = " + arrEnd[1] + ",  Surtir = " + arrEnd[8]);
			}
			
			System.out.println("Terminado.... "); 
			JOptionPane.showMessageDialog(null, "Load Request successfully completed ");
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	
		return listReques;
	}
	
	public List<OrderVo> readRequest_orderCSV(int idCompany, String path) {
		int idItem = 0;
		
		List<OrderVo> listReques = new ArrayList<OrderVo>();
		OrderVo orderVo;
		
		ClosingDao clo = new ClosingDao();
		
		List<String[]> r;
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            r = reader.readAll();
        
			int listIndex = 0;
			
			for (String[] data_txt : r) {
				System.out.println("String[" + listIndex++ + "] : " + Arrays.toString(data_txt));
				System.out.println(data_txt[3]+"\n");
				
				idItem = clo.qryIdItem(data_txt[1].trim(), idCompany);
				int cantOrd = (int) Math.round(Double.parseDouble(data_txt[12].trim()));
				int orItemExi = (int) Math.round(Double.parseDouble(data_txt[5].trim()));
				
				if(idItem == 0){
					JOptionPane.showMessageDialog(null, "Articulo: '"+data_txt[1].trim()+"' no encontrado", "Error", JOptionPane.ERROR_MESSAGE);		
				} else {	
					if(cantOrd != 0){
						String orItemDes = clo.qryDesItem(idItem, idCompany);
						String orSubgDes = clo.qrySubItem(idItem, idCompany);
						String orItemNum = data_txt[1].trim();
						double orOrdeCos = Double.parseDouble(data_txt[24].trim());									

						orderVo = new OrderVo(idItem, orSubgDes, orItemNum, orItemDes, cantOrd, orItemExi, orOrdeCos);
						listReques.add(orderVo);	
					}
				}			
			}
			
			System.out.println("Terminado.... "); 
			JOptionPane.showMessageDialog(null, "Load Request successfully completed ");
			
		} catch (CsvException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	
		return listReques;
	}
	
	public List readRequest_RepoCSV(int idCompany, String path ){
		boolean isRepo = false;
		int idItem = 0;
		
		List<OrderVo> listReques = new ArrayList<OrderVo>();
		OrderVo orderVo;
		
		ClosingDao clo = new ClosingDao();
		
		List<String[]> r;
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            r = reader.readAll();
        
			int listIndex = 0;
			for (String[] data_txt : r) {
				System.out.println("String[" + listIndex++ + "] : " + Arrays.toString(data_txt));
				System.out.println(data_txt[3]+"\n");
				
				idItem = clo.qryIdItem(data_txt[1], idCompany);
				int cantOrd = (int) Math.round(Double.parseDouble(data_txt[12].trim()));
				int orItemExi = (int) Math.round(Double.parseDouble(data_txt[5].trim()));
				
				if(idItem != 0)
					isRepo = clo.qrySubgRepo(idItem, idCompany);	
				
				if((idItem != 0) && (isRepo) && (cantOrd != 0)){
					String orItemDes = clo.qryDesItem(idItem, idCompany);
					String orSubgDes = clo.qrySubItem(idItem, idCompany);
					String orItemNum = data_txt[1].trim();
					double orOrdeCos = Double.parseDouble(data_txt[24].trim());									
					
					orderVo = new OrderVo(idItem, orSubgDes, orItemNum, orItemDes, cantOrd, orItemExi, orOrdeCos);
					listReques.add(orderVo);
				}
				
				if(idItem == 0 )	
					JOptionPane.showMessageDialog(null, "Articulo: '"+data_txt[1].trim()+"' no encontrado", "Error", JOptionPane.ERROR_MESSAGE);		
				
			}
			
			System.out.println("Terminado.... "); 
			JOptionPane.showMessageDialog(null, "Load Request successfully completed ");
			
		} catch (CsvException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return listReques;
	}
}
