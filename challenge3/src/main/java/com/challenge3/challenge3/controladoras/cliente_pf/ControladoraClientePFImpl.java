package com.challenge3.challenge3.controladoras.cliente_pf;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.challenge3.challenge3.dtos.cliente_pf.AtualizacaoClientePFDTO;
import com.challenge3.challenge3.dtos.cliente_pf.CriacaoClientePFDTO;
import com.challenge3.challenge3.dtos.cliente_pf.respostas.RespostaAtualizacaoClientePFDTO;
import com.challenge3.challenge3.dtos.cliente_pf.respostas.RespostaCriacaoClientePFDTO;
import com.challenge3.challenge3.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;
import com.challenge3.challenge3.servicos.ServicoClientePF;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Validated
@RestController
@RequestMapping(value = "/clientes-pf", produces = "application/json")
@AllArgsConstructor
public class ControladoraClientePFImpl implements ControladoraClientePF {
    private static final String UUID_INVALIDO_MENSAGEM_ERRO = "UUID invalido: %s";
    private static final String CONFLITO_MENSAGEM_ERRO = "Ja existe um cliente PF para os dados fornecidos";

    private final ServicoClientePF servico;

    public ResponseEntity<List<RespostaSelecaoClientePFDTO>> listarTodos() {
        final List<RespostaSelecaoClientePFDTO> resposta = this.servico.listarTodos();
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    public ResponseEntity<RespostaSelecaoClientePFDTO> acharPorId(
            @PathVariable final String id) {
        try {
            final RespostaSelecaoClientePFDTO resposta = this.servico.acharPorId(UUID.fromString(id));
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(UUID_INVALIDO_MENSAGEM_ERRO, id));
        }
    }

    public void remover(@PathVariable final String id) {
        try {
            this.servico.remover(UUID.fromString(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(UUID_INVALIDO_MENSAGEM_ERRO, id));
        }
    }

    public ResponseEntity<RespostaAtualizacaoClientePFDTO> atualizar(
            @PathVariable final String id,
            @RequestBody @Valid final AtualizacaoClientePFDTO atualizacaoClientePFDTO) {
        try {
            final RespostaAtualizacaoClientePFDTO resposta = this.servico.atualizar(UUID.fromString(id),
                    atualizacaoClientePFDTO);
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CONFLITO_MENSAGEM_ERRO);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(UUID_INVALIDO_MENSAGEM_ERRO, id));
        }
    }

    public ResponseEntity<RespostaCriacaoClientePFDTO> criar(
            @RequestBody @Valid final CriacaoClientePFDTO criacaoClientePFDTO) {
        try {
            final RespostaCriacaoClientePFDTO resposta = this.servico.criar(criacaoClientePFDTO);
            return new ResponseEntity<>(resposta, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CONFLITO_MENSAGEM_ERRO);
        }
    }
}