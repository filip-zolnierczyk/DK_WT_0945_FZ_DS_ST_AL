import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function OfficeList() {
  const [offices, setOffices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [deletingId, setDeletingId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        setError(null);

        const res = await fetch("http://localhost:8080/offices");
        if (!res.ok) {
          throw new Error(`HTTP ${res.status}`);
        }

        const json = await res.json();
        setOffices(json);
      } catch (err) {
        setError(err.message || "Błąd");
      } finally {
        setLoading(false);
      }
    }

    load();
  }, []);

  const handleDelete = async (id) => {
    try {
      setDeletingId(id);
      setError(null);
      const res = await fetch(`http://localhost:8080/offices/${id}`, {
        method: "DELETE",
      });
      if (res.status === 404) {
        throw new Error("Gabinet nie istnieje (404)");
      }
      if (res.status === 409) {
        throw new Error("Nie możesz usunąć gabinetu, jeśli są w nim zaplanowane dyżury (404)");
      }
      if (!res.ok && res.status !== 204) {
        throw new Error(`HTTP ${res.status}`);
      }
      setOffices((prev) => prev.filter((d) => d.id !== id));
    } catch (err) {
      setError(err.message || "Błąd usuwania");
    } finally {
      setDeletingId(null);
    }
  };

  if (loading) return <div>Ładowanie...</div>;
  if (error) return <div>Błąd: {error}</div>;
  return (
    <div className="w-[800px] border-1 rounded-md p-5 flex flex-col ">
      <div className="grid grid-cols-5 gap-4 font-medium p-2">
        <div>ID</div>
        <div>Imię</div>
        <div>Adres</div>
        <div>Opis</div>
      </div>

      <div className="mt-2 space-y-2">
        {offices.map((office) => (
          <div
            key={office.id ?? `${office.name}`}
            className="grid grid-cols-5 gap-4 p-2"
          >
            <div>{office.id}</div>
            <div>{office.name}</div>
            <div>{office.address}</div>
            <div>{office.description}</div>
            <div className={"w-full flex items-center gap-2"}>
              <button
                className={"bg-red-300 hover:bg-red-400 rounded-md p-1 flex-1"}
                onClick={() => handleDelete(office.id)}
                disabled={deletingId === office.id}
              >
                {deletingId === office.id ? "Usuwanie..." : "Usuń"}
              </button>
              {/* <button
                className={
                  "bg-gray-300 hover:bg-gray-400 rounded-md p-1 flex-1"
                }
                onClick={() => navigate(`/offices/${office.id}`)}
              >
                {" "}
                Info
              </button> */}
              <button
                className={
                  "bg-yellow-300 hover:bg-yellow-400 rounded-md p-1 flex-1"
                }
                onClick={() => navigate(`/offices/${office.id}/duties`)}
              >
                {" "}
                Dyżury
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default OfficeList;
