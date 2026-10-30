package com.foodxever.config;

import com.foodxever.model.*;
import com.foodxever.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;

/** Carga datos de demostración solo cuando las tablas correspondientes están vacías. */
@Component
public class FoodxEverDataInitializer implements ApplicationRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final ReservaRepository reservaRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final HistorialReservaRepository historialRepository;
    private final HorarioRecojoRepository horarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FoodxEverDataInitializer(RolRepository rolRepository,
                                    UsuarioRepository usuarioRepository,
                                    EstablecimientoRepository establecimientoRepository,
                                    CategoriaRepository categoriaRepository,
                                    ProductoRepository productoRepository,
                                    ReservaRepository reservaRepository,
                                    MetodoPagoRepository metodoPagoRepository,
                                    HistorialReservaRepository historialRepository,
                                    HorarioRecojoRepository horarioRepository,
                                    PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.reservaRepository = reservaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.historialRepository = historialRepository;
        this.horarioRepository = horarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        crearRoles();
        crearCategorias();
        crearUsuarioDemo();
        crearEstablecimientos();
        crearProductos();
        crearMetodosPago();
        crearReservaDemo();
        crearHistorialSiFalta();
        crearHorarios();
    }

    private void crearRoles() {
        if (rolRepository.count() > 0) return;
        rolRepository.saveAll(List.of(
                new Rol("CONSUMIDOR", "Persona que reserva productos"),
                new Rol("ESTABLECIMIENTO", "Gestiona ofertas de un establecimiento"),
                new Rol("ADMIN", "Administración de FoodxEver")
        ));
    }

    private void crearCategorias() {
        if (categoriaRepository.count() > 0) return;
        categoriaRepository.saveAll(List.of(
                new Categoria("Panadería", "Panadería", "Panes, bollos y packs de panadería"),
                new Categoria("Bollería", "Panadería", "Pasteles y productos de bollería"),
                new Categoria("Postres", "Cafetería", "Postres preparados durante el día"),
                new Categoria("Bebidas", "Cafetería", "Bebidas y complementos"),
                new Categoria("Sándwiches", "Cafetería", "Sándwiches y combos de cafetería"),
                new Categoria("Pizza", "Restaurante", "Pizzas del día"),
                new Categoria("Menú", "Restaurante", "Menús preparados"),
                new Categoria("Frutas y verduras", "Market", "Productos frescos"),
                new Categoria("Abarrotes", "Market", "Productos empacados y abarrotes")
        ));
    }

    private void crearUsuarioDemo() {
        if (usuarioRepository.count() > 0) return;
        Rol consumidor = rolRepository.findByNombreIgnoreCase("CONSUMIDOR")
                .orElseThrow(() -> new IllegalStateException("No se encontró el rol CONSUMIDOR"));
        Usuario demo = new Usuario(null, "Usuario Demo", "demo@foodxever.com",
                passwordEncoder.encode("123456"), "CONSUMIDOR", null);
        demo.setRolRef(consumidor);
        usuarioRepository.save(demo);
    }

    private void crearEstablecimientos() {
        if (establecimientoRepository.count() > 0) return;
        establecimientoRepository.saveAll(List.of(
                new Establecimiento(null, "Panadería San Miguel", "San Miguel, Lima", "Panadería", "987654321", "sanmiguel@foodxever.com"),
                new Establecimiento(null, "Café Central", "Pueblo Libre, Lima", "Cafetería", "987654322", "cafecentral@foodxever.com"),
                new Establecimiento(null, "Pizza House", "Magdalena del Mar, Lima", "Restaurante", "987654323", "pizzahouse@foodxever.com"),
                new Establecimiento(null, "Market La Esquina", "Callao, Callao", "Market", "987654324", "market@foodxever.com")
        ));
    }

    private void crearProductos() {
        if (productoRepository.count() > 0) return;
        agregarProducto("Pack sorpresa de panadería", "Panadería", "Panadería San Miguel", 8.90, 22.00, 4,
                "Pack con productos disponibles al finalizar el día.", "7:00 PM - 9:00 PM", "/img/panaderia.jpg");
        agregarProducto("Pack de postres", "Postres", "Café Central", 12.00, 24.00, 2,
                "Selección de postres preparados durante el día.", "8:00 PM - 9:30 PM", "/img/postres.jpg");
        agregarProducto("Pizza del día", "Pizza", "Pizza House", 16.90, 30.00, 5,
                "Pizza disponible antes del cierre del establecimiento.", "9:00 PM - 10:00 PM", "/img/pizza.jpg");
        agregarProducto("Pack de frutas y verduras", "Frutas y verduras", "Market La Esquina", 10.50, 23.00, 7,
                "Selección de frutas y verduras disponibles durante el día.", "6:00 PM - 8:00 PM", "/img/frutas-verduras.jpg");
        agregarProducto("Menú ejecutivo", "Menú", "Pizza House", 13.90, 24.00, 3,
                "Menú preparado durante el servicio de almuerzo.", "4:00 PM - 5:30 PM", "/img/menu-ejecutivo.jpg");
        agregarProducto("Sándwich + café", "Sándwiches", "Café Central", 7.90, 16.00, 3,
                "Sándwich acompañado de café disponible antes del cierre.", "5:30 PM - 7:30 PM", "/img/sandwich-cafe.jpg");
    }

    private void agregarProducto(String nombre, String categoriaNombre, String establecimientoNombre,
                                 double precio, double precioOriginal, int stock, String descripcion,
                                 String horario, String imagen) {
        Categoria categoria = categoriaRepository.findByNombreIgnoreCase(categoriaNombre)
                .orElseThrow(() -> new IllegalStateException("Categoría no encontrada: " + categoriaNombre));
        Establecimiento establecimiento = establecimientoRepository.findByNombreIgnoreCase(establecimientoNombre)
                .orElseThrow(() -> new IllegalStateException("Establecimiento no encontrado: " + establecimientoNombre));
        Producto producto = new Producto(null, nombre, categoriaNombre, precio, precioOriginal, stock,
                descripcion, horario, true, imagen);
        producto.setCategoriaRef(categoria);
        producto.setEstablecimientoRef(establecimiento);
        productoRepository.save(producto);
    }

    private void crearMetodosPago() {
        if (metodoPagoRepository.count() > 0) return;
        metodoPagoRepository.saveAll(List.of(
                new MetodoPago("Efectivo"),
                new MetodoPago("Tarjeta"),
                new MetodoPago("Yape / Plin")
        ));
    }

    private void crearReservaDemo() {
        if (reservaRepository.count() > 0) return;
        Producto producto = productoRepository.findAllByOrderByIdAsc().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay productos para la reserva de demostración"));
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase("demo@foodxever.com").orElse(null);
        Reserva reserva = new Reserva(null, producto.getId(), producto.getNombre(),
                "Cliente Demo", 1, producto.getPrecio(), "Pendiente");
        reserva.vincularProducto(producto);
        if (usuario != null) reserva.setUsuarioRef(usuario);
        reservaRepository.save(reserva);
    }

    private void crearHistorialSiFalta() {
        if (historialRepository.count() > 0) return;
        for (Reserva reserva : reservaRepository.findAll()) {
            historialRepository.save(new HistorialReserva(reserva, null,
                    reserva.getEstado(), "Registro inicial del historial"));
        }
    }

    private void crearHorarios() {
        if (horarioRepository.count() > 0) return;
        List<String> dias = List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        for (Establecimiento establecimiento : establecimientoRepository.findAllByOrderByIdAsc()) {
            for (String dia : dias) {
                HorarioRecojo horario = new HorarioRecojo();
                horario.setEstablecimiento(establecimiento);
                horario.setDiaSemana(dia);
                horario.setHoraInicio(LocalTime.of(16, 0));
                horario.setHoraFin(LocalTime.of(22, 0));
                horario.setActivo(true);
                horarioRepository.save(horario);
            }
        }
    }
}
