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
public class GroupVo {
    
	private Integer IdGroup;
    private Integer IdCompany;
    private String Name;

	public GroupVo(Integer IdGroup, Integer IdCompany, String Name) {
		this.IdGroup = IdGroup;
		this.IdCompany = IdCompany;
		this.Name = Name;
	}
	
	public Integer getIdGroup() {
		return IdGroup;
	}
	public void setIdGroup(Integer IdGroup) {
		this.IdGroup = IdGroup;
	}
	
	public Integer getIdCompany() {
		return IdCompany;
	}
	public void setIdCompany(Integer IdCompany) {
		this.IdCompany = IdCompany;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String Name) {
		this.Name = Name;
	}
	
		@Override
    public String toString(){
      return Name;
    }
}
