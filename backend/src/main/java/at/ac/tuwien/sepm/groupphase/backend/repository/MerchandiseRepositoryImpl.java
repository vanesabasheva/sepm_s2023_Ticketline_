package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;


public class MerchandiseRepositoryImpl extends SimpleJpaRepository<Merchandise, Integer> implements MerchandiseRepository {

    public MerchandiseRepositoryImpl(EntityManager em) {
        super(Merchandise.class, em);
    }
    
    @Override
    public List<Merchandise> findAllMerchandiseWithPoints() {
        Specification<Merchandise> filter = (root, query, cb) -> cb.greaterThan(root.get("pointsPrice"), 0);
        return findAll(filter);
    }
}
