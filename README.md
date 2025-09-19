# Proyecto Java - Conexión a Oracle y Unwrap de sus Objetos wraped

Este proyecto en **Java 1.8** se cpnecta a una base de datos **Oracle** y 
realizar operaciones de *unwrap* sobre los objetos Wraped.

## 🚀 Funcionalidades

- Conexión a base de datos Oracle usando **JDBC**.
- Ejecución de consultas sobre la base de datos.
- Uso de 'unwrap()' para acceder a clases específicas de Oracle:
- Extracción de metadatos de la base de datos.

## 📋 Requisitos

- **Java JDK 1.8** o superior.
- **Maven 3.x** o superior.
- Driver JDBC de Oracle (ojdbc8.jar).
- Acceso a una base de datos Oracle (versión 11g, 12c o superior).

config.env
db.url=jdbc:oracle:thin:@//<host>:<puerto>/<service_name>
db.user=tu_usuario
db.password=tu_password

