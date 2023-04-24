package com.co.uniquindio.persistencia.entidades;


import com.co.uniquindio.persistencia.enums.EnumCompra;
import com.co.uniquindio.persistencia.enums.EnumMedioPago;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal totalCompra;

    @Column(nullable = false)
    private EnumMedioPago medioPago;

    @Column(nullable = false)
    private EnumCompra estado;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private String numeroFactura;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "productos_compra",
            joinColumns = @JoinColumn(name = "id_compra", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_producto", nullable = false)
    )
    private List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.getCompras().add(this);
    }

    public void removerProducto(Producto producto) {
        productos.remove(producto);
        producto.getCompras().remove(this);
    }

}
