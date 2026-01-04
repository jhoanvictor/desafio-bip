package com.bip.backend.rest;

import com.bip.backend.dto.BeneficioDTO;
import com.bip.model.Beneficio;
import com.bip.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "Endpoints para gestão benefícios")
public class BeneficioController {

    @Autowired
    private BeneficioService beneficioService;

    @Operation(summary = "Listar todos benefícios", description = "Retorna todos os benefícios cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de benefícios.")
    })
    @GetMapping
    public List<Beneficio> list() throws Exception {
        return beneficioService.listAll();
    }

    @Operation(summary = "Buscar um benefício", description = "Retorna o benefício procurado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista o benefício correspondente ao id."),
            @ApiResponse(responseCode = "400", description = "Benefício não existe.")
    })
    @GetMapping("{id}")
    public Beneficio getBeneficio(@PathVariable long id) throws Exception {
        return beneficioService.getBeneficio(id);
    }

    @Operation(summary = "Transferir valores", description = "Transfere um valor de um benefício para o outro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada."),
            @ApiResponse(responseCode = "400", description = "Erro ao trasnferir valor.")
    })
    @PostMapping("/transferir")
    public ResponseEntity transferirBeneficio(@RequestBody BeneficioDTO beneficioDTO){
        try {
            beneficioService.transfer(beneficioDTO.fromId(), beneficioDTO.toId(), beneficioDTO.amount());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Cadastrar benefício", description = "Cadastrada um novo benefício")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar, problema no EJB")
    })
    @PostMapping
    public ResponseEntity criarBeneficio(@RequestBody Beneficio beneficio){
        try {
            beneficio = beneficioService.save(0, beneficio);
            return new ResponseEntity<>(beneficio, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao tentar criar benefício: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @Operation(summary = "Atualizar benefício", description = "Atualiza um benefício")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício atualizado"),
            @ApiResponse(responseCode = "400", description = "Benefício não existe"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar, problema no EJB")
    })
    @PutMapping("/{id}")
    public ResponseEntity atualizaBeneficio(@PathVariable long id, @RequestBody Beneficio beneficio){
        try {
            beneficioService.save(id, beneficio);
            return new ResponseEntity<Beneficio>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Deletar benefício", description = "Deleta um benefício")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício deletado"),
            @ApiResponse(responseCode = "400", description = "Benefício não existe"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar, problema no EJB")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity removerBeneficio(@PathVariable long id){
        try {
            beneficioService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
