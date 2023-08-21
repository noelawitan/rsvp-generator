package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "select t from Token t join User u on t.user.id = u.id where u.id = :id and (t.expired = false or t.revokeToken = false)")
    List<Token> findAllValidTokensByUserId(Long id);
}
