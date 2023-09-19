package com.purchase.rewards.rewardsdemo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purchase.rewards.rewardsdemo.beans.CustomerRewardsTotalBean;
import com.purchase.rewards.rewardsdemo.beans.PurchaseBean;
import com.purchase.rewards.rewardsdemo.beans.UserBean;
import com.purchase.rewards.rewardsdemo.service.IRewardsService;
import com.purchase.rewards.rewardsdemo.service.impl.RewardsService;

import io.swagger.v3.oas.annotations.Operation;


/**
 * @author brianjoseph
 * 
 * A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent between $50 and $100 in each transaction.
	(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
	
	http://localhost:8080/swagger-ui/index.html#/
 *  http://localhost:8080/v3/api-docs
 *  http://localhost:8080/v3/api-docs.yaml
 */
@CrossOrigin(origins = "http://localhost:56080")
@RestController
@RequestMapping("/purchase/rewards")
public class RewardsController {
	
	@Autowired
	private RewardsService rewardsService;
	
	@Operation(summary = "Add customer purchase", description = "Add a purchase if user doesnt exist create it")
	@PostMapping("/addPurchase")
	public ResponseEntity<String> addPurchase(@RequestBody PurchaseBean purchaseBean  ) {
		return rewardsService.createPurchase(purchaseBean);
	}
	
	@Operation(summary = "Add list of customer purchases", description = "Add a list of purchases if user doesnt exist create it")
	@PostMapping("/addPurchases")
	public List<UserBean> addPurchases(@RequestBody List<UserBean> purchaseBean  ) {
		
		return rewardsService.createPurchases(purchaseBean);
	}
	
	@Operation(summary = "Get purchase record", description = "Get a purchase record by id")
	@GetMapping("/purchase/{id}")
	public PurchaseBean getPurchaseById(@PathVariable int id) {
			return rewardsService.getPurchaseForCustomer(id);
	}
	
	@Operation(summary = "Get all purchase history", description = "Gets all the purchase history for every user")
	@GetMapping("/getPurchases")
	public List<PurchaseBean> getAllPurchasesByCustomers(){ 
		return rewardsService.getAllPurchases();
	}
	
	@Operation(summary = "Get customer purchase by parameter", description = "Gets all purchase records for user by name")
	@GetMapping("/customerByName/{name}")
	public List<PurchaseBean> getCustomerByName(@PathVariable String name){ 
		PurchaseBean cb = new PurchaseBean();
			cb.setName(name);
		return rewardsService.getCustomerByName(cb);
		
	}
	
	@Operation(summary = "Get customer purchase by response body", description = "Gets all purchase records for user by name")
	@GetMapping("/getCustomerByName")
	public List<PurchaseBean> getCustomerByName (@RequestBody PurchaseBean purchaseBean){ 
		return rewardsService.getCustomerByName(purchaseBean);
		
	}
	
	@Operation(summary = "Get all customer Rewards", description = "Gets all customer reward total and total for each month")
	@GetMapping("/customersTotals")
	public List<CustomerRewardsTotalBean> getCustomersTotals(){ 
		return rewardsService.calculateMonthlyandTotalRewards();
		
	}
	
	@Operation(summary = "Get all distinct customers", description = "Gets all distinct customers from purchase history table")
	@GetMapping("/distinctCustomer")
	public List<String> getDistinctCustomer(){ 
		return rewardsService.getDistinctCustomer();
		
	}
	
	@Operation(summary = "Add list of users", description = "Add list of users to User table")
	@PostMapping("/addUsers")
	public List<UserBean> addUsers(@RequestBody List<UserBean> userBean  ) {
		return rewardsService.addUsers(userBean);
	}
	
	@Operation(summary = "Delete purchase", description = "Delete purchase by id")
	@DeleteMapping("/deletePurchase/{id}")
	public void deletePurchase(@PathVariable int id) {
		rewardsService.deletePurchase(id);
		
	}
	
	@Operation(summary = "Update purchase", description = "Update purchase by id")
	@PutMapping("/updatePurchase/{id}")
	public void updatePurchase(@PathVariable int id,@RequestBody PurchaseBean purchaseBean){
		 rewardsService.updatePurchase(id, purchaseBean);
		
	}
		
	}


