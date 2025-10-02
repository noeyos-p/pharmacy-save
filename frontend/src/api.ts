import axios from 'axios'

// 생산환경(Nginx)에서는 /api가 백엔드로 프록시됩니다.
// 로컬 개발은 vite proxy(8080) 사용.
export const api = axios.create({ baseURL: '/api' })
// axios 객체를 갖고옴, api 주소 통일하기