package com.purchase.rewards.rewardsdemo.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="purchase_history")
public class PurchaseBean {
	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int purchaseId;
	private String name;
	private String email;
	private int purchaseAmount; 
	private int rewardsPoints;
	private Date createdDate;
	 
 	@ManyToOne(cascade = CascadeType.ALL)
 	@JoinColumn(name = "userID")
 	@JsonIgnore
 	private UserBean userBean; // Foreign key referencing the User table
	
 

	public PurchaseBean() {
		// TODO Auto-generated constructor stub
	}

	public PurchaseBean(int purchaseId, String name, String email, int purchaseAmount, int rewardsPoints,
			Date createdDate) {
		super();
		this.purchaseId = purchaseId;
		this.name = name;
		this.email = email;
		this.purchaseAmount = purchaseAmount;
		this.rewardsPoints = rewardsPoints;
		this.createdDate = createdDate;
	}




	public int getPurchaseId() {
		return purchaseId;
	}


	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		//DateFormat df = new SimpleDateFormat("yyy-MM-dd");
		//Date date = new Date();
		
		this.createdDate = createdDate;
	}
	public int getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(int purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public int getRewardsPoints() {
		return rewardsPoints;
	}
	public void setRewardsPoints(int rewardsPoints) {
		this.rewardsPoints = rewardsPoints;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	
	


}