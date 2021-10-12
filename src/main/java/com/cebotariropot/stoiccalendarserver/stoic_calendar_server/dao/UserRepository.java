package com.cebotariropot.stoiccalendarserver.stoic_calendar_server.dao;

import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<DAOUser, Integer> {
    DAOUser findByUsernameLikeIgnoreCase(@NonNull String username);
}
