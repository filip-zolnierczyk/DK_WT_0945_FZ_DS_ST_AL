import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function OfficeDetails() {
  const { id } = useParams();
  const [office, setOffice] = useState(null);
  const [duties, setDuties] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadData() {
      try {
        setLoading(true);
        setError(null);

        const [officesRes, dutiesRes] = await Promise.all([
          fetch("http://localhost:8080/offices"),
          fetch(`http://localhost:8080/offices/${id}/duties`),
        ]);

        if (!officesRes.ok || !dutiesRes.ok)
          throw new Error("Błąd pobierania danych z serwera");

        const allOffices = await officesRes.json();
        const officeDuties = await dutiesRes.json();

        const currentOffice = allOffices.find((o) => o.id.toString() === id);

        if (!currentOffice) throw new Error("Nie znaleziono takiego gabinetu");

        setOffice(currentOffice);
        setDuties(officeDuties);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, [id]);

  const formatDateTime = (isoString) => {
    const date = new Date(isoString);
    return {
      date: date.toLocaleDateString("pl-PL"),
      time: date.toLocaleTimeString("pl-PL", {
        hour: "2-digit",
        minute: "2-digit",
      }),
    };
  };

  if (loading) return <div className="text-center p-10">Ładowanie...</div>;
  if (error)
    return <div className="text-center p-10 text-red-600">Błąd: {error}</div>;
  if (!office) return <div className="text-center p-10">Brak danych</div>;

  return (
    <div className="w-[600px] mx-auto bg-white rounded-md p-6 shadow-lg border border-gray-100">
      <h2 className="text-2xl font-bold mb-6 text-blue-900 border-b pb-2">
        Gabinet: {office.name}
      </h2>

      <div className="space-y-3 mb-8 bg-blue-50 p-4 rounded-md">
        <div>
          <span className="font-semibold text-gray-700">Adres:</span>{" "}
          {office.address}
        </div>
        <div>
          <span className="font-semibold text-gray-700">Opis:</span>{" "}
          {office.description || "Brak opisu"}
        </div>
      </div>

      <div className="mt-6">
        <h3 className="text-lg font-semibold mb-4 text-gray-800">
            Dyżury
        </h3>

        {duties && duties.length > 0 ? (
          <div className="space-y-3">
            {duties.map((duty) => {
              const start = formatDateTime(duty.start);
              const finish = formatDateTime(duty.finish);

              return (
                <div
                  key={duty.id}
                  className="p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <div className="flex justify-between items-center">
                    <div>
                      <p className="text-xs text-gray-500 font-medium uppercase tracking-wider">
                        Lekarz prowadzący
                      </p>
                      <p className="font-bold text-gray-900 text-lg">
                        {duty.doctorName} {duty.doctorSurname}
                      </p>
                      <p className="text-sm text-blue-600 font-semibold">
                        {duty.specialization.name}
                      </p>
                    </div>
                    <div className="text-right border-l pl-4 border-blue-100">
                      <p className="font-bold text-blue-900">{start.date} - {finish.date}</p>
                      <p className="text-sm font-medium text-gray-600">
                        {start.time} — {finish.time}
                      </p>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        ) : (
          <p className="text-gray-500 italic text-center p-4 border border-dashed rounded">
            Brak zaplanowanych dyżurów w tym gabinecie.
          </p>
        )}
      </div>
    </div>
  );
}

export default OfficeDetails;
