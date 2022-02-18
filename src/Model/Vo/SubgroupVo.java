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
public class SubgroupVo {
	private Integer IdSubg;
    private Integer IdSGroup;
    private String Name;
	private String Description;

	public SubgroupVo(Integer IdSubg, Integer IdSGroup, String Name, String Description) {
		this.IdSubg = IdSubg;
		this.IdSGroup = IdSGroup;
		this.Name = Name;
		this.Description = Description;
	}

	public Integer getIdSubg() {
		return IdSubg;
	}
	public void setIdSubg(Integer IdSubg) {
		this.IdSubg = IdSubg;
	}

	public Integer getIdSGroup() {
		return IdSGroup;
	}
	public void setIdSGroup(Integer IdSGroup) {
		this.IdSGroup = IdSGroup;
	}

	public String getName() {
		return Name;
	}
	public void setName(String Name) {
		this.Name = Name;
	}

	public String getDescription() {
		return Description;
	}
	public void setDescription(String Description) {
		this.Description = Description;
	}
	
	@Override
    public String toString(){
      return Name;
    }
}
