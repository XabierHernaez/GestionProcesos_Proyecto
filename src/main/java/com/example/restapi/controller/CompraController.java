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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConciertoRepository conciertoRepository;

    // DTO para mapear CompraJPA a Compra del cliente
    public static class CompraDTO {
        private String email;
        private int conciertoId;
        private String tipoEntrada;
        private int cantidad;
        private double precioTotal;

        public CompraDTO(String email, int conciertoId, String tipoEntrada, int cantidad, double precioTotal) {
            this.email = email;
            this.conciertoId = conciertoId;
            this.tipoEntrada = tipoEntrada;
            this.cantidad = cantidad;
            this.precioTotal = precioTotal;
        }

        // Getters y setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getConciertoId() {
            return conciertoId;
        }

        public void setConciertoId(int conciertoId) {
            this.conciertoId = conciertoId;
        }

        public String getTipoEntrada() {
            return tipoEntrada;
        }

        public void setTipoEntrada(String tipoEntrada) {
            this.tipoEntrada = tipoEntrada;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public double getPrecioTotal() {
            return precioTotal;
        }

        public void setPrecioTotal(double precioTotal) {
            this.precioTotal = precioTotal;
        }
    }

    @PostMapping
    @Transactional
    public String realizarCompra(@RequestBody Map<String, Object> datosCompra) {
        String email = (String) datosCompra.get("email");
        int conciertoId = (int) datosCompra.get("conciertoId");
        String tipoEntrada = (String) datosCompra.get("tipoEntrada");
        int cantidad = (int) datosCompra.get("cantidad");

        System.out.println("Recibiendo compra: conciertoId=" + conciertoId + ", tipoEntrada=" + tipoEntrada + ", cantidad=" + cantidad);

        Optional<UsuarioJPA> usuarioOpt = usuarioRepository.findById(email);
        Optional<ConciertoJPA> conciertoOpt = conciertoRepository.findById(conciertoId);

        if (usuarioOpt.isEmpty() || conciertoOpt.isEmpty()) {
            return "Usuario o concierto no encontrado";
        }

        UsuarioJPA usuario = usuarioOpt.get();
        ConciertoJPA concierto = conciertoOpt.get();

        String tipoEntradaUpper = tipoEntrada.toUpperCase();
        double precioUnitario;
        switch (tipoEntradaUpper) {
            case "GENERAL":
                if (concierto.getCapacidadGeneral() < cantidad) {
                    return "No hay suficientes entradas generales disponibles";
                }
                concierto.setCapacidadGeneral(concierto.getCapacidadGeneral() - cantidad);
                precioUnitario = concierto.getPrecioGeneral();
                break;
            case "VIP":
                if (concierto.getCapacidadVIP() < cantidad) {
                    return "No hay suficientes entradas VIP disponibles";
                }
                concierto.setCapacidadVIP(concierto.getCapacidadVIP() - cantidad);
                precioUnitario = concierto.getPrecioVIP();
                break;
            case "PREMIUM":
                if (concierto.getCapacidadPremium() < cantidad) {
                    return "No hay suficientes entradas Premium disponibles";
                }
                concierto.setCapacidadPremium(concierto.getCapacidadPremium() - cantidad);
                precioUnitario = concierto.getPrecioPremium();
                break;
            default:
                return "Tipo de entrada inválido";
        }

        double precioTotal = precioUnitario * cantidad;
        CompraJPA compra = new CompraJPA();
        compra.setUsuario(usuario);
        compra.setConcierto(concierto);
        compra.setTipoEntrada(tipoEntradaUpper);
        compra.setCantidad(cantidad);
        compraRepository.save(compra);
        conciertoRepository.save(concierto);

        System.out.println("Compra guardada: conciertoId=" + conciertoId + ", tipoEntrada=" + tipoEntradaUpper + ", cantidad=" + cantidad);

        return "Compra realizada con éxito";
    }

    @GetMapping("/usuario")
    public List<CompraDTO> obtenerComprasPorUsuario(@RequestParam String email) {
        List<CompraJPA> compras = compraRepository.findByUsuarioEmail(email);
        return compras.stream().map(compra -> {
            double precioUnitario = getPrecioUnitario(compra.getConcierto(), compra.getTipoEntrada());
            return new CompraDTO(
                    compra.getUsuario().getEmail(),
                    compra.getConcierto().getId(),
                    compra.getTipoEntrada(),
                    compra.getCantidad(),
                    precioUnitario * compra.getCantidad()
            );
        }).collect(Collectors.toList());
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
            System.out.println("Consultando entradas vendidas: conciertoId=" + conciertoId + ", tipoEntrada=" + tipoEntradaUpper + ", entradasVendidas=" + entradasVendidas);
            return ResponseEntity.ok(entradasVendidas);
        } catch (Exception e) {
            System.err.println("Error en getEntradasVendidas: " + e.getMessage());
            return ResponseEntity.status(500).body(0);
        }
    }

    @GetMapping("/concierto/{id}")
    public ResponseEntity<List<CompraDTO>> obtenerComprasPorConcierto(@PathVariable("id") int conciertoId,
                                                                     @RequestParam String adminEmail) {
        try {
            System.out.println("Accediendo a compras para concierto: " + conciertoId + ", adminEmail: " + adminEmail);

            List<CompraJPA> compras = compraRepository.findByConciertoId(conciertoId);
            List<CompraDTO> comprasDTO = compras.stream().map(compra -> {
                double precioUnitario = getPrecioUnitario(compra.getConcierto(), compra.getTipoEntrada());
                return new CompraDTO(
                        compra.getUsuario().getEmail(),
                        compra.getConcierto().getId(),
                        compra.getTipoEntrada(),
                        compra.getCantidad(),
                        precioUnitario * compra.getCantidad()
                );
            }).collect(Collectors.toList());
            return ResponseEntity.ok(comprasDTO);
        } catch (Exception e) {
            System.err.println("Error en obtenerComprasPorConcierto: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    private double getPrecioUnitario(ConciertoJPA concierto, String tipoEntrada) {
        switch (tipoEntrada.toUpperCase()) {
            case "GENERAL":
                return concierto.getPrecioGeneral();
            case "VIP":
                return concierto.getPrecioVIP();
            case "PREMIUM":
                return concierto.getPrecioPremium();
            default:
                return 0.0;
        }
    }
}
