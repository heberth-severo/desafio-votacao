package com.hvas.votacao.repository;

import com.hvas.votacao.model.Pauta;
import com.hvas.votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaAndAssociadoId(Pauta pauta, Long associadoId);
    long countByPautaAndVoto(Pauta pauta, boolean voto);
}
