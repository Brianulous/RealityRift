package com.teamsix.controller.item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsix.model.bean.item.ItemDTO;
import com.teamsix.model.dao.ItemRepository;
import com.teamsix.service.OrderService;

@RestController
@RequestMapping("/chart")
public class ChartController {

	private final ItemRepository itemRepository;

	private final OrderService orderService;

	@Autowired
	public ChartController(ItemRepository itemRepository, OrderService orderService) {
		super();
		this.itemRepository = itemRepository;
		this.orderService = orderService;
	}

	@GetMapping("/itemSales")
	public ResponseEntity<Map<String, Integer>> getSales() {
		//正常商業邏輯下不抓全部商品可能會更好(例如:只抓銷量超過1000商品)，來增加效能
		List<ItemDTO> items = itemRepository.findAll();
		Map<String, Integer> salesData = new HashMap<>();

		for (ItemDTO item : items) {
			//商品:銷量
			salesData.put(item.getItemname(), item.getSalescount());
		}

		return ResponseEntity.ok(salesData);
	}

	@GetMapping("/itemAmount")
	public ResponseEntity<Map<String, BigDecimal>> getAmount() {
		List<ItemDTO> items = itemRepository.findAll();
		Map<String, BigDecimal> salesData = new HashMap<>();
		for (ItemDTO item : items) {
			//商品:銷售額
			BigDecimal salesAmount = BigDecimal.valueOf(item.getSalescount()).multiply(item.getPrice());
			salesData.put(item.getItemname(), salesAmount);
		}

		return ResponseEntity.ok(salesData);
	}

	@GetMapping("/amount/{year}")
	public ResponseEntity<List<Map<String, Object>>> getSalesByYear(@PathVariable int year) {
		List<Map<String, Object>> sales = orderService.getSalesByYear(year);
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}

}
