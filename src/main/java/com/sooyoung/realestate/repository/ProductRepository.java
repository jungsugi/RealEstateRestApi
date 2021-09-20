package com.sooyoung.realestate.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sooyoung.realestate.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
    @Query("select p from Product p where ?1 between p.startedAt and p.finishedAt")
	List<Product> findByTime(@Param("time") LocalDateTime time);
	
    @Query("select p from Product p where ?1 between p.startedAt and p.finishedAt and p.id = ?2")
	Optional<Product> findByTimeAndProductId(@Param("time") LocalDateTime time
											,@Param("productId") long productid);
    
    
    
}
