import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

function BookAppointment() {
  const { patientId } = useParams();
  const navigate = useNavigate();

  const [doctors, setDoctors] = useState([]);
  const [specializations, setSpecializations] = useState([]);
  const [selectedSpec, setSelectedSpec] = useState("All");
  const [availableSlots, setAvailableSlots] = useState([]);
  const [loading, setLoading] = useState(false);

  const SLOT_DURATION_MS = 15 * 60 * 1000;

  useEffect(() => {
    fetch("http://localhost:8080/doctors")
      .then((res) => res.json())
      .then((data) => {
        setDoctors(data);
        const specs = [
          ...new Set(
            data
              .map((d) => {
                if (!d.specialization) return null;
                return typeof d.specialization === "object"
                  ? d.specialization.name
                  : d.specialization;
              })
              .filter(Boolean),
          ),
        ];
        setSpecializations(specs);
      })
      .catch((err) => console.error("Błąd fetch lekarzy:", err));
  }, []);

  const findSlots = async () => {
    setLoading(true);
    setAvailableSlots([]);

    try {
      const filteredDocs =
        selectedSpec === "All"
          ? doctors
          : doctors.filter((d) => {
              const sName =
                typeof d.specialization === "object"
                  ? d.specialization.name
                  : d.specialization;
              return sName === selectedSpec;
            });

      let allFreeSlots = [];

      for (const doc of filteredDocs) {
        const dutiesRes = await fetch(
          `http://localhost:8080/doctors/${doc.id}/duties`,
        );
        const duties = await dutiesRes.json();

        for (const duty of duties) {
          const slotsRes = await fetch(
            `http://localhost:8080/appointments/${duty.id}`,
          );
          const serverSlots = await slotsRes.json();

          const freeServerSlots = serverSlots.filter(
            (s) => s.occupied === false,
          );

          freeServerSlots.forEach((s) => {
            allFreeSlots.push({
              dutyId: duty.id,
              doctorFullName:
                s.doctorName || `${doc.doctorName} ${doc.doctorSurname}`,
              officeName: s.officeName,
              specialization: s.specializationName,
              start: new Date(s.startTime),
            });
          });
        }
      }

      setAvailableSlots(allFreeSlots.sort((a, b) => a.start - b.start));
    } catch (err) {
      console.error("Błąd podczas pobierania terminów:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleBook = async (slot) => {
    const dateStr = slot.start.toISOString().split(".")[0];

    try {
      const res = await fetch("http://localhost:8080/appointments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          patientId: parseInt(patientId),
          dutyId: slot.dutyId,
          startTime: dateStr,
        }),
      });

      if (res.ok) {
        alert("Wizyta została zarezerwowana!");
        findSlots();
      } else {
        const errorData = await res.text();
        alert("Serwer odrzucił rezerwację: " + errorData);
      }
    } catch (err) {
      alert("Błąd połączenia z serwerem.");
    }
  };

  return (
    <div className="max-w-[1000px] mx-auto p-6 bg-white rounded-lg shadow-lg border mt-10">
      <h2 className="text-2xl font-bold mb-6 text-blue-900 border-b pb-2">
        Terminy wizyt
      </h2>

      <div className="flex gap-4 mb-8">
        <div className="flex-1">
          <label className="block text-xs font-bold text-gray-500 mb-1 uppercase">
            Specjalizacja
          </label>
          <select
            className="w-full border-2 border-gray-100 p-2 rounded-md bg-gray-50 font-semibold"
            value={selectedSpec}
            onChange={(e) => setSelectedSpec(e.target.value)}
          >
            <option value="All">
              Wszystkie specjalizacje ({specializations.length})
            </option>
            {specializations.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>
        <div className="flex items-end">
          <button
            onClick={findSlots}
            className="bg-blue-600 text-white px-8 py-2 rounded-md hover:bg-blue-700 font-bold h-[45px]"
          >
            SZUKAJ
          </button>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-10 font-bold text-blue-500">
          Ładowanie...
        </div>
      ) : (
        <div className="border rounded-lg overflow-hidden">
          <table className="w-full text-left">
            <thead className="bg-gray-50">
              <tr>
                <th className="p-4 text-xs font-bold text-gray-400">LEKARZ</th>
                <th className="p-4 text-xs font-bold text-gray-400">TERMIN</th>
                <th className="p-4 text-xs font-bold text-gray-400 text-center">
                  AKCJA
                </th>
              </tr>
            </thead>
            <tbody className="divide-y">
              {availableSlots.length > 0 ? (
                availableSlots.map((slot, idx) => (
                  <tr key={idx} className="hover:bg-gray-50">
                    <td className="p-4">
                      <div className="font-bold">{slot.doctorFullName}</div>
                      <div className="text-xs text-gray-500">
                        {slot.officeName}
                      </div>
                    </td>
                    <td className="p-4 font-semibold text-blue-700">
                      {slot.start.toLocaleString("pl-PL", {
                        day: "2-digit",
                        month: "2-digit",
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </td>
                    <td className="p-4 text-center">
                      <button
                        onClick={() => handleBook(slot)}
                        className="bg-green-600 text-white px-4 py-1.5 rounded font-bold text-sm"
                      >
                        REZERWUJ
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan="3"
                    className="p-10 text-center text-gray-400 italic"
                  >
                    Brak wolnych terminów. Sprawdź konsolę (F12).
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default BookAppointment;
