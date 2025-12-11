import { Outlet, Link } from 'react-router-dom';

function Layout() {
    return (
        <div className="flex flex-col items-center p-10 gap-6">
            <h1 className="text-3xl font-bold">Szpital</h1>
            <nav className="flex gap-3 bg-blue-800 text-white rounded-md w-[500px] justify-center">
                <Link className="p-2 hover:bg-blue-900 p-3" to="/">Strona główna</Link>
                <Link className="p-2 hover:bg-blue-900 p-3" to="/add-doctor">Dodaj lekarza</Link>
            </nav>
            <Outlet />
        </div>
    );
}

export default Layout;