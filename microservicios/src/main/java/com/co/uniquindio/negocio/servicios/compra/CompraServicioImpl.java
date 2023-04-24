package com.co.uniquindio.negocio.servicios.compra;

import com.co.uniquindio.negocio.respuestas.*;
import com.co.uniquindio.persistencia.dto.CompraDTO;
import com.co.uniquindio.persistencia.dto.ProductoDTO;
import com.co.uniquindio.persistencia.dto.UsuarioDTO;
import com.co.uniquindio.persistencia.entidades.Compra;
import com.co.uniquindio.persistencia.entidades.Producto;
import com.co.uniquindio.persistencia.entidades.Usuario;
import com.co.uniquindio.persistencia.enums.EnumCompra;
import com.co.uniquindio.persistencia.repositorios.CompraRepo;
import com.co.uniquindio.persistencia.repositorios.ProductoRepo;
import com.co.uniquindio.persistencia.repositorios.UsuarioRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class CompraServicioImpl implements CompraServicio {

    private final CompraRepo compraRepo;
    private final UsuarioRepo usuarioRepo;

    private final ProductoRepo productoRepo;

    public CompraServicioImpl(CompraRepo compraRepo, UsuarioRepo usuarioRepo, ProductoRepo productoRepo) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    @Override
    public CancelarCompraRespuesta cancelarCompra(Integer id) throws Exception {
        Optional<Compra> compra = compraRepo.findById(id);
        if (compra.get().getEstado().equals(EnumCompra.CANCELADO)) {
            throw new Exception("La compra ya se encuentra cancelada");
        }

        compra.get().setEstado(EnumCompra.CANCELADO);

        compraRepo.save(compra.get());

        return CancelarCompraRespuesta.builder()
                .correoUsuario(compra.get().getUsuario().getCorreo())
                .totalCompra(compra.get().getTotalCompra())
                .numeroFactura(compra.get().getNumeroFactura())
                .estado(compra.get().getEstado())
                .fecha(compra.get().getFecha())
                .build();
    }

    @Override
    public EstadoCompraRespuesta estadoCompra(Integer id) throws Exception {
        Optional<Compra> compra = compraRepo.findById(id);

        if (compra.isEmpty()) {
            throw new Exception("La compra no existe");
        }

        return EstadoCompraRespuesta.builder()
                .estado(compra.get().getEstado())
                .build();
    }

    @Override
    public CrearCompraRespuesta crearCompra(CompraDTO compra) throws Exception {

        CrearCompraRespuesta crearCompraRespuesta;

        Date fechaActual = Date.from(Instant.now());
        String numeroFactura = String.valueOf(UUID.randomUUID()).replace("-", "");
        Usuario usuario = verificarUsuario(compra.getUsuario());
        List<Producto> listaProductos = verificarProductos(compra.getProductos());

        Compra guardarCompra = Compra.builder()
                .totalCompra(calcularTotalCompra(compra.getProductos()))
                .medioPago(compra.getMedioPago())
                .estado(EnumCompra.EN_PROCESO)
                .fecha(fechaActual)
                .numeroFactura(numeroFactura)
                .productos(listaProductos)
                .usuario(usuario)
                .build();

        compraRepo.save(guardarCompra);

        crearCompraRespuesta = CrearCompraRespuesta.builder()
                .correoUsuario(usuario.getCorreo())
                .numeroFactura(guardarCompra.getNumeroFactura())
                .totalCompra(guardarCompra.getTotalCompra())
                .estado(guardarCompra.getEstado())
                .fecha(guardarCompra.getFecha())
                .productos(compra.getProductos())
                .build();

        actualizarStock(compra.getProductos());

        return crearCompraRespuesta;
    }

    @Override
    public List<HistorialCompraRespuesta> historialCompras(String correo) throws Exception {
        Optional<List<Compra>> compras = compraRepo.historialCompras(correo);
        List<HistorialCompraRespuesta> historialCompras = new ArrayList<>();

        System.out.println(compras.get().size());
        for (int i = 0; i < compras.get().size(); i++) {
            HistorialCompraRespuesta historialCompraRespuesta = HistorialCompraRespuesta.builder()
                    .totalCompra(compras.get().get(i).getTotalCompra())
                    .medioPago(compras.get().get(i).getMedioPago())
                    .estado(compras.get().get(i).getEstado())
                    .fecha(compras.get().get(i).getFecha())
                    .numeroFactura(compras.get().get(i).getNumeroFactura())
                    .correoUsuario(compras.get().get(i).getUsuario().getCorreo())
                    .productos(compras.get().get(i).getProductos())
                    .build();
            historialCompras.add(historialCompraRespuesta);
        }
        return historialCompras;
    }

    @Override
    public DetalleCompraRespuesta detalleCompra(String numeroFactura) throws Exception {
        Optional<Compra> compra = compraRepo.detalleCompra(numeroFactura);

        return DetalleCompraRespuesta.builder()
                .totalCompra(compra.get().getTotalCompra())
                .medioPago(compra.get().getMedioPago())
                .estado(compra.get().getEstado())
                .fecha(compra.get().getFecha())
                .numeroFactura(compra.get().getNumeroFactura())
                .correoUsuario(compra.get().getUsuario().getCorreo())
                .productos(compra.get().getProductos())
                .build();

    }

    private Usuario verificarUsuario(UsuarioDTO usuarioDTO) throws Exception {
        Optional<Usuario> usuarioRegistrado = usuarioRepo.findByCorreo(usuarioDTO.getCorreo());
        if (usuarioRegistrado.isPresent()) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioDTO.getId());
            usuario.setCorreo(usuarioDTO.getCorreo());
            return usuario;
        } else {
            throw new Exception("El usuario no se encuentra registrado");
        }
    }

    private List<Producto> verificarProductos(List<ProductoDTO> productos) throws Exception {

        List<Producto> listaProductos = new ArrayList<>();

        for (ProductoDTO productoDTO : productos) {
            Optional<Producto> productoDisponible = productoRepo.findById(productoDTO.getId());

            if (productoDisponible.orElseThrow(() -> new Exception("Producto no existe"))
                    .getStock() > productoDTO.getCantidadCompra()) {
                Producto producto = new Producto();
                producto.setId(productoDTO.getId());
                producto.setNombre(productoDTO.getNombre());
                producto.setReferencia(productoDTO.getReferencia());
                producto.setPrecio(productoDTO.getPrecio());

                listaProductos.add(producto);
            } else {
                throw new Exception("No se encuentra en inventario la cantidad de productos que desea comprar");
            }

        }
        return listaProductos;
    }

    private void actualizarStock(List<ProductoDTO> productoDTOS) {
        for (ProductoDTO productoDTO : productoDTOS) {
            Optional<Producto> producto = productoRepo.findById(productoDTO.getId());

            int stockActual = producto.get().getStock();
            int compraProducto = productoDTO.getCantidadCompra();
            producto.get().setStock(stockActual - compraProducto);

            Producto productoActualizar = new Producto(
                    producto.get().getId(),
                    producto.get().getReferencia(),
                    producto.get().getNombre(),
                    producto.get().getPrecio(),
                    producto.get().getStock(),
                    producto.get().getCompras()
            );
            productoRepo.save(productoActualizar);
        }
    }

    private BigDecimal calcularTotalCompra(List<ProductoDTO> productoDTOS) {
        BigDecimal totalCompra = new BigDecimal(0);
        for (ProductoDTO productoDTO : productoDTOS) {
            Optional<Producto> producto = productoRepo.findById(productoDTO.getId());

            BigDecimal precio = producto.get().getPrecio();
            BigDecimal cantidadCompra = BigDecimal.valueOf(productoDTO.getCantidadCompra());

            totalCompra = totalCompra.add(precio.multiply(cantidadCompra));

        }

        return totalCompra;
    }

}
