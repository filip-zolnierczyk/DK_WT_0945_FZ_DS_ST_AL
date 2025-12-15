import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";

function DoctorList() {
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [deletingId, setDeletingId] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                setError(null);

                const res = await fetch("http://localhost:8080/doctors");
                if (!res.ok) {
                    throw new Error(`HTTP ${res.status}`);
                }

                const json = await res.json();
                setDoctors(json);
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
            const res = await fetch(`http://localhost:8080/doctors/${id}`, {
                method: "DELETE",
            });
            if (res.status === 404) {
                throw new Error("Lekarz nie istnieje (404)");
            }
            if (!res.ok && res.status !== 204) {
                throw new Error(`HTTP ${res.status}`);
            }
            setDoctors((prev) => prev.filter((d) => d.id !== id));
        } catch (err) {
            setError(err.message || "Błąd usuwania");
        } finally {
            setDeletingId(null);
        }
    };

    if (loading) return <div>Ładowanie...</div>;
    if (error) return <div>Błąd: {error}</div>;
    return (
        <div className="w-[500px] border-1 rounded-md p-5 flex flex-col ">
            <div className="grid grid-cols-4 gap-4 font-medium p-2">
                <div>Imię</div>
                <div>Nazwisko</div>
                <div>Specjalizacja</div>
            </div>

            <div className="mt-2 space-y-2">
                {doctors.map((doctor) => (
                    <div key={doctor.id ?? `${doctor.name}-${doctor.surname}`} className="grid grid-cols-4 gap-4 p-2">
                        <div>{doctor.name}</div>
                        <div>{doctor.surname}</div>
                        <div>{doctor.specialization}</div>
                        <div className={"w-full flex items-center gap-2"}>
                            <button
                                className={"bg-red-300 hover:bg-red-400 rounded-md p-1 flex-1"}
                                onClick={() => handleDelete(doctor.id)}
                                disabled={deletingId === doctor.id}
                            >
                                {deletingId === doctor.id ? "Usuwanie..." : "Usuń"}
                            </button>
                            <button
                                className={"bg-gray-300 hover:bg-gray-400 rounded-md p-1 flex-1"}
                                onClick={() => navigate(`/doctors/${doctor.id}`)}
                            > Info
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default DoctorList