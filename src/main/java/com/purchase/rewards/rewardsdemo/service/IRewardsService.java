package com.purchase.rewards.rewardsdemo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.purchase.rewards.rewardsdemo.beans.CustomerRewardsTotalBean;
import com.purchase.rewards.rewardsdemo.beans.MonthlyRewardBean;
import com.purchase.rewards.rewardsdemo.beans.PurchaseBean;
import com.purchase.rewards.rewardsdemo.beans.UserBean;

public interface IRewardsService {

	ResponseEntity<String> createPurchase(PurchaseBean purchaseBean);

	List<UserBean> createPurchases(List<UserBean> purchaseBean);

	PurchaseBean getPurchaseForCustomer(int id);

	List<String> getDistinctCustomer();

	List<PurchaseBean> getAllPurchases();

	List<PurchaseBean> getCustomerByName(PurchaseBean customerBean);

	int calculateRewards(int purchaseAmount);

	List<CustomerRewardsTotalBean> calculateMonthlyandTotalRewards();

	void assignMonthlyRewards(CustomerRewardsTotalBean customerRewardsTotalBean,List<MonthlyRewardBean> monthlyRewards);

	List<UserBean> addUsers(List<UserBean> userBean);

	void deletePurchase(int id);

	void updatePurchase(int id, PurchaseBean purchaseBean);

}