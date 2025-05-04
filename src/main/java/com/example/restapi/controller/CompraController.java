package com.example.restapi.controller;

import com.example.restapi.server.jpa.CompraJPA;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.CompraRepository;
import com.example.restapi.server.repository.ConciertoRepository;
import com.example.restapi.server.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConciertoRepository conciertoRepository;

    @PostMapping
    public String realizarCompra(
            @RequestParam String email,
            @RequestParam int conciertoId,
            @RequestParam String tipoEntrada,
            @RequestParam int cantidad
    ) {
        // Buscar usuario y concierto
        Optional<UsuarioJPA> usuarioOpt = usuarioRepository.findById(email);
        Optional<ConciertoJPA> conciertoOpt = conciertoRepository.findById(conciertoId);

        if (usuarioOpt.isEmpty() || conciertoOpt.isEmpty()) {
            return "Usuario o concierto no encontrado";
        }

        UsuarioJPA usuario = usuarioOpt.get();
        ConciertoJPA concierto = conciertoOpt.get();

        // Validar capacidad según tipo de entrada
        switch (tipoEntrada.toUpperCase()) {
            case "GENERAL":
                if (concierto.getCapacidadGeneral() < cantidad)
                    return "No hay suficientes entradas generales disponibles";
                concierto.setCapacidadGeneral(concierto.getCapacidadGeneral() - cantidad);
                break;
            case "VIP":
                if (concierto.getCapacidadVIP() < cantidad)
                    return "No hay suficientes entradas VIP disponibles";
                concierto.setCapacidadVIP(concierto.getCapacidadVIP() - cantidad);
                break;
            case "PREMIUM":
                if (concierto.getCapacidadPremium() < cantidad)
                    return "No hay suficientes entradas Premium disponibles";
                concierto.setCapacidadPremium(concierto.getCapacidadPremium() - cantidad);
                break;
            default:
                return "Tipo de entrada inválido";
        }

        // Guardar la compra y actualizar el concierto
        CompraJPA compra = new CompraJPA(usuario, concierto, tipoEntrada.toUpperCase(), cantidad);
        compraRepository.save(compra);
        conciertoRepository.save(concierto);

        return "Compra realizada con éxito";
    }
    
}
