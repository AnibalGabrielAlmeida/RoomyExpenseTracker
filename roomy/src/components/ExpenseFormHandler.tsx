import { useState } from "react";

export default function ExpenseFormReact() {
  const [formData, setFormData] = useState({
    name: "",
    amount: "",
    date: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError("");
    setSuccess(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const data = {
      name: formData.name,
      amount: parseFloat(formData.amount),
      date: formData.date,
      category: "VARIABLE",
      description: "",
      houseId: 1,
    };

    try {
      const res = await fetch("http://localhost:8080/api/expenses/createExpense", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      if (!res.ok) throw new Error(await res.text());

      setFormData({ name: "", amount: "", date: "" });
      setSuccess(true);
    } catch (err) {
      if (err instanceof Error) setError(err.message);
      else setError("Error inesperado al enviar el formulario.");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4" id="expense-form">
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
          className="w-full rounded-md border-gray-300 p-2 shadow-sm focus:ring focus:ring-green-500"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Monto</label>
        <input
          type="number"
          name="amount"
          value={formData.amount}
          onChange={handleChange}
          min="0"
          required
          className="w-full rounded-md border-gray-300 p-2 shadow-sm focus:ring focus:ring-green-500"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Fecha</label>
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleChange}
          required
          className="w-full rounded-md border-gray-300 p-2 shadow-sm focus:ring focus:ring-green-500"
        />
      </div>

      {error && <p className="text-sm text-red-600">{error}</p>}
      {success && <p className="text-sm text-green-600">Gasto enviado con éxito ✅</p>}

      <button
        type="submit"
        className="w-full bg-green-600 text-white py-2 rounded-md font-semibold shadow-md hover:bg-green-700"
      >
        Guardar gasto
      </button>
    </form>
  );
}
