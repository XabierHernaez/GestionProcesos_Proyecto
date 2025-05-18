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

/**
 * @class CompraController
 * @brief Controlador REST para gestionar las compras de entradas para conciertos.
 *
 * Permite registrar compras, obtener compras por usuario o concierto, y consultar entradas vendidas por tipo.
 */
@RestController
@RequestMapping("/compras")
public class CompraController {

    /**< Repositorio para gestionar las compras */
    @Autowired
    private CompraRepository compraRepository;

    /**< Repositorio para gestionar los usuarios */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**< Repositorio para gestionar los conciertos */
    @Autowired
    private ConciertoRepository conciertoRepository;

    /**
     * @class CompraDTO
     * @brief Clase DTO interna para representar una compra simplificada.
     */
    public static class CompraDTO {
        /**< Email del usuario */
        private String email;

        /**< ID del concierto */
        private int conciertoId;

        /**< Tipo de entrada (GENERAL, VIP, PREMIUM) */
        private String tipoEntrada;

        /**< Cantidad de entradas compradas */
        private int cantidad;

        /**< Precio total de la compra */
        private double precioTotal;

        /** Constructor */
        public CompraDTO(String email, int conciertoId, String tipoEntrada, int cantidad, double precioTotal) {
            this.email = email;
            this.conciertoId = conciertoId;
            this.tipoEntrada = tipoEntrada;
            this.cantidad = cantidad;
            this.precioTotal = precioTotal;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getConciertoId() { return conciertoId; }
        public void setConciertoId(int conciertoId) { this.conciertoId = conciertoId; }

        public String getTipoEntrada() { return tipoEntrada; }
        public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public double getPrecioTotal() { return precioTotal; }
        public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }
    }

    /**
     * @brief Registra una compra de entradas para un concierto.
     *
     * @param datosCompra Mapa con los datos de la compra (email, conciertoId, tipoEntrada, cantidad).
     * @return Mensaje indicando el resultado de la operación.
     */
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

    /**
     * @brief Obtiene las compras realizadas por un usuario.
     *
     * @param email Email del usuario.
     * @return Lista de objetos CompraDTO.
     */
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

    /**
     * @brief Obtiene la cantidad de entradas vendidas para un concierto y tipo de entrada.
     *
     * @param conciertoId ID del concierto.
     * @param tipoEntrada Tipo de entrada (GENERAL, VIP, PREMIUM).
     * @return Cantidad de entradas vendidas.
     */
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

    /**
     * @brief Obtiene las compras de un concierto específico para el administrador.
     *
     * @param conciertoId ID del concierto.
     * @param adminEmail Email del administrador solicitante.
     * @return Lista de objetos CompraDTO correspondientes al concierto.
     */
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

    /**
     * @brief Calcula el precio unitario según el tipo de entrada del concierto.
     *
     * @param concierto Objeto ConciertoJPA.
     * @param tipoEntrada Tipo de entrada (GENERAL, VIP, PREMIUM).
     * @return Precio unitario de la entrada.
     */
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