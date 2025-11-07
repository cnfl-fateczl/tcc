package com.gerencia_restaurante.adapters.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerencia_restaurante.application.port.in.CadastrarPedidoComanda;
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
    public PedidoComandaItens listaUmPedido(@PathVariable("id") Long id)
    {
        return pedidoComandaService.procurarPorId(id);
    }

    @PostMapping
    public PedidoComandaItens cadastrarPedidoComanda(@RequestBody CadastrarPedidoComanda dto)
    {
        return pedidoComandaService.salvar(dto);
    }
}
