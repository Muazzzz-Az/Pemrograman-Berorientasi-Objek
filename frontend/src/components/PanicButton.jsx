import React, { useState } from 'react';
import { AlertTriangle } from 'lucide-react';

export default function PanicButton({ onTrigger }) {
  const [isHolding, setIsHolding] = useState(false);

  // Mencegah klik tidak sengaja (Accidental Click Prevention)
  const handleClick = () => {
    if (window.confirm('PERINGATAN: Apakah Anda yakin ingin mengirimkan Laporan Darurat? Satgas akan segera merespons ke lokasi Anda.')) {
      onTrigger();
    }
  };

  return (
    <div className="flex flex-col items-center justify-center p-8">
      <button 
        onClick={handleClick}
        onMouseDown={() => setIsHolding(true)}
        onMouseUp={() => setIsHolding(false)}
        onMouseLeave={() => setIsHolding(false)}
        className={`relative group flex items-center justify-center w-56 h-56 rounded-full bg-danger text-white transition-all duration-300 ${
          isHolding 
            ? 'scale-95 shadow-[0_0_20px_rgba(255,59,48,0.8)]' 
            : 'hover:scale-105 shadow-[0_0_40px_rgba(255,59,48,0.6)] hover:shadow-[0_0_60px_rgba(255,59,48,0.8)]'
        }`}
      >
        {/* Efek Gelombang Animasi */}
        <div className="absolute inset-0 rounded-full border-4 border-white/30 animate-ping"></div>
        
        <div className="flex flex-col items-center z-10">
          <AlertTriangle size={72} className="mb-2" />
          <span className="text-2xl font-extrabold tracking-widest uppercase">Darurat</span>
        </div>
      </button>
      
      <p className="mt-8 text-gray-500 text-sm font-medium text-center max-w-xs">
        Tekan tombol ini <strong className="text-danger">HANYA</strong> jika Anda mengalami ancaman fisik atau nyawa.
      </p>
    </div>
  );
}