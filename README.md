# Sistema de Gestión de Alquileres de Propiedades Temporales

Este proyecto es una API RESTful desarrollada en Java utilizando Spring Boot, diseñada para gestionar propiedades temporales para alquiler. Implementa las funcionalidades de registro, edición, eliminación lógica, arrendamiento y listado de propiedades, siguiendo principios de arquitectura limpia (Hexagonal).

---

## Funcionalidades principales

- **Registro de Propiedades**: Registra propiedades con campos como nombre, ubicación, disponibilidad, URL de imagen y precio.
- **Listado de Propiedades**: Filtra propiedades disponibles según un rango de precio definido por el usuario.
- **Edición de Propiedades**: Permite modificar información de las propiedades con validaciones específicas.
- **Eliminación Lógica**: Realiza un borrado lógico de las propiedades según reglas de negocio.
- **Arrendamiento de Propiedades**: Cambia el estado de la propiedad al ser arrendada, para que no se muestre como disponible.

---

## Requisitos de instalación

1. **Java**: Java 21 o superior.
2. **Maven**: Versión 3.8 o superior.
3. **Base de Datos**: PostgreSQL configurado con:
   - Usuario: `prueba`
   - Contraseña: `pruebapass`
4. **Herramientas adicionales**:
   - Postman (para probar la API).
   - Git (para clonar el repositorio).

---

## Configuración inicial

### Clonar el repositorio
```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DEL_PROYECTO>

### Configuración de la base de datos
Crea la base de datos en PostgreSQL: 

sql
Copy code
CREATE DATABASE rental_management;
Configura las credenciales de acceso en el archivo application.properties:

properties
Copy code
spring.datasource.url=jdbc:postgresql://localhost:5432/rental_management
spring.datasource.username=prueba
spring.datasource.password=pruebapass
Construir el proyecto
bash
Copy code
mvn clean install
Ejecución
Inicia el servidor:
bash
Copy code
mvn spring-boot:run
La API estará disponible en el puerto 8181 (http://localhost:8181).
Endpoints principales
Autenticación
Login: POST /auth/login
Request:
json
Copy code
{
  "username": "tu_usuario",
  "password": "tu_contraseña"
}
Response:
json
Copy code
{
  "jwt": "token_generado"
}
Gestión de Propiedades
Registrar una propiedad
Endpoint: POST /properties
Request:
json
Copy code
{
  "name": "Casa Bonita",
  "location": "Bogota",
  "available": true,
  "imageUrl": "https://example.com/image.jpg",
  "price": 3000000
}
Listar propiedades
Endpoint: GET /properties
Opcional: Filtrar por precio
http
Copy code
GET /properties?minPrice=1000000&maxPrice=5000000
Response:
json
Copy code
{
  "status": "La solicitud fue exitosa",
  "properties": [
    {
      "id": 1,
      "name": "Casa Bonita",
      "location": "Bogota",
      "price": 3000000,
      "available": true
    }
  ]
}
Editar propiedad
Endpoint: PUT /properties/{id}
Request:
json
Copy code
{
  "name": "Casa Hermosa",
  "location": "Bogota",
  "available": true,
  "imageUrl": "https://example.com/image_new.jpg",
  "price": 3500000
}
Eliminar propiedad
Endpoint: DELETE /properties/{id}
Response:
json
Copy code
{
  "message": "Propiedad eliminada lógicamente"
}
Arrendar propiedad
Endpoint: POST /properties/{id}/rent
Response:
json
Copy code
{
  "id": 1,
  "name": "Casa Bonita",
  "available": false
}

