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
public class BranchVo {
	
	private Integer IdBranch;
    private Integer IdCompany;
    private String LongName;
	private String ShortName;

	public BranchVo(Integer IdBranch, Integer IdCompany, String LongName, String ShortName) {
		this.IdBranch = IdBranch;
		this.IdCompany = IdCompany;
		this.LongName = LongName;
		this.ShortName = ShortName;
	}

	public Integer getIdBranch() {
		return IdBranch;
	}
	public void setIdBranch(Integer IdBranch) {
		this.IdBranch = IdBranch;
	}

	public Integer getIdCompany() {
		return IdCompany;
	}
	public void setIdCompany(Integer IdCompany) {
		this.IdCompany = IdCompany;
	}

	public String getLongName() {
		return LongName;
	}
	public void setLongName(String LongName) {
		this.LongName = LongName;
	}

	public String getShortName() {
		return ShortName;
	}
	public void setShortName(String ShortName) {
		this.ShortName = ShortName;
	}
	
	@Override
    public String toString(){
      return ShortName;
    }
}
