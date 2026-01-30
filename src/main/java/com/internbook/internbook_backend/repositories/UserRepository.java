package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.dtos.FavouriteShortResponseDto;
import com.internbook.internbook_backend.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @EntityGraph(attributePaths = {"department"})
    Optional<User> findOneById(Long id);

    @Query("SELECT COUNT(s) > 0 FROM User u JOIN u.favouriteSubmissions s WHERE u.id = :userId AND s.id = :subId")
    boolean isFavourite(@Param("userId") Long userId, @Param("subId") Integer subId);

    @Modifying
    @Query(value = "INSERT INTO favourites (user_id, submission_id) VALUES (:userId, :subId)", nativeQuery = true)
    void addFavourite(@Param("userId") Long userId, @Param("subId") Integer subId);

    @Modifying
    @Query(value = "DELETE FROM favourites WHERE user_id = :userId AND submission_id = :subId", nativeQuery = true)
    int removeFavourite(@Param("userId") Long userId, @Param("subId") Integer subId);

    @Query("""
        select count(s)
        from User u
        join u.favouriteSubmissions s
        where u.id = :userId
    """)
    long countFavouritesByUserId(@Param("userId") Long userId);

    @Query("""
    select new com.internbook.internbook_backend.dtos.FavouriteShortResponseDto(
        s.id,
        c.id,
        c.name,
        su.name,
        su.isAnonymous
    )
    from User u
    join u.favouriteSubmissions s
    join s.company c
    join s.user su
    where u.id = :userId
    order by s.createdAt desc
""")
    List<FavouriteShortResponseDto> findTop3Favourites(
            @Param("userId") Long userId,
            Pageable pageable
    );

}