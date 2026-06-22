# Despliegue AWS EKS - DDAA Platform Minimal

## 1. Contexto del despliegue

Este documento registra el proceso de despliegue en AWS EKS del proyecto mínimo `ddaa-platform-eks-minimal`.

Repositorio utilizado:

```text
https://github.com/flecaros84/ddaa-platform-eks-minimal
```

La versión desplegada corresponde a una arquitectura simplificada del sistema DDAA, preparada específicamente para cumplir el objetivo de orquestación, despliegue y automatización en AWS.

Esta versión excluye intencionalmente los siguientes componentes del proyecto completo:

* `auth-service`
* `api-gateway`
* `eureka-server`
* Redis
* RabbitMQ
* `notification-service`

La exclusión de estos componentes permite concentrar el despliegue en una arquitectura mínima funcional compuesta por frontend, backend y base de datos.

## 2. Arquitectura objetivo

La arquitectura objetivo en Kubernetes será la siguiente:

```text
Internet
   |
   v
Service frontend
type: LoadBalancer
   |
   v
Pod frontend / Nginx
Proxy /api hacia backend interno
   |
   v
Service ddaa-service
type: ClusterIP
   |
   v
Pod ddaa-service / Spring Boot
   |
   v
Service sqlserver
type: ClusterIP
   |
   v
Pod sqlserver + PVC
```

## 3. Decisiones de arquitectura

| Componente    | Tipo de exposición        | Justificación                                                                                                                      |
| ------------- | ------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Frontend      | `LoadBalancer`            | Es el único punto público de entrada a la aplicación. Permite acceder desde navegador mediante una URL pública generada por AWS.   |
| Backend       | `ClusterIP`               | El backend no se expone directamente a internet. Solo recibe tráfico interno desde el frontend mediante DNS interno de Kubernetes. |
| Base de datos | `ClusterIP`               | SQL Server queda aislado dentro del clúster. Solo el backend puede conectarse usando el nombre del servicio interno.               |
| Configuración | `ConfigMap`               | Se usará para variables no sensibles, como nombres de host, puertos o nombres de base de datos.                                    |
| Credenciales  | `Secret`                  | Se usará para contraseñas y datos sensibles, evitando dejarlos escritos directamente en los manifiestos principales.               |
| Persistencia  | `PersistentVolumeClaim`   | SQL Server requiere persistencia de datos. Se intentará usar PVC con la StorageClass disponible en EKS.                            |
| Autoscaling   | `HorizontalPodAutoscaler` | Se configurará HPA para demostrar escalamiento horizontal basado en métricas de CPU.                                               |

## 4. Servicios considerados

### 4.1 Frontend

Aplicación React/Vite servida mediante Nginx.

En local fue validada en:

```text
http://localhost:3000
```

En EKS será expuesta mediante:

```text
Service type: LoadBalancer
```

El frontend será responsable de enviar las peticiones `/api` hacia el backend interno.

### 4.2 Backend

Microservicio Spring Boot `ddaa-service`.

En local fue validado en:

```text
http://localhost:8082
```

Endpoints relevantes:

```text
GET /actuator/health
GET /api/ddaa
POST /api/ddaa
PUT /api/ddaa/{id}
DELETE /api/ddaa/{id}
```

En EKS será expuesto mediante:

```text
Service type: ClusterIP
```

### 4.3 Base de datos

Base de datos SQL Server ejecutada como contenedor dentro del clúster.

En EKS será expuesta mediante:

```text
Service type: ClusterIP
```

El backend se conectará usando DNS interno de Kubernetes:

```text
sqlserver:1433
```

## 5. Orden de implementación

El despliegue se realizará en el siguiente orden:

1. Preparación local del repositorio.
2. Creación de estructura documental y manifiestos Kubernetes.
3. Configuración del laboratorio AWS Academy.
4. Configuración de región, credenciales y herramientas CLI.
5. Creación del clúster EKS.
6. Configuración del node group.
7. Validación de conexión con `kubectl`.
8. Creación de repositorios ECR.
9. Construcción y publicación de imágenes Docker.
10. Despliegue de SQL Server.
11. Despliegue del backend `ddaa-service`.
12. Despliegue del frontend.
13. Validación funcional Frontend → Backend → Base de datos.
14. Configuración de HPA.
15. Revisión de logs y eventos.
16. Creación de pipeline GitHub Actions.
17. Validación de despliegue automatizado.
18. Registro de evidencias, problemas y soluciones.
19. Limpieza final de recursos AWS si corresponde.

## 6. Evidencias a recopilar

Durante el proceso se recopilarán evidencias de:

* clúster EKS creado;
* node group activo;
* nodos disponibles;
* repositorios ECR;
* imágenes publicadas;
* pods en estado `Running`;
* servicios Kubernetes creados;
* URL pública del frontend;
* backend respondiendo dentro del clúster;
* conexión backend → SQL Server;
* logs con `kubectl logs`;
* HPA creado;
* métricas de CPU;
* ejecución del pipeline GitHub Actions;
* redeploy exitoso;
* problemas encontrados y soluciones aplicadas.

## 7. Estado de avance

| Etapa                         | Estado      | Observación                                    |
| ----------------------------- | ----------- | ---------------------------------------------- |
| Rama `feature/aws-eks-deploy` | Completado  | Rama creada para el despliegue AWS/EKS.        |
| Documento de despliegue       | En progreso | Se crea este archivo como bitácora técnica.    |
| Estructura Kubernetes         | Pendiente   | Se crearán carpetas y manifiestos base.        |
| Clúster EKS                   | Pendiente   | Se creará en laboratorio AWS Academy.          |
| Repositorios ECR              | Pendiente   | Se crearán para frontend y backend.            |
| Despliegue Kubernetes         | Pendiente   | Se desplegarán SQL Server, backend y frontend. |
| HPA                           | Pendiente   | Se configurará autoscaling horizontal.         |
| Pipeline GitHub Actions       | Pendiente   | Se automatizará build, push y deploy.          |

## 8. Consideraciones del laboratorio AWS Academy

El despliegue se realizará dentro de un laboratorio AWS Academy / Learner Lab.

Por este motivo se considerarán las siguientes restricciones:

* usar la región activa del laboratorio;
* evitar recursos innecesarios;
* revisar límites de permisos IAM;
* no exponer credenciales en el repositorio;
* registrar los recursos creados;
* eliminar recursos al finalizar si corresponde;
* priorizar una versión funcional mínima antes de optimizaciones adicionales.

## 9. Preparación local del repositorio

### 9.1 Rama de trabajo

Se creó la rama de trabajo `feature/aws-eks-deploy` para concentrar los cambios asociados al despliegue en AWS EKS.

Esta rama incluye la documentación técnica, la estructura inicial de manifiestos Kubernetes, la configuración base para servicios, variables, secretos, persistencia y autoscaling.

### 9.2 Validación del repositorio

Antes de iniciar la implementación se validó el estado local del repositorio con los siguientes comandos:

```powershell
git status -sb
git branch --show-current
git remote -v
```

Se confirmó que el repositorio remoto corresponde a:

```text
https://github.com/flecaros84/ddaa-platform-eks-minimal.git
```

Posteriormente se creó la rama de trabajo:

```powershell
git switch -c feature/aws-eks-deploy
```

## 10. Estructura inicial de Kubernetes

Se creó la carpeta principal `k8s/` para almacenar los manifiestos Kubernetes del proyecto.

La estructura quedó organizada por componente:

```text
k8s/
  namespace.yaml
  configmap.yaml
  secret.example.yaml
  kustomization.yaml

  sqlserver/
    sqlserver-pvc.yaml
    sqlserver-deployment.yaml
    sqlserver-service.yaml

  ddaa-service/
    ddaa-service-deployment.yaml
    ddaa-service-service.yaml

  frontend/
    frontend-nginx-configmap.yaml
    frontend-deployment.yaml
    frontend-service.yaml

  hpa/
    ddaa-service-hpa.yaml
    frontend-hpa.yaml
```

Esta separación permite distinguir claramente los recursos comunes, la base de datos, el backend, el frontend y la configuración de autoscaling.

## 11. Manifiestos Kubernetes preparados

### 11.1 Namespace

Se definió un namespace propio para aislar los recursos del proyecto dentro del clúster:

```text
ddaa
```

Archivo asociado:

```text
k8s/namespace.yaml
```

### 11.2 ConfigMap

Se creó un `ConfigMap` para centralizar variables no sensibles utilizadas por el backend.

Archivo asociado:

```text
k8s/configmap.yaml
```

Variables consideradas:

```text
DDAA_DB_NAME
DDAA_DB_HOST
DDAA_DB_PORT
DDAA_DB_URL
DDAA_JPA_DDL_AUTO
DDAA_SQL_INIT_MODE
DDAA_SAMPLE_DATA_ENABLED
```

### 11.3 Secret de ejemplo

Se creó un archivo de ejemplo para documentar las credenciales requeridas por la aplicación.

Archivo asociado:

```text
k8s/secret.example.yaml
```

Este archivo no contiene credenciales reales y se mantiene solo como referencia. Las credenciales reales serán configuradas posteriormente mediante un `Secret` de Kubernetes creado en el entorno del laboratorio o mediante secretos seguros del pipeline.

### 11.4 Base de datos SQL Server

Se prepararon los manifiestos para desplegar SQL Server dentro del clúster.

Archivos asociados:

```text
k8s/sqlserver/sqlserver-pvc.yaml
k8s/sqlserver/sqlserver-deployment.yaml
k8s/sqlserver/sqlserver-service.yaml
```

La base de datos se configura como un servicio interno:

```text
Service type: ClusterIP
```

Esta decisión evita exponer la base de datos a internet. El backend se conectará a SQL Server mediante DNS interno de Kubernetes:

```text
sqlserver:1433
```

Además, se incluyó un `PersistentVolumeClaim` para permitir persistencia de datos:

```text
sqlserver-pvc
```

### 11.5 Backend ddaa-service

Se prepararon los manifiestos para desplegar el backend Spring Boot `ddaa-service`.

Archivos asociados:

```text
k8s/ddaa-service/ddaa-service-deployment.yaml
k8s/ddaa-service/ddaa-service-service.yaml
```

El backend se configura como un servicio interno:

```text
Service type: ClusterIP
```

Esta decisión impide el acceso directo desde internet. El backend solo recibirá tráfico desde el frontend a través de la red interna del clúster.

El endpoint usado para probes de Kubernetes es:

```text
/actuator/health
```

### 11.6 Frontend

Se prepararon los manifiestos para desplegar el frontend React/Vite servido por Nginx.

Archivos asociados:

```text
k8s/frontend/frontend-nginx-configmap.yaml
k8s/frontend/frontend-deployment.yaml
k8s/frontend/frontend-service.yaml
```

El frontend se configura como el único punto público de entrada:

```text
Service type: LoadBalancer
```

Además, se configuró Nginx para redirigir las peticiones `/api` hacia el backend interno:

```text
http://ddaa-service:8082/api/
```

Con esta configuración, el usuario accede públicamente al frontend y el frontend se comunica internamente con el backend.

### 11.7 Autoscaling HPA

Se prepararon los manifiestos de Horizontal Pod Autoscaler para frontend y backend.

Archivos asociados:

```text
k8s/hpa/ddaa-service-hpa.yaml
k8s/hpa/frontend-hpa.yaml
```

Configuración definida:

```text
minReplicas: 1
maxReplicas: 3
averageUtilization: 50
```

El umbral de CPU del 50% se definió para demostrar escalamiento horizontal ante carga moderada, de acuerdo con el requisito de autoscaling en EKS.

Los HPA serán aplicados una vez que el clúster cuente con Metrics Server operativo.

## 12. Validación local de manifiestos Kubernetes

Antes de crear recursos reales en AWS, se validó localmente la estructura de los manifiestos Kubernetes mediante Kustomize.

Comando utilizado:

```powershell
kubectl kustomize .\k8s
```

El comando renderizó correctamente los recursos definidos, incluyendo:

- `Namespace`;
- `ConfigMap` general;
- `ConfigMap` de Nginx para el frontend;
- `Service` interno `ClusterIP` para `ddaa-service`;
- `Service` público `LoadBalancer` para `frontend`;
- `Service` interno `ClusterIP` para `sqlserver`;
- `PersistentVolumeClaim` para SQL Server;
- `Deployment` para backend;
- `Deployment` para frontend;
- `Deployment` para SQL Server.

Esta validación confirmó que los manifiestos pueden ser procesados correctamente antes de aplicarlos en AWS EKS.

## 13. Herramientas locales validadas

### 13.1 Docker

Se validó Docker con el siguiente comando:

```powershell
docker --version
```

Resultado obtenido:

```text
Docker version 28.4.0
```

### 13.2 kubectl

Se validó `kubectl` con el siguiente comando:

```powershell
kubectl version --client
```

Resultado obtenido:

```text
Client Version: v1.34.1
Kustomize Version: v5.7.1
```

### 13.3 AWS CLI

Inicialmente AWS CLI no estaba disponible en el equipo local.

Posteriormente se instaló AWS CLI v2 para Windows y se validó con:

```powershell
aws --version
```

Resultado obtenido:

```text
aws-cli/2.35.9 Python/3.14.5 Windows/11 exe/AMD64
```

### 13.4 eksctl

Inicialmente `eksctl` no estaba disponible en el equipo local.

Se descargó el archivo:

```text
eksctl_Windows_amd64.zip
```

Luego se descomprimió y se validó el ejecutable directamente desde:

```text
C:\tools\eksctl\eksctl.exe
```

Comando utilizado:

```powershell
& C:\tools\eksctl\eksctl.exe version
```

Resultado obtenido:

```text
0.227.0
```

El ejecutable quedó validado correctamente para ser usado en la creación y administración del clúster EKS.