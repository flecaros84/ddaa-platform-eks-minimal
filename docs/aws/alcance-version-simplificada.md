# Alcance de la versión simplificada AWS/EKS

## Objetivo

Construir una variante reducida de DDAA Platform para demostrar despliegue en Kubernetes/EKS con una cadena funcional mínima:

```text
Frontend público → ddaa-service interno → SQL Server interno
```

## Justificación

El proyecto original incluye autenticación Google OAuth2, API Gateway, Eureka, Redis, RabbitMQ y servicio de notificaciones. Esa arquitectura funciona localmente, pero aumenta el riesgo del primer despliegue cloud.

La variante simplificada permite concentrarse en los puntos evaluables del despliegue:

- contenedorización;
- imágenes en ECR;
- servicios en Kubernetes;
- balanceador público para frontend;
- backend interno mediante ClusterIP;
- base de datos interna o administrada;
- variables y secretos;
- autoscaling;
- logs;
- pipeline CI/CD.

## Componentes incluidos

| Componente | Rol |
|---|---|
| frontend | Interfaz pública React/Vite servida por Nginx |
| ddaa-service | API REST Spring Boot para CRUD DDAA |
| sqlserver | Base SQL Server para prueba local |
| sqlserver-init | Inicialización local de base y usuario |

## Componentes excluidos temporalmente

| Componente | Motivo |
|---|---|
| auth-service | Se excluye para evitar OAuth/callbacks en la primera etapa EKS |
| api-gateway | Se reemplaza por proxy Nginx `/api` en el frontend |
| eureka-server | Kubernetes provee DNS interno por Service |
| redis | No es necesario para validar CRUD mínimo |
| rabbitmq | No es necesario para validar CRUD mínimo |
| notification-service | Depende de RabbitMQ y SMTP, se deja para extensión |

## Próximo paso

Crear manifiestos Kubernetes para:

- `frontend Deployment` + `Service LoadBalancer` o `Ingress`;
- `ddaa-service Deployment` + `Service ClusterIP`;
- `sqlserver Deployment/StatefulSet` + `PersistentVolumeClaim` + `Service ClusterIP`;
- `Secret` para password de SQL Server;
- `ConfigMap` para variables no sensibles;
- `HorizontalPodAutoscaler` para `frontend` y `ddaa-service`.
