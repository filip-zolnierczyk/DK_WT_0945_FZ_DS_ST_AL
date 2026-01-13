import { useState, useEffect } from "react";

function AddDutyForm() {
    const [form, setForm] = useState({
        doctorId: "",
        officeId: "",
        start: "",
        finish: "",
    });

    const [doctors, setDoctors] = useState([]);
    const [offices, setOffices] = useState([]);
    const [status, setStatus] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [docsRes, offRes] = await Promise.all([
                    fetch("http://localhost:8080/doctors"),
                    fetch("http://localhost:8080/offices")
                ]);

                if (!docsRes.ok || !offRes.ok) throw new Error("Błąd pobierania danych");

                const docsData = await docsRes.json();
                const offData = await offRes.json();

                setDoctors(docsData);
                setOffices(offData);
            } catch (err) {
                console.error("Nie udało się pobrać list:", err);
            }
        };

        fetchData();
    }, []);

    const onChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        setStatus("loading");

        const payload = {
            ...form,
            doctorId: parseInt(form.doctorId),
            officeId: parseInt(form.officeId)
        };

        try {
            const res = await fetch("http://localhost:8080/duties", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });

            if (!res.ok) throw new Error("Błąd zapisu");

            setStatus("success");
            setForm({ doctorId: "", officeId: "", start: "", finish: "" });
        } catch (err) {
            setStatus("error");
        }
    };

    return (
        <div className="w-[600px] mx-auto bg-white rounded-md p-6 shadow-lg border border-gray-100">
            <h1 className="text-xl font-semibold mb-4">Dodaj nowy dyżur</h1>
            
            <form className="flex flex-col gap-5" onSubmit={onSubmit}>
                
                <div className="flex flex-col gap-1">
                    <label className="text-sm font-medium text-gray-700">Lekarz prowadzący:</label>
                    <select
                        name="doctorId"
                        value={form.doctorId}
                        onChange={onChange}
                        required
                        className="border rounded-md p-2 bg-gray-50 focus:ring-2 focus:ring-blue-400 outline-none"
                    >
                        <option value="">-- Wybierz lekarza --</option>
                        {doctors.map((doctor) => (
                            <option key={doctor.id} value={doctor.id}>
                                {doctor.name} {doctor.surname} ({doctor.specialization})
                            </option>
                        ))}
                    </select>
                </div>

                <div className="flex flex-col gap-1">
                    <label className="text-sm font-medium text-gray-700">Gabinet:</label>
                    <select
                        name="officeId"
                        value={form.officeId}
                        onChange={onChange}
                        required
                        className="border rounded-md p-2 bg-gray-50 focus:ring-2 focus:ring-blue-400 outline-none"
                    >
                        <option value="">-- Wybierz gabinet --</option>
                        {offices.map((office) => (
                            <option key={office.id} value={office.id}>
                                {office.name} - {office.address}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="flex flex-col gap-1">
                    <label className="text-sm font-medium text-gray-700">Początek dyżuru:</label>
                    <input
                        type="datetime-local"
                        name="start"
                        value={form.start}
                        onChange={onChange}
                        required
                        className="border rounded-md p-2 bg-gray-50 focus:ring-2 focus:ring-blue-400 outline-none"
                    />
                </div>

                <div className="flex flex-col gap-1">
                    <label className="text-sm font-medium text-gray-700">Koniec dyżuru:</label>
                    <input
                        type="datetime-local"
                        name="finish"
                        value={form.finish}
                        onChange={onChange}
                        required
                        className="border rounded-md p-2 bg-gray-50 focus:ring-2 focus:ring-blue-400 outline-none"
                    />
                </div>

                <button
                    type="submit"
                    disabled={status === "loading"}
                    className="mt-2 p-3 bg-blue-600 text-white font-semibold rounded-md hover:bg-blue-700 transition-colors disabled:bg-gray-400"
                >
                    {status === "loading" ? "Trwa zapisywanie..." : "Zarejestruj dyżur"}
                </button>

                {status === "success" && (
                    <div className="p-3 bg-green-100 text-green-700 rounded-md text-center">
                        Dyżur został pomyślnie dodany do grafiku!
                    </div>
                )}
                {status === "error" && (
                    <div className="p-3 bg-red-100 text-red-700 rounded-md text-center">
                        Wystąpił błąd. Sprawdź czy lekarz nie ma innego dyżuru w tym czasie.
                    </div>
                )}
            </form>
        </div>
    );
}

export default AddDutyForm;