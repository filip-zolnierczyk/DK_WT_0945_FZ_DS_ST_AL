import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function PatientList() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [deletingId, setDeletingId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        setError(null);
        const res = await fetch("http://localhost:8080/patients");
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setPatients(json);
      } catch (err) {
        setError(err.message || "Błąd");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Czy na pewno usunąć pacjenta?")) return;
    try {
      setDeletingId(id);
      const res = await fetch(`http://localhost:8080/patients/${id}`, {
        method: "DELETE",
      });
      if (res.status === 500) {
        throw new Error(
          "Nie możesz usunąć lekarza, któremu prawdopodobnie przypisana jest wizyta (500)",
        );
      }
      if (!res.ok && res.status !== 204) throw new Error(`HTTP ${res.status}`);
      setPatients((prev) => prev.filter((d) => d.id !== id));
    } catch (err) {
      setError(err.message || "Błąd usuwania");
    } finally {
      setDeletingId(null);
    }
  };

  if (loading) return <div className="p-5 text-center">Ładowanie...</div>;
  if (error) return <div className="p-5 text-red-500">Błąd: {error}</div>;

  return (
    <div className="w-[700px] border rounded-md p-5 flex flex-col bg-white shadow-sm">
      <h2 className="text-xl font-bold mb-4 text-blue-900">Lista Pacjentów</h2>
      <div className="grid grid-cols-5 gap-4 font-bold border-b pb-2 bg-gray-50 p-2">
        <div>Imię</div>
        <div>Nazwisko</div>
        <div>Pesel</div>
        <div className="col-span-2 text-center">Akcje</div>
      </div>

      <div className="mt-2 space-y-2">
        {patients.map((patient) => (
          <div
            key={patient.id}
            className="grid grid-cols-5 gap-4 p-2 items-center border-b border-gray-100 hover:bg-gray-50"
          >
            <div>{patient.name}</div>
            <div>{patient.surname}</div>
            <div className="text-sm font-mono">{patient.pesel}</div>
            <div className="col-span-2 flex flex-col gap-1">
              <div className="flex gap-1">
                <button
                  className="bg-blue-500 hover:bg-blue-600 text-white rounded-md p-1 px-3 flex-1 text-sm transition-colors"
                  onClick={() => navigate(`/patients/${patient.id}`)}
                >
                  Szczegóły
                </button>
                <button
                  className="bg-green-500 hover:bg-green-600 text-white rounded-md p-1 px-3 flex-1 text-sm transition-colors"
                  onClick={() => navigate(`/patients/${patient.id}/book`)}
                >
                  Umów
                </button>
              </div>
              <button
                className="bg-red-300 hover:bg-red-400 text-red-900 rounded-md p-1 px-2 w-full text-xs transition-colors"
                onClick={() => handleDelete(patient.id)}
                disabled={deletingId === patient.id}
              >
                {deletingId === patient.id ? "..." : "Usuń pacjenta"}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default PatientList;
