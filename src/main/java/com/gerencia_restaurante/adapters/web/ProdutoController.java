package com.gerencia_restaurante.adapters.web;

import com.gerencia_restaurante.application.mapper.ProdutoMapper;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.service.ProdutoService;
import com.gerencia_restaurante.domain.entity.Produto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoMapper produtoMapper;

    @GetMapping
    public String listaProdutos(Model model) {
        model.addAttribute("listaProdutos", produtoService.procurarTodos());
        return "produto/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizarProduto dto;
        if (id != null) {
            Produto produto = produtoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
            dto = produtoMapper.toAtualizarProduto(produto);
        } else {
            dto = new AtualizarProduto(null, "", "", "", new BigDecimal("0.00"));
        }
        model.addAttribute("produto", dto);
        return "produto/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("produto") @Valid AtualizarProduto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            return "produto/formulario";
        }
        try {
            Produto produtoSalvo = produtoService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Produto '" + produtoSalvo.getNome() + "' atualizado com sucesso!"
                    : "Produto '" + produtoSalvo.getNome() + "' criado com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/produto";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/produto/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteProduto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "Produto " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/produto";
    }

    @PutMapping
    @Transactional
    public String atualizar (AtualizarProduto dados) {
        produtoService.salvarOuAtualizar(dados);
        return "redirect:marca";
    }
}

