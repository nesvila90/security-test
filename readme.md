# Proyecto de Seguridad con Spring Boot

Este proyecto es una aplicación de seguridad desarrollada con Spring Boot, utilizando JPA para la persistencia de datos, un banco de datos en memoria H2, autenticación JWT, y otras buenas prácticas de desarrollo de software. La aplicación está diseñada para ser construida y probada fácilmente utilizando Gradle.

## Requisitos


[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

- **Java 21**
- **Gradle 7.0 o superior**
- **Git**

## Características

- **Banco de datos en memoria:** H2.
- **Proceso de build:** Gradle.
- **Persistencia con JPA:** Hibernate.
- **Framework:** Spring Boot.
- **Autenticación:** JWT.
- **Documentación:** Swagger.
- **Buenas prácticas y patrones de diseño.**

## Instalación

1. **Clona el repositorio:**

```bash
git clone https://github.com/nesvila90/security-test
```

2. **Navega al directorio del proyecto:**

```bash
cd security-test
```

3. **Construye el proyecto:**

```bash
./gradlew clean build
```

4. **Ejecuta la aplicación:**

```bash
./gradlew bootRun
```

## Probar la Aplicación

### Base de Datos en Memoria

La aplicación utiliza una base de datos en memoria H2 que se inicializa automáticamente al arrancar la aplicación. No se necesita ningún script adicional para crear la base de datos.

### Acceso a H2 Console

La consola H2 está disponible en:

```
http://localhost:8080/h2-console
```

Configura la consola H2 con los siguientes parámetros:

- **JDBC URL:** `jdbc:h2:mem:testdb` o tomalo de la consola
- **User Name:** `sa`
- **Password:** (deja en blanco)

### Documentación Swagger

La documentación Swagger está disponible en:

```
http://localhost:8080/swagger-ui.html
```

### Endpoints Principales

- **Registrar Usuario:**

```
POST /api/users/register
```

- **Autenticar Usuario:**

```
POST /api/auth/login
```
### Diseño de la solución

 -- **Diagramas C4**
 -- ** Diagrama de Sequencia**

## Diagramas C4

 Diagrama de Contexto - Nivel 1
![mermaid-diagram-2024-07-30-125119](https://github.com/user-attachments/assets/cc37e91f-bd8e-46a7-a065-0dca6f1625cc)

Diagrama de Contenedores - Nivel 2
![mermaid-diagram-2024-07-30-130132](https://github.com/user-attachments/assets/106332c6-9dc4-4c87-8fcb-1fd12f192fd5)


Diagrama de Componentes - Nivel 1
![mermaid-diagram-2024-07-30-130137](https://github.com/user-attachments/assets/9e930a8e-e04a-44ec-86a7-dd9714a5cfb3)

## Diagrama de Sequencia
Aspectos a modo global - Partiendo desde el diseño de la solución.
![mermaid-diagram-2024-07-30-130208](https://github.com/user-attachments/assets/f3cd8d71-1191-4f41-a1b4-6042bd2ef92b)


## Estructura del Proyecto

- **Controladores:** Manejan las solicitudes HTTP entrantes y devuelven las respuestas adecuadas.
- **Servicios:** Contienen la lógica de negocio de la aplicación.
- **Repositorios:** Interactúan con la base de datos utilizando JPA.
- **Entidades:** Representan las tablas de la base de datos.
- **DTOs:** Objetos de Transferencia de Datos para encapsular los datos enviados entre el cliente y el servidor.
- **Configuración:** Configuración de seguridad, JWT, y otros parámetros de la aplicación.

## Diagrama de la Solución

![Diagrama de la Solución](diagrama.png)

(Nota: Adjunta tu diagrama de la solución aquí)

## Patrones de Diseño y Buenas Prácticas

- **Inyección de Dependencias:** Utilizando anotaciones de Spring como `@Autowired`, `@Service`, `@Repository`, y `@Controller`.
- **DTO (Data Transfer Object):** Para transferir datos entre las capas de la aplicación.
- **Mapper:** Utilizando MapStruct para mapear entre entidades y DTOs.
- **Singleton:** Para la configuración de JWT y otros componentes de configuración.

## Ejemplos de Código

### Controlador de Usuario

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }
}
```

### Servicio de Autenticación

```java
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserAuthenticatedResponseDTO authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtTokenProvider.generateToken(authentication);
        var principal = (UserDetailsImpl) authentication.getPrincipal();
        return new UserAuthenticatedResponseDTO(jwt, principal.getId(), principal.getUsername(), principal.getEmail(), new ArrayList<>());
    }
}
```

### Configuración JWT

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwt = getJwtFromRequest(request);

        if (jwt != null && tokenProvider.validateToken(jwt)) {
            String username = tokenProvider.getUserIdFromJWT(jwt);

            UserDetails userDetails = userService.loadUserByUsername(username);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

## Repositorio Público

El código fuente completo está disponible en el siguiente repositorio público:

[GitHub - nesvila90/security](https://github.com/nesvila90/security-test)

## Contacto

Si tienes alguna pregunta o problema, no dudes en comunicarte con nosotros.

---
