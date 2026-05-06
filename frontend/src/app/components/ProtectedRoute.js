'use client';
import { useEffect, useState } from 'react';
import { useRouter, usePathname } from 'next/navigation';

export default function ProtectedRoute({ children }) {
  const router = useRouter();
  const pathname = usePathname();
  const [verificado, setVerificado] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');

    if (!token && pathname.startsWith('/dashboard')) {
      router.replace('/login');
      return;
    }

    if (token && (pathname === '/login' || pathname === '/registro')) {
      router.replace('/dashboard');
      return;
    }

    setVerificado(true);
  }, [router, pathname]);

  if (!verificado) {
    return (
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#fff',
        gap: '16px'
      }}>
        <div style={{
          width: '44px',
          height: '44px',
          border: '4px solid #e5e7eb',
          borderTop: '4px solid #3b82f6',
          borderRadius: '50%',
          animation: 'spin 0.8s linear infinite'
        }} />
        <p style={{
          color: '#6b7280',
          fontSize: '14px',
          fontFamily: 'sans-serif'
        }}>
          Verificando sesión...
        </p>
        <style>{`
          @keyframes spin {
            to { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    );
  }

  return <>{children}</>;
}