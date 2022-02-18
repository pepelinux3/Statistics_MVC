/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Martin3
 */
public class ViewPrincipal extends JFrame{
    
    private JPanel PBase;
    
    private JPanel PHeader;
    private JPanel PBody;
    private JPanel PFoot;
    
	public JMenu me_closing;
	public JMenu me_movs;
	public JMenu me_info;

	public JMenuItem mi_new_items;
	public JMenuItem mi_items;
	public JMenuItem mi_stock;
	public JMenuItem mi_sales;
	public JMenuItem mi_purchase;
	public JMenuItem mi_order;
	public JMenuItem mi_repo;
	public JMenuItem mi_update;
	public JMenuItem mi_reqRepo;
	public JMenuItem mi_reqOrder;
	
    public JButton bt_refresh;
    public JButton bt_Cancel;
	public JButton bt_Save;
    
	public JCheckBox chk_NoPart;
    public JCheckBox chk_Description;
	public JCheckBox chk_NoComplete;
	public JCheckBox chk_Group;
    public JCheckBox chk_SubGroup;
	public JCheckBox chk_Branch;
	public JCheckBox chk_OnlyStock;
	
	//public JTextField tf_Date; 
    public JTextField tf_NoPart; 
    public JTextField tf_Description; 
	
	public JComboBox cb_Group;
	public JComboBox cb_SubGroup;
	public JComboBox cb_Branch;
	
	public JDateChooser cb_calendar;
	
	public JScrollPane scItem;
	public JTable tab_Items;
	public JTable tab_Stati;
    
    public ViewPrincipal (){
        settingFrame();
        createMenu();
		
        panelHeader();
		panelStatistics();
        panelBody();
        panelFooter();
        
        panelBase();
    }
    
    private void settingFrame(){
       setTitle("Estadisticas - SAI ERP");
       setSize(800, 532);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	private void createMenu(){
		JMenuBar mb =new JMenuBar();
		
        me_closing = new JMenu("Importar Cierres");
		me_movs = new JMenu("Importar Movimientos");
		me_info = new JMenu("Informacion");
  
		mi_items = new JMenuItem("Cargar Articulos  ");
		mi_new_items = new JMenuItem("Nuevos Articulos ");
	    mi_stock = new JMenuItem("Inventario");  
        mi_sales = new JMenuItem("Ventas");
		mi_purchase = new JMenuItem("Compras ");
		mi_order = new JMenuItem("Orden de Compra* ");   // txt
		mi_repo = new JMenuItem("Orden Reposicion* ");   // txt
		mi_update = new JMenuItem("Actualizacion");
		mi_reqRepo = new JMenuItem("Orden Reposicion*");    //  cvs
		mi_reqOrder = new JMenuItem("Orden de Compra*");    //  cvs
		
		me_closing.add(mi_new_items);
		me_closing.add(mi_items);
		
		me_movs.add(mi_stock);
		me_movs.add(mi_sales);
		me_movs.add(mi_purchase);
		//me_movs.add(mi_order);
		//me_movs.add(mi_repo);
		me_movs.add(mi_reqRepo);
		me_movs.add(mi_reqOrder);
		
		me_info.add(mi_update);
		
		mb.add(me_closing);
		mb.add(me_movs);
		mb.add(me_info);
		
		setJMenuBar(mb);
    }
	
    private void panelBase(){
        PBase = new JPanel(new BorderLayout());
        PBase.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        
        getContentPane().add(PBase);      
        
        PBase.add(PHeader, BorderLayout.NORTH);
        PBase.add(PBody, BorderLayout.CENTER);
        PBase.add(PFoot, BorderLayout.SOUTH); 
    }
     
    private void panelHeader(){
        FlowLayout space = new FlowLayout(FlowLayout.LEFT);     
        space.setVgap(55);			
		space.setHgap(0);		
       		
        PHeader = new JPanel(new BorderLayout());
        PHeader.setBorder(new EtchedBorder(EtchedBorder.RAISED));      
        
		JPanel paLeft = new JPanel(space);
		JPanel paRight = new JPanel(new BorderLayout());
		
		paRight.setLayout(null);
		
		// ************************   CheckBoxs   *****************************		
        chk_Description = new JCheckBox (" Descripcion:");
	    chk_Description.setBounds(5, 30, 105, 20);
		
		chk_NoPart = new JCheckBox (" No. de Parte:");
		chk_NoPart.setBounds(5, 55, 105, 20);
		
		chk_NoComplete = new JCheckBox ("Clave Completa");
		chk_NoComplete.setBounds(106, 80, 120, 20);	
		// -----------------------------------------------------
		chk_Group = new JCheckBox ("       Grupo:");
		chk_Group.setBounds(350, 5, 90, 20);
		
		chk_SubGroup = new JCheckBox ("SubGrupo:");
		chk_SubGroup.setBounds(350, 30, 90, 20);
		
		chk_Branch = new JCheckBox ("  Sucursal:");
		chk_Branch.setBounds(350, 55, 90, 20);
			
		chk_OnlyStock = new JCheckBox (" con Existencia");
		chk_OnlyStock.setBounds(436, 80, 180, 20);
		
		// **********************   Labels    *********************************
		JLabel lb_path = new JLabel("       Fecha:");
		lb_path.setForeground(Color.GRAY);
		lb_path.setBounds(46, 5, 70, 20);
		
		//lb_iteClos = new JLabel("Item Closing");
		//lb_iteClos.setForeground(Color.BLUE);
		//lb_iteClos.setBounds(300, 80, 150, 20);
		
		// **********************   TextField    ******************************
		//tf_Date = new JTextField();
		//tf_Date.setBounds(110, 5, 140, 20);
		
		cb_calendar = new JDateChooser();
		cb_calendar.setBounds(110, 5, 140, 20);
			
		tf_Description = new JTextField();
		tf_Description.setBounds(110, 30, 140, 20);
		
		tf_NoPart = new JTextField();
		tf_NoPart.setBounds(110, 55, 140, 20);
					
		// ********************************************************************
		cb_Group = new JComboBox();
		cb_Group.setBounds(440, 5, 160, 20);
		
		cb_SubGroup = new JComboBox();
		cb_SubGroup.setBounds(440, 30, 160, 20);
		
		cb_Branch = new JComboBox();
		cb_Branch.setBounds(440, 55, 160, 20);

		// ********************************************************************
		bt_refresh = new JButton ();
        bt_refresh.setBounds(620, 5, 40, 40);
		
		//*********************************************************************
		paRight.add(chk_NoPart, BorderLayout.CENTER);
		paRight.add(chk_Description, BorderLayout.CENTER);
		paRight.add(chk_NoComplete, BorderLayout.CENTER);
		
		paRight.add(chk_Group, BorderLayout.CENTER);
		paRight.add(chk_SubGroup, BorderLayout.CENTER);
		paRight.add(chk_Branch, BorderLayout.CENTER);
		paRight.add(chk_OnlyStock, BorderLayout.CENTER);
		
		paRight.add(lb_path, BorderLayout.CENTER);
		//paRight.add(lb_iteClos, BorderLayout.CENTER);
		
		//paRight.add(tf_Date, BorderLayout.CENTER);
		paRight.add(cb_calendar, BorderLayout.CENTER);
		paRight.add(tf_NoPart, BorderLayout.CENTER);
		paRight.add(tf_Description, BorderLayout.CENTER);
		
		paRight.add(cb_Group, BorderLayout.CENTER);
		paRight.add(cb_SubGroup, BorderLayout.CENTER);
		paRight.add(cb_Branch, BorderLayout.CENTER);
		
		paRight.add(bt_refresh, BorderLayout.CENTER);
		  
        PHeader.add(paLeft, BorderLayout.WEST);
		PHeader.add(paRight, BorderLayout.CENTER);
    }
        
	private void panelStatistics(){
		PBody = new JPanel(new BorderLayout());
        PBody.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		String[] colItem = {"Tipo", "Mes 1", "Mes 2", "Mes 3", "Mes 4", "Mes 5", "Mes 6"};
        Object[][] datItem = { };
                             
        tab_Stati = new JTable(datItem, colItem);
		tab_Stati.setPreferredScrollableViewportSize(new Dimension(500, 60));
		
        JScrollPane scTable = new JScrollPane();
        scTable.getViewport().add(tab_Stati);
                      
		PBody.add(scTable, BorderLayout.NORTH);	
	}
	
    private void panelBody(){		
		String[] colItem = {"Sucursal", "Subgrupo", "Id Articulo ", "Id Suc", "No. de Parte", "Descripcion", "Cantidad", "Costo", "Precio"};
        Object[][] datItem = { };
                             
        tab_Items = new JTable(datItem, colItem);
		
        scItem = new JScrollPane();
        scItem.getViewport().add(tab_Items);
                      
        PBody.add(scItem, BorderLayout.CENTER);
    }
    
    private void panelFooter(){ 
        PFoot = new JPanel(new BorderLayout());
        PFoot.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        
        FlowLayout space = new FlowLayout(FlowLayout.CENTER);
        space.setVgap(6);
        PFoot.setLayout(space);
        
        bt_Cancel = new JButton("Cancelar");
        bt_Cancel.setPreferredSize(new Dimension(95,30));
		
		bt_Save = new JButton("Guardar");
        bt_Save.setPreferredSize(new Dimension(95,30));
        
        PFoot.add(bt_Cancel, BorderLayout.WEST);
		PFoot.add(bt_Save, BorderLayout.WEST); 
    }
}
