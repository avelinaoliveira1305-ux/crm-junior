package br.com.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.crm.model.Lead;
import br.com.crm.service.LeadService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "*") // Permite requisicoes do frontend ou Postman
public class LeadController {

    @Autowired
    private LeadService leadService;

    // GET: Listar todos os leads -> http://localhost:8080/api/leads
    @GetMapping
    public List<Lead> listarTodos() {
        return leadService.listarTodos();
    }

    // GET: Buscar por ID -> http://localhost:8080/api/leads/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Lead> buscarPorId(@PathVariable Long id) {
        return leadService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Cadastrar novo lead
    @PostMapping
    public Lead salvar(@RequestBody Lead lead) {
        return leadService.salvar(lead);
    }

    // DELETE: Remover lead -> http://localhost:8080/api/leads/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        leadService.deletar(id);
        return ResponseEntity.noContent().build();
    }
 // PUT: Atualizar lead -> http://localhost:8080/api/leads/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Lead> atualizar(@PathVariable Long id, @RequestBody Lead lead) {
        try {
            Lead atualizado = leadService.atualizar(id, lead);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}