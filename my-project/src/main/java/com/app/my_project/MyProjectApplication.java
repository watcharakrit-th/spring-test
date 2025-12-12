package com.app.my_project;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.entity.CampaignEntity;
import com.app.my_project.entity.DiscountEntity;
import com.app.my_project.entity.ProductEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = "com.app.my_project")
public class MyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyProjectApplication.class, args);
	}

	@Autowired
	private ObjectMapper objectMapper;

	public static final Set<String> ALLOWED_CAMPAIGNS = Set.of(
			"Fixed amount", "Percentage discount", "Percentage discount by item category", "Discount by points",
			"Special campaigns");
	public static final Set<String> ALLOWED_CATEGORIES = Set.of(
			"Clothing", "Accessories", "Electronics");

	@PostMapping("/helloo")
	public Object helloo(@RequestBody JsonNode rootNode) {
		// read json
		JsonNode campaign = rootNode.get(0);
		JsonNode cart = rootNode.get(1);

		// map index 0 of RequestBody to CampaignEntity
		List<CampaignEntity> campaignEntities = objectMapper.convertValue(campaign,
				objectMapper.getTypeFactory().constructCollectionType(List.class, CampaignEntity.class));
		campaignEntities.sort(Comparator.comparing(CampaignEntity::getCategory));

		// check duplicate category
		try {
			Set<String> seen = new HashSet<>();
			Set<String> duplicates = campaignEntities.stream()
					.map(CampaignEntity::getCategory)
					.filter(category -> category != null) // Ignore null
					.filter(category -> !seen.add(category)) // Return true if ALREADY in set
					.collect(Collectors.toSet());
			if (!duplicates.isEmpty()) {
				throw new RuntimeException("Duplicate categories found: " + duplicates);
			}
		} catch (Exception e) {
			return e.getMessage();
		}

		// map index 1 of RequestBody to ProductEntity
		List<ProductEntity> productEntities = objectMapper.convertValue(cart,
				objectMapper.getTypeFactory().constructCollectionType(List.class, ProductEntity.class));

		// next 40 lines = calculating parts
		Double totalPrice = 0.0;
		Double totalPriceAfterDiscount = 0.0;
		for (ProductEntity product : productEntities) {
			totalPrice += product.getProductPrice();
			totalPriceAfterDiscount += product.getProductPrice();
		}

		for (CampaignEntity ce : campaignEntities) {
			switch (ce.getCategory()) {
				case "Coupon":
					switch (ce.getCampaigns()) {
						case "Fixed amount":
							totalPriceAfterDiscount -= ((Number) ce.getAmount()).doubleValue();
							break;
						case "Percentage discount":
							totalPriceAfterDiscount -= ((Number) ce.getAmount()).doubleValue() / 100 * totalPrice;
							break;
					}
					break;
				case "On Top":
					switch (ce.getCampaigns()) {
						case "Percentage discount by item category":
							// ce.getAmount() is a List<Object> ----- ["Clothing", 10] or ["Electronics",
							// 15] or ["Accessories", 5] .....
							List<Object> amountList = (List<Object>) ce.getAmount();
							Double sumOfMatchedCategoryPrice = 0.0;

							for (ProductEntity product : productEntities) {
								if (product.getProductCategory().equals(amountList.get(0))) {
									sumOfMatchedCategoryPrice += product.getProductPrice();
								}
							}
							totalPriceAfterDiscount -= sumOfMatchedCategoryPrice
									* ((Number) amountList.get(1)).doubleValue() / 100;
							break;
						case "Discount by points":
							System.out.println(totalPrice);
							if (totalPrice * 0.2 < ((Number) ce.getAmount()).doubleValue()) {
								totalPriceAfterDiscount -= totalPrice * 0.2;
								break;
							}
							totalPriceAfterDiscount -= ((Number) ce.getAmount()).doubleValue();
							break;
					}
					break;
				case "Seasonal":
					switch (ce.getCampaigns()) {
						case "Special campaigns":
							// ce.getAmount() is a List<Number> ----- [every_X_THB, discount_Y_THB] .....
							List<Number> amountList = (List<Number>) ce.getAmount();

							Long every_X_THB = amountList.get(0).longValue();
							Long discount_Y_THB = amountList.get(1).longValue();

							totalPriceAfterDiscount -= ((Number) totalPrice).longValue() / every_X_THB * discount_Y_THB;
							break;
					}
					break;
				default:
					return totalPrice;

			}
		}
		BigDecimal totalPriceAfterDiscount_BigDecimal = new BigDecimal(Double.toString(totalPriceAfterDiscount));
		return totalPriceAfterDiscount_BigDecimal.setScale(2, RoundingMode.HALF_UP);
	}

	//
	//
	//

	// @PostMapping("/hello")
	// public Object hello(@RequestBody JsonNode rootNode) {

	// JsonNode campaign = rootNode.get(0);
	// JsonNode cart = rootNode.get(1);
	// // if (campaign.get("couponName") == null
	// // || ALLOWED_CAMPAIGNS.contains(campaign.get("couponName").asText()) ==
	// false)
	// // {
	// // return "couponName is missing or invalid";
	// // }
	// // if (campaign.get("onTopName") == null
	// // || ALLOWED_CAMPAIGNS.contains(campaign.get("onTopName").asText()) ==
	// false) {
	// // return "onTopName is missing or invalid";
	// // }
	// // if (campaign.get("onTopCategory") == null
	// // || ALLOWED_CATEGORIES.contains(campaign.get("onTopCategory").asText()) ==
	// // false) {
	// // return "onTopCategory is missing or invalid";
	// // }
	// // if (campaign.get("seasonalName") == null
	// // || ALLOWED_CAMPAIGNS.contains(campaign.get("seasonalName").asText()) ==
	// // false) {
	// // return "seasonalName is missing or invalid";
	// // }

	// DiscountEntity discountEntity = objectMapper.convertValue(campaign,
	// DiscountEntity.class);
	// List<ProductEntity> productEntities = objectMapper.convertValue(cart,
	// objectMapper.getTypeFactory().constructCollectionType(List.class,
	// ProductEntity.class));

	// Double totalPrice = 0.0;
	// for (ProductEntity product : productEntities) {
	// totalPrice += product.getProductPrice();
	// }
	// Double maxPrice = totalPrice;

	// if (ALLOWED_CAMPAIGNS.contains(discountEntity.getCouponName())) {
	// totalPrice -= calculateCoupon(discountEntity.getCouponName(),
	// discountEntity.getCouponAmount(), totalPrice);
	// System.out.println(totalPrice);
	// }
	// if (ALLOWED_CAMPAIGNS.contains(discountEntity.getOnTopName())) {
	// totalPrice -= calculateOnTop(discountEntity.getOnTopName(),
	// discountEntity.getOnTopAmount(),
	// discountEntity.getOnTopCategory(), productEntities, totalPrice);
	// System.out.println(totalPrice);
	// }
	// if (ALLOWED_CAMPAIGNS.contains(discountEntity.getSeasonalName())) {
	// totalPrice -= calculateSeasonal(discountEntity.getSeasonalCap(),
	// discountEntity.getSeasonalAmount(), maxPrice);
	// System.out.println(totalPrice);
	// }
	// System.out.println("-----------------------------------------");

	// BigDecimal bd = new BigDecimal(Double.toString(totalPrice));
	// return bd.setScale(2, RoundingMode.HALF_UP);
	// }

	// public Double calculateCoupon(String couponName, Double couponAmount, Double
	// totalPrice) {
	// switch (couponName) {
	// case "Fixed amount":
	// return couponAmount;
	// case "Percentage discount":
	// return totalPrice * couponAmount / 100;
	// default:
	// return 0.0;
	// }
	// }

	// public Double calculateOnTop(String onTopName, Double onTopAmount, String
	// onTopCategory,
	// List<ProductEntity> productEntities,
	// Double totalPrice) {
	// Double percentageDiscount = 0.0;

	// switch (onTopName) {
	// case "Percentage discount by item category":
	// for (ProductEntity product : productEntities) {
	// if (product.getProductCategory().equals(onTopCategory)) {

	// percentageDiscount += product.getProductPrice();
	// }
	// }
	// percentageDiscount = percentageDiscount * onTopAmount / 100;

	// return percentageDiscount;
	// case "Discount by points":
	// return onTopAmount;
	// default:
	// return 0.0;
	// }

	// }

	// public Double calculateSeasonal(Long seasonalCap, Double seasonalAmount,
	// Double maxPrice) {
	// return (double) ((maxPrice.longValue() / seasonalCap) *
	// seasonalAmount.longValue());
	// }

}
