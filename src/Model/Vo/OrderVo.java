/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Vo;

/**
 *
 * @author Martin3
 */
public class OrderVo {
	int idItem;
	//int idBranch;
	String naSubg;
	String noItem;
	String deItem;
	int cant;
	int requ;
	double cost;

	//public OrderVo(int idItem, int idBranch, String naSubg, String noItem, String deItem, int cant, double cost) {
	public OrderVo(int idItem, String naSubg, String noItem, String deItem, int cant, int requ, double cost) {
		this.idItem = idItem;
		//this.idBranch = idBranch;
		this.naSubg = naSubg;
		this.noItem = noItem;
		this.deItem = deItem;
		this.cant = cant;
		this.requ = requ;
		this.cost = cost;
	}

	public int getIdItem() {
		return idItem;
	}
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}
	/*
	public int getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(int idBranch) {
		this.idBranch = idBranch;
	}
	*/
	public String getNaSubg() {
		return naSubg;
	}
	public void setNaSubg(String naSubg) {
		this.naSubg = naSubg;
	}

	public String getNoItem() {
		return noItem;
	}
	public void setNoItem(String noItem) {
		this.noItem = noItem;
	}

	public String getDeItem() {
		return deItem;
	}
	public void setDeItem(String deItem) {
		this.deItem = deItem;
	}

	public int getCant() {
		return cant;
	}
	public void setCant(int cant) {
		this.cant = cant;
	}

	public int getRequ() {
		return requ;
	}
	public void setRequ(int requ) {
		this.requ = requ;
	}
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	
}
