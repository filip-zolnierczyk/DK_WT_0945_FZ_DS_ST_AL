import { useState } from "react";

function AddOfficeForm() {
  const [form, setForm] = useState({ name: "", address: "", description: "" });
  const [status, setStatus] = useState(null);

  const onChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setStatus("loading");
    try {
      const res = await fetch("http://localhost:8080/offices", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (!res.ok) throw new Error("Błąd zapisu");
      setStatus("success");
      setForm({ name: "", address: "", description: "" });
    } catch (err) {
      setStatus("error");
    }
  };

  return (
    <div className={"w-[500px] mx-auto bg-white rounded-md p-6 shadow"}>
      <h1 className={"text-xl font-semibold mb-4"}>Dodaj gabinet</h1>
      <form className={"flex flex-col gap-3"} onSubmit={onSubmit}>
        <input
          className={"border rounded p-2"}
          name="name"
          placeholder="Nazwa"
          value={form.name}
          onChange={onChange}
          required
        />
        <input
          className={"border rounded p-2"}
          name="address"
          placeholder="Adres"
          value={form.address}
          onChange={onChange}
          required
        />
        <input
          className={"border rounded p-2"}
          name="description"
          placeholder="Opis"
          value={form.description}
          onChange={onChange}
          required
        />
        <button
          className={"p-2 bg-blue-500 text-white rounded hover:bg-blue-600"}
          type="submit"
        >
          Zapisz
        </button>
        {status === "loading" && <span>Zapisywanie...</span>}
        {status === "success" && (
          <span className={"text-green-600"}>Zapisano.</span>
        )}
        {status === "error" && (
          <span className={"text-red-600"}>Wystąpił błąd.</span>
        )}
      </form>
    </div>
  );
}

export default AddOfficeForm;
