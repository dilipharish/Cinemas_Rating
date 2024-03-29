package cinema_real.service;

import org.springframework.data.jpa.repository.JpaRepository;

import cinema_real.models.Cinemas;

public interface ProductRepository extends JpaRepository<Cinemas,Integer> {
	

}
