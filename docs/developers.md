## Developers

### Prerequisites:
- NPM (18.16.0)
- Java (8+)

### Tech Stack:
- Frontend: `SvelteKit`
- Backend: `Ktor, JetBrains Exposed`

### Storage configuration:
Scripts are stored in a H2 database stored in the directory of the app. If the backend server is started in development mode, an in-memory H2 instance will be used instead.

### Setting up:
- Clone this project using `git clone https://github.com/GrowlyX/on-bot-typescript`.
- **Editing frontend:** 
  - Start a development environment using: 
    - `npm run dev`
    - *(The project is configured to proxy the backend API on port 6969)*
    - *(Vite is configured to listen and recompile when file changes are detected)*
- **Editing backend:**
  - Start a development environment using:
    - `./gradlew backend:build`
    - The web server will be started on port: `6969`
  - Create WebBundle:
    - `./gradlew webbundle:build`
    - Your fat jar (including static resources from the frontend) will be available in `/webbundle/build/libs`.
