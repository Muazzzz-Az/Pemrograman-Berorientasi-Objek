import { Outlet, Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

export default function MainLayout() {
  const { isAuthenticated } = useAuthStore();

  // Jika belum login, lempar kembali ke halaman login
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="min-h-screen flex flex-col md:flex-row bg-gray-50">
      {/* SIDEBAR UNTUK DESKTOP (Sembunyi di HP) */}
      <aside className="hidden md:flex w-64 bg-usu text-white flex-col shadow-xl">
        <div className="p-6 text-2xl font-bold tracking-wider border-b border-usu-light">
          SAFETRACK
        </div>
        <nav className="flex-1 p-4 space-y-2">
          {/* Menu navigasi nanti ditaruh di sini */}
          <div className="p-3 bg-usu-light rounded-lg cursor-pointer">Dashboard</div>
        </nav>
      </aside>

      {/* HEADER UNTUK MOBILE (Sembunyi di Desktop) */}
      <header className="md:hidden bg-usu text-white p-4 text-center text-xl font-bold shadow-md fixed top-0 w-full z-10">
        SAFETRACK
      </header>

      {/* AREA KONTEN UTAMA */}
      <main className="flex-1 p-4 md:p-8 mt-14 md:mt-0 mb-16 md:mb-0 overflow-y-auto">
        <Outlet /> {/* Komponen halaman akan di-render di dalam sini */}
      </main>

      {/* BOTTOM NAVIGATION UNTUK MOBILE (Sembunyi di Desktop) */}
      <nav className="md:hidden fixed bottom-0 w-full bg-white border-t border-gray-200 flex justify-around p-3 shadow-[0_-2px_10px_rgba(0,0,0,0.05)] z-10">
        {/* Ikon navigasi nanti ditaruh di sini */}
        <div className="text-usu font-medium text-sm">Dashboard</div>
      </nav>
    </div>
  );
}