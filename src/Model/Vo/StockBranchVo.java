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
public class StockBranchVo {
	int idBrach;
	int idItem;
	int idSubg;
	int idCompa;
	int exis;
	String naBranch;
	String naSubg;
	String noItem;
	String naItem;
	double cost;
/*
	public StockBranchVo(int idBrach, int idItem, int idSubg, int idCompa, String naBranch, String noItem, String naItem, double cost) {
		this.idBrach = idBrach;
		this.idItem = idItem;
		this.idSubg = idSubg;
		this.idCompa = idCompa;
		this.naBranch = naBranch;
		this.noItem = noItem;
		this.naItem = naItem;
		this.cost = cost;
	}
*/

	public int getIdBrach() {
		return idBrach;
	}
	public void setIdBrach(int idBrach) {
		this.idBrach = idBrach;
	}

	public int getIdItem() {
		return idItem;
	}
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public int getIdSubg() {
		return idSubg;
	}
	public void setIdSubg(int idSubg) {
		this.idSubg = idSubg;
	}

	public int getIdCompa() {
		return idCompa;
	}
	public void setIdCompa(int idCompa) {
		this.idCompa = idCompa;
	}

	public int getExis() {
		return exis;
	}
	public void setExis(int exis) {
		this.exis = exis;
	}

	public String getNaBranch() {
		return naBranch;
	}
	public void setNaBranch(String naBranch) {
		this.naBranch = naBranch;
	}

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

	public String getNaItem() {
		return naItem;
	}
	public void setNaItem(String naItem) {
		this.naItem = naItem;
	}

	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}

}
