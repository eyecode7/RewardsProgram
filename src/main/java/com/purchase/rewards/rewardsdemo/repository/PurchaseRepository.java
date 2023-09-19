package com.purchase.rewards.rewardsdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.purchase.rewards.rewardsdemo.beans.MonthlyTotal;
import com.purchase.rewards.rewardsdemo.beans.PurchaseBean;
import com.purchase.rewards.rewardsdemo.beans.RewardsTotal;


@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseBean,Integer > {

	String SELECT_TOTALS_BY_CUST = "select name, sum (rewards_points) as totalRewardsPoints FROM purchase_history group by name";
	String SELECT_MONTHLY_TOTALS_BY_CUST = "SELECT monthname (created_date) as RewardMonth, name,\n"
			+ "       SUM(rewards_points) as TotalPointsPerMonth\n"
			+ "FROM purchase_history\n"
			+ "GROUP BY monthname (created_date), name order by name";
	String CUST_LIST = "select distinct(NAME) as name  FROM purchase_history";
	String GET_PURCHASES_BY_NAME = "select u.* from purchase_history u where u.name = :#{#customer.name}";
	String DELETE_PURCHASE = "DELETE FROM purchase_history WHERE PURCHASE_ID =:purchaseId";
	String UPDATE_PURCHASE= "UPDATE purchase_history SET name= :#{#customer.name}, email= :#{#customer.email},purchase_amount=:#{#customer.purchaseAmount},created_date=:#{#customer.createdDate},rewards_points= :#{#customer.rewardsPoints}  where PURCHASE_ID =:purchaseId";
	
	@Query(value = SELECT_TOTALS_BY_CUST, nativeQuery = true)
	public List<RewardsTotal> totalRewardsPoint();
	
	@Query(value = SELECT_MONTHLY_TOTALS_BY_CUST, nativeQuery = true)
	public List<MonthlyTotal> monthlyTotalRewards();
	
	@Query(value = CUST_LIST, nativeQuery = true)
	public List<String> custList();
	
	@Query(value = GET_PURCHASES_BY_NAME, nativeQuery = true)
	List<PurchaseBean> findUsersByCustomersFirstname(@Param("customer") PurchaseBean customer);
	
	@Transactional
	@Modifying
	@Query(value = DELETE_PURCHASE, nativeQuery = true)
	public void deletePurchase(@Param("purchaseId") int id);
	
	@Transactional
	@Modifying
	@Query(value = UPDATE_PURCHASE, nativeQuery = true)
	public void updatePurchase(@Param("purchaseId") int id, @Param("customer") PurchaseBean customer);
	
	

}
