package com.hvas.votacao.service;

import com.hvas.votacao.model.Pauta;
import com.hvas.votacao.model.Sessao;
import com.hvas.votacao.model.Voto;
import com.hvas.votacao.repository.PautaRepository;
import com.hvas.votacao.repository.SessaoRepository;
import com.hvas.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotingServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private CpfValidationService cpfValidationService;

    @InjectMocks
    private VotingService votingService;

    private Pauta pauta;
    private Sessao sessao;
    private Voto voto;

    @BeforeEach
    public void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricao("Descrição da Pauta");

        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);
        sessao.setAberta(true);
        sessao.setDataInicio(LocalDateTime.now());
        sessao.setDataFim(LocalDateTime.now().plusMinutes(1));

        voto = new Voto();
        voto.setId(1L);
        voto.setPauta(pauta);
        voto.setAssociadoId(1L);
        voto.setVoto(true);
    }

    @Test
    public void testCriarPauta() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        Pauta createdPauta = votingService.criarPauta("Descrição da Pauta");

        assertNotNull(createdPauta);
        assertEquals("Descrição da Pauta", createdPauta.getDescricao());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    public void testAbrirSessao() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        Sessao openedSessao = votingService.abrirSessao(1L, null);

        assertNotNull(openedSessao);
        assertTrue(openedSessao.isAberta());
        assertEquals(pauta, openedSessao.getPauta());
        verify(sessaoRepository, times(1)).save(any(Sessao.class));
    }

    @Test
    public void testVotar() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(votoRepository.existsByPautaAndAssociadoId(any(Pauta.class), anyLong())).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);
        when(cpfValidationService.podeVotar(anyString())).thenReturn(true);

        Voto newVoto = votingService.votar(1L, 1L, true, "12345678901");

        assertNotNull(newVoto);
        assertTrue(newVoto.isVoto());
        assertEquals(1L, newVoto.getAssociadoId());
        verify(votoRepository, times(1)).save(any(Voto.class));
        verify(cpfValidationService, times(1)).podeVotar(anyString());
    }

    @Test
    public void testResultadoVotacao() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaAndVoto(pauta, true)).thenReturn(10L);
        when(votoRepository.countByPautaAndVoto(pauta, false)).thenReturn(5L);

        String resultado = votingService.resultadoVotacao(1L);

        assertEquals("Resultado: Sim = 10, Não = 5", resultado);
        verify(votoRepository, times(1)).countByPautaAndVoto(pauta, true);
        verify(votoRepository, times(1)).countByPautaAndVoto(pauta, false);
    }
}
