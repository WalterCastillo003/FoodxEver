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

## Configuración local en Windows / PowerShell

1. Instala PostgreSQL y pgAdmin 4.
2. En pgAdmin, crea una base de datos llamada `foodxever`.
3. Abre PowerShell en la carpeta raíz del proyecto (la que contiene `mvnw.cmd`). Ejecuta estas variables; reemplaza la contraseña por la que configuraste durante la instalación:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/foodxever"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "TU_CONTRASENA_LOCAL"
```

4. Inicia la aplicación:

```powershell
.\mvnw.cmd spring-boot:run
```

5. Abre `http://localhost:8080`. En el primer inicio se crearán las tablas y se agregarán datos de demostración únicamente cuando las tablas de catálogo estén vacías. Los datos guardados a partir de ahí se conservan en PostgreSQL.

Cuenta de demostración creada en una base nueva:
- Correo: `demo@foodxever.com`
- Contraseña: `123456`

No uses esa contraseña en un sistema real. Las cuentas nuevas guardan la contraseña con BCrypt.

## Variables del servicio de aplicación en Railway

Añade un servicio PostgreSQL al mismo proyecto de Railway y configura en el servicio web estas variables usando el selector de referencias de Railway. Sustituye `Postgres` por el nombre real del servicio de base de datos si tiene otro nombre.

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}
```

No guardes estas credenciales en `application.properties`, GitHub ni en capturas públicas. La aplicación usa `PORT` de Railway para el puerto web.

## Consultas y listas en cascada

- `/consultas`: primer listado DTO une productos, categorías y establecimientos; segundo listado DTO une reservas, productos y establecimientos. El filtro por establecimiento aplica a ambas consultas.
- `/productos/nuevo` y `/productos/editar/{id}`: elegir establecimiento actualiza la lista de categorías compatibles.
- `/reservas/nueva` y `/reservas/editar/{id}`: elegir establecimiento actualiza la lista de productos disponibles.

## Importante para este avance

`spring.jpa.hibernate.ddl-auto=update` facilita el desarrollo académico y crea las tablas faltantes sin borrar registros existentes. Antes de una entrega definitiva, conserva una copia de seguridad de la base de datos y no cambies esa propiedad a `create` o `create-drop`, porque podrían borrar datos.


## Módulos añadidos: pagos y reseñas

- `http://localhost:8080/pagos`: CRUD de pagos vinculados a una reserva y a un método de pago. El importe se calcula del lado del servidor a partir del total de la reserva. Permite guardar más de un registro por reserva para representar intentos, pagos o devoluciones.
- `http://localhost:8080/resenas`: CRUD de reseñas vinculadas a un usuario y a un establecimiento; la reserva es opcional. La calificación se valida entre 1 y 5.

Ambos módulos utilizan servicios y repositorios Spring Data JPA, por lo que los datos se guardan en PostgreSQL. La tabla `historial_reservas` es de auditoría y se genera desde los cambios de estado de reservas; no conviene ofrecer edición manual de sus registros históricos.

## Administración de las tablas del modelo

Se ha incorporado un panel administrativo protegido por sesión y rol `ADMIN`:

- `/administracion`: panel principal.
- `/administracion/usuarios`: CRUD de cuentas. Las contraseñas se guardan con BCrypt y nunca se muestran en las tablas ni en los formularios de edición.
- `/administracion/roles`: CRUD de roles personalizados. Los roles base `ADMIN`, `CONSUMIDOR` y `ESTABLECIMIENTO` están protegidos porque son necesarios para el funcionamiento de la aplicación. Un rol asignado a usuarios no se puede renombrar ni eliminar.
- `/administracion/categorias`: CRUD de categorías compatibles con los tipos de establecimiento. Las categorías asignadas a productos no se pueden eliminar ni desactivar.
- `/administracion/metodos-pago`: CRUD de métodos de pago. Los métodos utilizados por pagos existentes no se pueden eliminar ni desactivar.
- `/administracion/horarios`: CRUD de horarios de recojo por establecimiento.
- `/administracion/historial`: consulta de solo lectura del historial de reservas. Esta tabla es una bitácora automática, por lo que no permite edición o eliminación manual.

De esta manera, diez tablas tienen operaciones CRUD desde la aplicación: `roles`, `usuarios`, `establecimientos`, `categorias`, `productos`, `reservas`, `metodos_pago`, `pagos`, `resenas` y `horarios_recojo`. La tabla restante, `historial_reservas`, conserva su propósito de auditoría y solo se consulta.

### Crear el primer administrador en local

El proyecto no contiene una contraseña administrativa fija. Para crear el primer administrador local, abre PowerShell en la carpeta que contiene `mvnw.cmd` y configura variables en esa misma terminal antes de arrancar Spring Boot:

```powershell
$env:FOODXEVER_ADMIN_EMAIL = 'admin@foodxever.com'
$securePassword = Read-Host "Contraseña inicial del administrador (mínimo 10 caracteres)" -AsSecureString
$env:FOODXEVER_ADMIN_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
.\mvnw.cmd spring-boot:run
```

El comando solicita la contraseña sin mostrarla mientras la escribes. Usa una clave privada de al menos 10 caracteres; no la compartas ni la guardes en un archivo que se suba a GitHub. En el primer arranque, si ese correo todavía no existe, se crea la cuenta con rol `ADMIN` y contraseña cifrada mediante BCrypt. Si el correo ya existe, el inicializador no modifica esa cuenta.

Para crear el administrador remoto, configura también `FOODXEVER_ADMIN_EMAIL` y `FOODXEVER_ADMIN_PASSWORD` como variables del **servicio web** en Railway (no en PostgreSQL) y aplica el despliegue. La base local y la base de Railway son independientes, por lo que el administrador se crea por separado en cada una. Si el correo elegido ya existe en la base remota, utiliza un correo administrativo distinto.

Una vez creada la cuenta, inicia sesión en `/login`. El menú **Administración** aparecerá solo para cuentas con rol `ADMIN`. Desde Usuarios puedes actualizar la contraseña del administrador; al editarla, deja el campo de contraseña vacío para conservar la actual o escribe una nueva de al menos seis caracteres.
