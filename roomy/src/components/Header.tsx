import React, { useState } from "react";

const Header: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => setMenuOpen(!menuOpen);

  return (
    <header className="bg-primary text-text shadow-md p-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">RoomyTracker</h1>

      {/* Botón menú móvil */}
      <button
        className="md:hidden"
        onClick={toggleMenu}
        aria-label="Toggle menu"
      >
        ☰
      </button>

      {/* Navegación */}
      <nav
        className={`${
          menuOpen ? "block" : "hidden"
        } md:block space-x-6 text-lg`}
      >
        <a href="/" className="hover:underline">
          Inicio
        </a>
        <a href="/houses" className="hover:underline">
          Casas
        </a>
        <a href="/expenses" className="hover:underline">
          Gastos
        </a>
        <a href="/newExpense" className="hover:underline">
          Nuevo Gasto
        </a>
      </nav>
    </header>
  );
};

export default Header;
