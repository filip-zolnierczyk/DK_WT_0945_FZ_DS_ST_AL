import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function DoctorDetails() {
    const { id } = useParams();
    const [doctor, setDoctor] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                setError(null);
                const res = await fetch(`http://localhost:8080/doctors/${id}`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();
                setDoctor(json);
            } catch (err) {
                setError(err.message || "Błąd");
            } finally {
                setLoading(false);
            }
        }
        load();
    }, [id]);

    if (loading) return <div>Ładowanie...</div>;
    if (error) return <div>Błąd: {error}</div>;
    if (!doctor) return <div>Brak danych</div>;

    return (
        <div className="w-[500px] mx-auto bg-white rounded-md p-6 shadow">
            <h2 className="text-xl font-semibold mb-4">Szczegóły lekarza</h2>
            <div className="space-y-2">
                <div><span className="font-medium">Imię:</span> {doctor.name}</div>
                <div><span className="font-medium">Nazwisko:</span> {doctor.surname}</div>
                <div><span className="font-medium">PESEL:</span> {doctor.pesel}</div>
                <div><span className="font-medium">Adres:</span> {doctor.address}</div>
                <div><span className="font-medium">Specjalizacja:</span> {doctor.specialization}</div>
            </div>
        </div>
    );
}

export default DoctorDetails;