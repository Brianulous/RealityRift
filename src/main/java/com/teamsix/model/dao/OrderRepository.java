package com.teamsix.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamsix.model.bean.item.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {

	Page<Orders> findByMemberMemno(int memno, Pageable pageable);

	// whether change nativeQuery into JPQL? think about it !
	@Query("SELECT NEW map(YEAR(o.orderDate) as year, MONTH(o.orderDate) as month, SUM(o.totalAmount) as totalSales) "
			+ "FROM Orders o " + "WHERE YEAR(o.orderDate) = :year " + "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) "
			+ "ORDER BY month ASC")
	List<Map<String, Object>> findSalesByYear(@Param("year") int year);
	// UGLY UGLY UGLY UGLY!!!!

}
