import React, { useState } from 'react';
import { useAuthStore } from '../store/useAuthStore';
import { ShieldAlert, Loader2 } from 'lucide-react';
import api from '../utils/api'; // Mengimpor konfigurasi Axios kita

export default function Auth() {
  const login = useAuthStore((state) => state.login);
  
  // Status UI
  const [isLogin, setIsLogin] = useState(true);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  
  // Status Form Data
  const [nim, setNim] = useState('');
  const [password, setPassword] = useState('');
  const [namaLengkap, setNamaLengkap] = useState(''); // Khusus untuk Register
  const [role, setRole] = useState('STUDENT');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMsg('');

    try {
      if (isLogin) {
        // --- LOGIKA LOGIN ---
        // Menembak endpoint POST /api/users/login di Spring Boot
        const response = await api.post('/users/login', {
          nim: nim,
          password: password,
          role: role
        });

        // Jika berhasil, masukkan data ke Zustand Store (Sesi Aktif)
        // Asumsi backend mengembalikan obyek user: { nim: "...", name: "..." }
        login(response.data, role);

      } else {
        // --- LOGIKA REGISTER (PENDAFTARAN) ---
        // Menembak endpoint POST /api/users/register di Spring Boot
        await api.post('/users/register', {
          nim: nim,
          password: password,
          fullName: namaLengkap,
          role: role
        });

        // Jika sukses mendaftar, kembalikan UI ke mode Login
        alert('Pendaftaran berhasil! Silakan masuk menggunakan NIM Anda.');
        setIsLogin(true);
        setPassword('');
      }
    } catch (error) {
      // Penanganan Error O(1) berdasarkan respons Backend
      if (error.response) {
        // Backend merespons dengan kode error (misal: 401 Unauthorized, 404 Not Found)
        setErrorMsg(error.response.data.message || 'Kredensial tidak valid atau akun tidak ditemukan.');
      } else if (error.request) {
        // Request terkirim tapi server Spring Boot mati / tidak merespons
        setErrorMsg('Tidak dapat terhubung ke server. Pastikan Backend berjalan.');
      } else {
        setErrorMsg('Terjadi kesalahan pada sistem.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 p-4 font-sans">
      <div className="bg-white rounded-[32px] shadow-2xl w-full max-w-md overflow-hidden border border-slate-200">
        
        {/* Header / Logo Area */}
        <div className="bg-emerald-700 p-8 text-center text-white relative">
          <div className="flex justify-center mb-4 relative z-10">
            <div className="p-3 bg-white/20 rounded-2xl backdrop-blur-sm border border-white/30">
              <ShieldAlert size={48} className="text-white drop-shadow-md" />
            </div>
          </div>
          <h1 className="text-3xl font-black tracking-tight relative z-10">SAFETRACK</h1>
          <p className="text-emerald-100 mt-1 text-xs font-bold uppercase tracking-widest relative z-10">Sistem Pelaporan Darurat</p>
          
          {/* Efek Dekorasi SVG */}
          <div className="absolute top-0 right-0 w-32 h-32 bg-white opacity-5 rounded-full -translate-y-1/2 translate-x-1/2"></div>
        </div>

        {/* Form Area */}
        <div className="p-8">
          <h2 className="text-xl font-black text-slate-800 mb-6 tracking-tight">
            {isLogin ? 'Autentikasi Sistem' : 'Pendaftaran Akun'}
          </h2>

          {/* Alert Error Banner */}
          {errorMsg && (
            <div className="mb-6 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 text-sm font-bold rounded-r-lg animate-[fade-in_0.3s]">
              {errorMsg}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            
            {!isLogin && (
              <div>
                <label className="block text-xs font-bold text-slate-600 mb-1 uppercase tracking-wide">Nama Lengkap</label>
                <input 
                  type="text" 
                  value={namaLengkap}
                  onChange={(e) => setNamaLengkap(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-all text-slate-800 font-medium bg-slate-50 focus:bg-white"
                  placeholder="Masukkan nama lengkap"
                  required={!isLogin}
                />
              </div>
            )}

            <div>
              <label className="block text-xs font-bold text-slate-600 mb-1 uppercase tracking-wide">NIM / NIP / ID Petugas</label>
              <input 
                type="text" 
                value={nim}
                onChange={(e) => setNim(e.target.value)}
                className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-all text-slate-800 font-medium bg-slate-50 focus:bg-white"
                placeholder="Contoh: 241401050"
                required
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-600 mb-1 uppercase tracking-wide">Kata Sandi</label>
              <input 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-all text-slate-800 font-medium bg-slate-50 focus:bg-white"
                placeholder="••••••••"
                required
              />
            </div>

            <div className="pt-2 pb-4 border-b border-slate-100">
              <label className="block text-[10px] font-bold text-slate-400 mb-2 uppercase tracking-widest">Akses Sebagai</label>
              <div className="flex items-center space-x-6">
                <label className="flex items-center space-x-2 cursor-pointer group">
                  <input type="radio" checked={role === 'STUDENT'} onChange={() => setRole('STUDENT')} className="text-emerald-600 focus:ring-emerald-600 h-4 w-4" />
                  <span className="text-sm font-bold text-slate-700 group-hover:text-emerald-700 transition-colors">Mahasiswa</span>
                </label>
                <label className="flex items-center space-x-2 cursor-pointer group">
                  <input type="radio" checked={role === 'ADMIN'} onChange={() => setRole('ADMIN')} className="text-emerald-600 focus:ring-emerald-600 h-4 w-4" />
                  <span className="text-sm font-bold text-slate-700 group-hover:text-emerald-700 transition-colors">Satgas / Admin</span>
                </label>
              </div>
            </div>

            <button 
              type="submit"
              disabled={isLoading}
              className="w-full bg-slate-800 hover:bg-slate-900 text-white font-bold py-3.5 rounded-xl transition-all shadow-lg flex justify-center items-center active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                <Loader2 className="animate-spin text-white" size={20} />
              ) : (
                isLogin ? 'Masuk ke Sistem' : 'Daftarkan Akun'
              )}
            </button>
          </form>

          <div className="mt-6 text-center">
            <button 
              onClick={() => { setIsLogin(!isLogin); setErrorMsg(''); }}
              className="text-xs text-slate-500 font-bold hover:text-emerald-700 transition-colors uppercase tracking-wider"
            >
              {isLogin ? 'Belum punya akun? Daftar di sini' : 'Sudah punya akun? Masuk'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}