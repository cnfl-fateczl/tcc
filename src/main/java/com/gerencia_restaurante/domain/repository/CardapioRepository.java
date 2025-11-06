package com.gerencia_restaurante.domain.repository;

import com.gerencia_restaurante.domain.entity.Cardapio;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CardapioRepository extends JpaRepository<Cardapio, Long> {
    public List<Cardapio> findByNome(String nome);
}
