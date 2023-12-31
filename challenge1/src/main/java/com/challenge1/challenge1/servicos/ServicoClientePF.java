package com.challenge1.challenge1.servicos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.challenge1.challenge1.dominio.cliente_pf.ClientePF;
import com.challenge1.challenge1.dtos.cliente_pf.AtualizacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.CriacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.mapeadores.MapeadorAtualizacaoClientePF;
import com.challenge1.challenge1.dtos.cliente_pf.mapeadores.MapeadorCriacaoClientePF;
import com.challenge1.challenge1.dtos.cliente_pf.mapeadores.MapeadorSelecaoClientePF;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaAtualizacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaCriacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;
import com.challenge1.challenge1.repositorios.RepositorioClientePF;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoClientePF {
    private static final String CLIENTE_NAO_ENCONTRADO_MENSAGEM_ERRO = "Cliente PF nao encontrado";

    private final RepositorioClientePF repositorio;
    private final MapeadorCriacaoClientePF mapeadorCriacao;
    private final MapeadorAtualizacaoClientePF mapeadorAtualizacao;
    private final MapeadorSelecaoClientePF mapeadorSelecao;

    public RespostaCriacaoClientePFDTO criar(final CriacaoClientePFDTO criacaoClientePFDTO) {
        log.info("Criando cliente PF: {}", criacaoClientePFDTO);
        return this.mapeadorCriacao
                .paraDto(this.repositorio.save(this.mapeadorCriacao.paraEntidade(criacaoClientePFDTO)));
    }

    public RespostaAtualizacaoClientePFDTO atualizar(
            final UUID id,
            final AtualizacaoClientePFDTO atualizacaoClientePFDTO) {
        this.acharEntidadePorId(id);
        final ClientePF clientePFAtualizado = this.mapeadorAtualizacao.paraEntidade(id, atualizacaoClientePFDTO);
        log.info("Atualizando cliente PF com id={}", id);
        return this.mapeadorAtualizacao.paraDto(this.repositorio.save(clientePFAtualizado));
    }

    public void remover(final UUID id) {
        final ClientePF clientePF = this.acharEntidadePorId(id);
        log.info("Removendo cliente PF com id={}", id);
        this.repositorio.delete(clientePF);
    }

    public RespostaSelecaoClientePFDTO acharPorId(final UUID id) {
        final ClientePF clientePF = this.acharEntidadePorId(id);
        log.info("Buscando cliente PF com id={}", id);
        return this.mapeadorSelecao.paraDto(clientePF);
    }

    public List<RespostaSelecaoClientePFDTO> listarTodos() {
        log.info("Listando todos os clientes PF");
        return this.repositorio
                .findAll()
                .stream()
                .map(this.mapeadorSelecao::paraDto)
                .collect(Collectors.toList());
    }

    ClientePF acharEntidadePorId(final UUID id) {
        return this.repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        CLIENTE_NAO_ENCONTRADO_MENSAGEM_ERRO));
    }
}
