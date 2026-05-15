import React, { useState } from 'react';
import { useAuthStore } from '../store/useAuthStore';
import { MapPin, Bell, LogOut, Activity, Radio, Phone, CheckCircle, Shield, Inbox } from 'lucide-react';

export default function AdminDashboard() {
  const { logout } = useAuthStore();
  
  // Status Logika Pusat Komando
  const [tabAktif, setTabAktif] = useState('DARURAT'); // DARURAT | ANTREAN
  const [statusSistem, setStatusSistem] = useState('AMAN'); // AMAN | AKTIF | DIKERAHKAN
  
  // State murni kosong, TIDAK ADA data dump
  const [antreanLaporan, setAntreanLaporan] = useState([]); 

  // Fungsi Simulasi UI (Tanpa data palsu)
  const picuAlarm = () => { setStatusSistem('AKTIF'); setTabAktif('DARURAT'); };
  const kerahkanTim = () => setStatusSistem('DIKERAHKAN');
  const selesaikanKasus = () => setStatusSistem('AMAN');

  return (
    <div className="min-h-screen bg-slate-50 font-sans flex flex-col">
      {/* --- BAGIAN ATAS --- */}
      <header className="px-8 py-4 bg-white/80 backdrop-blur-md sticky top-0 z-50 border-b border-slate-200 flex items-center justify-between shadow-sm">
        <div className="flex items-center gap-3">
           <div className="w-10 h-10 bg-emerald-700 rounded-xl flex items-center justify-center text-white shadow-lg shadow-emerald-700/20">
             <Shield size={20} />
           </div>
           <div>
             <h1 className="font-black text-slate-800 text-lg leading-none tracking-tight">PUSAT KOMANDO</h1>
             <p className="text-[10px] text-emerald-700 font-bold uppercase tracking-wider mt-0.5">Satgas Keamanan Kampus</p>
           </div>
        </div>

        <div className="hidden md:flex items-center gap-2 bg-emerald-50 border border-emerald-100 px-4 py-2 rounded-full">
           <Activity size={16} className="text-emerald-600" />
           <span className="text-sm font-bold text-emerald-800">Sistem Berjalan Normal</span>
        </div>

        <div className="flex items-center gap-4">
           <div className={`flex items-center gap-2 px-3 py-1.5 rounded-full text-[10px] font-bold border ${statusSistem !== 'AMAN' ? 'bg-red-50 text-red-600 border-red-200 animate-pulse' : 'bg-emerald-50 text-emerald-600 border-emerald-200'}`}>
              <div className={`w-2 h-2 rounded-full ${statusSistem !== 'AMAN' ? 'bg-red-500' : 'bg-emerald-500'}`}></div>
              {statusSistem !== 'AMAN' ? 'ALARM BAHAYA' : 'TERHUBUNG'}
           </div>
           <button onClick={logout} className="px-4 py-2 bg-red-50 text-red-600 text-xs font-bold rounded-xl border border-red-100 hover:bg-red-100 transition-colors flex items-center gap-2">
             <LogOut size={14} /> Keluar
           </button>
        </div>
      </header>

      {/* --- KONTEN UTAMA --- */}
      <main className="flex-1 p-6 md:p-8 max-w-[1600px] mx-auto w-full">
        <div className="flex justify-between items-end mb-8">
           <div>
              <h2 className="text-2xl font-black text-slate-800 tracking-tight">Pemantauan Wilayah</h2>
              <p className="text-sm text-slate-500 mt-1 font-medium">Klasifikasi Prioritas Otomatis (Triage)</p>
           </div>
           
           {statusSistem !== 'AMAN' && (
              <div className="bg-red-600 text-white px-6 py-3 rounded-2xl font-bold shadow-xl shadow-red-500/30 flex items-center gap-3 animate-bounce border-2 border-red-400">
                 <Radio className="animate-spin-slow" /> DARURAT FISIK MEMBUTUHKAN TINDAKAN!
              </div>
           )}
        </div>

        <div className="grid grid-cols-12 gap-6 h-[75vh]">
             {/* PANEL KIRI: PETA */}
             <div className="col-span-12 lg:col-span-8 bg-white rounded-[32px] overflow-hidden relative border border-slate-200 shadow-sm group">
                <img 
                  src="/peta-usu.png" 
                  className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" 
                  alt="Peta Area" 
                  onError={(e) => { e.target.onerror = null; e.target.src = "https://placehold.co/1200x800?text=Peta+Kampus+USU"; }}
                />
                <div className="absolute top-6 left-6 bg-white/90 backdrop-blur px-4 py-2 rounded-2xl shadow-lg border border-white/50">
                  <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider flex items-center gap-2">
                    <MapPin size={14} className="text-emerald-600" /> Sensor Koordinat
                  </h4>
                </div>

                {statusSistem !== 'AMAN' && (
                  <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 flex flex-col items-center z-20">
                     <div className="bg-red-600 text-white px-4 py-2 rounded-xl text-xs font-bold mb-3 shadow-xl animate-bounce whitespace-nowrap border-2 border-white">
                        SOS Terdeteksi
                     </div>
                     <div className="relative">
                        <div className="absolute inset-0 bg-red-500 rounded-full animate-ping opacity-50 w-full h-full scale-150"></div>
                        <div className="bg-white p-2 rounded-full shadow-2xl relative z-10 border-2 border-red-100">
                           <MapPin size={32} className="text-red-600 fill-current" />
                        </div>
                     </div>
                  </div>
                )}
             </div>

             {/* PANEL KANAN: TABULASI & AKSI */}
             <div className="col-span-12 lg:col-span-4 flex flex-col h-full">
                <div className="bg-white p-6 rounded-[32px] shadow-sm border border-slate-200 flex-1 flex flex-col relative overflow-hidden">
                   
                   {/* Sistem Tab */}
                   <div className="flex bg-slate-100 rounded-xl p-1 mb-6">
                     <button onClick={() => setTabAktif('DARURAT')} className={`flex-1 py-2 text-xs font-bold rounded-lg transition-all ${tabAktif === 'DARURAT' ? 'bg-white text-red-600 shadow-sm' : 'text-slate-500'}`}>
                       RADAR DARURAT
                     </button>
                     <button onClick={() => setTabAktif('ANTREAN')} className={`flex-1 py-2 text-xs font-bold rounded-lg transition-all ${tabAktif === 'ANTREAN' ? 'bg-white text-emerald-700 shadow-sm' : 'text-slate-500'}`}>
                       ANTREAN INVESTIGASI
                     </button>
                   </div>
                   
                   {/* Konten Tab Darurat */}
                   {tabAktif === 'DARURAT' && (
                     <>
                       {statusSistem !== 'AMAN' ? (
                         <div className="bg-red-50 border border-red-100 p-6 rounded-3xl relative z-10 flex-1 flex flex-col animate-[slide-up_0.5s] shadow-inner">
                            <h4 className="text-2xl font-black text-slate-800 mb-2 leading-tight tracking-tight">Sinyal Masuk!</h4>
                            <p className="text-sm text-slate-600 mb-6 leading-relaxed font-medium">Terdeteksi ancaman fisik di area kampus. Audio sedang direkam secara otomatis.</p>
                            <div className="mt-auto space-y-3">
                              {statusSistem === 'AKTIF' ? (
                                <button onClick={kerahkanTim} className="w-full py-4 bg-red-600 hover:bg-red-700 text-white font-black rounded-2xl shadow-xl shadow-red-500/20 transition-all flex items-center justify-center gap-2">
                                   <Radio size={20} /> KERAHKAN TIM SATGAS
                                </button>
                              ) : (
                                <div className="space-y-3">
                                   <div className="bg-emerald-100 text-emerald-800 p-4 rounded-2xl text-xs font-black text-center border border-emerald-200 flex items-center justify-center gap-2 shadow-inner">
                                      <CheckCircle size={18} /> TIM MENUJU LOKASI
                                   </div>
                                   <button onClick={selesaikanKasus} className="w-full py-3 bg-slate-800 hover:bg-slate-900 text-white font-bold rounded-2xl text-xs transition-colors">
                                      TUTUP KASUS (RESET)
                                   </button>
                                </div>
                              )}
                              <button className="w-full py-3 bg-white border-2 border-slate-100 rounded-2xl text-xs font-bold text-slate-700 flex items-center justify-center gap-2">
                                 <Phone size={16} /> DENGARKAN AUDIO (LIVE)
                              </button>
                            </div>
                         </div>
                       ) : (
                         <div className="flex-1 flex flex-col items-center justify-center text-slate-400 opacity-60 border-2 border-dashed border-slate-200 rounded-3xl bg-slate-50/50">
                            <Shield size={64} className="mb-4 text-slate-300" />
                            <p className="text-sm font-bold text-slate-500">Radar Bersih</p>
                            <button onClick={picuAlarm} className="mt-4 text-[10px] bg-slate-200 px-3 py-1 rounded-full text-slate-600 font-bold uppercase">Uji Coba Alarm</button>
                         </div>
                       )}
                     </>
                   )}

                   {/* Konten Tab Antrean (Mewakili Polimorfisme) */}
                   {tabAktif === 'ANTREAN' && (
                     <div className="flex-1 overflow-y-auto pr-2 flex flex-col">
                        {antreanLaporan.length > 0 ? (
                          antreanLaporan.map((laporan, index) => (
                             <div key={index} className="p-4 border border-slate-200 rounded-2xl bg-white shadow-sm hover:border-emerald-300 transition-colors mb-3">
                                <p className="text-sm font-bold text-slate-800">Laporan Anonim #{laporan.id}</p>
                             </div>
                          ))
                        ) : (
                          <div className="flex-1 flex flex-col items-center justify-center text-slate-400 opacity-60 border-2 border-dashed border-slate-200 rounded-3xl p-8 bg-slate-50/50 mt-2">
                             <Inbox size={48} className="mb-4 text-slate-300" />
                             <p className="text-sm font-bold text-slate-500">Antrean Kosong</p>
                             <p className="text-xs text-center mt-2 font-medium">Belum ada laporan investigasi yang masuk ke sistem.</p>
                          </div>
                        )}
                     </div>
                   )}
                </div>
             </div>
          </div>
      </main>
    </div>
  );
}