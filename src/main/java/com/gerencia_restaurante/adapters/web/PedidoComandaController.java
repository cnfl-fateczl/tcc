package com.gerencia_restaurante.adapters.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerencia_restaurante.application.port.in.CadastrarItemPedido;
import com.gerencia_restaurante.application.port.in.CadastrarPedidoComanda;
import com.gerencia_restaurante.application.port.out.ItemPedidoSaida;
import com.gerencia_restaurante.application.port.out.PedidoComandaItens;
import com.gerencia_restaurante.application.port.out.PedidoComandaResumo;
import com.gerencia_restaurante.application.service.PedidoComandaService;

@RestController
@RequestMapping("/comanda")
@CrossOrigin("*")
public class PedidoComandaController
{
    @Autowired
    private PedidoComandaService pedidoComandaService;

    @GetMapping
    public List<PedidoComandaResumo> listarTodos()
    {
        return pedidoComandaService.procurarTodos();
    }

    @GetMapping("/{id}")
    public PedidoComandaResumo listaUmPedido(@PathVariable("id") Long id)
    {
        return pedidoComandaService.procurarPorId(id);
    }

    @GetMapping("/{id}/itens")
    public PedidoComandaItens listarItensDoPedido(@PathVariable("id") Long id)
    {
        return pedidoComandaService.listarItensPedido(id);
    }

    @PostMapping
    public PedidoComandaItens cadastrarPedidoComanda(@RequestBody CadastrarPedidoComanda dto)
    {
        return pedidoComandaService.salvar(dto);
    }

    @PostMapping("/{id}/itens")
    public ItemPedidoSaida adicionarItemAoPedido(@PathVariable("id") Long id, @RequestBody CadastrarItemPedido dto)
    {
        return pedidoComandaService.adicionarItem(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirPedido(@PathVariable("id") Long id)
    {
        pedidoComandaService.apagarPorId(id);
    }

    @DeleteMapping("{id}/itens/{numeroItem}")
    public void excluirItemPedido(@PathVariable("id") Long id, @PathVariable("numeroItem") Integer numeroItem)
    {
        pedidoComandaService.removerItem(id, numeroItem);
    }
}
