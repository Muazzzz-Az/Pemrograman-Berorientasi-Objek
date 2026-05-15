import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './store/useAuthStore';
import Auth from './pages/Auth';
import StudentDashboard from './pages/StudentDashboard';
import AdminDashboard from './pages/AdminDashboard';

export default function App() {
  const { isAuthenticated, role } = useAuthStore();

  return (
    <Router>
      <Routes>
        {/* Rute Publik */}
        <Route 
          path="/login" 
          element={!isAuthenticated ? <Auth /> : <Navigate to={role === 'ADMIN' ? '/admin' : '/student'} replace />} 
        />
        
        {/* Rute Terproteksi Langsung (Tanpa MainLayout) */}
        <Route 
          path="/student" 
          element={
            isAuthenticated && role === 'STUDENT' 
              ? <StudentDashboard /> 
              : <Navigate to="/login" replace />
          } 
        />

        <Route 
          path="/admin" 
          element={
            isAuthenticated && role === 'ADMIN' 
              ? <AdminDashboard /> 
              : <Navigate to="/login" replace />
          } 
        />

        {/* Redirect */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}