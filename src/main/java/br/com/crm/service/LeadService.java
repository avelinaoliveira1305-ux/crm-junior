package br.com.crm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.crm.model.Lead;
import br.com.crm.repository.LeadRepository;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    // Listar todos os leads
    public List<Lead> listarTodos() {
        return leadRepository.findAll();
    }

    // Buscar um lead por ID
    public Optional<Lead> buscarPorId(Long id) {
        return leadRepository.findById(id);
    }

    // Salvar ou atualizar lead
    public Lead salvar(Lead lead) {
        return leadRepository.save(lead);
    }
 // Atualizar lead existente (Update)
    public Lead atualizar(Long id, Lead leadAtualizado) {
        return leadRepository.findById(id).map(lead -> {
            lead.setNome(leadAtualizado.getNome());
            lead.setEmail(leadAtualizado.getEmail());
            lead.setTelefone(leadAtualizado.getTelefone());
            lead.setEmpresa(leadAtualizado.getEmpresa());
            lead.setStatus(leadAtualizado.getStatus());
            return leadRepository.save(lead);
        }).orElseThrow(() -> new RuntimeException("Lead não encontrado com o ID: " + id));
    }

    // Deletar lead por ID
    public void deletar(Long id) {
        leadRepository.deleteById(id);
    }
}