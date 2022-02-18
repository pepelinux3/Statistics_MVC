/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import Model.Model;
import Model.ReadFile;
import Model.Vo.BranchVo;
import Model.Vo.GroupVo;
import Model.Vo.StockBranchVo;
import Model.Vo.SubgroupVo;
import View.ViewPrincipal;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Martin3
 */
public class Controller implements ActionListener, ItemListener  {
    private ViewPrincipal myView;
	private Model myModel;
	private Integer idCompany, modeAct; 
	private boolean modePuOrder; 
    
    public void startController(){
		myView.cb_Branch.setModel(myModel.getBranch_DB());
	    myView.bt_refresh.setIcon(new ImageIcon(getClass().getResource("/icon/actualizar 30px.png"))); 
		//--------------------------------------------------------------------------
		myView.cb_calendar.getJCalendar().setWeekOfYearVisible(false);
		myView.cb_calendar.getJCalendar().setMaxDayCharacters(1);
	
		java.util.Date fecha = new Date();
		myView.cb_calendar.setDate(fecha);
		
		JTextFieldDateEditor editor = (JTextFieldDateEditor) myView.cb_calendar.getDateEditor();
		editor.setEditable(false);
		//--------------------------------------------------------------------------
		settingTableItem();
		addEventChecks();
		addEventSelRow();
		addEventKeyb();
		
		filterCBGroup();
		filterCBSubgroup();
		
		this.myView.bt_Cancel.addActionListener(this);
		this.myView.bt_Save.addActionListener(this);
		this.myView.bt_refresh.addActionListener(this);
		
		this.myView.mi_items.addActionListener(this);
		this.myView.mi_new_items.addActionListener(this);
		this.myView.mi_stock.addActionListener(this);
		this.myView.mi_sales.addActionListener(this);
		this.myView.mi_purchase.addActionListener(this);
		this.myView.mi_order.addActionListener(this);
		this.myView.mi_repo.addActionListener(this);
		this.myView.mi_update.addActionListener(this);
		this.myView.mi_reqOrder.addActionListener(this);
		this.myView.mi_reqRepo.addActionListener(this);
			
		this.myView.cb_Branch.addItemListener(this);
		this.myView.cb_Group.addItemListener(this);
		
        this.myView.setVisible(true);	
		//this.myView.me_closing.setEnabled(false);
		
		startChecks();
		startStatistics();
    }
      
	public void setModel(Model myModel) {
        this.myModel = myModel;
    } 
	
    public void setView(ViewPrincipal view) {
        this.myView = view;
    }
	
	private void settingTableItem(){
		myView.tab_Items.getColumnModel().getColumn(0).setPreferredWidth(10);
		myView.tab_Items.getColumnModel().getColumn(1).setPreferredWidth(10);
		myView.tab_Items.getColumnModel().getColumn(2).setPreferredWidth(10);
		myView.tab_Items.getColumnModel().getColumn(3).setPreferredWidth(10);
        myView.tab_Items.getColumnModel().getColumn(4).setPreferredWidth(100); 
        myView.tab_Items.getColumnModel().getColumn(5).setPreferredWidth(220);
		myView.tab_Items.getColumnModel().getColumn(6).setPreferredWidth(10);
        myView.tab_Items.getColumnModel().getColumn(7).setPreferredWidth(10);
		myView.tab_Items.getColumnModel().getColumn(8).setPreferredWidth(10);		
		
		DefaultTableCellRenderer lrig = new DefaultTableCellRenderer();
		lrig.setHorizontalAlignment(SwingConstants.RIGHT);
		
		myView.tab_Items.getColumnModel().getColumn(6).setCellRenderer(lrig);
		myView.tab_Items.getColumnModel().getColumn(7).setCellRenderer(lrig);
		myView.tab_Items.getColumnModel().getColumn(8).setCellRenderer(lrig);
				
		//  to do invisible the id Item Column
		myView.tab_Items.getColumnModel().getColumn(2).setMaxWidth(0);
		myView.tab_Items.getColumnModel().getColumn(2).setMinWidth(0);
		myView.tab_Items.getColumnModel().getColumn(2).setPreferredWidth(0);
		myView.tab_Items.getColumnModel().getColumn(2).setResizable(false);
		
		myView.tab_Items.getColumnModel().getColumn(3).setMaxWidth(0);
		myView.tab_Items.getColumnModel().getColumn(3).setMinWidth(0);
		myView.tab_Items.getColumnModel().getColumn(3).setPreferredWidth(0);
		myView.tab_Items.getColumnModel().getColumn(3).setResizable(false);
		
	//	myView.tab_Items.getColumnModel().getColumn(9).setMaxWidth(0);
	//	myView.tab_Items.getColumnModel().getColumn(9).setMinWidth(0);
	//	myView.tab_Items.getColumnModel().getColumn(9).setPreferredWidth(0);
	//	myView.tab_Items.getColumnModel().getColumn(9).setResizable(false);
	}
	
	private void settingTableStat(){
		DefaultTableCellRenderer lcen = new DefaultTableCellRenderer();
		lcen.setHorizontalAlignment(SwingConstants.CENTER);
		
		myView.tab_Stati.getColumnModel().getColumn(1).setCellRenderer(lcen);
		myView.tab_Stati.getColumnModel().getColumn(2).setCellRenderer(lcen);
		myView.tab_Stati.getColumnModel().getColumn(3).setCellRenderer(lcen);
		myView.tab_Stati.getColumnModel().getColumn(4).setCellRenderer(lcen);
		myView.tab_Stati.getColumnModel().getColumn(5).setCellRenderer(lcen);
		myView.tab_Stati.getColumnModel().getColumn(6).setCellRenderer(lcen);
	}
	
	private void addEventSelRow(){
		ListSelectionModel cellSelectionModel = myView.tab_Items.getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		SelecctRow rSel = new SelecctRow();
		cellSelectionModel.addListSelectionListener(rSel);
	}
	
	private void addEventKeyb(){
		KeyboardText mayus = new KeyboardText();
        
        myView.tf_NoPart.addKeyListener(mayus);
        myView.tf_Description.addKeyListener(mayus);
		
		KeyboardTable tCell = new KeyboardTable();
		myView.tab_Items.addKeyListener(tCell);
	}
	
	private void addEventChecks(){			
        CheckEvents evt = new CheckEvents();
		
        this.myView.chk_Branch.addItemListener(evt);
		this.myView.chk_Group.addItemListener(evt);
		this.myView.chk_SubGroup.addItemListener(evt);
		
		this.myView.chk_Description.addItemListener(evt);
		this.myView.chk_NoPart.addItemListener(evt);
	}
	
	private void startStatistics(){
		Object[][] datStat = { };
		myView.tab_Stati.setModel(myModel.getModelTabStat_Empy(datStat));
	}
	
	public void LoadTable(DefaultTableModel model){        
        myView.tab_Items.setModel(model);
        settingTableItem();
    } 
	
	private void startChecks(){
		myView.chk_Description.setSelected(true);
		myView.chk_NoPart.setSelected(true);
		myView.chk_Group.setSelected(true);
		myView.chk_SubGroup.setSelected(true);
		myView.chk_Branch.setSelected(true);
		
		myView.chk_Description.setSelected(false);
		myView.chk_NoPart.setSelected(false);
		myView.chk_Group.setSelected(false);
		myView.chk_SubGroup.setSelected(false);
		myView.chk_Branch.setSelected(false);
		
		myView.chk_NoPart.setSelected(true);
		myView.chk_NoComplete.setSelected(true);
	}
	
	private void filterCBGroup(){		
		BranchVo object = (BranchVo) myView.cb_Branch.getSelectedItem();
        idCompany = ((BranchVo)object).getIdCompany();
		
		this.myView.cb_Group.setModel(myModel.getGroup_DB(idCompany));
	}
	
	private void filterCBSubgroup(){		
		GroupVo object = (GroupVo) myView.cb_Group.getSelectedItem();
        Integer idGroup = ((GroupVo)object).getIdGroup();
      
		this.myView.cb_SubGroup.setModel(myModel.getSubg_DB(idGroup));
	}

	private Boolean validateTogether(){
        Boolean vali = true;
        
        if(!myView.chk_NoPart.isSelected()  &&  !myView.chk_Description.isSelected()  &&  !myView.chk_Branch.isSelected()  &&  !myView.chk_Group.isSelected() &&  !myView.chk_SubGroup.isSelected()){  // FALTAN LOS OTROS CHECKS
            JOptionPane.showMessageDialog(null, "Error, no hay ningun campo activado");
            vali = false;
        }  

        if(myView.chk_NoPart.isSelected()  &&  myView.tf_NoPart.getText().isEmpty() ){  // FALTAN LOS JTextField
            JOptionPane.showMessageDialog(null, "Error, todos los campos estan vacios");
            vali = false;    
        }
		
		if(myView.chk_Description.isSelected()  &&  myView.tf_Description.getText().isEmpty()){  // FALTAN LOS JTextField
            JOptionPane.showMessageDialog(null, "Error, todos los campos estan vacios");
            vali = false;    
        }
		
        return vali;
    }
	
	private boolean validateSeparate (StockBranchVo myStock){
        Boolean vali = true;                           
        
        if(myView.chk_NoPart.isSelected()) 
            if(!myView.tf_NoPart.getText().isEmpty())
                myStock.setNoItem(myView.tf_NoPart.getText());                
            else 
                vali = false;
        
        if(myView.chk_Description.isSelected()) 
            if(!myView.tf_Description.getText().isEmpty())
                myStock.setNaItem(myView.tf_Description.getText());                 
            else 
                vali = false;
                     
        if(vali == false){
            JOptionPane.showMessageDialog(null, "Error, hay un campo seleccionado vacio");
        }
		
        return vali;
    } 
	
	private void searchItems (){
		boolean comp = false;
		boolean stoc = false;
		int gru = 0; 
		int sub = 0; 
		int bra = 0;
			
		if(myView.chk_NoComplete.isSelected())
			comp = true;
		
		if(myView.chk_OnlyStock.isSelected())
			stoc = true;
				
			
		if(myView.chk_Branch.isSelected()){
			BranchVo obj1 = (BranchVo) myView.cb_Branch.getSelectedItem();
			bra = ((BranchVo)obj1).getIdBranch();
		}			
			
		if(myView.chk_Group.isSelected()){
			GroupVo obj2 = (GroupVo) myView.cb_Group.getSelectedItem();
			gru = ((GroupVo)obj2).getIdGroup();
		}	
			
		if(myView.chk_SubGroup.isSelected()){
			SubgroupVo obj3 = (SubgroupVo) myView.cb_SubGroup.getSelectedItem();
			sub = ((SubgroupVo)obj3).getIdSubg();
		}	
			
		StockBranchVo stob = new StockBranchVo();
			
		if(validateTogether() && validateSeparate(stob)){
			myModel.queryTableModel(stob, comp, stoc, bra, gru, sub); 				
		}
		
		modePuOrder = false;
	}
	
	public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == myView.cb_Branch) {  
			filterCBGroup();
			filterCBSubgroup();
        }
		
		if (e.getSource() == myView.cb_Group) {  
			filterCBSubgroup();
        }
    }
	
	@Override
    public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == myView.bt_Cancel){
            System.out.println("Exit Button.... ");
            System.exit(0);
        }   
		    
		if(e.getSource() == myView.bt_refresh){
            System.out.println("refrescar ....");
			searchItems();
        }
		
		if(e.getSource() == myView.bt_Save){
            ControllerFile ctrlFile = new ControllerFile();
			
            ctrlFile.setView(myView);
            ctrlFile.setModel(myModel);
			
			if((modeAct == 8) || (modeAct == 9))
				ctrlFile.save_Request();
			else
				ctrlFile.save_Order();
        }
		
		if(e.getSource() == myView.mi_new_items){
			ControllerFile ctrlFile = new ControllerFile();
			
            ctrlFile.setView(myView);
            ctrlFile.setModel(myModel);
			
			modeAct = 0;
			ctrlFile.openFile(modeAct, idCompany, 0, "xls");
        }
		
		if(e.getSource() == myView.mi_items){		
			int reply = JOptionPane.showConfirmDialog(null, "Esta seguro que desea hacer el cierre?", "Cracion del cierre", JOptionPane.YES_NO_OPTION);
			
			if (reply == JOptionPane.YES_OPTION) {
				ControllerFile ctrlFile = new ControllerFile();

				ctrlFile.setView(myView);
				ctrlFile.setModel(myModel);

				//modeAct = 2;
				//ctrlFile.openFile(modeAct, idCompany, 0, "xls");

				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Date date = myView.cb_calendar.getDate();

				long d = date.getTime();
				java.sql.Date sDate = new java.sql.Date(d);
				String sDateOne = sDate.toString().substring(0,7)+"-01";

				myModel.addClosing_NewMonth(sDateOne);
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(null, "Termina de generar el cierre..");
			}
        }
		
		if(e.getSource() == myView.mi_stock){
			ControllerFile ctrlFile = new ControllerFile();
			
            ctrlFile.setView(myView);
            ctrlFile.setModel(myModel);
			
			modeAct = 1;
			ctrlFile.openFile(modeAct, idCompany, 0, "txt");
        }
		
		if(e.getSource() == myView.mi_sales){
			ControllerFile ctrlFile = new ControllerFile();
			
            ctrlFile.setView(myView);
            ctrlFile.setModel(myModel);
			
			modeAct = 3;
			//ctrlFile.openFile(modeAct, idCompany, 0, "txt"); // cambia de xls a txt
			ctrlFile.openFile(modeAct, idCompany, 0, "xls");
        }
		
		if(e.getSource() == myView.mi_purchase){
			System.out.println("prueba.. purchase..");
		}
		
		if(e.getSource() == myView.mi_order){
			if(myView.chk_Branch.isSelected()){ 
				ControllerFile ctrlFile = new ControllerFile();
			
				ctrlFile.setView(myView);
				ctrlFile.setModel(myModel);
			
				BranchVo object = (BranchVo) myView.cb_Branch.getSelectedItem();
				int idBranch = ((BranchVo)object).getIdBranch();
			
				myModel.setControllerFile(ctrlFile);
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
				modeAct = 6;
				ctrlFile.openFile(modeAct, idCompany, idBranch, "txt");
				settingTableItem();
				modePuOrder = true;
				
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else{
				JOptionPane.showMessageDialog(null, "Debes seleccionar una sucursal", "Error", JOptionPane.WARNING_MESSAGE);
			}
        }
		
		if(e.getSource() == myView.mi_repo){
			if(myView.chk_Branch.isSelected()){ 
				ControllerFile ctrlFile = new ControllerFile();
			
				ctrlFile.setView(myView);
				ctrlFile.setModel(myModel);
			
				BranchVo object = (BranchVo) myView.cb_Branch.getSelectedItem();
				int idBranch = ((BranchVo)object).getIdBranch();
			
				myModel.setControllerFile(ctrlFile);
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				modeAct = 7;
				ctrlFile.openFile(modeAct, idCompany, idBranch, "txt");
				settingTableItem();
				modePuOrder = true;
				
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else{
				JOptionPane.showMessageDialog(null, "Debes seleccionar una sucursal", "Error", JOptionPane.WARNING_MESSAGE);
			}
        }
		
		if(e.getSource() == myView.mi_update){
			JOptionPane.showMessageDialog(null, "Articulos:     2021-08-01 \nExistencia:  2021-08-01 \nVentas:        2021-08-01 \nCompras:    2021-08-01 ", "Fecha de Actualizacion", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource() == myView.mi_reqOrder){
			//JOptionPane.showMessageDialog(null, "Prueba Mensaje.. ", "Titulo", JOptionPane.INFORMATION_MESSAGE);
			if(myView.chk_Branch.isSelected()){ 
				ControllerFile ctrlFile = new ControllerFile();
			
				ctrlFile.setView(myView);
				ctrlFile.setModel(myModel);
			
				BranchVo object = (BranchVo) myView.cb_Branch.getSelectedItem();
				int idBranch = ((BranchVo)object).getIdBranch();
			
				myModel.setControllerFile(ctrlFile);
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				modeAct = 8;
				ctrlFile.openFile(modeAct, idCompany, idBranch, "txt");
				settingTableItem();			
				
				modePuOrder = true;
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else{
				JOptionPane.showMessageDialog(null, "Debes seleccionar una sucursal", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		if(e.getSource() == myView.mi_reqRepo){
			//JOptionPane.showMessageDialog(null, "Prueba Mensaje.. ", "Titulo", JOptionPane.INFORMATION_MESSAGE);
			if(myView.chk_Branch.isSelected()){ 
				ControllerFile ctrlFile = new ControllerFile();
			
				ctrlFile.setView(myView);
				ctrlFile.setModel(myModel);
			
				BranchVo object = (BranchVo) myView.cb_Branch.getSelectedItem();
				int idBranch = ((BranchVo)object).getIdBranch();
			
				myModel.setControllerFile(ctrlFile);
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				modeAct = 9;
				ctrlFile.openFile(modeAct, idCompany, idBranch, "txt");
				settingTableItem();			
				
				modePuOrder = true;
				myView.scItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else{
				JOptionPane.showMessageDialog(null, "Debes seleccionar una sucursal", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public class CheckEvents implements ItemListener{
    
        @Override
        public void itemStateChanged(ItemEvent e) { 

            if(e.getSource() == myView.chk_Branch)  {
                if(myView.chk_Branch.isSelected())
                    myView.cb_Branch.setEnabled(true);           
                else
                    myView.cb_Branch.setEnabled(false);
            }
			
			if(e.getSource() == myView.chk_Group)  {
                if(myView.chk_Group.isSelected())
                    myView.cb_Group.setEnabled(true);           
                else
                    myView.cb_Group.setEnabled(false);
            }
			
			if(e.getSource() == myView.chk_SubGroup)  {
                if(myView.chk_SubGroup.isSelected())
                    myView.cb_SubGroup.setEnabled(true);           
                else
                    myView.cb_SubGroup.setEnabled(false);
            }
			// --------------------------------------------------------------
			if(e.getSource() == myView.chk_Description)  {
                if(myView.chk_Description.isSelected()){
                    myView.tf_Description.setEnabled(true);
                    myView.tf_Description.requestFocus();
                }
                else
                    myView.tf_Description.setEnabled(false);
            }
			
			if(e.getSource() == myView.chk_NoPart)  {
                if(myView.chk_NoPart.isSelected()){
                    myView.tf_NoPart.setEnabled(true);
                    myView.tf_NoPart.requestFocus();
					System.out.println("seleccion  no parte... ");
                }
                else
                    myView.tf_NoPart.setEnabled(false);
            }
        } 
    }
	
	public class SelecctRow implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if(!e.getValueIsAdjusting()){
				String selIdItem = null;
				String selIdBran = null;

				int[] selectedRow = myView.tab_Items.getSelectedRows();
				
				for (int i=0; i<selectedRow.length; i++) {
					selIdItem = (String) myView.tab_Items.getValueAt(selectedRow[i], 2);
					selIdBran = (String) myView.tab_Items.getValueAt(selectedRow[i], 3);
				}				  
				
				if(selIdItem != null){
					int idItem = Integer.parseInt(selIdItem);
					int idBran = Integer.parseInt(selIdBran);

					myView.tab_Stati.setModel(myModel.getModelTabStat_Query(idItem, idBran));
					settingTableStat();
					System.out.println("Selected: " + selIdItem);
				}				
			}	
		}
	}
	
	public class KeyboardText implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
		char c =e.getKeyChar();
            
            if(Character.isLowerCase(c)){
                String cad = (""+c).toUpperCase();
                c=cad.charAt(0);
                e.setKeyChar(c);
            }
		}

        @Override
        public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				searchItems();
            }
		}

        @Override
        public void keyReleased(KeyEvent e) {}      
    }
	
	public class KeyboardTable implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {	
			char caracter = e.getKeyChar();
            
			if (((caracter < '0') || (caracter > '9')) && (caracter != '\b')) {
				e.consume();
			}
		}

        @Override
        public void keyPressed(KeyEvent e) {
			boolean bandera = true;		
			
			if(myView.tab_Items.isColumnSelected(8) && modePuOrder == true){
				if(bandera == true) {
					if((e.getKeyCode() != KeyEvent.VK_DOWN) && (e.getKeyCode() != KeyEvent.VK_UP) && (e.getKeyCode() != KeyEvent.VK_LEFT) && (e.getKeyCode() != KeyEvent.VK_RIGHT) ){
						myView.tab_Items.setValueAt("", myView.tab_Items.getSelectedRow(), 8);
					}	

					bandera = false;
				}	
			}		
		}

        @Override
        public void keyReleased(KeyEvent e) {
		
		}      
    }
}
