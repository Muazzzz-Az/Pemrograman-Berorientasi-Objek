import { create } from 'zustand';

export const useAuthStore = create((set) => ({
  user: null,       // Menyimpan data user (nim, nama, dll)
  role: null,       // 'STUDENT' atau 'ADMIN'
  isAuthenticated: false,

  // Fungsi untuk login (nanti dihubungkan ke Axios)
  login: (userData, userRole) => set({ 
    user: userData, 
    role: userRole, 
    isAuthenticated: true 
  }),

  // Fungsi untuk logout (O(1) reset state)
  logout: () => set({ 
    user: null, 
    role: null, 
    isAuthenticated: false 
  }),
}));