package com.agacorporation.demo.repository.specifications;

import com.agacorporation.demo.domain.RoomReservation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class RoomReservationsSpecifications {
    public static Specification<RoomReservation> findByPhrase(final String phrase)
    {
        return (root, query, cb) -> {
            if(StringUtils.isEmpty(phrase) == false){
                String phraseLike = "%"+phrase.toUpperCase() +"%";
                return cb.or(
                        cb.or(
                                cb.like(cb.upper(root.get("user").get("firstName")), phraseLike),
                                cb.like(cb.upper(root.get("user").get("lastName")), phraseLike)
                        ),
                        cb.like(cb.upper(root.get("user").get("login")), phraseLike)
                );
            }
            return null;
        };
    }


}