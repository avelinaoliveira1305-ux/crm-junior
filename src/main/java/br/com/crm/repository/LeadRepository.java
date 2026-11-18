package br.com.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.crm.model.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
}