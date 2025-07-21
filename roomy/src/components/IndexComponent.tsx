import React, { useEffect, useState } from "react";

type HouseDTO = {
  id: number;
  name: string;
  address: string;
  roommates: string[];
};

const IndexPage: React.FC = () => {
  const [houses, setHouses] = useState<HouseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalMonthExpense, setTotalMonthExpense] = useState<number>(0);

  // Fetch casas
  const fetchHouses = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/house/getAll");
      if (!res.ok) throw new Error("Error cargando casas");
      const data = await res.json();
      if (typeof data === "string") {
        // Cuando backend responde mensaje tipo "no hay casas"
        setHouses([]);
      } else {
        setHouses(data);
      }
    } catch (e: any) {
      setError(e.message);
    }
  };

  // Fetch total gastos del mes
  const fetchTotalMonthExpense = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/expenses/getTotalCurrentMonth");
      if (!res.ok) throw new Error("Error cargando gastos");
      const data = await res.json();
      setTotalMonthExpense(data.total || 0);
    } catch (e: any) {
      setError(e.message);
    }
  };

  useEffect(() => {
    setLoading(true);
    Promise.all([fetchHouses(), fetchTotalMonthExpense()]).finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-center mt-8">Cargando datos...</p>;
  }

  if (error) {
    return <p className="text-center mt-8 text-red-600">Error: {error}</p>;
  }

  return (
    <main className="p-6 max-w-4xl mx-auto">
      <h1 className="text-4xl font-bold mb-6 text-center">RoomyTracker</h1>

      <section className="mb-8 bg-gray-50 p-4 rounded shadow">
        <h2 className="text-2xl font-semibold mb-2">Tus casas</h2>
        {houses.length === 0 ? (
          <p>No tienes casas registradas.</p>
        ) : (
          <ul className="list-disc list-inside">
            {houses.map((house) => (
              <li key={house.id}>
                <strong>{house.name}</strong> - {house.address} (
                {house.roommates.length} roommate{house.roommates.length !== 1 ? "s" : ""})
              </li>
            ))}
          </ul>
        )}
        <a
          href="/houses/new"
          className="inline-block mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
        >
          Crear nueva casa
        </a>
      </section>

      <section className="mb-8 bg-gray-50 p-4 rounded shadow">
        <h2 className="text-2xl font-semibold mb-2">Gasto total del mes</h2>
        <p className="text-xl font-bold">${totalMonthExpense.toFixed(2)}</p>
        <a
          href="/expenses/new"
          className="inline-block mt-4 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
        >
          Nuevo gasto
        </a>
      </section>

      <footer className="text-center text-sm text-gray-500 mt-12">
        Cada gasto anotado es un paso hacia tu independencia financiera.
      </footer>
    </main>
  );
};

export default IndexPage;
