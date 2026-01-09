import { Outlet, Link } from "react-router-dom";

function Layout() {
  return (
    <div className="flex flex-col items-center p-10 gap-6">
      <h1 className="text-3xl font-bold">Szpital</h1>

      <nav className="grid grid-cols-4 gap-0 bg-blue-800 text-white rounded-md w-full max-w-4xl overflow-hidden">
        <Link
          className="p-4 hover:bg-blue-900 text-center border-b border-r border-blue-700"
          to="/"
        >
          Lekarze
        </Link>
        <Link
          className="p-4 hover:bg-blue-900 text-center border-b border-r border-blue-700"
          to="/add-doctor"
        >
          Dodaj lekarza
        </Link>
        <Link
          className="p-4 hover:bg-blue-900 text-center border-b border-r border-blue-700"
          to="/patients"
        >
          Pacjenci
        </Link>
        <Link
          className="p-4 hover:bg-blue-900 text-center border-b border-blue-700"
          to="/add-patient"
        >
          Dodaj Pacjenta
        </Link>

        <Link
          className="p-4 hover:bg-blue-900 text-center border-r border-blue-700"
          to="/offices"
        >
          Gabinety
        </Link>
        <Link
          className="p-4 hover:bg-blue-900 text-center border-r border-blue-700"
          to="/add-office"
        >
          Dodaj Gabinet
        </Link>
        <Link
          className="p-4 hover:bg-blue-900 text-center border-r border-blue-700"
          to="/add-duty"
        >
          Dodaj Dyżur
        </Link>

      </nav>

      <Outlet />
    </div>
  );
}

export default Layout;
