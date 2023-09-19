package com.purchase.rewards.rewardsdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.purchase.rewards.rewardsdemo.beans.PurchaseBean;
import com.purchase.rewards.rewardsdemo.repository.PurchaseRepository;
import com.purchase.rewards.rewardsdemo.repository.UserRepository;
import com.purchase.rewards.rewardsdemo.service.impl.RewardsService;

@SpringBootTest
@RunWith(SpringRunner.class)
class RewardsdemoApplicationTests {

	
	
	@Autowired
	private RewardsService rewardsService;
	
	@MockBean
	private PurchaseRepository purchaseRepository;
	
	@MockBean
	private UserRepository userRepository;
	
	@Test
	public void getPurchasesTest() {
	    Date current = new Date();
		when(purchaseRepository.findAll()).thenReturn(Stream
				.of(new PurchaseBean(1 ,"Brian", "brian@aol.com", 123, 13,current), new PurchaseBean(1 ,"Ian", "ian@aol.com", 1223, 134,current)).collect(Collectors.toList()));
		assertEquals(2, rewardsService.getAllPurchases().size());
		
	}

}




