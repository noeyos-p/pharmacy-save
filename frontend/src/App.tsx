import React, { useState, useEffect } from "react"
import "bootstrap/dist/css/bootstrap.min.css"
import { api } from "./api"   // axios 인스턴스 import

type OutputDto = {
  pharmacyName: string
  pharmacyAddress: string
  distance: string
  directionURL: string
  roadViewURL: string
}

function App() {
  const [address, setAddress] = useState("")
  const [results, setResults] = useState<OutputDto[]>([])
  const [mode, setMode] = useState<"search" | "result">("search")
  const [daumLoaded, setDaumLoaded] = useState(false)

  // ✅ 카카오 우편번호 API 스크립트 동적 로드
  useEffect(() => {
    const script = document.createElement("script")
    script.src = "https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"
    script.async = true
    script.onload = () => setDaumLoaded(true)
    document.body.appendChild(script)
  }, [])

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const response = await api.post<OutputDto[]>("/search", { address })
      setResults(response.data)
      setMode("result")
    } catch (err) {
      console.error("검색 실패:", err)
    }
  }

  return (
    <div className="container mt-3">
      {mode === "search" && (
        <>
          <h2 className="text-center">Pharmacy Recommendation</h2>
          <form onSubmit={handleSearch}>
            <label htmlFor="address_kakao">
              주소를 입력하시면 주소 기준으로 가까운 약국의 위치 최대 3곳 추천드립니다.
            </label>
            <input
              type="text"
              className="form-control"
              id="address_kakao"
              value={address}
              placeholder="주소를 입력하세요. ex) 서울특별시 성북구 종암로 10길"
              readOnly
              onClick={() => {
                if (daumLoaded && (window as any).daum?.Postcode) {
                  new (window as any).daum.Postcode({
                    oncomplete: (data: any) => setAddress(data.address),
                  }).open()
                } else {
                  alert("카카오 주소 API가 아직 준비되지 않았습니다. 잠시 후 다시 시도해주세요.")
                }
              }}
            />
            <button type="submit" className="btn btn-primary mt-2">
              Search
            </button>
          </form>
          <div className="grid-image d-flex justify-content-center mt-3">
            <img src="/images/map.png" alt="map" style={{ width: "50%", height: "450px", margin: "20px" }}/>
            <img src="/images/road.png" alt="road" style={{ width: "50%", height: "450px", margin: "20px" }}/>
          </div>
        </>
      )}

      {mode === "result" && (
        <>
          <h2 className="text-center">Pharmacy Recommendation Results</h2>
          <table className="table table-hover mt-3">
            <thead>
              <tr>
                <th>약국명</th>
                <th>약국 주소</th>
                <th>거리</th>
                <th>길안내 링크</th>
                <th>로드뷰 링크</th>
              </tr>
            </thead>
            <tbody>
              {results.map((out, idx) => (
                <tr key={idx}>
                  <td>{out.pharmacyName}</td>
                  <td>{out.pharmacyAddress}</td>
                  <td>{out.distance}</td>
                  <td><a href={out.directionURL} target="_blank" rel="noreferrer">길안내</a></td>
                  <td><a href={out.roadViewURL} target="_blank" rel="noreferrer">로드뷰</a></td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="grid-image d-flex justify-content-center mt-3">
            <img src="/images/kakao.png" alt="kakao" style={{ width: "70%", height: "550px", margin: "10px" }}/>
          </div>
          <div className="text-center mt-4">
            <button className="btn btn-secondary" onClick={() => setMode("search")}>
              다시 검색하기
            </button>
          </div>
        </>
      )}
    </div>
  )
}

export default App
