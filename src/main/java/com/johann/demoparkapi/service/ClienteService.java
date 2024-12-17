package com.johann.demoparkapi.service;

import com.johann.demoparkapi.entity.Cliente;
import com.johann.demoparkapi.exception.CpfUniqueViolationException;
import com.johann.demoparkapi.exception.EntityNotFoundException;
import com.johann.demoparkapi.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;


    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new CpfUniqueViolationException(String.format("CPF: '%s' não pode ser cadastrado, pois já existe no sistema.", cliente.getCpf()));
        }
    }


    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente id %s não encontrado no sistema", id)));
    }
}
