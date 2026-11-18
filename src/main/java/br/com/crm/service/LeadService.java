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
 // Relatório consolidado para acompanhamento do funil de vendas
    public java.util.Map<String, Object> gerarRelatorio() {
        java.util.List<br.com.crm.model.Lead> leads = leadRepository.findAll();
        
        long totalLeads = leads.size();
        
        java.util.Map<String, Long> leadsPorStatus = leads.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        l -> (l.getStatus() != null && !l.getStatus().isBlank()) ? l.getStatus().toUpperCase() : "NOVO",
                        java.util.stream.Collectors.counting()
                ));

        long concluidos = leadsPorStatus.getOrDefault("CONCLUIDO", 0L) + leadsPorStatus.getOrDefault("GANHO", 0L);
        long emAndamento = totalLeads - concluidos - leadsPorStatus.getOrDefault("PERDIDO", 0L);

        java.util.Map<String, Object> relatorio = new java.util.HashMap<>();
        relatorio.put("totalLeads", totalLeads);
        relatorio.put("emAndamento", emAndamento);
        relatorio.put("concluidos", concluidos);
        relatorio.put("distribuicaoStatus", leadsPorStatus);
        
        return relatorio;
    }
}