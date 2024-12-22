package org.example.avtodiller.repositories;

import org.example.avtodiller.models.CityModel;
import org.example.avtodiller.models.GoodModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public interface GoodRepository extends JpaRepository<GoodModel, Long> {
    List<GoodModel> findAll(Specification<GoodModel> specification);

    default List<GoodModel> findByField(String content,
                                        String name,
                                        CityModel cityOfDeparture,
                                        String dateOfDeparture,
                                        CityModel cityOfArrival,
                                        String dateOfArrival) {
        return findAll((Specification<GoodModel>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(content != null && !content.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("content")),
                        "%" + content.toLowerCase() + "%"));
            }

            if(name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (cityOfDeparture != null && cityOfDeparture.getId() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("cityOfDeparture").get("id").as(Long.class),
                        cityOfDeparture.getId()
                ));
            }

            if (cityOfArrival != null && cityOfArrival.getId() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("cityOfArrival").get("id"),
                        cityOfArrival.getId()
                ));
            }

            if (dateOfDeparture != null && !dateOfDeparture.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("dateOfDeparture"), "%" + dateOfDeparture + "%"));
            }

            if (dateOfArrival != null && !dateOfArrival.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("dateOfArrival"), "%" + dateOfArrival + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        });
    }
}
