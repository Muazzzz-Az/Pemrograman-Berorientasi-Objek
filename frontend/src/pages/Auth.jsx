import React, { useState } from 'react';
import { useAuthStore } from '../store/useAuthStore';
import { ShieldAlert } from 'lucide-react';

export default function Auth() {
  const login = useAuthStore((state) => state.login);
  const [isLogin, setIsLogin] = useState(true);
  
  // State untuk form
  const [nim, setNim] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('STUDENT'); // Default Mahasiswa

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Nanti di sini kita pasang Axios fetch ke Backend (GET /api/users/{nim})
    // Simulasi Login O(1) untuk menguji UI
    login({ nim, name: 'Pengguna Test' }, role);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-usu-dark p-4">
      <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md overflow-hidden">
        
        {/* Header / Logo Area */}
        <div className="bg-usu p-8 text-center text-white relative">
          <div className="flex justify-center mb-4">
            <ShieldAlert size={56} className="text-white animate-pulse" />
          </div>
          <h1 className="text-3xl font-extrabold tracking-wider">SAFETRACK</h1>
          <p className="text-usu-light mt-2 text-sm">Platform Pelaporan Darurat Kampus</p>
        </div>

        {/* Form Area */}
        <div className="p-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-6">
            {isLogin ? 'Masuk ke Akun' : 'Daftar Akun Baru'}
          </h2>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-1">NIM / NIP</label>
              <input 
                type="text" 
                value={nim}
                onChange={(e) => setNim(e.target.value)}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-usu focus:border-usu outline-none transition-all"
                placeholder="Masukkan NIM/NIP Anda"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-1">Kata Sandi</label>
              <input 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-usu focus:border-usu outline-none transition-all"
                placeholder="••••••••"
                required
              />
            </div>

            <div className="flex items-center space-x-4 pt-2">
              <label className="flex items-center space-x-2 cursor-pointer">
                <input 
                  type="radio" 
                  checked={role === 'STUDENT'} 
                  onChange={() => setRole('STUDENT')}
                  className="text-usu focus:ring-usu h-4 w-4"
                />
                <span className="text-sm font-medium text-gray-700">Mahasiswa</span>
              </label>
              <label className="flex items-center space-x-2 cursor-pointer">
                <input 
                  type="radio" 
                  checked={role === 'ADMIN'} 
                  onChange={() => setRole('ADMIN')}
                  className="text-usu focus:ring-usu h-4 w-4"
                />
                <span className="text-sm font-medium text-gray-700">Admin/Satgas</span>
              </label>
            </div>

            <button 
              type="submit"
              className="w-full bg-usu hover:bg-usu-light text-white font-bold py-3 rounded-xl transition-all shadow-md mt-4"
            >
              {isLogin ? 'Masuk' : 'Daftar'}
            </button>
          </form>

          <div className="mt-6 text-center">
            <button 
              onClick={() => setIsLogin(!isLogin)}
              className="text-sm text-usu font-semibold hover:underline"
            >
              {isLogin ? 'Belum punya akun? Daftar di sini' : 'Sudah punya akun? Masuk'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}