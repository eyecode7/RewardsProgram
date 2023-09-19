package com.purchase.rewards.rewardsdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.purchase.rewards.rewardsdemo.beans.UserBean;

@Repository
public interface UserRepository extends JpaRepository<UserBean,Integer > {
	
	String GET_USERS_BY_EMAIL = "select u.* from USERS u where u.email = :#{#user.email}";
	
	
	@Query(value = GET_USERS_BY_EMAIL, nativeQuery = true)
	public UserBean findUsersByEmail(@Param("user") UserBean user);

}
