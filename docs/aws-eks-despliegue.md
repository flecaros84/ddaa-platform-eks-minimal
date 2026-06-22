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

La arquitectura objetivo en AWS EKS será la siguiente:

```text
Internet
   |
   v
Load Balancer de AWS creado por Kubernetes
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

A nivel de infraestructura AWS, el clúster se desplegará sobre una VPC nueva dedicada al proyecto, sin utilizar la VPC default del laboratorio.

La red considera:

```text
VPC propia para DDAA
2 zonas de disponibilidad
2 subredes públicas
2 subredes privadas
Node group administrado de EKS
```

En esta implementación, y para ajustarse al entorno AWS Academy usado en clases, el node group se ubicará en subredes públicas con asignación automática de IP pública. Las subredes privadas se mantienen como parte de la arquitectura de red solicitada, pero los servicios internos se aislarán principalmente mediante primitivas de Kubernetes:

```text
frontend: LoadBalancer
backend: ClusterIP
sqlserver: ClusterIP
```

De esta forma, solamente el frontend queda accesible desde internet. El backend y la base de datos no se exponen públicamente.

## 3. Decisiones de arquitectura

| Componente | Configuración | Justificación |
|---|---|---|
| VPC | VPC nueva dedicada | Se evita usar la VPC default del laboratorio y se mantiene una red propia para el despliegue EKS. |
| Zonas de disponibilidad | 2 AZ | Permite distribuir la infraestructura base del clúster en más de una zona. |
| Subredes públicas | 2 subredes | Se usan para los nodos worker y para permitir la creación del balanceador de carga del frontend. |
| Subredes privadas | 2 subredes | Se crean como parte de la arquitectura solicitada, aunque en esta versión mínima los nodos se ubican en subredes públicas para evitar dependencia de NAT Gateway. |
| NAT Gateway | Deshabilitado | Se omite para reducir complejidad y costos en el laboratorio AWS Academy. |
| Node group | Managed Node Group público | Los nodos worker quedan en subredes públicas, siguiendo el enfoque práctico usado en el laboratorio de clases. |
| Frontend | `Service type: LoadBalancer` | Es el único punto público de entrada a la aplicación. Kubernetes solicita a AWS la creación del balanceador. |
| Backend | `Service type: ClusterIP` | El backend no se expone directamente a internet. Solo recibe tráfico interno desde el frontend. |
| Base de datos | `Service type: ClusterIP` | SQL Server queda aislado dentro del clúster. Solo el backend debe conectarse a la base de datos. |
| Configuración | `ConfigMap` | Se usa para variables no sensibles, como host, puerto y nombre de base de datos. |
| Credenciales | `Secret` | Se usa para contraseñas y datos sensibles. No se almacenan credenciales reales en el repositorio. |
| Persistencia | `PersistentVolumeClaim` | SQL Server requiere persistencia de datos. Se define PVC para la base de datos. |
| Autoscaling | `HorizontalPodAutoscaler` | Se utilizará HPA para demostrar escalamiento horizontal basado en CPU. |
| Load Balancer Controller | No utilizado | Para compatibilidad con AWS Academy, se evita instalar AWS Load Balancer Controller y no se usan anotaciones avanzadas ALB/NLB. |

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
4. Configuración de credenciales temporales, región y herramientas CLI.
5. Definición declarativa del clúster EKS con `eksctl`.
6. Creación de VPC dedicada, subredes públicas, subredes privadas y clúster EKS.
7. Creación del node group administrado.
8. Validación de conexión con `kubectl`.
9. Validación de nodos worker y pods del sistema.
10. Creación de repositorios ECR.
11. Construcción y publicación de imágenes Docker.
12. Ajuste de imágenes ECR en los manifiestos Kubernetes.
13. Creación del Secret real en Kubernetes.
14. Despliegue de SQL Server.
15. Despliegue del backend `ddaa-service`.
16. Despliegue del frontend.
17. Validación funcional Frontend → Backend → Base de datos.
18. Configuración de HPA.
19. Revisión de logs y eventos.
20. Creación de pipeline GitHub Actions.
21. Validación de despliegue automatizado.
22. Registro de evidencias, problemas y soluciones.
23. Limpieza final de recursos AWS si corresponde.

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

- usar la región activa del laboratorio;
- evitar recursos innecesarios;
- revisar límites de permisos IAM;
- no exponer credenciales en el repositorio;
- registrar los recursos creados;
- eliminar recursos al finalizar si corresponde;
- priorizar una versión funcional mínima antes de optimizaciones adicionales.

Además, se adoptan las siguientes decisiones específicas para compatibilidad con el entorno de clases:

- no usar la VPC default como arquitectura final;
- crear una VPC dedicada para el clúster EKS;
- crear subredes públicas y privadas en dos zonas de disponibilidad;
- ubicar los nodos worker en subredes públicas para evitar dependencia de NAT Gateway;
- exponer solo el frontend mediante `Service type: LoadBalancer`;
- mantener backend y base de datos como `ClusterIP`;
- no instalar AWS Load Balancer Controller;
- no usar anotaciones avanzadas ALB/NLB en el manifiesto del frontend;
- usar un `Service type: LoadBalancer` simple para que AWS Academy aprovisione el balanceador compatible con el laboratorio.

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

## 14. Configuración de credenciales temporales AWS Academy

El despliegue se realiza usando credenciales temporales entregadas por AWS Academy Learner Lab.

Para pruebas locales se creó un script PowerShell no versionado en Git:

```text
scripts/aws-lab-env.ps1
```

Este archivo carga las variables de entorno necesarias para la sesión local:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
AWS_DEFAULT_REGION
```

Las credenciales no se almacenan en los manifiestos Kubernetes ni en el repositorio.

La región configurada para el laboratorio fue:

```text
us-east-1
```

La identidad AWS se validó con el siguiente comando:

```powershell
aws sts get-caller-identity
```

Resultado general:

```text
Account: 409321960537
Rol asumido: voclabs
Región: us-east-1
```

## 15. Revisión inicial de recursos AWS y criterio de red

Antes de crear el clúster EKS se revisó el estado inicial de AWS.

### 15.1 Clústeres EKS existentes

Comando utilizado:

```powershell
aws eks list-clusters --region $env:AWS_DEFAULT_REGION
```

Resultado:

```text
No existían clústeres EKS creados previamente.
```

### 15.2 VPC default detectada

Comando utilizado:

```powershell
aws ec2 describe-vpcs --region $env:AWS_DEFAULT_REGION --query "Vpcs[*].{VpcId:VpcId,Default:IsDefault,Cidr:CidrBlock}" --output table
```

Resultado relevante:

```text
VPC ID: vpc-0a762d3b7098b54c2
CIDR: 172.31.0.0/16
Default: True
```

Aunque existía una VPC default en el laboratorio, se decidió no usarla como arquitectura final del proyecto. La pauta y el enfoque trabajado en clases requerían demostrar configuración de red, por lo que se optó por crear una VPC dedicada para el clúster EKS.

### 15.3 Subredes default detectadas

Comando utilizado:

```powershell
aws ec2 describe-subnets --region $env:AWS_DEFAULT_REGION --query "Subnets[*].{SubnetId:SubnetId,VpcId:VpcId,AZ:AvailabilityZone,Cidr:CidrBlock,PublicIp:MapPublicIpOnLaunch}" --output table
```

La revisión mostró subredes asociadas a la VPC default. Estas subredes no fueron usadas para el despliegue final del clúster.

### 15.4 Roles IAM disponibles

Comando utilizado:

```powershell
aws iam list-roles --query "Roles[?contains(RoleName, 'LabRole') || contains(RoleName, 'voclabs') || contains(RoleName, 'eks')].[RoleName,Arn]" --output table
```

Resultado relevante:

```text
LabRole: arn:aws:iam::409321960537:role/LabRole
voclabs: arn:aws:iam::409321960537:role/voclabs
```

Se revisó el rol `LabRole` y se confirmó que podía ser usado dentro del laboratorio para la creación del clúster y del node group administrado.

### 15.5 Criterio aplicado

La revisión inicial permitió definir el siguiente criterio:

```text
No usar VPC default.
Crear VPC dedicada para EKS.
Usar dos zonas de disponibilidad.
Crear subredes públicas y privadas.
Ubicar node group en subredes públicas.
Exponer solo el frontend con LoadBalancer.
Mantener backend y base de datos como servicios internos ClusterIP.
```

## 16. Creación del clúster Amazon EKS

Para el despliegue se creó un clúster Amazon EKS llamado `ddaa-eks` en la región `us-east-1`.

La creación se realizó mediante `eksctl`, usando un archivo declarativo versionado en el repositorio:

```text
aws/eks-cluster.yaml
```

El objetivo fue mantener la configuración como infraestructura declarativa, alineada con el enfoque DevOps de trabajar con archivos versionables y reutilizables.

### 16.1 Archivo de configuración del clúster

Archivo:

```text
aws/eks-cluster.yaml
```

Configuración utilizada:

```yaml
apiVersion: eksctl.io/v1alpha5
kind: ClusterConfig

metadata:
  # Cluster EKS para el despliegue minimo del proyecto DDAA.
  name: ddaa-eks
  region: us-east-1
  version: "1.34"

availabilityZones:
  - us-east-1c
  - us-east-1d

iam:
  # Rol existente del AWS Academy Learner Lab.
  serviceRoleARN: arn:aws:iam::409321960537:role/LabRole

vpc:
  # VPC nueva y dedicada para el cluster.
  # No se usa la VPC default del laboratorio.
  cidr: "10.20.0.0/16"

  # Se deshabilita NAT para ajustarse al laboratorio AWS Academy.
  # Los nodos worker se ubicaran en subredes publicas.
  nat:
    gateway: Disable

  clusterEndpoints:
    publicAccess: true
    privateAccess: true

managedNodeGroups:
  - name: ng-ddaa-public

    # En clases se usa t3.medium/t3.large segun disponibilidad.
    # Partimos con t3.medium para cuidar recursos del laboratorio.
    instanceType: t3.medium

    # Node group en subredes publicas, alineado con el laboratorio.
    privateNetworking: false

    desiredCapacity: 2
    minSize: 1
    maxSize: 3

    volumeSize: 20

    iam:
      # Rol existente para los nodos EC2 del node group.
      instanceRoleARN: arn:aws:iam::409321960537:role/LabRole
```

### 16.2 Validación previa con dry-run

Antes de crear recursos reales en AWS, se validó la configuración con:

```powershell
eksctl create cluster -f .\aws\eks-cluster.yaml --dry-run
```

El `dry-run` confirmó que la configuración era válida y que se crearían los recursos principales del clúster:

```text
Cluster: ddaa-eks
Region: us-east-1
Kubernetes version: 1.34
Node group: ng-ddaa-public
Instance type: t3.medium
Desired capacity: 2
Min size: 1
Max size: 3
VPC CIDR: 10.20.0.0/16
NAT: Disable
Endpoint public access: true
Endpoint private access: true
```

### 16.3 Creación real del clúster

Comando utilizado:

```powershell
eksctl create cluster -f .\aws\eks-cluster.yaml
```

Durante la creación, `eksctl` generó una VPC dedicada para el clúster y distribuyó subredes en dos zonas de disponibilidad:

```text
us-east-1c
us-east-1d
```

La salida de creación informó las siguientes subredes por zona:

```text
us-east-1c:
  public:  10.20.0.0/19
  private: 10.20.64.0/19

us-east-1d:
  public:  10.20.32.0/19
  private: 10.20.96.0/19
```

El proceso creó el plano de control de EKS, los addons base y el node group administrado.

Addons instalados durante la creación:

```text
vpc-cni
kube-proxy
coredns
metrics-server
```

La instalación de `metrics-server` es relevante porque posteriormente permitirá configurar y validar el `HorizontalPodAutoscaler`.

### 16.4 Resultado de creación del clúster

La creación finalizó correctamente con el siguiente resultado:

```text
EKS cluster "ddaa-eks" in "us-east-1" region is ready
```

También se guardó automáticamente la configuración local de acceso a Kubernetes:

```text
C:\Users\fabia\.kube\config
```

## 17. Validación del clúster EKS

### 17.1 Validación de nodos worker

Comando utilizado:

```powershell
kubectl get nodes -o wide
```

Resultado:

```text
NAME                           STATUS   ROLES    VERSION               INTERNAL-IP    EXTERNAL-IP
ip-10-20-37-208.ec2.internal   Ready    <none>   v1.34.9-eks-93b80c6   10.20.37.208   52.54.65.248
ip-10-20-8-184.ec2.internal    Ready    <none>   v1.34.9-eks-93b80c6   10.20.8.184    34.207.246.79
```

Se confirma que el node group quedó operativo con dos nodos worker en estado `Ready`.

### 17.2 Validación de pods del sistema

Comando utilizado:

```powershell
kubectl get pods -A
```

Resultado relevante:

```text
NAMESPACE     NAME                              READY   STATUS
kube-system   aws-node-ctr4x                    2/2     Running
kube-system   aws-node-gs5jg                    2/2     Running
kube-system   coredns-6976d5bf49-2fdmv          1/1     Running
kube-system   coredns-6976d5bf49-f5w8x          1/1     Running
kube-system   kube-proxy-fsdg5                  1/1     Running
kube-system   kube-proxy-jd5fg                  1/1     Running
kube-system   metrics-server-774f4c6dff-jwzmq   1/1     Running
kube-system   metrics-server-774f4c6dff-wg97f   1/1     Running
```

Todos los componentes base del clúster quedaron en estado `Running`.

### 17.3 Validación del estado del clúster

Comando utilizado:

```powershell
aws eks describe-cluster --region $env:AWS_DEFAULT_REGION --name ddaa-eks --query "cluster.{Name:name,Status:status,Version:version,VpcId:resourcesVpcConfig.vpcId,Subnets:resourcesVpcConfig.subnetIds,EndpointPublic:resourcesVpcConfig.endpointPublicAccess,EndpointPrivate:resourcesVpcConfig.endpointPrivateAccess}" --output table
```

Resultado:

```text
Name: ddaa-eks
Status: ACTIVE
Version: 1.34
VpcId: vpc-065f299d3e15d128c
EndpointPublic: True
EndpointPrivate: True
```

Subredes asociadas al clúster:

```text
subnet-095c5236fb40333fb
subnet-07a545f69394dc62e
subnet-09ed6d05f1a4d0396
subnet-0f19516a98de6a1b3
```

## 18. Validación de VPC y subredes creadas

Se validaron las subredes asociadas al clúster con el siguiente comando:

```powershell
aws ec2 describe-subnets `
  --subnet-ids subnet-095c5236fb40333fb subnet-07a545f69394dc62e subnet-09ed6d05f1a4d0396 subnet-0f19516a98de6a1b3 `
  --query "Subnets[*].{SubnetId:SubnetId,VpcId:VpcId,AZ:AvailabilityZone,Cidr:CidrBlock,PublicIp:MapPublicIpOnLaunch}" `
  --output table
```

Resultado:

```text
VPC: vpc-065f299d3e15d128c

Subred pública:
  SubnetId: subnet-095c5236fb40333fb
  AZ: us-east-1c
  CIDR: 10.20.0.0/19
  PublicIp: True

Subred pública:
  SubnetId: subnet-07a545f69394dc62e
  AZ: us-east-1d
  CIDR: 10.20.32.0/19
  PublicIp: True

Subred privada:
  SubnetId: subnet-09ed6d05f1a4d0396
  AZ: us-east-1c
  CIDR: 10.20.64.0/19
  PublicIp: False

Subred privada:
  SubnetId: subnet-0f19516a98de6a1b3
  AZ: us-east-1d
  CIDR: 10.20.96.0/19
  PublicIp: False
```

La VPC creada cumple con la estructura solicitada para el laboratorio:

```text
2 zonas de disponibilidad
2 subredes públicas
2 subredes privadas
```

Las subredes públicas tienen asignación automática de IP pública habilitada, por lo que pueden alojar los nodos worker y permitir la creación del balanceador de carga del frontend.

Las subredes privadas quedan creadas como parte de la arquitectura de red, aunque en esta versión mínima del laboratorio el node group se ubicó en las subredes públicas para evitar dependencia de NAT Gateway.

## 19. Validación del node group

Se validó el grupo de nodos administrado con el siguiente comando:

```powershell
aws eks describe-nodegroup `
  --region $env:AWS_DEFAULT_REGION `
  --cluster-name ddaa-eks `
  --nodegroup-name ng-ddaa-public `
  --query "nodegroup.{Name:nodegroupName,Status:status,InstanceTypes:instanceTypes,CapacityType:capacityType,Min:scalingConfig.minSize,Desired:scalingConfig.desiredSize,Max:scalingConfig.maxSize,Subnets:subnets}" `
  --output table
```

Resultado:

```text
Name: ng-ddaa-public
Status: ACTIVE
CapacityType: ON_DEMAND
InstanceType: t3.medium
Desired: 2
Min: 1
Max: 3
```

Subredes usadas por el node group:

```text
subnet-095c5236fb40333fb
subnet-07a545f69394dc62e
```

Estas corresponden a las subredes públicas creadas en la VPC del clúster.

## 20. Justificación de arquitectura de red usada

La arquitectura se adaptó al laboratorio AWS Academy y al material trabajado en clases.

Se utilizó una VPC nueva dedicada al clúster EKS, sin usar la VPC default del laboratorio.

El clúster se creó en dos zonas de disponibilidad para mejorar disponibilidad:

```text
us-east-1c
us-east-1d
```

Se crearon dos subredes públicas y dos subredes privadas. El node group se configuró en las subredes públicas, ya que el laboratorio usado en clases considera nodos worker con IP pública automática para asegurar conectividad con el control plane y servicios AWS.

A nivel de Kubernetes, la separación de exposición se realizará mediante tipos de `Service`:

```text
frontend: LoadBalancer
backend: ClusterIP
sqlserver: ClusterIP
```

Esto permite que solamente el frontend quede expuesto públicamente mediante un balanceador de carga de AWS, mientras que el backend y la base de datos quedan accesibles únicamente dentro del clúster.

No se utilizarán anotaciones avanzadas ALB/NLB ni AWS Load Balancer Controller en esta etapa. Para mantener compatibilidad con las restricciones del entorno AWS Academy, el frontend se expondrá con un `Service type: LoadBalancer` simple.