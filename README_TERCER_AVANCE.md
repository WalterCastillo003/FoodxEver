# FoodxEver — guía de base de datos para el tercer avance

## Tecnología

- Java 25.
- Spring Boot + Spring Data JPA/Hibernate.
- PostgreSQL en local y Railway.
- Thymeleaf y Bootstrap se mantienen como vistas de la aplicación.
- Las credenciales se leen de variables de entorno; no se deben subir contraseñas al repositorio.

## Tablas del modelo (11)

1. `roles`: roles permitidos de las cuentas.
2. `usuarios`: datos de cuenta; la contraseña se guarda con hash BCrypt.
3. `establecimientos`: locales que publican productos.
4. `categorias`: categorías válidas y tipo de establecimiento al que pertenecen.
5. `productos`: ofertas, precio, stock, imagen, categoría y establecimiento.
6. `reservas`: cliente, producto reservado, cantidad, total, estado y fecha.
7. `metodos_pago`: catálogo de medios de pago.
8. `pagos`: pagos asociados a una reserva y a un método de pago.
9. `resenas`: calificación de un establecimiento por un usuario.
10. `historial_reservas`: historial de cambios de estado de cada reserva.
11. `horarios_recojo`: horario semanal por establecimiento.

Hibernate crea estas tablas y las relaciones a partir de las entidades JPA. Se usan claves foráneas para enlazar usuarios con roles, productos con categorías y establecimientos, reservas con productos y usuarios, pagos con reservas y métodos de pago, reseñas con usuarios/establecimientos y el historial con reservas.