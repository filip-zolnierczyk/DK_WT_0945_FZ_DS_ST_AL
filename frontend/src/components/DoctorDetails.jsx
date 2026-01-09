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

    const formatDateTime = (isoString) => {
        const date = new Date(isoString);
        return {
            date: date.toLocaleDateString('pl-PL'),
            time: date.toLocaleTimeString('pl-PL', { hour: '2-digit', minute: '2-digit' })
        };
    };

    if (loading) return <div className="text-center p-10">Ładowanie...</div>;
    if (error) return <div className="text-center p-10 text-red-600">Błąd: {error}</div>;
    if (!doctor) return <div className="text-center p-10">Brak danych</div>;

    return (
        <div className="w-[600px] mx-auto bg-white rounded-md p-6 shadow-lg border border-gray-100">
            <h2 className="text-2xl font-bold mb-6 text-blue-900 border-b pb-2">Szczegóły lekarza</h2>
            
            <div className="space-y-3 mb-8">
                <div className="flex justify-between">
                    <span className="font-semibold text-gray-600">Imię i nazwisko:</span>
                    <span>{doctor.name} {doctor.surname}</span>
                </div>
                <div className="flex justify-between">
                    <span className="font-semibold text-gray-600">Specjalizacja:</span>
                    <span className="bg-blue-100 text-blue-800 px-2 py-0.5 rounded text-sm font-medium">
                        {doctor.specialization}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="font-semibold text-gray-600">Adres:</span>
                    <span>{doctor.address}</span>
                </div>
            </div>

            <div className="mt-6">
                <h3 className="text-lg font-semibold mb-4 text-gray-800">Planowane dyżury</h3>
                
                {doctor.duties && doctor.duties.length > 0 ? (
                    <div className="space-y-3">
                        {doctor.duties.map((duty) => {
                            const start = formatDateTime(duty.start);
                            const finish = formatDateTime(duty.finish);
                            
                            return (
                                <div key={duty.id} className="p-4 border rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors">
                                    <div className="flex justify-between items-center">
                                        <div>
                                            <p className="font-bold text-blue-700">
                                                {duty.officeName || "Gabinet nieprzypisany"}
                                            </p>
                                            <p className="text-sm text-gray-500">{duty.office?.address}</p>
                                        </div>
                                        <div className="text-right">
                                            <p className="font-medium text-gray-800">{start.date} - {finish.date}</p>
                                            <p className="text-sm font-semibold text-gray-600">
                                                {start.time} — {finish.time}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                ) : (
                    <p className="text-gray-500 italic bg-gray-50 p-4 rounded text-center">
                        Brak zaplanowanych dyżurów dla tego lekarza.
                    </p>
                )}
            </div>
        </div>
    );
}

export default DoctorDetails;