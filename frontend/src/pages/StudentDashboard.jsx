import React, { useState, useEffect } from 'react';
import { Shield, Phone, Mic, Navigation, FileText } from 'lucide-react';
import api from '../utils/api';
import { useAuthStore } from '../store/useAuthStore';

export default function StudentDashboard() {
  const { user, logout } = useAuthStore();
  
  // Status Logika Darurat
  const [hitungMundur, setHitungMundur] = useState(3);
  const [sedangMenghitung, setSedangMenghitung] = useState(false);
  const [statusDarurat, setStatusDarurat] = useState('AMAN'); // AMAN | AKTIF | DIKERAHKAN

  const mulaiSiklus = () => {
    setSedangMenghitung(true);
    setHitungMundur(3);
  };

  const picuDarurat = () => {
    // 1. Meminta paksa akses GPS ke perangkat (Laptop/HP)
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        async (posisi) => {
          try {
            // 2. Merakit data sesuai format @RequestParam di Spring Boot
            const formData = new FormData();
            formData.append('nim', user.nim);
            formData.append('latitude', posisi.coords.latitude);
            formData.append('longitude', posisi.coords.longitude);
            formData.append('description', 'Sinyal SOS Darurat dikirim otomatis dari perangkat.');

            // 3. Tembak ke Backend (Timpa aturan JSON dari api.js secara paksa)
            await api.post('/reports/emergency', formData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            });
            
            // 4. Ubah UI menjadi Darurat jika tembakan sukses
            setStatusDarurat('DIKERAHKAN');
            
          } catch (error) {
            console.error("Error Backend:", error.response?.data || error.message);
            alert("Gagal mengirim laporan ke Server. Pastikan Backend berjalan.");
            setStatusDarurat('AMAN');
          }
        },
        (error) => {
          // Jika mahasiswa menolak klik "Allow Location" di browser
          alert("Peringatan: Bantuan tidak dapat dikirim karena Anda menolak akses lokasi GPS!");
          setStatusDarurat('AMAN');
          setSedangMenghitung(false);
        }
      );
    } else {
      alert("Browser Anda tidak mendukung pelacakan lokasi GPS.");
      setStatusDarurat('AMAN');
    }
  };

  const batalkanDarurat = () => {
    setStatusDarurat('AMAN');
    setSedangMenghitung(false);
  };

  useEffect(() => {
    let pewaktu;
    if (sedangMenghitung && hitungMundur > 0) {
      pewaktu = setTimeout(() => setHitungMundur(h => h - 1), 1000);
    } else if (sedangMenghitung && hitungMundur === 0) {
      setSedangMenghitung(false);
      picuDarurat();
    }
    return () => clearTimeout(pewaktu);
  }, [sedangMenghitung, hitungMundur]);

  return (
    <div className="min-h-screen bg-slate-50 font-sans flex flex-col">
      {/* --- BAGIAN ATAS (DESKTOP) --- */}
      <header className="hidden md:flex items-center justify-between px-8 py-4 bg-white/80 backdrop-blur-md sticky top-0 z-50 border-b border-slate-200 shadow-sm">
        <div className="flex items-center gap-3">
           <div className="w-10 h-10 bg-emerald-700 rounded-xl flex items-center justify-center text-white shadow-lg shadow-emerald-700/20">
             <Shield size={20} />
           </div>
           <div>
             <h1 className="font-black text-slate-800 text-lg leading-none tracking-tight">SAFETRACK</h1>
             <p className="text-[10px] text-emerald-700 font-bold uppercase tracking-wider mt-0.5">Sistem Pelaporan Darurat</p>
           </div>
        </div>
        <div className="flex items-center gap-4">
           <span className="text-sm font-bold text-slate-600">Halo, {user?.name || 'Mahasiswa'}</span>
           <button onClick={logout} className="px-4 py-2 bg-red-50 text-red-600 text-xs font-bold rounded-xl border border-red-100 hover:bg-red-100 transition-colors">
             Keluar
           </button>
        </div>
      </header>

      {/* --- BAGIAN ATAS (SELULER) --- */}
      <div className="md:hidden flex justify-between items-center p-5 bg-white/90 backdrop-blur-md sticky top-0 z-40 shadow-sm">
         <div className="flex items-center gap-2">
            <Shield className="text-emerald-700" size={24} />
            <span className="font-black text-slate-800 text-lg tracking-tight">SAFETRACK</span>
         </div>
         <button onClick={logout} className="text-xs font-bold text-slate-400 hover:text-red-600 transition-colors">Keluar</button>
      </div>

      {/* --- KONTEN UTAMA --- */}
      <main className="flex-1 p-4 pb-8 flex flex-col justify-center items-center">
        <div className="w-full max-w-2xl flex-1 flex flex-col items-center justify-center animate-[fade-in_0.3s]">
           
           {sedangMenghitung ? (
             <div className="flex flex-col items-center justify-center text-center animate-pulse">
               <div className="text-[120px] font-black text-red-500 leading-none mb-6 drop-shadow-xl">{hitungMundur}</div>
               <h2 className="text-xl font-bold text-slate-700 mb-8">Tekan Batal jika salah tekan</h2>
               <button onClick={batalkanDarurat} className="px-10 py-4 bg-slate-200 rounded-full font-bold text-slate-600 text-lg hover:bg-slate-300 transition-colors shadow-sm">
                 BATALKAN
               </button>
             </div>
           ) : statusDarurat !== 'AMAN' ? (
             <div className="flex flex-col items-center justify-center text-center w-full">
                <div className={`w-full p-8 rounded-[40px] border-4 relative overflow-hidden shadow-2xl ${statusDarurat === 'DIKERAHKAN' ? 'bg-emerald-50 border-emerald-500 shadow-emerald-500/20' : 'bg-red-50 border-red-500 shadow-red-500/20'}`}>
                   <div className={`absolute top-0 left-0 w-full bg-white/60 backdrop-blur-sm p-2 text-xs font-black uppercase tracking-widest ${statusDarurat === 'DIKERAHKAN' ? 'text-emerald-700' : 'text-red-600'}`}>
                      {statusDarurat === 'DIKERAHKAN' ? 'SATGAS MENUJU LOKASI' : 'SINYAL DARURAT AKTIF'}
                   </div>

                   <div className="mt-8 mb-6">
                      {statusDarurat === 'DIKERAHKAN' ? (
                         <div className="w-32 h-32 bg-emerald-500 rounded-full flex items-center justify-center text-white mx-auto shadow-xl animate-bounce border-4 border-emerald-200">
                            <Navigation size={48} />
                         </div>
                      ) : (
                         <div className="w-32 h-32 bg-red-500 rounded-full flex items-center justify-center text-white mx-auto shadow-xl animate-pulse border-4 border-red-200">
                            <Mic size={48} />
                         </div>
                      )}
                   </div>

                   <h2 className={`text-2xl font-black mb-2 tracking-tight ${statusDarurat === 'DIKERAHKAN' ? 'text-emerald-700' : 'text-red-600'}`}>
                      {statusDarurat === 'DIKERAHKAN' ? 'BANTUAN DATANG!' : 'MEREKAM SUARA...'}
                   </h2>
                   <p className="text-slate-600 text-sm mb-8 px-4 font-medium leading-relaxed">
                      {statusDarurat === 'DIKERAHKAN' 
                        ? 'Petugas Satgas telah menerima lokasi Anda dan bergerak cepat menuju titik koordinat.' 
                        : 'Lokasi Anda disiarkan langsung ke Pusat Komando. Jangan matikan perangkat Anda.'}
                   </p>

                   <div className="flex flex-col gap-3">
                      <button className="w-full py-4 bg-white border-2 border-slate-100 rounded-2xl font-bold text-slate-700 flex items-center justify-center gap-2 shadow-sm hover:bg-slate-50 transition-colors">
                         <Phone size={18} /> Hubungi CS Satgas
                      </button>
                      <button onClick={batalkanDarurat} className="w-full py-4 bg-slate-800 hover:bg-slate-900 text-white rounded-2xl font-bold shadow-lg transition-colors active:scale-95">
                         SAYA SUDAH AMAN / BATALKAN
                      </button>
                   </div>
                </div>
             </div>
           ) : (
             <div className="flex flex-col items-center pt-8 w-full">
                <h2 className="text-2xl font-black text-slate-800 mb-3 tracking-tight">Butuh Bantuan Cepat?</h2>
                <p className="text-slate-500 text-sm mb-12 px-8 text-center leading-relaxed font-medium">
                  Gunakan tombol ini <strong className="text-red-600">HANYA</strong> untuk ancaman nyawa/fisik. <br/>
                  Lokasi dan rekaman suara akan diambil otomatis.
                </p>

                <div className="relative mb-16">
                   <div className="absolute inset-0 bg-red-200 rounded-full animate-ping opacity-30"></div>
                   <button onClick={mulaiSiklus} className="w-64 h-64 bg-gradient-to-br from-red-500 to-red-600 rounded-full shadow-[0_20px_60px_rgba(220,38,38,0.4)] flex flex-col items-center justify-center text-white active:scale-95 transition-transform border-4 border-white/30 relative z-10">
                      <Shield size={64} className="mb-2" />
                      <span className="text-4xl font-black tracking-[0.15em] ml-2">SOS</span>
                      <span className="text-[10px] font-bold mt-2 opacity-90 bg-black/20 px-3 py-1 rounded-full uppercase tracking-widest border border-white/10">
                        Aktif dalam 3 detik
                      </span>
                   </button>
                </div>

                {/* Integrasi Form Non-Darurat (Polimorfisme) */}
                <div className="w-full bg-white rounded-3xl p-6 shadow-sm border border-slate-200 flex flex-col sm:flex-row items-center justify-between gap-4 mt-auto">
                   <div className="flex items-center gap-4">
                     <div className="p-3 bg-emerald-50 text-emerald-700 rounded-xl">
                       <FileText size={28} />
                     </div>
                     <div className="text-left">
                       <h3 className="font-black text-slate-800">Laporan Investigasi</h3>
                       <p className="text-xs text-slate-500 font-medium">Laporkan tindakan bullying atau pelecehan secara anonim.</p>
                     </div>
                   </div>
                   <button className="w-full sm:w-auto px-6 py-3 bg-slate-800 hover:bg-slate-900 text-white font-bold text-sm rounded-xl transition-colors">
                     Isi Formulir
                   </button>
                </div>
             </div>
           )}
        </div>
      </main>
    </div>
  );
}