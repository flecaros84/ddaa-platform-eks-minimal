# DDAA Platform - Versión simplificada para AWS EKS

Esta versión reduce el proyecto original a una arquitectura mínima para demostrar despliegue de contenedores, comunicación Frontend → Backend → Base de datos y preparación posterior para Kubernetes/EKS.

## Alcance de esta versión

Incluye:

- `frontend`: React + Vite servido con Nginx.
- `ddaa-service`: Spring Boot con API REST para CRUD DDAA.
- `sqlserver`: base de datos SQL Server local en Docker Compose.
- `sqlserver-init`: contenedor auxiliar que crea la base `ddaa` y el usuario `ddaa_user`.

No incluye en esta variante:

- `api-gateway`.
- `auth-service`.
- OAuth Google.
- JWT.
- `eureka-server`.
- Redis.
- RabbitMQ.
- `notification-service`.

La arquitectura completa sigue perteneciendo al proyecto original. Esta variante existe para facilitar un despliegue EKS evaluable con menos piezas críticas.

## Arquitectura local

```text
Navegador
  ↓
http://localhost:3000
  ↓
frontend / Nginx
  ↓ proxy /api
http://ddaa-service:8082/api
  ↓
SQL Server / ddaa
```

## Requisitos locales

- Docker Desktop.
- Git.
- Opcional para desarrollo sin contenedores: Java 17, Maven, Node.js 22 o compatible.


> Nota: para la prueba local, el script `database/init/01-init-ddaa.sql` crea el usuario `ddaa_user` con la clave `DdaaUser2026!`. Si cambias `DDAA_DB_PASSWORD` en `.env`, actualiza también ese script o recrea la lógica de inicialización.

## Ejecución rápida con Docker Compose

Copia el archivo de variables:

```bash
cp .env.example .env
```

En Windows PowerShell puedes usar:

```powershell
Copy-Item .env.example .env
```

Levanta la plataforma:

```bash
docker compose up --build
```

URLs de prueba:

- Frontend: <http://localhost:3000>
- Backend directo: <http://localhost:8082/api/ddaa>
- Health backend: <http://localhost:8082/actuator/health>
- Swagger UI: <http://localhost:8082/swagger-ui.html>

## Prueba funcional esperada

1. Abrir <http://localhost:3000>.
2. Ver el listado inicial de derechos DDAA cargado por datos de muestra.
3. Crear un nuevo DDAA desde el formulario lateral.
4. Editar un registro.
5. Eliminar un registro.
6. Confirmar que el frontend consume `/api/ddaa` sin exponer directamente la base de datos.

## Comandos útiles

Ver logs del backend:

```bash
docker compose logs -f ddaa-service
```

Ver logs del frontend:

```bash
docker compose logs -f frontend
```

Reiniciar desde cero, borrando la base local:

```bash
docker compose down -v
```

Luego:

```bash
docker compose up --build
```

Probar API por consola:

```bash
curl http://localhost:8082/actuator/health
curl http://localhost:8082/api/ddaa
curl http://localhost:8082/api/ddaa/form-options
```

También puedes usar los scripts incluidos:

```powershell
./scripts/probar-local.ps1
```

```bash
./scripts/probar-local.sh
```

## Desarrollo local sin contenedor para el frontend

Levanta la base y backend con Docker Compose:

```bash
docker compose up --build sqlserver sqlserver-init ddaa-service
```

Luego entra al frontend:

```bash
cd frontend
npm install
npm run dev
```

Vite queda en <http://localhost:5173> y reenvía `/api` hacia `http://localhost:8082`.

## Notas para AWS/EKS

Esta versión queda preparada para el siguiente paso:

- Imagen `frontend` publicada en ECR.
- Imagen `ddaa-service` publicada en ECR.
- `frontend` expuesto por LoadBalancer o Ingress/ALB.
- `ddaa-service` como `ClusterIP` interno.
- `sqlserver` como `ClusterIP` interno para demo o reemplazable por RDS SQL Server.
- Passwords migrables a Kubernetes Secrets.
- Variables no sensibles migrables a ConfigMaps.

## Commit sugerido

```bash
git add .
git commit -m "feat: crea version simplificada para despliegue EKS"
```
