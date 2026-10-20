import { useEffect, useState } from "react";
import "./App.css"

type Status= "loading" | "ok" | "error"

function App() {
  const [status, setStatus] = useState<Status>("loading");

  useEffect(() => {
      const controller = new AbortController();

      fetch("/api/health", {signal: controller.signal})
          .then((res) => {
              if (!res.ok) throw new Error(`HTTP status ${res.status}`);
              return res.json() as Promise< { status:string } >;
          })
          .then((data) => setStatus(data.status === "ok" ? "ok" : "error"))
          .catch((err) => {
              if (err.name !== "AbortError") setStatus("error");
          });

      return () => controller.abort();
  }, []);

  return (
      <main className="app">
          <h1 className="app__title">Kestrel</h1>
          <p className={`app__status app__status--${status}`}>
              Backend: {status}
          </p>
      </main>
  );
}

export default App;