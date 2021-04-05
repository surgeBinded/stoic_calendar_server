package com.cebotariropot.stoiccalendarserver.stoic_calendar_server.dao;

import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<DAOUser, Integer> {
}
