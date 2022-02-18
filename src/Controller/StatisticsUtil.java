/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import DataBase.ConnectDB;
import Model.Model;
import View.ViewPrincipal;
import javax.swing.UIManager;
/**
 *
 * @author Martin3
 */
public class StatisticsUtil {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		
		ConnectDB con = new ConnectDB();       
        
        if(con.getConnection() != null){ 
			
			try{
               //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               //  UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
               //  UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
               //  UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
			   //  UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");   // Black
			     UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");       // Black 2
			   //  UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
			   //  UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			   //  UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");    //  amarillo			   
            }
            catch (Exception e) { 
            }
			
			Controller myController;
			
			Model myModel;
			ViewPrincipal myView; 

			myController = new Controller();
			
			myModel = new Model();
			myView = new ViewPrincipal();

			myController.setModel(myModel);
			myController.setView(myView);

			myModel.setController(myController);
				
			myController.startController();
		}
	}
}
