import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

function PatientDetails() {
  const { patientId } = useParams();
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const res = await fetch(
        `http://localhost:8080/patients/${patientId}/appointments`,
      );
      if (!res.ok) throw new Error("Nie udało się pobrać wizyt.");
      const data = await res.json();
      setAppointments(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, [patientId]);

  const cancelAppointment = async (appointmentId) => {
    if (!window.confirm("Czy na pewno chcesz odwołać tę wizytę?")) return;

    try {
      const res = await fetch(
        `http://localhost:8080/appointments/${appointmentId}`,
        {
          method: "DELETE",
        },
      );
      if (res.ok) {
        setAppointments((prev) => prev.filter((a) => a.id !== appointmentId));
        alert("Wizyta została odwołana.");
      } else {
        throw new Error("Błąd podczas usuwania wizyty.");
      }
    } catch (err) {
      alert(err.message);
    }
  };

  if (loading)
    return <div className="p-10 text-center">Ładowanie wizyt...</div>;

  return (
    <div className="max-w-2xl mx-auto mt-10 p-6 bg-white rounded-lg shadow-md border">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-blue-900">Wizyty pacjenta</h2>
        <button
          onClick={() => navigate("/patients")}
          className="text-gray-500 hover:underline text-sm"
        >
          Powrót do listy
        </button>
      </div>

      {error && <div className="text-red-500 mb-4">{error}</div>}

      <div className="space-y-4">
        {appointments.length === 0 ? (
          <p className="text-gray-500 italic text-center py-10">
            Brak zarezerwowanych wizyt.
          </p>
        ) : (
          appointments.map((appt) => (
            <div
              key={appt.id}
              className="flex justify-between items-center p-4 border rounded-md hover:border-blue-300 transition-colors"
            >
              <div>
                <div className="font-bold text-lg">{appt.doctorName}</div>
                <div className="text-sm text-blue-600 font-semibold">
                  {appt.specializationName}
                </div>
                <div className="text-sm text-gray-500">
                  {new Date(appt.startTime).toLocaleString("pl-PL", {
                    day: "2-digit",
                    month: "2-digit",
                    year: "numeric",
                    hour: "2-digit",
                    minute: "2-digit",
                  })}
                </div>
                <div className="text-xs text-gray-400 mt-1 uppercase">
                  {appt.officeName}
                </div>
              </div>
              <button
                onClick={() => cancelAppointment(appt.id)}
                className="bg-red-100 text-red-700 hover:bg-red-700 hover:text-white px-4 py-2 rounded-md text-sm font-bold transition-all"
              >
                Odwołaj
              </button>
            </div>
          ))
        )}
      </div>

      <button
        onClick={() => navigate(`/patients/${patientId}/book`)}
        className="w-full mt-8 bg-green-600 text-white py-3 rounded-md font-bold hover:bg-green-700 transition-colors"
      >
        Umów nową wizytę
      </button>
    </div>
  );
}

export default PatientDetails;
