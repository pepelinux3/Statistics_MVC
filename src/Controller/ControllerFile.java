/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Model;
import View.ViewPrincipal;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Martin3
 */
public class ControllerFile {
	private Model fi_model;
    private ViewPrincipal fi_view;
	
	public void setView(ViewPrincipal fi_view) {
		this.fi_view = fi_view;
    }
	
    public void setModel(Model fi_model) {
        this.fi_model = fi_model;
    }
	
	public void openFile(int itemMenu, int idCompany, int idBranch, String typeFile){
		String sPath;
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("."+typeFile, typeFile);    
        fileChooser.setFileFilter(imgFilter);

        int result = fileChooser.showOpenDialog(this.fi_view);
		
        if (result != JFileChooser.CANCEL_OPTION){
            File fileName = fileChooser.getSelectedFile();

            if ((fileName == null) || (fileName.getName().equals(""))){
				sPath = "";
            } else {
                sPath = fileName.getAbsolutePath();
				chooseItemMenu(itemMenu, idCompany, idBranch, sPath);
            }
			
        } else {
			sPath = "";
		}
	}
	
	public void save_Order(){
		JFileChooser file=new JFileChooser();
		int selDialo = file.showSaveDialog(null);
		
		if (selDialo == JFileChooser.APPROVE_OPTION){
			File guarda = file.getSelectedFile();
			
			try{	
				if(guarda !=null){
					BufferedWriter br = new BufferedWriter(new FileWriter(guarda+".txt"));

					for (int i=0; i<fi_view.tab_Items.getRowCount(); i++) {
						String txtCan = fi_view.tab_Items.getValueAt(i, 8).toString()+".000";
						String txtCos = (fi_view.tab_Items.getValueAt(i, 7).toString()+"0").replace(",", "");

						br.write(String.format("%-40s", fi_view.tab_Items.getValueAt(i, 4).toString()));
						br.write(String.format("%-7s", "PIEZA"));
						br.write(String.format("%-12s", "0.000"));
						br.write(String.format("%-49s", "0.000"));
						br.write(String.format("%-8s", "1"));
						br.write(String.format("%10s", txtCan));
						br.write(String.format("%22s", txtCos));  

						br.newLine();
					}

					br.close();	
					
					JOptionPane.showMessageDialog(null,"El archivo se a guardado Exitosamente", "Información",JOptionPane.INFORMATION_MESSAGE);
								
				} else{
					JOptionPane.showMessageDialog(null,"El archivo no se ha guardado", "Advertencia",JOptionPane.WARNING_MESSAGE);
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null, "Error al guardar el archivo!","Error!", JOptionPane.ERROR_MESSAGE);
			}
		} 
	}
	
	public void save_Request(){
		JFileChooser file=new JFileChooser();
		int selDialo = file.showSaveDialog(null);
		
		if (selDialo == JFileChooser.APPROVE_OPTION){
			File guarda = file.getSelectedFile();
			
			try{	
				if(guarda !=null){
					BufferedWriter br = new BufferedWriter(new FileWriter(guarda+".txt"));

					for (int i=0; i<fi_view.tab_Items.getRowCount(); i++) {
						String txtCan = fi_view.tab_Items.getValueAt(i, 8).toString()+".000";
						String txtCos = (fi_view.tab_Items.getValueAt(i, 9).toString()+"0").replace(",", "");

						if(txtCos.equals("0.000"))
							txtCos = "0.010";
						
						if(!txtCan.equals("0.000")){
							br.write(String.format("%-40s", fi_view.tab_Items.getValueAt(i, 4).toString()));
							br.write(String.format("%-7s", "PIEZA"));
							br.write(String.format("%-12s", "0.000"));
							br.write(String.format("%-49s", "0.000"));
							br.write(String.format("%-8s", "1"));
							br.write(String.format("%10s", txtCan));
							br.write(String.format("%22s", txtCos));
							br.newLine();
						}
					}

					br.close();	
					JOptionPane.showMessageDialog(null,"El archivo se a guardado Exitosamente", "Información",JOptionPane.INFORMATION_MESSAGE);
				} else{
					JOptionPane.showMessageDialog(null,"El archivo no se ha guardado", "Advertencia",JOptionPane.WARNING_MESSAGE);
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null, "Error al guardar el archivo!","Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void LoadTableModel(DefaultTableModel model){
		fi_view.tab_Items.setModel(model);
	}
	
	private void chooseItemMenu(int itemMenu, int idCompany, int idBranch, String sPath){
		Date date = fi_view.cb_calendar.getDate();
		
		long d = date.getTime();
		java.sql.Date sDate = new java.sql.Date(d);
		String sDateOne = sDate.toString().substring(0,7)+"-01";
		
		switch (itemMenu) {
			case 0:
				fi_model.setFile_xls_NewItems(idCompany, sPath, sDateOne);					
				break;	
				
            case 1:
				fi_model.setFile_txt_Stock(idCompany, sPath, sDateOne);					
				break;

            case 2: 
				fi_model.setFile_xls_items(idCompany, sPath, sDateOne);  //El cierre no esta activo.. razon: INSERT-SELECT
				break;

            case 3:
				fi_model.setFile_xls_sales(idCompany, sPath, sDateOne);
				//fi_model.setFile_txt_sales(idCompany, sPath, sDateOne);
				System.out.println ("Opcion para ventas"); 
				break;
				
			case 6:
				fi_model.setFile_txt_Order(idCompany, idBranch, sPath);
				System.out.println ("Opcion para pedidos"); 
				break;
				
			case 7:
				fi_model.setFile_txt_Repo(idCompany, idBranch, sPath);
				System.out.println ("Opcion para reposicion"); 
				break;
				
			case 8:
				System.out.println ("Opcion para punto de reorden Order"); 
				
				fi_model.setFile_txt_reqOrder(idCompany, idBranch, sPath);
				
				fi_view.tab_Items.getColumnModel().getColumn(9).setMaxWidth(0);
				fi_view.tab_Items.getColumnModel().getColumn(9).setMinWidth(0);
				fi_view.tab_Items.getColumnModel().getColumn(9).setPreferredWidth(0);
				fi_view.tab_Items.getColumnModel().getColumn(9).setResizable(false);		
				break;
				
			case 9:
				System.out.println ("Opcion para punto de reorden repo");
				fi_model.setFile_txt_reqRepo(idCompany, idBranch, sPath);
				
				fi_view.tab_Items.getColumnModel().getColumn(9).setMaxWidth(0);
				fi_view.tab_Items.getColumnModel().getColumn(9).setMinWidth(0);
				fi_view.tab_Items.getColumnModel().getColumn(9).setPreferredWidth(0);
				fi_view.tab_Items.getColumnModel().getColumn(9).setResizable(false);		
				break;

            default: 
				System.out.println ("Default...."); 
				break;
		} 
		//} else{
		//	JOptionPane.showMessageDialog(null, "Field date is Empty!",
		//	"Error Date", JOptionPane.WARNING_MESSAGE);
		//	System.out.println ("No hay fecha..."); 
		//}
	}
}
