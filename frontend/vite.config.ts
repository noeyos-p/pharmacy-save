import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 로컬 개발 백엔드
        // 배포가 되면 컨테이너 이름으로 바꿔줌
        changeOrigin: true
      }
    }
  }
})