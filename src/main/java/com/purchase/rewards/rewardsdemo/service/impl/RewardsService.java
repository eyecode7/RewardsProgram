package com.purchase.rewards.rewardsdemo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.purchase.rewards.rewardsdemo.beans.CustomerRewardsTotalBean;
import com.purchase.rewards.rewardsdemo.beans.MonthlyRewardBean;
import com.purchase.rewards.rewardsdemo.beans.MonthlyTotal;
import com.purchase.rewards.rewardsdemo.beans.PurchaseBean;
import com.purchase.rewards.rewardsdemo.beans.RewardsTotal;
import com.purchase.rewards.rewardsdemo.beans.UserBean;
import com.purchase.rewards.rewardsdemo.repository.PurchaseRepository;
import com.purchase.rewards.rewardsdemo.repository.UserRepository;
import com.purchase.rewards.rewardsdemo.service.IRewardsService;

@Service
public class RewardsService implements IRewardsService {

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private UserRepository userRepository;

	int twoPointRewards;
	int onePointRewards;
	int points;
	int totalPoints;

	
	@Override
	public ResponseEntity<String> createPurchase(PurchaseBean purchaseBean) {
		UserBean userBean = new UserBean();
		userBean.setEmail(purchaseBean.getEmail());
		userBean = userRepository.findUsersByEmail(userBean);

		if (userBean != null && userBean.getId() != 0) {

			purchaseBean.setRewardsPoints(calculateRewards(purchaseBean.getPurchaseAmount()));
			purchaseBean.setUserBean(userRepository.getReferenceById(userBean.getId()));
			purchaseRepository.save(purchaseBean);
			return new ResponseEntity<String>("Purchase has been added", HttpStatus.OK);

		} else if (purchaseBean != null && (purchaseBean.getEmail() != null)) {
			UserBean newUser = new UserBean();
			ArrayList<PurchaseBean> customerPurchases = new ArrayList<>();
			customerPurchases.add(purchaseBean);

			newUser.setName(purchaseBean.getName());
			newUser.setEmail(purchaseBean.getEmail());
			newUser.setPurchaseBean(customerPurchases);
			purchaseBean.setRewardsPoints(calculateRewards(purchaseBean.getPurchaseAmount()));

			userRepository.save(newUser);

			return new ResponseEntity<String>("New User has been added", HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("User Email is NULL", HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public List<UserBean> createPurchases(List<UserBean> purchaseBean) {

		List<UserBean> rewardsCustomer = purchaseBean;

		for (UserBean customer : rewardsCustomer) {
			for (PurchaseBean purchase : customer.getPurchaseBean()) {
				purchase.setRewardsPoints(calculateRewards(purchase.getPurchaseAmount()));
			}
		}

		return userRepository.saveAll(rewardsCustomer);
	}

	@Override
	public PurchaseBean getPurchaseForCustomer(int id) {
		return purchaseRepository.findById(id).orElse(null);
	}

	@Override
	public List<String> getDistinctCustomer() {
		return purchaseRepository.custList();
	}

	@Override
	public List<PurchaseBean> getAllPurchases() {
		return purchaseRepository.findAll();
	}

	@Override
	public List<PurchaseBean> getCustomerByName(PurchaseBean customerBean) {
		return purchaseRepository.findUsersByCustomersFirstname(customerBean);
	}

	@Override
	public int calculateRewards(int purchaseAmount) {
		// A customer receives 2 points for every dollar spent over $100 in each
		// transaction, plus 1 point for every dollar spent between $50 and $100 in each
		// transaction.
		// (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
		int rewards = 0;

		if (purchaseAmount > 100) {

			points = purchaseAmount - 100; //get points for anything above $100
			twoPointRewards = points * 2;
			rewards = twoPointRewards + 50;
//			System.out.println("purchaseAmount " + purchaseAmount + " twoPointRewards " + twoPointRewards
//					+ " totalPoints " + rewards);

		}
		if (purchaseAmount <= 100 && purchaseAmount > 50) {

			onePointRewards = purchaseAmount - 50;
			rewards = onePointRewards;
//			System.out.println("onePointRewards " + rewards);

		} else if (purchaseAmount <= 50) {
			System.out.println("Sorry no rewards :(");
		}

		return rewards;
	}

	@Override
	public List<CustomerRewardsTotalBean> calculateMonthlyandTotalRewards() {
		//CustomerRewardsTotalBean name, totalRewards, list of rewards monthly
		//MonthlyRewardBean name, month, totalPoints
		//RewardsTotal TotalRewardsPoints, name
		//MonthlyTotal rewardMonth, totalPtsPerMonth, name
		
		
		List<CustomerRewardsTotalBean> custRewardsList = new ArrayList<CustomerRewardsTotalBean>();
		List<MonthlyRewardBean> monthlyRewardsList = new ArrayList<MonthlyRewardBean>();
		List<RewardsTotal> rewardsTotal = purchaseRepository.totalRewardsPoint(); //total rewards per customer
		List<MonthlyTotal> monthlyTotal = purchaseRepository.monthlyTotalRewards(); //total rewards per customer every month

		for (RewardsTotal rewardsFromDB : rewardsTotal) {
			CustomerRewardsTotalBean totalRewardsPerCustomer = new CustomerRewardsTotalBean();
			totalRewardsPerCustomer.setName(rewardsFromDB.getName());
			totalRewardsPerCustomer.setTotalRewardsPoints(rewardsFromDB.getTotalRewardsPoints());
			custRewardsList.add(totalRewardsPerCustomer);
//			System.out.println("Name " + rewardsFromDB.getName() + " total Rewards " + rewardsFromDB.getTotalRewardsPoints());
		}

		for (MonthlyTotal monthlyTotalFromDB : monthlyTotal) {

			MonthlyRewardBean totalRewardsPerCustomerMonthly = new MonthlyRewardBean();

			totalRewardsPerCustomerMonthly.setName(monthlyTotalFromDB.getName());
			totalRewardsPerCustomerMonthly.setMonth(monthlyTotalFromDB.getRewardMonth());
			totalRewardsPerCustomerMonthly.setTotalPoints(monthlyTotalFromDB.getTotalPointsPerMonth());
//			System.out.println("Name " + monthlyTotalFromDB.getName() + " total Rewards Monthly " + monthlyTotalFromDB.getTotalPointsPerMonth()
//					+ " Reward Month " + monthlyTotalFromDB.getRewardMonth());
			monthlyRewardsList.add(totalRewardsPerCustomerMonthly);

		}

		for (CustomerRewardsTotalBean custrewards : custRewardsList) {
			assignMonthlyRewards(custrewards, monthlyRewardsList);

		}

		return custRewardsList;

	}

	@Override
	public void assignMonthlyRewards(CustomerRewardsTotalBean customerRewardsTotalBean,List<MonthlyRewardBean> monthlyRewards) {
		
		List<MonthlyRewardBean> customerMonthlyRewardsList = new ArrayList<MonthlyRewardBean>();
		for (MonthlyRewardBean monthlyRewardList : monthlyRewards) {
			
			if (monthlyRewardList.getName().equalsIgnoreCase(customerRewardsTotalBean.getName())) {
				MonthlyRewardBean monthlyRewardEachCust = new MonthlyRewardBean();
				monthlyRewardEachCust.setName(monthlyRewardList.getName());
				monthlyRewardEachCust.setMonth(monthlyRewardList.getMonth());
				monthlyRewardEachCust.setTotalPoints(monthlyRewardList.getTotalPoints());
				customerMonthlyRewardsList.add(monthlyRewardEachCust);

				customerRewardsTotalBean.setMonthlyRewardsPoints(customerMonthlyRewardsList);
			}

		}

	}

	@Override
	public List<UserBean> addUsers(List<UserBean> userBean) {

		List<UserBean> rewardsCustomer = userBean;

		for (UserBean customer : rewardsCustomer) {
			for (PurchaseBean customerBean : customer.getPurchaseBean()) {
				customerBean.setRewardsPoints(calculateRewards(customerBean.getPurchaseAmount()));

			}
		}

		return userRepository.saveAll(userBean);

	}

	@Override
	public void deletePurchase(int id) {
		purchaseRepository.deletePurchase(id);
		;

	}

	@Override
	public void updatePurchase(int id, PurchaseBean purchaseBean) {

		purchaseBean.setRewardsPoints(calculateRewards(purchaseBean.getPurchaseAmount()));

		purchaseRepository.updatePurchase(id, purchaseBean);

	}

}
