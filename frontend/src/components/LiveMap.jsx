import React from 'react';

export default function LiveMap({ activeReports = [] }) {
  return (
    <div className="relative w-full h-full min-h-[400px] bg-gray-200 rounded-2xl overflow-hidden border border-gray-300 shadow-inner">
      
      {/* Background Placeholder: Nanti bisa diganti dengan <img src="/peta-usu.png" className="w-full h-full object-cover" /> */}
      <div className="absolute inset-0 flex items-center justify-center bg-gray-100">
        <p className="text-gray-400 font-bold text-xl tracking-wider">PETA KAMPUS USU (SIMULASI)</p>
      </div>

      {/* Render Marker Darurat berdasarkan Data Koordinat */}
      {activeReports.map((report) => (
        <div 
          key={report.id} 
          className="absolute z-10 transform -translate-x-1/2 -translate-y-1/2 flex flex-col items-center"
          style={{ top: `${report.latitude}%`, left: `${report.longitude}%` }} 
        >
          {/* Ikon Marker Berkedip */}
          <span className="relative flex h-8 w-8 mb-1">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-danger opacity-75"></span>
            <span className="relative inline-flex rounded-full h-8 w-8 bg-danger items-center justify-center border-2 border-white shadow-lg">
               <div className="h-2 w-2 bg-white rounded-full"></div>
            </span>
          </span>
          {/* Label NIM Korban */}
          <div className="bg-white px-3 py-1 rounded-md text-xs font-bold shadow-lg border border-red-200 text-danger whitespace-nowrap">
            {report.nim}
          </div>
        </div>
      ))}
    </div>
  );
}