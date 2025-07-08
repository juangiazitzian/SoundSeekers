# 🎧 SoundSeekers

SoundSeekers es una plataforma web orientada a la promoción de artistas emergentes y la difusión de eventos musicales. Nace como respuesta a la falta de una solución centralizada que permita descubrir fácilmente conciertos de nuevos talentos, en un contexto donde la demanda por propuestas culturales accesibles no para de crecer.

A partir de entrevistas y encuestas realizadas a distintos perfiles de usuarios (asistentes, artistas y organizadores), se identificó un interés marcado en los espectáculos de músicos independientes y una necesidad concreta de visibilización para quienes inician su carrera.

SoundSeekers propone un modelo que permite a los artistas publicar y promocionar sus eventos de forma gratuita, financiando la plataforma mediante publicidad segmentada. El desarrollo se plantea en fases, comenzando con un Producto Mínimo Viable (MVP) previsto para el cuarto trimestre de 2024.

En su hoja de ruta se incluyen funcionalidades avanzadas como:

Filtros personalizados según gustos musicales y ubicación

Notificaciones automáticas sobre nuevos eventos

Integración con tecnologías innovadoras como blockchain y NFTs para la validación de entradas y recompensas digitales

El objetivo es brindar una solución integral que conecte a artistas, público y organizadores, fortaleciendo el ecosistema musical desde sus bases.

---

## 📁 Estructura del Proyecto

```
SoundSeekers/
├── frontend/   # Aplicación React
├── backend/    # API REST en Java Spring Boot
```

---

## 🚀 Cómo levantar el proyecto localmente

### 1. Clonar el repositorio

```bash
git clone https://github.com/juangiazitzian/SoundSeekers.git
cd SoundSeekers
```

---

## 🌐 Frontend (React)

### 📦 Requisitos
- Node.js (v16 o superior)
- npm o yarn

### ▶️ Levantar el frontend

```bash
cd frontend
npm install        # o yarn install
npm start          # o yarn start
```

Esto abre la app en `http://localhost:3000`

---

## 🔧 Backend (Spring Boot)

### ☕ Requisitos
- Java 17+
- Maven

### ▶️ Levantar el backend

```bash
cd backend
./mvnw spring-boot:run
```

Por defecto corre en `http://localhost:8080`

---

## 🛠️ Variables de entorno

Podés crear archivos `.env` en `frontend/` o `backend/` si necesitás configurar valores como:

```
REACT_APP_API_URL=http://localhost:8080/api
```
