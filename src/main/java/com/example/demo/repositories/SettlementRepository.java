package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.*;

import java.util.Date;
import java.util.List;

public interface SettlementRepository extends CrudRepository<Settlements, Long> {

    public List<Settlements> findByGroup(Group group);

    public List<Settlements>findAll();

    public List<Settlements> findByGroupId(long id);

    public List<Settlements> findByReceiver(User receiver);

    public List<Settlements> findByPayer(User payer);

    public List<Settlements> findBySettledDate(Date date);

    public List<Settlements> findByPayerIdOrReceiverId(Long payerId, Long receiverId);

    public List<Settlements> findByGroupAndSettledDate(Group group, Date date);
}
