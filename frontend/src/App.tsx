import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

type OutputDto = {
  pharmacyName: string;
  pharmacyAddress: string;
  distance: string;
  directionURL: string;
  roadViewURL: string;
};

function App() {
  const [address, setAddress] = useState("");
  const [results, setResults] = useState<OutputDto[]>([]);
  const [mode, setMode] = useState<"search" | "result">("search");

  useEffect(() => {
    const input = document.getElementById("address_kakao") as HTMLInputElement;
    if (input) {
      input.addEventListener("click", () => {
        new (window as any).daum.Postcode({
          oncomplete: function (data: any) {
            setAddress(data.address);
          },
        }).open();
      });
    }
  }, []);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/search", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ address }),
    });

    const data = await response.json();
    setResults(data);
    setMode("result"); // ✅ 검색 끝나면 결과 화면으로 전환
  };

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
            />
            <button type="submit" className="btn btn-primary mt-2">
              Search
            </button>
          </form>

          <div className="grid-image d-flex justify-content-center mt-3">
            <img
              src="/images/map.png"
              alt="map"
              className="img-responsive img-rounded"
              style={{ width: "50%", height: "450px", margin: "20px" }}
            />
            <img
              src="/images/road.png"
              alt="road"
              className="img-responsive img-rounded"
              style={{ width: "50%", height: "450px", margin: "20px" }}
            />
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
                  <td>
                    <a href={out.directionURL} target="_blank" rel="noreferrer">
                      길안내
                    </a>
                  </td>
                  <td>
                    <a href={out.roadViewURL} target="_blank" rel="noreferrer">
                      로드뷰
                    </a>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="grid-image d-flex justify-content-center mt-3">
            <img
              src="/images/kakao.png"
              alt="kakao"
              className="img-responsive img-rounded"
              style={{ width: "70%", height: "550px", margin: "10px" }}
            />
          </div>

          {/* ✅ 다시 검색 버튼 */}
          <div className="text-center mt-4">
            <button
              className="btn btn-secondary"
              onClick={() => setMode("search")}
            >
              다시 검색하기
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default App;
