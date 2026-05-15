import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './store/useAuthStore';
import MainLayout from './layouts/MainLayout';
import Auth from './pages/Auth';

/**
 * KOMPONEN DUMMY DASHBOARD
 * (Akan dipindahkan ke file terpisah di src/pages/ pada langkah berikutnya)
 */
const StudentDashboard = () => (
  <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 h-full">
    <div className="flex justify-between items-center mb-6">
      <h2 className="text-2xl font-bold text-gray-800">Dashboard Mahasiswa</h2>
      <span className="px-3 py-1 bg-green-100 text-usu text-xs font-bold rounded-full">STATUS: AMAN</span>
    </div>
    <div className="flex flex-col items-center justify-center py-10 border-2 border-dashed border-gray-200 rounded-2xl">
      <p className="text-gray-500 text-center px-6">
        Tombol darurat (Panic Button) dan form pelaporan anonim akan muncul di area ini.
      </p>
    </div>
  </div>
);

const AdminDashboard = () => (
  <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 h-full">
    <div className="flex justify-between items-center mb-6">
      <h2 className="text-2xl font-bold text-gray-800">Command Center</h2>
      <span className="px-3 py-1 bg-red-100 text-danger text-xs font-bold rounded-full animate-pulse">LIVE MONITORING</span>
    </div>
    <div className="bg-gray-50 rounded-2xl p-4 h-64 flex items-center justify-center border border-gray-200">
      <p className="text-gray-400 italic">Peta real-time dan log laporan masuk akan tampil di sini.</p>
    </div>
  </div>
);

/**
 * KOMPONEN UTAMA APLIKASI
 * Mengatur jalannya rute (Routing) dan proteksi akses akun.
 */
export default function App() {
  const { isAuthenticated, role } = useAuthStore();

  return (
    <Router>
      <Routes>
        {/* Rute Publik: Login & Register */}
        <Route 
          path="/login" 
          element={!isAuthenticated ? <Auth /> : <Navigate to={role === 'ADMIN' ? '/admin' : '/student'} replace />} 
        />
        
        {/* Rute Terproteksi: Harus Login & Melewati MainLayout */}
        <Route element={<MainLayout />}>
          
          {/* Jalur khusus Mahasiswa */}
          <Route 
            path="/student" 
            element={
              isAuthenticated && role === 'STUDENT' 
                ? <StudentDashboard /> 
                : <Navigate to="/login" replace />
            } 
          />

          {/* Jalur khusus Admin / Satgas */}
          <Route 
            path="/admin" 
            element={
              isAuthenticated && role === 'ADMIN' 
                ? <AdminDashboard /> 
                : <Navigate to="/login" replace />
            } 
          />
          
        </Route>

        {/* Redirect otomatis jika rute tidak ditemukan */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}