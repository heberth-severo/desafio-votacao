package com.hvas.votacao.controller;

import com.hvas.votacao.model.Pauta;
import com.hvas.votacao.model.Sessao;
import com.hvas.votacao.model.Voto;
import com.hvas.votacao.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/votacao")
public class VotingController {

    @Autowired
    private VotingService votingService;

    @PostMapping("/pautas")
    public Pauta criarPauta(@RequestBody String descricao) {
        return votingService.criarPauta(descricao);
    }

    @PostMapping("/pautas/{pautaId}/sessao")
    public Sessao abrirSessao(@PathVariable Long pautaId, @RequestParam(required = false) LocalDateTime dataFim) {
        return votingService.abrirSessao(pautaId, dataFim);
    }

    @PostMapping("/pautas/{pautaId}/votos")
    public Voto votar(@PathVariable Long pautaId, @RequestParam Long associadoId, @RequestParam boolean voto, @RequestParam String cpf) {
        return votingService.votar(pautaId, associadoId, voto, cpf);
    }

    @GetMapping("/pautas/{pautaId}/resultado")
    public String resultadoVotacao(@PathVariable Long pautaId) {
        return votingService.resultadoVotacao(pautaId);
    }
}
