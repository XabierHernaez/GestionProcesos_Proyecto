package com.example.restapi.controller;

import com.example.restapi.server.jpa.CompraJPA;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.CompraRepository;
import com.example.restapi.server.repository.ConciertoRepository;
import com.example.restapi.server.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;
import java.util.Map;

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
    @Transactional
    public String realizarCompra(@RequestBody Map<String, Object> datosCompra) {
        String email = (String) datosCompra.get("email");
        int conciertoId = (int) datosCompra.get("conciertoId");
        String tipoEntrada = (String) datosCompra.get("tipoEntrada");
        int cantidad = (int) datosCompra.get("cantidad");

        // Log para depuración
        System.out.println("Recibiendo compra: conciertoId=" + conciertoId + ", tipoEntrada=" + tipoEntrada + ", cantidad=" + cantidad);

        Optional<UsuarioJPA> usuarioOpt = usuarioRepository.findById(email);
        Optional<ConciertoJPA> conciertoOpt = conciertoRepository.findById(conciertoId);

        if (usuarioOpt.isEmpty() || conciertoOpt.isEmpty()) {
            return "Usuario o concierto no encontrado";
        }

        UsuarioJPA usuario = usuarioOpt.get();
        ConciertoJPA concierto = conciertoOpt.get();

        // Verificar disponibilidad y actualizar capacidad
        String tipoEntradaUpper = tipoEntrada.toUpperCase();
        switch (tipoEntradaUpper) {
            case "GENERAL":
                if (concierto.getCapacidadGeneral() < cantidad) {
                    return "No hay suficientes entradas generales disponibles";
                }
                concierto.setCapacidadGeneral(concierto.getCapacidadGeneral() - cantidad);
                break;
            case "VIP":
                if (concierto.getCapacidadVIP() < cantidad) {
                    return "No hay suficientes entradas VIP disponibles";
                }
                concierto.setCapacidadVIP(concierto.getCapacidadVIP() - cantidad);
                break;
            case "PREMIUM":
                if (concierto.getCapacidadPremium() < cantidad) {
                    return "No hay suficientes entradas Premium disponibles";
                }
                concierto.setCapacidadPremium(concierto.getCapacidadPremium() - cantidad);
                break;
            default:
                return "Tipo de entrada inválido";
        }

        CompraJPA compra = new CompraJPA(usuario, concierto, tipoEntradaUpper, cantidad);
        compraRepository.save(compra);
        conciertoRepository.save(concierto);

        // Log para confirmar guardado
        System.out.println("Compra guardada: id=" + compra.getId() + ", conciertoId=" + conciertoId + ", tipoEntrada=" + tipoEntradaUpper + ", cantidad=" + cantidad);

        return "Compra realizada con éxito";
    }

    @GetMapping("/usuario")
    public List<CompraJPA> obtenerComprasPorUsuario(@RequestParam String email) {
        return compraRepository.findByUsuarioEmail(email);
    }

    @GetMapping("/concierto/{id}/vendidas/{tipo}")
    public ResponseEntity<Integer> getEntradasVendidas(@PathVariable("id") int conciertoId,
                                                      @PathVariable("tipo") String tipoEntrada) {
        try {
            String tipoEntradaUpper = tipoEntrada.toUpperCase();
            if (!tipoEntradaUpper.equals("GENERAL") && !tipoEntradaUpper.equals("VIP") && !tipoEntradaUpper.equals("PREMIUM")) {
                return ResponseEntity.badRequest().body(0);
            }
            int entradasVendidas = compraRepository.sumCantidadByConciertoIdAndTipoEntrada(conciertoId, tipoEntradaUpper);
            // Log detallado para depuración
            System.out.println("Consultando entradas vendidas: conciertoId=" + conciertoId + ", tipoEntrada=" + tipoEntradaUpper + ", entradasVendidas=" + entradasVendidas);
            return ResponseEntity.ok(entradasVendidas);
        } catch (Exception e) {
            System.err.println("Error en getEntradasVendidas: " + e.getMessage());
            return ResponseEntity.status(500).body(0);
        }
    }
}
