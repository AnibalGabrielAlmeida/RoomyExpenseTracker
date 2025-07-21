import React, { useState } from "react";

interface HouseCreationDTO {
  name: string;
  address: string;
}

export default function HouseForm() {
  const [form, setForm] = useState<HouseCreationDTO>({
    name: "",
    address: "",
  });

  const [status, setStatus] = useState<null | "success" | "error">(null);
  const [message, setMessage] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setStatus(null);

    try {
      const res = await fetch("http://localhost:8080/api/house/createHouse", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(form),
      });

      if (res.ok) {
        setStatus("success");
        setMessage("Casa creada exitosamente.");
        setForm({ name: "", address: "" });
      } else {
        const errorText = await res.text();
        setStatus("error");
        setMessage(`Error: ${errorText}`);
      }
    } catch (err) {
      setStatus("error");
      setMessage("Error de conexión con el servidor.");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-md mx-auto">
      <h2 className="text-xl font-semibold">Crear nueva casa</h2>

      <div>
        <label className="block mb-1 text-sm text-gray-700">Nombre</label>
        <input
          name="name"
          value={form.name}
          onChange={handleChange}
          required
          className="w-full border rounded-lg px-3 py-2"
        />
      </div>

      <div>
        <label className="block mb-1 text-sm text-gray-700">Dirección</label>
        <input
          name="address"
          value={form.address}
          onChange={handleChange}
          required
          className="w-full border rounded-lg px-3 py-2"
        />
      </div>

      <button
        type="submit"
        className="bg-primary text-white px-4 py-2 rounded hover:bg-hover transition"
      >
        Crear casa
      </button>

      {status === "success" && <p className="text-green-600">{message}</p>}
      {status === "error" && <p className="text-red-600">{message}</p>}
    </form>
  );
}
